package com.algonquincollegelive.oh000024.doorsopenottawa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.algonquincollegelive.oh000024.doorsopenottawa.model.BuildingPOJO;
import com.algonquincollegelive.oh000024.doorsopenottawa.service.MyService;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.HttpMethod;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.RequestPackage;

import static com.algonquincollegelive.oh000024.doorsopenottawa.MainActivity.DOO_URL;
import static com.algonquincollegelive.oh000024.doorsopenottawa.MainActivity.NEW_BUILDING_DATA;
import static com.algonquincollegelive.oh000024.doorsopenottawa.MainActivity.NEW_BUILDING_IMAGE;

public class Building_New extends AppCompatActivity {
    private final static int CAMERA_REQUEST_CODE = 100;

    private EditText nameEditText;
    private EditText addressEditText;
    private ImageView photoView;
    private Bitmap    bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_new);

        nameEditText = (EditText) findViewById(R.id.editName);
        addressEditText = (EditText) findViewById(R.id.editAddress);
        photoView = (ImageView) findViewById(R.id.photoView);
        bitmap = null;

        FloatingActionButton photoButton = (FloatingActionButton) findViewById(R.id.cameraButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });
        Button saveButton = (Button) findViewById(R.id.saveNewBuildingButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText buildingName = (EditText) findViewById(R.id.editName);
                EditText buildingAddress = (EditText) findViewById(R.id.editAddress);

                String name = nameEditText.getText().toString();
                String address = addressEditText.getText().toString();

                // Validation Rule: all fields are required
                if (name.isEmpty()) {
                    nameEditText.setError( "Please Enter the Name");
                    nameEditText.requestFocus();
                    return;
                }

                if (address.isEmpty()) {
                    addressEditText.setError( "Please Enter the Address");
                    addressEditText.requestFocus();
                    return;
                }
                BuildingPOJO building = new BuildingPOJO();
                building.setNameEN( name);
                building.setAddressEN(address);

                RequestPackage requestPackage = new RequestPackage();
                requestPackage.setMethod( HttpMethod.POST );

                requestPackage.setEndPoint(DOO_URL);//"https://doors-open-ottawa.mybluemix.net/buildings");
                requestPackage.setParam("nameEN", building.getNameEN());
                requestPackage.setParam("addressEN", building.getAddressEN());

                Intent intent = new Intent(getApplicationContext(), MyService.class);
                intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
                startService(intent);

                intent = new Intent();
                intent.putExtra(NEW_BUILDING_DATA, building);
                if (bitmap != null) {
                    intent.putExtra(NEW_BUILDING_IMAGE, bitmap);
                }

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                if(bitmap != null){
                    //there is a picture
                    photoView.setImageBitmap(bitmap);
                }
            }

            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled: Take a Photo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
