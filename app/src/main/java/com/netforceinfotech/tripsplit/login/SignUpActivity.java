package com.netforceinfotech.tripsplit.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;
import com.netforceinfotech.tripsplit.util.Validation;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    UserSessionManager userSessionManager;
    Context context;
    Button buttonSignIn;
    EditText etname, etemail, etpassword, etconfirmpassword, etdob, etcountry, etaddress;
    String name, email, password, confirmpassword, dob, country, address, country_code = "";
    ArrayList<Country> countries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = this;
        userSessionManager = new UserSessionManager(context);
        initView();
        setupToolBar("Sign Up!");
        getPermission();
        getLocation();
    }


    private void setupToolBar(String app_name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(app_name);


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
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        etname = (EditText) findViewById(R.id.etname);
        etemail = (EditText) findViewById(R.id.etemail);
        etpassword = (EditText) findViewById(R.id.etpassword);
        etconfirmpassword = (EditText) findViewById(R.id.etconfirmpassword);
        etdob = (EditText) findViewById(R.id.etdob);
        etcountry = (EditText) findViewById(R.id.etcountry);
        etaddress = (EditText) findViewById(R.id.etaddress);
        setupCountry();
        buttonSignIn.setOnClickListener(this);
        etdob.setOnClickListener(this);

    }

    private void setupCountry() {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            String country_code = locale.getISO3Country();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(new Country(country, country_code));
            }
        }
        Collections.sort(countries);
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(context, etcountry);
        int size = countries.size();
        for (int i = 0; i < size; i++) {
            droppyBuilder.addMenuItem(new DroppyMenuItem(countries.get(i).country));
        }
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                SignUpActivity.this.country_code = countries.get(id).code;
                etcountry.setText(countries.get(id).country);
            }
        });
        droppyBuilder.build();

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignIn:
                name = etname.getText().toString().trim();
                email = etemail.getText().toString().trim();
                password = etpassword.getText().toString();
                confirmpassword = etconfirmpassword.getText().toString();
                country = etcountry.getText().toString();
                dob = etdob.getText().toString();
                address = etaddress.getText().toString();

                if (!Validation.isEmailAddress(etemail, true)) {
                    showMessage("not valid email");
                    return;
                }
                if (name.length() <= 0 || email.length() <= 0 || password.length() <= 0) {
                    showMessage("Name or email or password cannot be blank");
                    return;

                }
                if (!password.equals(confirmpassword)) {
                    showMessage("Missmatch password");
                    return;
                }
                login(name, email, password, country, address, dob);
                break;
            case R.id.etdob:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        SignUpActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
        }

    }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    private void login(String name, String email, String password, String country, String address, String dob) {
        try {
            name = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String baseUrl = getString(R.string.url);
        String url = baseUrl + "services.php?opt=register&facebook=0&fb_token=&fb_id=&reg_id=&name=" + name
                + "&email=" + email + "&dob=" + dob + "&country" + country + "&country_code=" + country_code + "&send_otp=true&password=" + password;
        Log.i("kurl", url);
        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null) {
                            Log.i("kresult", result.toString());
                            if (result.get("status").getAsString().equalsIgnoreCase("success")) {
                                JsonArray data = result.getAsJsonArray("data");
                                JsonObject jsonObject = data.get(0).getAsJsonObject();
                                String user_id = jsonObject.get("user_id").getAsString();
                                userSessionManager.setUserId(user_id);
                                Intent intent = new Intent(context, OTPActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Date date2 = new Date();
        monthOfYear = monthOfYear + 1;
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date2 = date_format.parse(year + "-" + monthOfYear + "-" + dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat outDate = new SimpleDateFormat("yyyy-MM-dd");


        etdob.setText(outDate.format(date2));

    }
}
