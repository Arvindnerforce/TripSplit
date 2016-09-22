package com.netforceinfotech.tripsplit.posttrip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.netforceinfotech.tripsplit.R;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback
{

    static final LatLng TutorialsPoint = new LatLng(21 , 57);




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        MapFragment     googleMap = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        googleMap.getMapAsync(GoogleMapActivity.this);


        }

    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }


}