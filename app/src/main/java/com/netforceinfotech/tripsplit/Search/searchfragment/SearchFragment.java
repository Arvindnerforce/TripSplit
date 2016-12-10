package com.netforceinfotech.tripsplit.Search.searchfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.Search.SearchData;
import com.netforceinfotech.tripsplit.Search.searchfragment.subfragment.CarAdapter;
import com.netforceinfotech.tripsplit.Search.searchfragment.subfragment.RecyclerViewFragment;
import com.netforceinfotech.tripsplit.general.UserSessionManager;
import com.netforceinfotech.tripsplit.posttrip.GoogleMapActivity;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class SearchFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, GoogleMapActivity.AddressListner {
    String sort = "Best match";
    private static final int TRAVEL_TO = 2;
    private static final int TRAVEL_FROM = 1;
    private Calendar calendar;
    SearchData searchdata;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    CarAdapter adapter;
    public TextView date_txt, travel_from, travel_to;
    RelativeLayout relativeLayoutSort, relativeLayoutGlobe;
    LinearLayout linearLayoutRefine, linearlayoutSearch;

    Context context;
    private SearchClickedListner searchClickedListner;
    private RecyclerViewFragment dashboardFragment;
    private double dest_lat, dest_lon, source_lat, source_lon;
    UserSessionManager userSessionManager;
    TextView textViewSort;
    String type;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment, container, false);
        context = getActivity();
        userSessionManager = new UserSessionManager(context);
        type = this.getArguments().getString("type");
        initView(view);


        return view;


    }

    private void initView(View view) {
        textViewSort = (TextView) view.findViewById(R.id.textViewSort);
        linearlayoutSearch = (LinearLayout) view.findViewById(R.id.linearlayoutSearch);
        linearlayoutSearch.setOnClickListener(this);
        travel_from = (TextView) view.findViewById(R.id.travel_from);
        travel_from.setOnClickListener(this);
        travel_to = (TextView) view.findViewById(R.id.travel_to);
        travel_to.setOnClickListener(this);
        date_txt = (TextView) view.findViewById(R.id.textviewETD);
        date_txt.setOnClickListener(this);
        relativeLayoutGlobe = (RelativeLayout) view.findViewById(R.id.relativeLayoutGlobe);
        relativeLayoutSort = (RelativeLayout) view.findViewById(R.id.relativeLayoutSort);
        linearLayoutRefine = (LinearLayout) view.findViewById(R.id.linearlayoutRefine);
        linearLayoutRefine.setOnClickListener(this);
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(getActivity(), relativeLayoutSort);
        final ArrayList<String> sorts = new ArrayList<>();
        sorts.add(getString(R.string.best_match));
        sorts.add(getString(R.string.low_cost));
        sorts.add(getString(R.string.highest_cost));
        sorts.add(getString(R.string.date));
        sort = sorts.get(0);
        droppyBuilder.addMenuItem(new DroppyMenuItem(getString(R.string.best_match)));
        droppyBuilder.addMenuItem(new DroppyMenuItem(getString(R.string.low_cost)));
        droppyBuilder.addMenuItem(new DroppyMenuItem(getString(R.string.highest_cost)));
        droppyBuilder.addMenuItem(new DroppyMenuItem(getString(R.string.date)));
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                sort = sorts.get(id);
            }
        });
        droppyBuilder.build();
        setupRecyclerViewdFragment();


    }


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.linearlayoutSearch:
                if (travel_from.getText().length() <= 0 || travel_to.getText().length() <= 0 || date_txt.getText().length() <= 0) {
                    showMessage(getString(R.string.cantbeempy));
                    return;
                } else {
                    searchTrip();
                }
                break;
            case R.id.linearlayoutRefine:
                clearAllData();
                break;
            case R.id.textviewETD:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(SearchFragment.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.travel_from:
                Intent google_intent = new Intent(getActivity(), GoogleMapActivity.class);
                google_intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                google_intent.putExtra("choose_source", true);
                startActivityForResult(google_intent, TRAVEL_FROM);
            case R.id.travel_to:
                Intent google_intent2 = new Intent(getActivity(), GoogleMapActivity.class);
                google_intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                google_intent2.putExtra("choose_source", false);
                startActivityForResult(google_intent2, TRAVEL_TO);
        }


    }

    private void searchTrip() {
        JsonObject json = new JsonObject();
        json.addProperty("dest_lat", dest_lat);
        json.addProperty("dest_lon", dest_lon);
        json.addProperty("source_lat", source_lat);
        json.addProperty("source_lon", source_lon);
        json.addProperty("etd", date_txt.getText().toString());
        json.addProperty("range", userSessionManager.getSearchRadius());
        json.addProperty("sort", sort);
        json.addProperty("type", type);
        Log.i("type",type);
        String baseUrl = getString(R.string.url);
        //http://netforce.biz/tripesplit/mobileApp/api/services.php?opt=search_trip
        String url = baseUrl + "services.php?opt=search_trip";

        Ion.with(context)
                .load(url)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (result == null) {
                            showMessage("something wrong");
                        } else {
                            Log.i("result", result.toString());
                        }
                    }
                });
    }

    private void clearAllData() {
        try {

        } catch (Exception ex) {

        }
        travel_from.setText("Travel From");
        travel_to.setText("Travel To");
        date_txt.setText("Select date and time");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == TRAVEL_TO) {
            String address = data.getStringExtra("address");
            dest_lat = data.getDoubleExtra("lat", 0);
            dest_lon = data.getDoubleExtra("lon", 0);
            travel_to.setText(address);
        }
        if (resultCode == getActivity().RESULT_OK && requestCode == TRAVEL_FROM) {
            String address = data.getStringExtra("address");
            source_lat = data.getDoubleExtra("lat", 0);
            source_lon = data.getDoubleExtra("lon", 0);
            travel_from.setText(address);
        }
    }

    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";
        date_txt.append(" " + hourString + ":" + minuteString);
    }


    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Date date2 = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date2 = date_format.parse(year + "-" + monthOfYear + "-" + dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat outDate = new SimpleDateFormat("EEE dd MMM yy");


        date_txt.setText(outDate.format(date2));

        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), true
        );
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");

    }

    @Override
    public void gotAddress(String address, boolean source) {
        if (source) {
            travel_from.setText(address);
        } else {
            travel_to.setText(address);
        }

    }

    private void replaceFragment(Fragment newFragment, String tag) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.drawer_layout, newFragment, tag);
        transaction.commit();
    }

    private void setupRecyclerViewdFragment() {
        dashboardFragment = new RecyclerViewFragment();
        String tagName = dashboardFragment.getClass().getName();
        replaceFragment(dashboardFragment, tagName);

    }


    private void showMessage(String clicked) {
        Toast.makeText(context, clicked, Toast.LENGTH_SHORT).show();
    }

    public interface SearchClickedListner {
        public void clicked(String source, String destination);
    }

    public void setSearchClickedListner(SearchClickedListner searchClickedListner) {
        this.searchClickedListner = searchClickedListner;
    }
}