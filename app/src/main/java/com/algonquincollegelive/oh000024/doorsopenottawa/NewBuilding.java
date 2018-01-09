package com.algonquincollegelive.oh000024.doorsopenottawa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.algonquincollegelive.oh000024.doorsopenottawa.model.BuildingPOJO;
import com.algonquincollegelive.oh000024.doorsopenottawa.service.MyService;
import com.algonquincollegelive.oh000024.doorsopenottawa.service.UploadImageFileService;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.HttpMethod;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.RequestPackage;

/**
 *  Activity fo a new building
 *  @author jake Oh (oh000024@algonquinlive.com)
 */
public class NewBuilding extends AppCompatActivity {

    private static final String  TAG = "CRUD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_building);
    }

    public void onPressedSave(View v){

        createNewBuilding();

    }

    public void onPressedCancel(View v){
        setResult(0);
        finish();
    }

//    curl --include -X POST --header "Content-Type: application/json" --data '{
//            "nameEN": "1234 test",
//            "addressEN": "123 Main St."
//}' --basic --user 'oh000024:password' https://doors-open-ottawa.mybluemix.net/buildings

    private void createNewBuilding() {

        BuildingPOJO building = new BuildingPOJO();

        // Set the planet's Id to 0
        // Problem: Mars has a planet Id of 0
        // Solution: the web service will change the Id from 0 to the next Id to uniquely identify
        //           this planet (pluto)
        //           The next available Id is: 8

        // The web service requires values for *each* property (i.e. instance variable).
        EditText buildingName = (EditText) findViewById(R.id.buildingNameET);
        EditText buildingAddress = (EditText) findViewById(R.id.addressET);

        System.out.println("BUILDING NAME: " +buildingName.getText().toString());
        System.out.println("BUILDING ADDRESS: " +buildingAddress.getText().toString());


        if((buildingName.getText().length() == 0) || (buildingAddress.getText().length() ==0 )){
            Toast.makeText(getApplicationContext(),"Please, input valide data", Toast.LENGTH_SHORT).show();
            buildingAddress.setError("Address of a building required");
            buildingName.setError("Name of a building required");
            return;
        }

        building.setNameEN( buildingName.getText().toString());
        building.setAddressEN(buildingAddress.getText().toString());

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod( HttpMethod.POST );
        requestPackage.setEndPoint(MainActivity.DOO_URL+"/form");//"https://doors-open-ottawa.mybluemix.net/buildings");
        requestPackage.setParam("nameEN", building.getNameEN());
        requestPackage.setParam("addressEN", building.getAddressEN());

        Intent intent = new Intent(this, UploadImageFileService.class);
        intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
        startService(intent);

        Log.i(TAG, "createBuilding(): " + building.getNameEN());
        finish();
    }
}
