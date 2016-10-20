package com.netforceinfotech.tripsplit.posttrip;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.map.GetDirectionsAsyncTask4;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    static final LatLng TutorialsPoint = new LatLng(21, 57);
    PlacesAutocompleteTextView placesAutocomplete;
    LatLng currentLatLng;
    public GoogleMap Gmap;
    Context context;
    static double source_latitude, source_lat;
    static double source_longitude, source_log;
    static double destination_latitude = 30.325558;
    static double destination_longitude = 77.9470939;
    private Polyline newPolyline;
    LatLng latLng;
    Button search, done;
    boolean source_place;
    String MY_PREFS_NAME = "preference_data";
    SharedPreferences.Editor editor;
    String address = "";
    AddressListner addressListner = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);


        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        context = this;

        source_place = getIntent().getExtras().getBoolean("choose_source");

        System.out.println("gsdysugd============" + source_place);

        done = (Button) findViewById(R.id.done_button);

        MapFragment googleMap = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        googleMap.getMapAsync(GoogleMapActivity.this);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent().putExtra("address", address);
                setResult(RESULT_OK, intent);
                finish();
            }


        });


    }

    public void setAddressListner(AddressListner addressListner) {
        this.addressListner = addressListner;
    }

    public void onMapReady(GoogleMap map) {
        Gmap = map;
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), GoogleMapActivity.this)) {

            Gmap.getUiSettings().setRotateGesturesEnabled(false);
            // getLocation();

        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), GoogleMapActivity.this);
        }

        Gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                clearMarker();
                Gmap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("Selected Location"));
                currentLatLng = latLng;


            }
        });

        placesAutocomplete = (PlacesAutocompleteTextView) findViewById(R.id.places_autocomplete);
        search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String strAddress = placesAutocomplete.getText().toString();
                latLng = getLocationFromAddress(getApplicationContext(), strAddress);
                currentLatLng = latLng;
                Log.i("address", strAddress);
                clearMarker();
                try {
                    System.out.println("latitute =====" + latLng.toString());

                    if (source_place == true) {
                        addressListner.gotAddress(strAddress, true);
                        Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 8.0f));
                        Gmap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_location_green50)).title("Source"));
                        editor.putString("source_latitude", Double.toString(latLng.latitude));
                        editor.putString("destination_latitude", Double.toString(latLng.longitude));
                        editor.commit();
                    } else {
                        addressListner.gotAddress(strAddress, false);
                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                        source_lat = Double.parseDouble(prefs.getString("source_latitude", null));
                        source_log = Double.parseDouble(prefs.getString("destination_latitude", null));
                        Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 8.0f));

                        findDirections(source_lat, source_log,
                                latLng.latitude, latLng.longitude,
                                GMapV2Direction.MODE_DRIVING);
                    }
                } catch (Exception e) {

                    e.printStackTrace();

                    Toast.makeText(getApplicationContext(), "please enter correct destination", Toast.LENGTH_SHORT).show();
                }


            }
        });

        placesAutocomplete.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {

                        // do something awesome with the selected place

                        try {

                            String strAddress = place.description;
                            GoogleMapActivity.this.address = strAddress;
                            latLng = getLocationFromAddress(getApplicationContext(), strAddress);
                            currentLatLng = latLng;
                            Log.i("address", strAddress);
                            clearMarker();
                            //  Gmap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("Selected Location"));
                            // Gmap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("Selected Location"));
                            if (source_place == true) {
                                Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 8.0f));
                                Gmap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_location_green50)).title("Source"));
                                editor.putString("source_latitude", Double.toString(latLng.latitude));
                                editor.putString("destination_latitude", Double.toString(latLng.longitude));
                                editor.commit();
                                addressListner.gotAddress(strAddress, true);
                            } else {
                                Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 8.0f));
                                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                source_lat = Double.parseDouble(prefs.getString("source_latitude", null).toString());
                                source_log = Double.parseDouble(prefs.getString("destination_latitude", null).toString());
                                findDirections(source_lat, source_log,
                                        latLng.latitude, latLng.longitude,
                                        GMapV2Direction.MODE_DRIVING);
                                addressListner.gotAddress(strAddress, false);
                            }


                        } catch (Exception e) {
                        }

                    }
                }
        );

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


    private void clearMarker() {
        try {
            Gmap.clear();
        } catch (Exception ex) {

        }


    }


    public void requestPermission(String strPermission, int perCode, Context _c, Activity _a) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a, strPermission)) {
            Toast.makeText(getApplicationContext(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
        }
    }

    public static boolean checkPermission(String strPermission, Context _c, Activity _a) {
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {


        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Gmap.getUiSettings().setRotateGesturesEnabled(false);
                    //getLocation();

                } else {

                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();

                }
                break;

        }
    }


    public void findDirections(double fromPositionDoubleLat,
                               double fromPositionDoubleLong, double toPositionDoubleLat,
                               double toPositionDoubleLong, String mode) {

        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask4.USER_CURRENT_LAT,
                String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask4.USER_CURRENT_LONG,
                String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask4.DESTINATION_LAT,
                String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask4.DESTINATION_LONG,
                String.valueOf(toPositionDoubleLong));
        map.put(GetDirectionsAsyncTask4.DIRECTIONS_MODE, mode);

        GetDirectionsAsyncTask4 asyncTask = new GetDirectionsAsyncTask4(GoogleMapActivity.this);
        asyncTask.execute(map);


    }

    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {

        PolylineOptions rectLine = new PolylineOptions().width(10).color(R.color.polyline_color);

        for (int i = 0; i < directionPoints.size(); i++) {
            rectLine.add(directionPoints.get(i));
        }

        if (newPolyline != null) {
            newPolyline.remove();
        }

        newPolyline = Gmap.addPolyline(rectLine);

        MarkerOptions marker2 = new MarkerOptions().position(
                new LatLng(source_lat, source_log)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_location_green50)).title(
                "My Location");

        MarkerOptions marker3 = new MarkerOptions().position(
                new LatLng(latLng.latitude, latLng.longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_location_red50)).title(
                "Dehradun");

        Gmap.addMarker(marker2);
        Gmap.addMarker(marker3);

    }

    public interface AddressListner {
        void gotAddress(String address, boolean source);
    }

}
