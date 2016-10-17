package com.netforceinfotech.tripsplit.login;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.netforceinfotech.tripsplit.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class SignUpActivity extends AppCompatActivity {

    Context context;
    EditText etname, etemail, etpassword, etconfirmpassword, etdob, etcountry, etaddress, etpincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = this;
        initView();
        getPermission();
        getLocation();
    }

    private void getLocation() {
        SmartLocation.with(context).location()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        getAddress(location.getLongitude(), location.getLatitude());
                    }
                });
    }

    private void getAddress(double longitude, double latitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        String address, city, state, country, postalCode, knownName;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            try {
                knownName = addresses.get(0).getFeatureName();
                etaddress.append(knownName + ", ");
            } catch (Exception ex) {

            }
            try {
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                etaddress.append(address + ", ");
            } catch (Exception ex) {
                etaddress.append("");
            }
            try {
                city = addresses.get(0).getLocality();
                etaddress.append(city + ", ");
            } catch (Exception ex) {
                etaddress.append("");
            }
            try {
                state = addresses.get(0).getAdminArea();
                etaddress.append(state + ", ");
            } catch (Exception ex) {
                etaddress.append("");
            }
            try {
                country = addresses.get(0).getCountryName();
                etaddress.append(country + ", ");
            } catch (Exception ex) {
                postalCode = addresses.get(0).getPostalCode();
                etaddress.append(postalCode + ", ");
            }

        } catch (IOException e) {
            Log.i("klocation", "unknown location");
        }


    }

    private void initView() {
        etname = (EditText) findViewById(R.id.etname);
        etemail = (EditText) findViewById(R.id.etemail);
        etpassword = (EditText) findViewById(R.id.etpassword);
        etconfirmpassword = (EditText) findViewById(R.id.etconfirmpassword);
        etdob = (EditText) findViewById(R.id.etdob);
        etcountry = (EditText) findViewById(R.id.etcountry);
        etaddress = (EditText) findViewById(R.id.etaddress);
        etpincode = (EditText) findViewById(R.id.etpincode);
    }

    private void getPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String[] permission = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,

            };

            ActivityCompat.requestPermissions(this,
                    permission, 1);
        }
    }
}
