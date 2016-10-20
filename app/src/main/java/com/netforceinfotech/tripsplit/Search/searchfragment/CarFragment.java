package com.netforceinfotech.tripsplit.Search.searchfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.Search.SearchData;
import com.netforceinfotech.tripsplit.Search.searchfragment.subfragment.CarAdapter;
import com.netforceinfotech.tripsplit.Search.searchfragment.subfragment.CarData;
import com.netforceinfotech.tripsplit.Search.searchfragment.subfragment.RecyclerViewFragment;
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


public class CarFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, GoogleMapActivity.AddressListner {


    private Calendar calendar;
    SearchData searchdata;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    CarAdapter adapter;
    public TextView date_txt, travel_from, travel_to;
    RelativeLayout relativeLayoutSort, relativeLayoutGlobe;
    LinearLayout linearLayoutRefine;

    Context context;
    private SearchClickedListner searchClickedListner;
    private RecyclerViewFragment dashboardFragment;

    public CarFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_car, container, false);
        context = getActivity();
        initView(view);


        return view;


    }

    private void initView(View view) {
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

        droppyBuilder.addMenuItem(new DroppyMenuItem("Sort"));
        droppyBuilder.addMenuItem(new DroppyMenuItem("Refine"));
        droppyBuilder.addMenuItem(new DroppyMenuItem("Globe"));
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            }
        });
        droppyBuilder.build();
        setupRecyclerViewdFragment();


    }


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.linearlayoutRefine:
                clearAllData();
                break;
            case R.id.textviewETD:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(CarFragment.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.travel_from:
                Intent google_intent = new Intent(getActivity(), GoogleMapActivity.class);
                google_intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                google_intent.putExtra("choose_source", true);
                startActivityForResult(google_intent, 1);
            case R.id.travel_to:
                Intent google_intent2 = new Intent(getActivity(), GoogleMapActivity.class);
                google_intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                google_intent2.putExtra("choose_source", false);
                startActivityForResult(google_intent2, 2);
        }


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
        if (resultCode == getActivity().RESULT_OK && requestCode == 2) {
            String address = data.getStringExtra("address");
            travel_to.setText(address);
        }
        if (resultCode == getActivity().RESULT_OK && requestCode == 1) {
            String address = data.getStringExtra("address");
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

    public void clicked() {
        showMessage("clicked adapter");
        dashboardFragment.updateTextValue(travel_from.getText().toString(), travel_to.getText().toString());
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