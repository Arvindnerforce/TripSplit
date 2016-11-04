package com.netforceinfotech.tripsplit.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;
import com.netforceinfotech.tripsplit.util.Validation;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    UserSessionManager userSessionManager;
    Context context;
    Button buttonSignIn;
    EditText etname, etemail, etpassword, etconfirmpassword, etdob, etcountry, etaddress;
    String name, email, password, confirmpassword, dob, country, address, country_code = "";
    ArrayList<CountryData> countries = new ArrayList<>();
    private MaterialDialog progressDialog;
    Button buttonOk;
    EditText etDay, etMonth, etYear;
    private MaterialDialog dialogDoB;
    private String dobString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = this;
        userSessionManager = new UserSessionManager(context);
        initView();
        setupToolBar("Sign Up!");
        getPermission();
    }


    private void setupToolBar(String app_name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(app_name);


    }

    private void initView() {

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0).build();
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        etname = (EditText) findViewById(R.id.etname);
        etemail = (EditText) findViewById(R.id.etemail);
        etpassword = (EditText) findViewById(R.id.etpassword);
        etconfirmpassword = (EditText) findViewById(R.id.etconfirmpassword);
        etdob = (EditText) findViewById(R.id.etdob);
        etcountry = (EditText) findViewById(R.id.etcountry);
        etaddress = (EditText) findViewById(R.id.etaddress);
        buttonSignIn.setOnClickListener(this);
        etdob.setOnClickListener(this);
        etcountry.setOnClickListener(this);
        try {

            CountryPicker picker = CountryPicker.newInstance("Select CountryData");
            Country countryData = picker.getUserCountryInfo(this);
            etcountry.setText(countryData.getName());
        } catch (Exception ex) {

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
            case R.id.etcountry:
                showCountryPopUp();
                break;
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
                register(name, email, password, country, address, dob);
                break;
            case R.id.etdob:
                showEditDoBPopup();
                break;
            case R.id.buttonOk:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date test = sdf.parse(etYear.getText().toString() + "-" + etMonth.getText().toString() + "-" + etDay.getText().toString());
                } catch (ParseException pe) {
                    //Date is invalid, try next format
                    showMessage("Not valid date");
                    return;
                }

                dobString = etDay.getText().toString() + "/" + etMonth.getText().toString() + "/" + etYear.getText().toString();
                etdob.setText(dobString);
                dialogDoB.dismiss();
                break;

        }

    }

    private void showCountryPopUp() {
        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                etcountry.setText(name);
                country_code = code;
                country = name;
                picker.dismiss();
            }
        });
    }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    private void register(String name, final String email, String password, String country, String address, String dob) {
        showProgressbar();
        try {
            name = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            address = URLEncoder.encode(address, "UTF-8");
        } catch (Exception ex) {

        }
        try {
            country = URLEncoder.encode(country, "UTF-8");
        } catch (Exception ex) {

        }
        password=password.trim();
        dob = getServerDateFormat(dob);
        String baseUrl = getString(R.string.url);
        String url = baseUrl + "services.php?opt=register&facebook=0&fb_token=&fb_id=&reg_id=" + userSessionManager.getRegId() + "&name=" + name
                + "&email=" + email + "&dob=" + dob + "&country=" + country + "&country_code=" + country_code + "&send_otp=true&password=" + password + "&address=" + address + "&send_otp=1";
        Log.i("kurl", url);
        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        dismissProgressbar();
                        if (result != null) {
                            Log.i("kresult", result.toString());
                            if (result.get("status").getAsString().equalsIgnoreCase("success")) {
                                JsonArray data = result.getAsJsonArray("data");
                                JsonObject jsonObject = data.get(0).getAsJsonObject();
                                String user_id = jsonObject.get("user_id").getAsString();
                                userSessionManager.setUserId(user_id);
                                userSessionManager.setEmail(email);
                                Intent intent = new Intent(context, OTPActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            showMessage("something went wrong. Please check internet connection");
                        }
                    }
                });
    }

    private void dismissProgressbar() {
        progressDialog.dismiss();
    }

    private void showProgressbar() {
        progressDialog.show();
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

    private void showEditDoBPopup() {

        boolean wrapInScrollView = true;
        dialogDoB = new MaterialDialog.Builder(context)
                .title(R.string.editdob)
                .customView(R.layout.editdob, wrapInScrollView)
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
        buttonOk = (Button) dialogDoB.findViewById(R.id.buttonOk);
        etDay = (EditText) dialogDoB.findViewById(R.id.etDay);
        etMonth = (EditText) dialogDoB.findViewById(R.id.etMonth);
        etYear = (EditText) dialogDoB.findViewById(R.id.etYear);
        buttonOk.setOnClickListener(this);
        etDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etDay.length() == 2) {
                    int day = Integer.parseInt(etDay.getText().toString());
                    if (day > 31) {
                        showMessage("Invalid date");
                        return;
                    }
                    etMonth.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etMonth.length() == 2) {
                    int day = Integer.parseInt(etMonth.getText().toString());
                    if (day > 12) {
                        showMessage("Invalid month");
                        return;
                    }
                    etYear.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etYear.length() == 4) {
                    int dobYear = Integer.parseInt(etYear.getText().toString());
                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    if (dobYear > year) {
                        showMessage("DoB cannot be in future");
                        return;
                    }
                    buttonOk.performClick();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private String getFormattedDate(String dob) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = fmt.parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        return fmtOut.format(date);
    }

    private String getServerDateFormat(String s) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = fmt.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy-MM-dd");
        return fmtOut.format(date);
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
        dob = etdob.getText().toString();

    }
}
