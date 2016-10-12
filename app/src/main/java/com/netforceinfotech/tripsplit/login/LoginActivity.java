package com.netforceinfotech.tripsplit.login;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.netforceinfotech.tripsplit.Dashboard.DashboardActivity;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.WrapContentViewPager;
import com.netforceinfotech.tripsplit.tutorial.DefaultIntro;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final long SPLASH_TIME_OUT = 2000;
    Button sign_button, buttonCustomFB;
    private CallbackManager mCallbackManager;
    private LoginButton buttonFacebook;
    private Profile profile;
    WrapContentViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_login);
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

        initViewPager();
        setupFacebook();

    }

    private void initViewPager() {
        final PagerAdapterLogin adapter = new PagerAdapterLogin
                (getSupportFragmentManager(), 2);
        viewPager.setPagingEnabled(true);
        viewPager.setAdapter(adapter);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }

    private void initView() {
        sign_button = (Button) findViewById(R.id.sign_button);
        buttonCustomFB = (Button) findViewById(R.id.buttonCustomFB);
        buttonCustomFB.setOnClickListener(this);
        sign_button.setOnClickListener(this);
        viewPager = (WrapContentViewPager) findViewById(R.id.viewPager);

    }

    private void setupFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        buttonFacebook = (LoginButton) findViewById(R.id.login_button);
        ArrayList<String> permissions = new ArrayList<String>();
        permissions.add("email");
        permissions.add("user_birthday");
        buttonFacebook.setReadPermissions(permissions);
        buttonFacebook.registerCallback(mCallbackManager, mCallBack);
        profile = Profile.getCurrentProfile();

        if (profile != null) {
            showMessage("You are logged in");
        }

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
                                String fbName;
                                try {
                                    fbName = object.getString("name");
                                } catch (JSONException e) {
                                    fbName = "";
                                    e.printStackTrace();
                                }
                                String fbId;
                                try {
                                    fbId = object.getString("id");
                                } catch (JSONException e) {
                                    fbId = "";
                                    e.printStackTrace();
                                }
                                String fbEmail;
                                try {
                                    fbEmail = object.getString("email");
                                } catch (JSONException e) {
                                    fbEmail = "";
                                    e.printStackTrace();
                                }
                                String fbBirthday;
                                try {
                                    fbBirthday = object.getString("birthday");
                                } catch (JSONException e) {
                                    fbBirthday = "";
                                    e.printStackTrace();
                                }
                                String fbGender;
                                try {
                                    fbGender = object.getString("gender");
                                } catch (JSONException e) {
                                    fbGender = "";
                                    e.printStackTrace();
                                }
                                showMessage("facebook done. check it");
                                String fbToken = accessToken.getToken();
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonCustomFB:
                buttonFacebook.performClick();
                break;
            case R.id.sign_button:
                Intent i = new Intent(LoginActivity.this, DefaultIntro.class);
                startActivity(i);
                break;
        }
    }
    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(i);

                    finish();
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
