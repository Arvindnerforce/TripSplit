package com.netforceinfotech.tripsplit.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;
import com.netforceinfotech.tripsplit.tutorial.BaseIntro;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etotp;
    Button buttonVerify;
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        userSessionManager = new UserSessionManager(this);
        initView();
        setupToolBar("OTP verification");
    }

    private void initView() {
        etotp = (EditText) findViewById(R.id.etotp);
        buttonVerify = (Button) findViewById(R.id.buttonVerify);
        buttonVerify.setOnClickListener(this);
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

    private void setupToolBar(String app_name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(app_name);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonVerify:
                if (etotp.getText().toString().trim().length() <= 0) {
                    showMessage("Enter OTP");
                    return;
                }
                verifyOTP(etotp.getText().toString().trim());
                break;
        }
    }

    private void verifyOTP(String trim) {

        /*
        * services.php?opt=verifycustomer&otp=5018&id=1
        * */
        String baseUrl = getString(R.string.url);
        String url = baseUrl + "services.php?opt=verifycustomer&otp=" + trim + "&id=" + userSessionManager.getUserId();
        Ion.with(getApplicationContext())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result != null) {
                            Log.i("kresult", result.toString());
                            String status = result.get("status").getAsString();
                            if (status.equalsIgnoreCase("success")) {
                                showMessage("verified successfully");
                                Intent intent = new Intent(OTPActivity.this, BaseIntro.class);
                                startActivity(intent);
                            } else {
                                showMessage("OTP does not match");
                            }
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showMessage(String s) {

    }
}
