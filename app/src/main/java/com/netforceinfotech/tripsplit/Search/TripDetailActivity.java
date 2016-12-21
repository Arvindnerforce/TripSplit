package com.netforceinfotech.tripsplit.Search;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.netforceinfotech.tripsplit.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

public class TripDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    RelativeLayout reviewlayout;
    Button buttonBookIt;
    LinearLayout linearLayoutReturn;
    TextView textViewETDReturn, textViewETAReturn, textViewCountryCode, textViewDateCreated, textViewName, textViewAge, textViewAddress, textViewTrip, textViewAboutMe, textViewETD, textViewETA, textViewSpace, textViewDate, textViewPax, textViewAgeGroup, textViewTripSplit, textViewItenerary, textViewTotalCost, textViewYourShare;
    ImageView imageViewDp, imageViewStar1, imageViewStar2, imageViewStar3, imageViewStar4, imageViewStar5, imageViewEmail, imageViewMessage, imageViewType, imageViewTrip;

    private void initView() {
        buttonBookIt = (Button) findViewById(R.id.buttonBookIt);
        buttonBookIt.setOnClickListener(this);
        linearLayoutReturn = (LinearLayout) findViewById(R.id.linearLayoutReturn);
        textViewETDReturn = (TextView) findViewById(R.id.textViewETDReturn);
        textViewETAReturn = (TextView) findViewById(R.id.textViewETAReturn);
        linearLayoutReturn.setVisibility(View.GONE);
        imageViewTrip = (ImageView) findViewById(R.id.imageViewTrip);
        reviewlayout = (RelativeLayout) findViewById(R.id.reviewlayout);
        textViewCountryCode = (TextView) findViewById(R.id.textViewCountryCode);
        textViewDateCreated = (TextView) findViewById(R.id.textViewDateCreated);
        textViewName = (TextView) findViewById(R.id.textviewName);
        textViewAge = (TextView) findViewById(R.id.textViewAge);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewTrip = (TextView) findViewById(R.id.textViewTrip);
        textViewAboutMe = (TextView) findViewById(R.id.textViewAboutMe);
        textViewETD = (TextView) findViewById(R.id.textViewETD);
        textViewETA = (TextView) findViewById(R.id.textViewETA);
        textViewSpace = (TextView) findViewById(R.id.textViewSpace);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewPax = (TextView) findViewById(R.id.textViewPax);
        textViewAgeGroup = (TextView) findViewById(R.id.textViewAgeGroup);
        textViewTripSplit = (TextView) findViewById(R.id.textViewTripSplit);
        textViewItenerary = (TextView) findViewById(R.id.textViewItenerary);
        textViewTotalCost = (TextView) findViewById(R.id.textViewTotalCost);
        textViewYourShare = (TextView) findViewById(R.id.textViewYourShare);
        imageViewDp = (ImageView) findViewById(R.id.imageViewDp);
        imageViewStar1 = (ImageView) findViewById(R.id.imageViewStar1);
        imageViewStar2 = (ImageView) findViewById(R.id.imageViewStar2);
        imageViewStar3 = (ImageView) findViewById(R.id.imageViewStar3);
        imageViewStar4 = (ImageView) findViewById(R.id.imageViewStar4);
        imageViewStar5 = (ImageView) findViewById(R.id.imageViewStar5);
        imageViewEmail = (ImageView) findViewById(R.id.imageViewEmail);
        imageViewMessage = (ImageView) findViewById(R.id.imageViewMessage);
        imageViewType = (ImageView) findViewById(R.id.imageViewType);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        context = this;
        Bundle bundle = getIntent().getExtras();
        String trip_id = bundle.getString("trip_id");
        setupToolBar("Trip Detail");
        initView();
        getTripDetail(trip_id);

    }


    private void setupToolBar(String app_name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(app_name);


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

    private void getTripDetail(String trip_id) {
        //http://netforce.biz/tripesplit/mobileApp/api/services.php?opt=splitz_detail
        String baseUrl = getString(R.string.url);
        String url = baseUrl + "services.php?opt=splitz_detail";
        Ion.with(context)
                .load("POST", url)
                .setBodyParameter("trip_id", trip_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (result == null) {
                            showMessage("something wrong");
                        } else {
                            if (result.get("status").getAsString().equalsIgnoreCase("success")) {
                                Log.i("result", result.toString());
                                JsonObject data = result.getAsJsonObject("data");
                                JsonObject my_splitz = data.getAsJsonObject("my_splitz");
                                setupTripDetail(my_splitz);

                            }

                        }
                    }
                });

    }

    private void setupTripDetail(JsonObject my_splitz) {
        String tour_id, start_price, date_created, user_id, cartype, pax, space, trip_group, age_group_lower, age_group_upper, trip, vehical_type,
                depart_address, country_code, etd, eta, iteinerary, image_name, currency,
                return_eta, return_etd, dest_address, id, username, email, profile_image, dob, address, country, aboutme, your_share, created_date;

        tour_id = my_splitz.get("tour_id").getAsString();
        start_price = my_splitz.get("start_price").getAsString();
        date_created = my_splitz.get("date_created").getAsString();
        cartype = my_splitz.get("cartype").getAsString();
        pax = my_splitz.get("pax").getAsString();
        space = my_splitz.get("space").getAsString();
        trip_group = my_splitz.get("trip_group").getAsString();
        age_group_lower = my_splitz.get("age_group_lower").getAsString();
        age_group_upper = my_splitz.get("age_group_upper").getAsString();
        trip = my_splitz.get("trip").getAsString();
        vehical_type = my_splitz.get("vehical_type").getAsString();
        depart_address = my_splitz.get("depart_address").getAsString();
        dest_address = my_splitz.get("dest_address").getAsString();

        country_code = my_splitz.get("country_code").getAsString();
        etd = my_splitz.get("etd").getAsString();
        eta = my_splitz.get("eta").getAsString();
        iteinerary = my_splitz.get("iteinerary").getAsString();
        image_name = my_splitz.get("image_name").getAsString();
        currency = my_splitz.get("currency").getAsString();
        return_eta = my_splitz.get("return_eta").getAsString();
        return_etd = my_splitz.get("return_etd").getAsString();
        id = my_splitz.get("id").getAsString();
        username = my_splitz.get("username").getAsString();
        email = my_splitz.get("email").getAsString();
        profile_image = my_splitz.get("profile_image").getAsString();
        dob = my_splitz.get("dob").getAsString();
        address = my_splitz.get("address").getAsString();
        country = my_splitz.get("country").getAsString();
        aboutme = my_splitz.get("aboutme").getAsString();
        your_share = my_splitz.get("your_share").getAsString();
        created_date = my_splitz.get("created_date").getAsString();
        Glide.with(context).load(image_name).error(R.drawable.ic_blank).into(imageViewTrip);
        textViewCountryCode.setText(country_code);
        textViewDateCreated.setText(getFormatedDate(created_date, 1));
        Glide.with(context).load(profile_image).error(R.drawable.ic_error).into(imageViewDp);
        textViewName.setText(username);
        setAge(dob, textViewAge);
        textViewAddress.setText(address);
        imageViewEmail.setOnClickListener(this);
        imageViewMessage.setOnClickListener(this);
        switch (cartype) {
            case "car":
                Glide.with(context).load(R.drawable.ic_car_primary).into(imageViewType);
                break;
            case "aeroplane":
                Glide.with(context).load(R.drawable.ic_redplane).into(imageViewType);

                break;
            case "bus":
                Glide.with(context).load(R.drawable.ic_bus_primary).into(imageViewType);

                break;
            case "ship":
                Glide.with(context).load(R.drawable.ic_ship_primary).into(imageViewType);

                break;
        }
        textViewTrip.setText(depart_address + " TO " + dest_address);
        textViewAboutMe.setText(aboutme);
        reviewlayout.setOnClickListener(this);
        textViewETD.setText(getFormetedTime(etd));
        textViewETA.setText(getFormetedTime(eta));
        textViewSpace.setText(space);
        textViewDate.setText(getFormatedDate(etd));
        textViewPax.setText(pax);
        textViewAgeGroup.setText(age_group_lower + " - " + age_group_upper);
        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        Currency currency1 = picker.getCurrencyCode(currency);
        String currencySymbol = currency1.getSymbol();
        textViewTripSplit.setText(currency + " " + currencySymbol + " " + start_price);
        textViewItenerary.setText(iteinerary);
        textViewTotalCost.setText(currency + " " + currencySymbol + " " + start_price);
        textViewYourShare.setText(your_share);
        if (trip.equalsIgnoreCase("1")) {
            linearLayoutReturn.setVisibility(View.VISIBLE);
            textViewETAReturn.setText(return_eta);
            textViewETDReturn.setText(return_etd);
        }


    }


    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewEmail:
                break;
            case R.id.imageViewMessage:
                break;
            case R.id.buttonBookIt:
                break;
            case R.id.reviewlayout:
                break;


        }

    }

    private void setAge(String dob, TextView textView) {
        if (dob.equalsIgnoreCase("0000-00-00")) {
            textView.setText("NA");
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(simpleDateFormat.parse(dob));// all done
                String age = getAge(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                textView.setText(age);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    private String getFormatedDate(String created_date, int i) {
        //2016-11-08
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = fmt.parse(created_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("dd MMM yyyy");
        return fmtOut.format(date);
    }

    private String getFormatedDate(String etd) {
        //Wed 21 Dec 2016 19:02
        SimpleDateFormat fmt = new SimpleDateFormat("EEE dd MMM yyyy HH:mm");
        Date date = null;
        try {
            date = fmt.parse(etd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("EEE dd, MMM yyyy");
        return fmtOut.format(date);
    }

    private String getFormetedTime(String etd) {
        SimpleDateFormat fmt = new SimpleDateFormat("EEE dd MMM yyyy HH:mm");
        Date date = null;
        try {
            date = fmt.parse(etd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm");
        return fmtOut.format(date);
    }

}
