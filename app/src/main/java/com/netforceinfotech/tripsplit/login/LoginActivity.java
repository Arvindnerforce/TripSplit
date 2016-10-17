package com.netforceinfotech.tripsplit.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.JSONArrayBody;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.tripsplit.Dashboard.DashboardActivity;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;
import com.netforceinfotech.tripsplit.general.WrapContentViewPager;
import com.netforceinfotech.tripsplit.tutorial.DefaultIntro;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import me.relex.circleindicator.CircleIndicator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final long SPLASH_TIME_OUT = 2000;
    Button buttonCustomFB, buttonSignIn;
    private CallbackManager mCallbackManager;
    private LoginButton buttonFacebook;
    private Profile profile;
    TextView textViewRegister;
    EditText editTextLEmail, editTextPassword;
    Context context;
    String knownName, address, city, state, postalCode, country;
    String fbName, fbId, fbEmail, fbBirthday, fbGender;
    boolean sendtOTP = false;
    UserSessionManager userSessionManager;
    private String country_code = "";
    private MaterialDialog dialogPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_login);
        context = this;
        userSessionManager = new UserSessionManager(context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };
        updateWithToken(AccessToken.getCurrentAccessToken());
        //  initViewPager();
        setupFacebook();
    }
/*

    private void initViewPager() {
        final PagerAdapterLogin adapter = new PagerAdapterLogin
                (getSupportFragmentManager(), 2);
        viewPager.setPagingEnabled(true);
        viewPager.setAdapter(adapter);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }
*/

    private void initView() {
        editTextLEmail = (EditText) findViewById(R.id.etemail);
        editTextPassword = (EditText) findViewById(R.id.etpassWord);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonCustomFB = (Button) findViewById(R.id.buttonCustomFB);
        textViewRegister = (TextView) findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(this);
        buttonCustomFB.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);
        //  viewPager = (WrapContentViewPager) findViewById(R.id.viewPager);

    }

    private void setupFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        buttonFacebook = (LoginButton) findViewById(R.id.login_button);
        ArrayList<String> permissions = new ArrayList<String>();
        permissions.add("email");
        permissions.add("user_birthday");
        permissions.add("user_location");
        buttonFacebook.setReadPermissions(permissions);
        buttonFacebook.registerCallback(mCallbackManager, mCallBack);
        profile = Profile.getCurrentProfile();
/*

        if (profile != null) {
            showMessage("You are logged in");
            Intent i = new Intent(LoginActivity.this, DefaultIntro.class);
            startActivity(i);
            finish();

        }
*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,
                resultCode, data);

    }


    private void showMessage(String s) {
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(final LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        public String home;

                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            Log.i("facebook", response.toString());
                            // Application code
                            if (object != null) {
                                //parameters.putString("fields", "id,name,email,gender, birthday,picture ");
                                AccessToken accessToken = loginResult.getAccessToken();
                                Profile profile = Profile.getCurrentProfile();

                                try {
                                    fbName = object.getString("name");
                                } catch (JSONException e) {
                                    fbName = "";
                                    e.printStackTrace();
                                }
                                try {
                                    fbId = object.getString("id");
                                } catch (JSONException e) {
                                    fbId = "";
                                    e.printStackTrace();
                                }
                                try {
                                    fbEmail = object.getString("email");
                                } catch (JSONException e) {
                                    fbEmail = "";
                                    e.printStackTrace();
                                }
                                try {
                                    fbBirthday = object.getString("birthday");
                                } catch (JSONException e) {
                                    fbBirthday = "";
                                    e.printStackTrace();
                                }
                                try {
                                    fbGender = object.getString("gender");
                                } catch (JSONException e) {
                                    fbGender = "";
                                    e.printStackTrace();
                                }
                                showMessage("facebook done. check it");
                                String fbToken = accessToken.getToken();
                                if (fbEmail.equalsIgnoreCase("") || fbEmail.length() == 0) {
                                    sendtOTP = true;
                                }
                                facebooklogin();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday,location");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {
            Log.i("LoginActivity", error.toString());
        }
    };

    private void facebooklogin() {
    /*    getPermission();
        getLocation();*/
          /*
        * services.php?opt=register&facebook=1&fb_token=qwertyu&fb_id=asdfasdf232324&reg_id=asdfasdf232324
        * &name=Kunsang Wangyal&email=kunwangyal05@yahoo.com&profile_image=imageURL&dob=1987-09-12&country=India&country_code=91&send_otp=true&state=delhi&pin=201301&city=Delhi&contactno=7838610665&password=123456
        * */
        try {
            fbName = URLEncoder.encode(fbName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String baseurl = getString(R.string.url);
        String url = baseurl + "services.php?opt=register&facebook=1&fb_token=&fb_id" + fbId + "&reg_id=" + userSessionManager.getRegId()
                + "&name=" + fbName + "&email" + "" + "&dob=" + fbBirthday + "&country=" + country + "&country_code=" + country_code + "&send_otp=" + sendtOTP + "&password=";
        Log.i("kresult", url);
        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null) {
                            Log.i("kresult", result.toString());
                            if (result.get("status").getAsString().equalsIgnoreCase("success")) {
                                /*JsonArray data = result.getAsJsonArray("data");
                                JsonObject jsonObject = data.get(0).getAsJsonObject();
                                String user_id = jsonObject.get("user_id").getAsString();
                                userSessionManager.setUserId(user_id);
                                Intent intent = new Intent(context, OTPActivity.class);
                                startActivity(intent);*/

                            }
                        } else if (result.get("status").getAsString().equalsIgnoreCase("failed")) {
                            try {
                                JsonArray data = result.getAsJsonArray("data");
                                JsonObject jsonObject = data.get(0).getAsJsonObject();
                                if (jsonObject.get("msg").getAsString().equalsIgnoreCase("Please Enter Email ID")) {
                                    showEditPicPopup();
                                }

                            } catch (Exception ex) {

                            }

                        }
                    }
                });

    }
    private void showEditPicPopup() {
        boolean wrapInScrollView = true;
        dialogPic = new MaterialDialog.Builder(context)
                .title("Email required")
                .customView(R.layout.custom_email, wrapInScrollView)
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
         EditText etemailpopup= (EditText) dialogPic.findViewById(R.id.etemailpopup);
        Button button= (Button) dialogPic.findViewById(R.id.buttonDonePopup);
        dialogPic.findViewById(R.id.linearLayoutPicture).setOnClickListener(this);

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

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            try {
                knownName = addresses.get(0).getFeatureName();
            } catch (Exception ex) {

            }
            try {
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            } catch (Exception ex) {
            }
            try {
                city = addresses.get(0).getLocality();
            } catch (Exception ex) {
            }
            try {
                state = addresses.get(0).getAdminArea();
            } catch (Exception ex) {
            }
            try {
                country = addresses.get(0).getCountryName();
            } catch (Exception ex) {
                postalCode = addresses.get(0).getPostalCode();
            }

        } catch (IOException e) {
            Log.i("klocation", "unknown location");
        }


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
            case R.id.buttonCustomFB:
                buttonFacebook.performClick();
                showMessage("clicked");
                //login();
                break;
            case R.id.buttonSignIn:
                Intent i = new Intent(LoginActivity.this, DefaultIntro.class);
                startActivity(i);
                break;
            case R.id.textViewRegister:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void login() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://netforcesales.com/tripesplit/mobileApp/api/services.php?opt=register&email=kunwangyal05@yahoo.com&fb_token=qwertyu&name=Kunsang%20Wangyal&fb_id=asdfasdf232324&reg_id=dsfsdfdfsdf";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);
                Log.i("Response", loginResponse.toString());
                User user = loginResponse.getUserData().get(0);
                Log.i("response", user.getMsg());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mRequestQueue.add(stringRequest);
    }

    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                   /* Intent i = new Intent(LoginActivity.this, DefaultIntro.class);
                    startActivity(i);

                    finish();*/
                }
            }, SPLASH_TIME_OUT);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                }
            }, SPLASH_TIME_OUT);
        }
    }

}
