package com.netforceinfotech.tripsplit.posttrip;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.netforceinfotech.tripsplit.R;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;

import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback
{

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    static final LatLng TutorialsPoint = new LatLng(21 , 57);
    PlacesAutocompleteTextView placesAutocomplete;
    LatLng currentLatLng;
    GoogleMap Gmap;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        context = this;
        MapFragment   googleMap = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        googleMap.getMapAsync(GoogleMapActivity.this);


    }

    public void onMapReady(GoogleMap map)
    {
        Gmap = map;
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,getApplicationContext(),GoogleMapActivity.this))
        {

            Gmap.getUiSettings().setRotateGesturesEnabled(false);
            getLocation();

        }
        else
        {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,PERMISSION_REQUEST_CODE_LOCATION,getApplicationContext(),GoogleMapActivity.this);
        }

        Gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {

                clearMarker();
                Gmap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("Selected Location"));
                currentLatLng = latLng;



            }
        });


        placesAutocomplete = (PlacesAutocompleteTextView) findViewById(R.id.places_autocomplete);
        placesAutocomplete.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener()
                {
                    @Override
                    public void onPlaceSelected(final Place place)
                    {
                        // do something awesome with the selected place
                        String strAddress = place.description;
                        LatLng latLng = getLocationFromAddress(getApplicationContext(), strAddress);
                        currentLatLng = latLng;
                        Log.i("address", strAddress);
                        clearMarker();
                        Gmap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("Selected Location"));
                        Gmap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("Selected Location"));
                        Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 5.0f));

                    }
                }
        );

    }

    private void getLocation()
    {
        SmartLocation.with(context).location()
                .oneFix()
                .start(new OnLocationUpdatedListener()
                {

                    public void onLocationUpdated(Location location)
                    {
                        LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                        Gmap.addMarker(new MarkerOptions().position(sydney).title("My Current Location"));
                        Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 5.0f));
                        currentLatLng = sydney;

                    }
                });
    }



    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            android.location.Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    private void clearMarker()
    {
        try
        {
            Gmap.clear();
        }
        catch (Exception ex)
        {

        }
    }



    public  void requestPermission(String strPermission,int perCode,Context _c,Activity _a)
    {

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a,strPermission))
        {
            Toast.makeText(getApplicationContext(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
        }
        else
        {

            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
        }
    }

    public static boolean checkPermission(String strPermission,Context _c,Activity _a){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        }
        else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    Gmap.getUiSettings().setRotateGesturesEnabled(false);
                    getLocation();

                } else {

                    Toast.makeText(getApplicationContext(),"Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();

                }
                break;

        }
    }



}
