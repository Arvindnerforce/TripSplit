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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.vision.text.Text;
import com.google.gson.JsonObject;
import com.hedgehog.ratingbar.RatingBar;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    //Defining Variables
    private Toolbar toolbar;
    Context context;
    private DrawerLayout drawerLayout;
    NavigationFragment drawer;
    UserSessionManager userSessionManager;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE};
    int PERMISSION_ALL = 1;
    private float reviewRating = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_dashboard);
        // Initializing Toolbar and setting it as the actionbar
        context = this;
        userSessionManager = new UserSessionManager(context);
        setupToolbar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        setupNavigationCustom();
        getPermission();
        getReviewStatus();
        /*if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        }*/

    }

    private void getReviewStatus() {
        //showReviewPopUp("kunsang", "12 dec 1990", userSessionManager.getProfileImage(), "3");

        showRequiestReviewPopUp("kunsang", "12 dec 1990", userSessionManager.getProfileImage(), "3");
        //http://netforce.biz/tripesplit/mobileApp/api/services.php?opt=splitz_detail

        /*String baseUrl = getString(R.string.url);
        String url = baseUrl + "services.php?opt=splitz_detail";
        Ion.with(context)
                .load("POST", url)
                .setBodyParameter("user_id", userSessionManager.getUserId())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (result == null) {
                        } else {
                            if (result.get("status").getAsString().equalsIgnoreCase("success")) {
                                Log.i("result", result.toString());
                                showReviewPopUp("kunsang", "12 dec 1990", userSessionManager.getProfileImage(), "3");
                            }

                        }
                    }
                });*/

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

    private void showRequiestReviewPopUp(String name, String dob, String imageUrl, String trip_id) {
        final RatingBar ratingBar;

        final MaterialDialog requestReviewBox = new MaterialDialog.Builder(this)
                .customView(R.layout.request_review_popup, false)
                .show();

        requestReviewBox.setCanceledOnTouchOutside(false);
        requestReviewBox.findViewById(R.id.buttonSendRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("sending request");
            }
        });
        requestReviewBox.findViewById(R.id.buttonLater).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestReviewBox.dismiss();
            }
        });
        TextView textViewSource = (TextView) requestReviewBox.findViewById(R.id.textViewSource);
        TextView textViewDestination = (TextView) requestReviewBox.findViewById(R.id.textViewDestination);
        CircleImageView circleImageView = (CircleImageView) requestReviewBox.findViewById(R.id.imageViewTrip);
        Glide.with(context).load(imageUrl).error(R.drawable.ic_error).into(circleImageView);

    }

    private void showReviewPopUp(String name, String dob, String imageUrl, String trip_id) {
        final RatingBar ratingBar;

        final MaterialDialog reviewBox = new MaterialDialog.Builder(this)
                .customView(R.layout.review_pop_up, false)
                .show();

        reviewBox.setCanceledOnTouchOutside(false);

        reviewBox.findViewById(R.id.imageViewClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        final EditText editText = (EditText) reviewBox.findViewById(R.id.editText);
        CircleImageView imageViewDp = (CircleImageView) reviewBox.findViewById(R.id.imageViewDp);
        TextView textViewName, textViewDob;
        textViewDob = (TextView) reviewBox.findViewById(R.id.textViewDob);
        textViewName = (TextView) reviewBox.findViewById(R.id.textViewName);
        ratingBar = (RatingBar) reviewBox.findViewById(R.id.ratingbar);
        Glide.with(getApplicationContext()).load(imageUrl).error(R.drawable.ic_error).into(imageViewDp);
        textViewDob.setText(getFormattedDob(dob));
        textViewName.setText(name);
        ratingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                reviewRating = RatingCount;
            }
        });
        reviewBox.findViewById(R.id.imageViewClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewBox.dismiss();
            }
        });
        reviewBox.findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reviewRating == -1) {
                    showMessage("Please Rate this trip first!!!");
                    return;
                }
                if (editText.length() <= 0) {
                    showMessage("Please write few word about trip !!!");
                }
            }
        });
    }

    private String getFormattedDob(String dob) {
        return dob;
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
