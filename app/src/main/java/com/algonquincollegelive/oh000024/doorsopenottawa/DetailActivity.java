package com.algonquincollegelive.oh000024.doorsopenottawa;

import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.algonquincollegelive.oh000024.doorsopenottawa.model.BuildingPOJO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Locale;
/**
 *  DetailActivity for show buildings
 *  @author jake Oh (oh000024@algonquinlive.com)
 */
public class DetailActivity extends FragmentActivity implements OnMapReadyCallback {

//    private static final String PHOTO_BASE_URL = "https://doors-open-ottawa.mybluemix.net/buildings/";
    private GoogleMap mMap;

    private Geocoder mGeocoder;
    BuildingPOJO mbuiling;
    String maddress;    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            mbuiling = (BuildingPOJO) extras.getParcelable("CLICKEDBUILDING");

        }
        TextView buildingTV  = (TextView)findViewById(R.id.buildNameText);
        EditText buildingET  = (EditText)findViewById(R.id.buildingDesc);
        ImageView buildingIM = (ImageView)findViewById(R.id.buildingIV);
        TextView openTV = (TextView)findViewById(R.id.openTV);

        try {
            maddress = mbuiling.getAddressEN() +" "+ mbuiling.getPostalCode();
            buildingET.setText(mbuiling.getDescriptionEN());
            buildingET.setSelection(2);
            buildingET.setEnabled(false);
            buildingTV.setText(mbuiling.getNameEN());
            openTV.setText("Open: " + mbuiling.getSaturdayStart().toString());
            String url = MainActivity.DOO_URL + "/"+ mbuiling.getBuildingId() + "/image";
            Picasso.with(this)
                    .load(url)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.noimagefound)
                    .fit()
                    .into(buildingIM);

            System.out.println("str addres: " + maddress);

            mGeocoder = new Geocoder(this, Locale.CANADA);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        pin(maddress);
    }
    /** Locate and pin locationName to the map. */
    private void pin( String locationName ) {
        try {

//            Address address = mGeocoder.getFromLocationName(locationName, 1).get(0);

//            if(address==null){
//                address.setLatitude(mbuiling.getLatitude());
//                address.setLongitude(mbuiling.getLongitude());
//            }
            LatLng ll = new LatLng( mbuiling.getLatitude(), mbuiling.getLongitude() );
            mMap.addMarker( new MarkerOptions().position(ll).title(locationName) );
            mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(ll, 17.F) );
            Toast.makeText(this, "Pinned: " + locationName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Not found: " + locationName, Toast.LENGTH_SHORT).show();
        }
    }
}
