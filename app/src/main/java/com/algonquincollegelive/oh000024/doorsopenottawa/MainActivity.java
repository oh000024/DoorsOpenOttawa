package com.algonquincollegelive.oh000024.doorsopenottawa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.algonquincollegelive.oh000024.doorsopenottawa.model.BuildingPOJO;
import com.algonquincollegelive.oh000024.doorsopenottawa.service.MyService;
import com.algonquincollegelive.oh000024.doorsopenottawa.service.UploadImageFileService;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.HttpMethod;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.NetworkHelper;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.RequestPackage;

/**
 *  MainActivity
 *  @author jake Oh (oh000024@algonquinlive.com)
 */
public class MainActivity extends AppCompatActivity {

    private static final String ABOUT_DIALOG_TAG = "About Dialog";
    public static final String DOO_URL = "https://doors-open-ottawa.mybluemix.net/buildings";

    public static final String  NEW_BUILDING_DATA = "NEW_BUILDING_DATA";
    public static final String  NEW_BUILDING_IMAGE = "NEW_BUILDING_IMAGE";
    public static final int     REQUEST_NEW_BUILDING = 1;
    public static final int     REQUEST_EDIT_BUILDING = 2;
    private static final int    NO_SELECTED_CATEGORY_ID = -1;
    private static final String REMEMBER_SELECTED_CATEGORY_ID = "lastSelectedCategoryId";

    private Toolbar toolbar;
    private static final Boolean    IS_LOCALHOST;
    private static final String     JSON_URL;
    private static final String     TAG = "CRUD";
    private boolean                 networkOk;
    private BuildingAdapter         mBuildingAdapter;
    private RecyclerView            mRecyclerView;
    private  int                    mNewBuildingId = 0 ;
    private ProgressBar             mProgressBar;
    private String[]                mCategories;

    private DrawerLayout            mDrawerLayout;
    private ListView                mDrawerList;
    private int                     mRememberSelectedCategoryId;
    private Bitmap                  mBitmap;

    public enum CATEGORI {RELIGIOUS,EMBASSIES,GOVERNMENT,FUNCTIONAL,GALLERIES,ACADEMIC,SPORTS,COMMUNITY,BUSINESS,MUSEUEMS,OTHER}


    public static enum RETURN_CODE {
        CANCEL(0),SUCCESS (200),  UNAUTHORIZED (401);
        private int value;

        RETURN_CODE(int value){
            this.value = value;
        }

        public int getVALUE(){
            return value;
        }
    }

    static {
        IS_LOCALHOST = false;
        JSON_URL = IS_LOCALHOST ? "http://10.0.2.2:3000/buildings" : "https://doors-open-ottawa.mybluemix.net/buildings";
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mProgressBar.setVisibility(View.INVISIBLE);

            if (intent.hasExtra(MyService.MY_SERVICE_PAYLOAD)) {
                BuildingPOJO[] buildingsArray = (BuildingPOJO[]) intent
                        .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
                Toast.makeText(MainActivity.this,
                        "Received " + buildingsArray.length + " buildings from service",
                        Toast.LENGTH_SHORT).show();
                mBuildingAdapter.setBuildings(buildingsArray);
                mRecyclerView.setAdapter( mBuildingAdapter );

            } else if (intent.hasExtra(MyService.MY_SERVICE_RESPONSE)) {
                String response = intent.getStringExtra(MyService.MY_SERVICE_RESPONSE);
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                String retmethode = response.split("/")[0];

                if((retmethode == "POST")  ||(retmethode =="PUT")){
                    int mNewBuildingId = Integer.parseInt(response.split("/")[1]);
                    uploadBitmap(mNewBuildingId);
                } else{
                    getBuildings(mRememberSelectedCategoryId);
                }

            } else if (intent.hasExtra(MyService.MY_SERVICE_EXCEPTION)) {
                String message = intent.getStringExtra(MyService.MY_SERVICE_EXCEPTION);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            if (intent.hasExtra(UploadImageFileService.UPLOAD_IMAGE_FILE_SERVICE_RESPONSE)) {
                String response = intent.getStringExtra(UploadImageFileService.UPLOAD_IMAGE_FILE_SERVICE_RESPONSE);
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                // the planet data has changed on the web service
                // fetch fetch all the planet data, and re-fresh the list
                getBuildings(mRememberSelectedCategoryId);

            } else if (intent.hasExtra(UploadImageFileService.UPLOAD_IMAGE_FILE_SERVICE_EXCEPTION)) {
                String message = intent.getStringExtra(UploadImageFileService.UPLOAD_IMAGE_FILE_SERVICE_EXCEPTION);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView + Adapter
        mBuildingAdapter = new BuildingAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvBuilding);
        mProgressBar = (ProgressBar) findViewById(R.id.pbNetwork);
        mProgressBar.setVisibility(View.INVISIBLE);

        //      Code to manage sliding navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCategories = getResources().getStringArray(R.array.categories);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_category_row, mCategories));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Category: " + mCategories[position], Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(mDrawerList);
                getBuildings(position);
            }
        });

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(UploadImageFileService.UPLOAD_IMAGE_FILE_SEVICE_MESSAGE));

        if (NetworkHelper.hasNetworkAccess(this)) {
            getBuildings(NO_SELECTED_CATEGORY_ID);
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Building_New.class);

                startActivityForResult(intent, REQUEST_NEW_BUILDING);
//                setUpToolbar();
            }
        });
        SharedPreferences settings = getSharedPreferences( getResources().getString(R.string.app_name), Context.MODE_PRIVATE );
        mRememberSelectedCategoryId = settings.getInt(REMEMBER_SELECTED_CATEGORY_ID, NO_SELECTED_CATEGORY_ID);
//        getBuildings(mRememberSelectedCategoryId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        int category = 0;

        switch (id) {
            case R.id.action_all_items:
                // fetch and display all buildings
                getBuildings(NO_SELECTED_CATEGORY_ID);
                return true;
            case R.id.action_choose_category:
                //open the drawer
                mDrawerLayout.openDrawer(mDrawerList);
                return true;
        }

        if ( item.isCheckable() ) {
            // remember which sort option the user picked
            item.setChecked( true );

            // which sort menu item did the user pick?
            switch( item.getItemId() ) {
                case R.id.action_sort_name_asc:
                    mBuildingAdapter.sortByNameAscending();
                    Log.i( TAG, "Sorting planets by name (a-z)" );
                    break;

                case R.id.action_sort_name_dsc:
                    mBuildingAdapter.sortByNameDescending();
                    Log.i( TAG, "Sorting planets by name (z-a)" );
                    break;
            }

            return true;
        } // END if item.isChecked()

        return super.onOptionsItemSelected(item);
    }

    /**
     * Fetch the planet data from the web service.
     *
     * Operation: HTTP GET /building
     */
    private void getBuildings(int selectedCategoryId) {
        mRememberSelectedCategoryId = selectedCategoryId;

        if (NetworkHelper.hasNetworkAccess(this)) {
            mProgressBar.setVisibility(View.VISIBLE);
            RequestPackage getPackage = new RequestPackage();
            getPackage.setMethod(HttpMethod.GET);
            getPackage.setEndPoint(JSON_URL);
            if (selectedCategoryId != NO_SELECTED_CATEGORY_ID) {
                getPackage.setParam("categoryId", selectedCategoryId + "");
            }

            Intent intent = new Intent(this, MyService.class);
            intent.putExtra(MyService.REQUEST_PACKAGE, getPackage);
            startService(intent);
        }else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }

        Log.i(TAG, "getBuildings()");
    }


    // TODO #9 - event handler for menu item Upload Image of building
    /**
     * Update binary image file of building
     *
     * Operation: HTTP POST /planets/id/image
     */
    private void uploadImageFileOfBuilding() {

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod( HttpMethod.POST );
        requestPackage.setEndPoint( JSON_URL + "/"+ Integer.toString(mNewBuildingId)+ "/image" );

        Intent intent = new Intent(this, UploadImageFileService.class);
        intent.putExtra(UploadImageFileService.REQUEST_PACKAGE, requestPackage);
        startService(intent);

        Log.i(TAG, "uploadImageFileOfBuilding()");
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_BUILDING) {
            if (resultCode == RESULT_OK) {
                BuildingPOJO newBuilding = data.getExtras().getParcelable(NEW_BUILDING_DATA);
                Bitmap image  = (Bitmap) data.getParcelableExtra(NEW_BUILDING_IMAGE);
                if (image != null) {
                    mBitmap = image;
                }

                Toast.makeText(this, "Added Building: " + newBuilding.getNameEN(), Toast.LENGTH_SHORT).show();
            }

            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled: Add New Building", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadBitmap(int buildingId) {
        if (mBitmap != null ) {
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setMethod(HttpMethod.POST);
            requestPackage.setEndPoint(JSON_URL + "/" + buildingId + "/image");

            Toast.makeText(this, "Uploaded image for Building Id: " + buildingId + "", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, UploadImageFileService.class);
            intent.putExtra(UploadImageFileService.REQUEST_PACKAGE, requestPackage);
            intent.putExtra(NEW_BUILDING_IMAGE, mBitmap);
            startService(intent);
        }
    }
}
