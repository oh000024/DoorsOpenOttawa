package com.algonquincollegelive.oh000024.doorsopenottawa;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.algonquincollegelive.oh000024.doorsopenottawa.model.BuildingPOJO;
import com.algonquincollegelive.oh000024.doorsopenottawa.service.MyService;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.HttpMethod;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.RequestPackage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import static com.algonquincollegelive.oh000024.doorsopenottawa.MainActivity.DOO_URL;

//import static com.algonquincollegelive.oh000024.doorsopenottawa.MainActivity.REQUEST_EDIT_BUILDING;


/**
 exposes a list of Building
 * *  @author Jake Oh (oh000024@algonquinlive.com)
 */


public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.ViewHolder> {

//    private final static int DETAIL_REQUEST_CODE = 100;
//    private final static int EDIT_REQUEST_CODE = 101;
    public static final String CLICKEDBUILDING = "CLICKEDBUILDING";
//    public static final String ADDRESS = "ADDRESS";
//    public static final String BUILDINGNAME = "BUILDINGNAME";
//    public static final String DESCRIPTION = "DESCRIPTION";
//    public static final String OPENTIME = "OPENTIME";
    private static final String TAG        = "BuildingAdapter";


//    private Map<Integer, Bitmap> mBitmaps;
    private ArrayList<Integer> mStars;
    private Context mContext;
    private ArrayList<BuildingPOJO>  mBuildings;

    public BuildingAdapter(Context mainActivity) {

        this.mContext = mainActivity;
        mStars = new ArrayList<Integer>();
        SharedPreferences keyValues = mContext.getSharedPreferences( mContext.getResources().getString(R.string.app_name), Context.MODE_PRIVATE );
        SharedPreferences.Editor keyValuesEditor = keyValues.edit();
//        JSONArray arr = new JSONArray(data);


        SharedPreferences pSharedPref = mContext.getApplicationContext().getSharedPreferences("oh000024", Context.MODE_PRIVATE);
        String list = pSharedPref.getString("ohSaveddata","");
        if(!list.isEmpty()){
            mStars = new Gson().fromJson(list, new TypeToken<ArrayList<Integer>>() {
            }.getType());

        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new BuildingAdapterViewHolder that holds the View for each list item
     */
    @Override
    public BuildingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View buildingView = inflater.inflate(R.layout.item_building, parent, false);
        ViewHolder viewHolder = new ViewHolder(buildingView);
        return viewHolder;
    }

    public final Context getmContext() {
        return mContext;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the building
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder The ViewHolder which should be updated to represent the
     *               contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final BuildingAdapter.ViewHolder holder, final int position) {
        final BuildingPOJO building = mBuildings.get(position);

        holder.tvName.setText(building.getNameEN());
        holder.tvAddress.setText(building.getAddressEN());
        String url = MainActivity.DOO_URL+"/"+ building.getBuildingId() + "/image";
        Picasso.with(mContext)
                .load(url)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.noimagefound)
                .fit()
                .into(holder.imageView);
        holder.imgDelete.setVisibility(View.INVISIBLE);
        holder.imgEdit.setVisibility(View.INVISIBLE);

        if(mStars.contains(building.getBuildingId())){
            holder.mStar.setChecked(true);
        } else {holder.mStar.setChecked(false);}

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if( (holder.imgDelete.getVisibility() == View.VISIBLE) && (holder.imgEdit.getVisibility() == View.VISIBLE)){
                    holder.imgDelete.setVisibility(View.INVISIBLE);
                    holder.imgEdit.setVisibility(View.INVISIBLE);
                } else {
                    holder.imgDelete.setVisibility(View.VISIBLE);
                    holder.imgEdit.setVisibility(View.VISIBLE);
                }
                return  true;
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,DetailActivity.class);
                intent.putExtra(CLICKEDBUILDING,building);
                mContext.startActivity(intent);
            }
        });

//        holder.setListeners();

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                // TODO - externalize strings to strings.xml
                builder.setTitle("Confirm")
                        .setMessage("Delete " + building.getBuildingId() + " - " + building.getNameEN() + "?")

                        // Displays: OK
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete this course
//                                Toast.makeText(mContext, "Deleted Course: " + aCourse.getCode(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Deleted Course: " + building.getBuildingId());
                                holder.imgDelete.setVisibility(View.INVISIBLE);
                                holder.imgEdit.setVisibility(View.INVISIBLE);


                                RequestPackage requestPackage = new RequestPackage();
                                requestPackage.setMethod( HttpMethod.DELETE );
//        requestPackage.setEndPoint(MainActivity.DOO_URL+"/form");//"https://doors-open-ottawa.mybluemix.net/buildings");
                                requestPackage.setEndPoint(DOO_URL+"/"+ Integer.toString(building.getBuildingId()));//"https://doors-open-ottawa.mybluemix.net/buildings");
                                requestPackage.setParam("buildingId", building.getBuildingId()+"");

                                Intent intent = new Intent(mContext, MyService.class);
                                intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
                                mContext.startService(intent);

                                mBuildings.remove(position);
                                BuildingAdapter.this.notifyDataSetChanged();

                                dialog.dismiss();
                            }
                        })

                        // Displays: Cancel
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();

                                Toast.makeText(mContext, "CANCELLED: Deleted Course: " + building.getBuildingId(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "CANCELLED: Deleted Course: " + building.getBuildingId());
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Edit Course: " + building.getBuildingId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, EditBuildingActivity.class);
                intent.putExtra(CLICKEDBUILDING, building);
                //mContext.startActivityForResult(intent, REQUEST_EDIT_BUILDING);
                mContext.startActivity(intent);
                holder.imgDelete.setVisibility(View.INVISIBLE);
                holder.imgEdit.setVisibility(View.INVISIBLE);
            }
        });
        holder.mStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(mContext,Boolean.toString(holder.mStar.isChecked()),Toast.LENGTH_SHORT).show();

                if (mStars.contains(building.getBuildingId())){
                    int index = mStars.indexOf(building.getBuildingId());
                    mStars.remove(index);

                }else{
                    mStars.add(building.getBuildingId());
                }

                SharedPreferences pSharedPref = mContext.getApplicationContext().getSharedPreferences("oh000024", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= pSharedPref.edit();

                String data = new Gson().toJson(mStars);
                editor.putString("ohSaveddata",data);

                editor.apply();
            }
        });
    }



    public void sortByNameAscending() {
        Collections.sort( mBuildings, new Comparator<BuildingPOJO>() {
            @Override
            public int compare( BuildingPOJO lhs, BuildingPOJO rhs ) {
                return lhs.getNameEN().compareTo( rhs.getNameEN() );
            }
        });

        notifyDataSetChanged();
    }

    public void sortByNameDescending() {
        Collections.sort( mBuildings, Collections.reverseOrder(new Comparator<BuildingPOJO>() {
            @Override
            public int compare( BuildingPOJO lhs, BuildingPOJO rhs ) {
                return lhs.getNameEN().compareTo( rhs.getNameEN() );
            }
        }));

        notifyDataSetChanged();
    }
    /**
     * This method simply returns the number of buildings to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of buildings
     */
    @Override
    public int getItemCount() {
        return mBuildings.size();
    }
    public void setBuildings(BuildingPOJO[] buildingsArray) {
        this.mBuildings = new ArrayList<>(buildingsArray.length);
        mBuildings.clear();
        mBuildings.addAll(new ArrayList<>(Arrays.asList(buildingsArray)));
        notifyDataSetChanged();
    }

    /**
     * Recycle the children views for a building list item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvAddress;
        public ImageView imageView;
        public ImageView imgDelete;
        public ImageView imgEdit;
        public View mView;
        public CheckBox mStar;
        int position;

        public ViewHolder(View buildingView) {
            super(buildingView);

            tvName = (TextView) buildingView.findViewById(R.id.buildNameText);
            tvAddress = (TextView) buildingView.findViewById(R.id.buildingAddressText);
            mView = buildingView;
            imageView = (ImageView) buildingView.findViewById(R.id.imageView);

            imgDelete   = (ImageView) itemView.findViewById(R.id.img_row_delete);
            imgEdit      = (ImageView) itemView.findViewById(R.id.img_row_edit);

            mStar = (CheckBox) itemView.findViewById(R.id.checkBox);

            imgDelete.setVisibility(View.INVISIBLE);
            imgEdit.setVisibility(View.INVISIBLE);
        }

    }

}
