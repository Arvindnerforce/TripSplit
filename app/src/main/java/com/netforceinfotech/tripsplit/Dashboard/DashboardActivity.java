package com.netforceinfotech.tripsplit.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;

public class DashboardActivity extends AppCompatActivity {
    //Defining Variables
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationFragment drawer;
    UserSessionManager userSessionManager;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE};
    int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_dashboard);
        // Initializing Toolbar and setting it as the actionbar
        userSessionManager = new UserSessionManager(getApplicationContext());
        setupToolbar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        setupNavigationCustom();
        //   getPermission();
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        }

    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textViewLogout = (TextView) toolbar.findViewById(R.id.textviewLogout);
        ImageView homeButton = (ImageView) toolbar.findViewById(R.id.homeButton);
        ImageView image_appicon = (ImageView) toolbar.findViewById(R.id.image_appicon);
        image_appicon.setVisibility(View.GONE);
        Glide.with(this)
                .fromResource()
                .asBitmap()
                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG, 100))
                .load(R.drawable.trip_splitz_logo_red_bg).into(image_appicon);
        homeButton.setVisibility(View.GONE);
        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp();


            }
        });
    }

    private void getPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String[] permission = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };

            ActivityCompat.requestPermissions(this,
                    permission, 1);


        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showPopUp() {
        new MaterialDialog.Builder(this)
                .title("Log out")
                .content("Are you sure you want to Log out?")
                .positiveText("Yes")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        userSessionManager.setIsLoggedIn(false);
                        userSessionManager.clearData();
                        try {
                            LoginManager.getInstance().logOut();
                            finish();
                        } catch (Exception ex) {

                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void setupNavigationCustom() {
        drawer = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        drawer.setup(R.id.fragment, (DrawerLayout) findViewById(R.id.drawer), toolbar);
    }

    @Override
    public void onBackPressed() {
        if (NavigationFragment.POSITION != 0) {
            drawerLayout.closeDrawers();
            drawer.setupHomeFragment();
            NavigationFragment.POSITION = 0;
        } else {
            ActivityCompat.finishAffinity(this);
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
