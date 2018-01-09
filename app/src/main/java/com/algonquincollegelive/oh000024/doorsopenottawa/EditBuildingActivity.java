package com.algonquincollegelive.oh000024.doorsopenottawa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.algonquincollegelive.oh000024.doorsopenottawa.model.BuildingPOJO;
import com.algonquincollegelive.oh000024.doorsopenottawa.service.MyService;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.HttpMethod;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.RequestPackage;
/**
 *  Edite Activity for buildings
 *  @author jake Oh (oh000024@algonquinlive.com)
 */
public class EditBuildingActivity extends AppCompatActivity {

    private final String UPDATE_URL = "https://doors-open-ottawa.mybluemix.net/buildings/:";
    private final String TAG = "EDIT";
    private BuildingPOJO mbuiling;
    EditText addressET;
    EditText descriptionET;

//    EditText descriptionET;

    CheckBox isNewBuilding;

    EditText saturdayStartET;
    EditText saturdayEndET;
    EditText sundayStartET;
    EditText sundayEndET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_building);

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            mbuiling = (BuildingPOJO) extras.getParcelable("CLICKEDBUILDING");
        }

        try {
            addressET = (EditText) findViewById(R.id.editAddressET);
            addressET.setText(mbuiling.getAddressEN().toString());
//        addressET.setText();

            descriptionET = (EditText) findViewById(R.id.editDescET);
            descriptionET.setText(mbuiling.getDescriptionEN());

            isNewBuilding = (CheckBox) findViewById(R.id.isNewBuildingCB);
            isNewBuilding.setChecked(mbuiling.getIsNewBuilding());

            saturdayStartET = (EditText) findViewById(R.id.editSaturdayStartET);
            saturdayStartET.setText(mbuiling.getSaturdayStart().toString());

            saturdayEndET = (EditText) findViewById(R.id.editSaturdayEndET);
            saturdayEndET.setText(mbuiling.getSaturdayClose().toString());

            sundayStartET = (EditText) findViewById(R.id.editSundayStartET);
            sundayStartET.setText(mbuiling.getSundayStart().toString());

            sundayEndET = (EditText) findViewById(R.id.editSundayEndET);
            sundayEndET.setText(mbuiling.getSundayClose().toString());
        } catch(Exception e){

        }
    }

    public  void onPressedUpdate(View v) {
        updateBuilding();
    }

    public void onPressedCancel(View v) {
        finish();
    }
    private void updateBuilding() {
        BuildingPOJO pluto = new BuildingPOJO();

        mbuiling.setAddressEN(addressET.getText().toString());
        mbuiling.setDescriptionEN(descriptionET.getText().toString());

        mbuiling.setIsNewBuilding(isNewBuilding.isChecked());

        mbuiling.setSaturdayStart(saturdayStartET.getText().toString());
        mbuiling.setSaturdayClose(saturdayEndET.getText().toString());

        mbuiling.setSundayStart(sundayStartET.getText().toString());
        mbuiling.setSundayClose(sundayEndET.getText().toString());

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod( HttpMethod.PUT );
        requestPackage.setEndPoint( MainActivity.DOO_URL+"/" + mbuiling.getBuildingId() );

        requestPackage.setParam("addressEN", mbuiling.getAddressEN());
        requestPackage.setParam("descriptionEN", mbuiling.getDescriptionEN() );
        requestPackage.setParam("isNewBuilding", mbuiling.getIsNewBuilding()+"" );

        requestPackage.setParam("saturdayStart", mbuiling.getSaturdayStart().toString() );
        requestPackage.setParam("saturdayClose", mbuiling.getSaturdayClose().toString() );

        requestPackage.setParam("sundayStart", mbuiling.getSundayStart().toString() );
        requestPackage.setParam("sundayClose", mbuiling.getSundayClose().toString() );


        Intent intent = new Intent(this, MyService.class);
        intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
        startService(intent);

        Log.i(TAG, "updatePlanetPluto(): " + mbuiling.getNameEN());
    }
}
