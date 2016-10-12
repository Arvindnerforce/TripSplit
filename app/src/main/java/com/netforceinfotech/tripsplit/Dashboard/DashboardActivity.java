package com.netforceinfotech.tripsplit.Dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.netforceinfotech.tripsplit.Home.HomeFragment;
import com.netforceinfotech.tripsplit.R;

public class DashboardActivity extends AppCompatActivity {
    //Defining Variables
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationFragment drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_dashboard);
        // Initializing Toolbar and setting it as the actionbar
        setupToolbar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        setupNavigationCustom();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textViewLogout = (TextView) toolbar.findViewById(R.id.textviewLogout);
        ImageView homeButton = (ImageView) toolbar.findViewById(R.id.homeButton);
        homeButton.setVisibility(View.GONE);
        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LoginManager.getInstance().logOut();
                    finish();
                } catch (Exception ex) {

                }

            }
        });
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
        } else {
            super.onBackPressed();
        }

    }


}
