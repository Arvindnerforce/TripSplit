package com.netforceinfotech.tripsplit.Search.searchfragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.Search.SearchAdapter;
import com.netforceinfotech.tripsplit.Search.SearchData;

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


public class CarFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener
{



    private Calendar calendar;
    SearchData searchdata;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    CarAdapter adapter;
    Button sort_button;
    ArrayList<CarData> highestDatas = new ArrayList<CarData>();
    TextView date_txt,travel_from,travel_to;

    Context context;

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

        sort_button = (Button) view.findViewById(R.id.sortbutton);

        setupRecyclerView(view);


        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(getActivity(), sort_button);

        droppyBuilder.addMenuItem(new DroppyMenuItem("Sort"));
        droppyBuilder.addMenuItem(new DroppyMenuItem("Refine"));
        droppyBuilder.addMenuItem(new DroppyMenuItem("Globe"));

        /*for (int i = 0; i < countries.size(); i++) {
            droppyBuilder.addMenuItem(new DroppyMenuItem(countries.get(i)));
        }*/
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface()
        {
            @Override
            public void call(View v, int id)
            {
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            }
        });
        droppyBuilder.build();

        return view;


    }


    private void setupRecyclerView(View v) {

        travel_from = (TextView) v.findViewById(R.id.travel_from);

        travel_from.setOnClickListener(this);

        travel_to = (TextView) v.findViewById(R.id.travel_to);

        travel_to.setOnClickListener(this);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);

        date_txt = (TextView) v.findViewById(R.id.date_text);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new CarAdapter(getActivity(), highestDatas);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        setupFinsihedDatas();
        adapter.notifyDataSetChanged();

        date_txt.setOnClickListener(this);

    }

    private void setupFinsihedDatas()
    {
        try
        {
            highestDatas.clear();
        }
        catch (Exception ex)
        {

        }
        highestDatas.add(new CarData("Tea", "imageurl"));
        highestDatas.add(new CarData("Tea", "imageurl"));
        highestDatas.add(new CarData("Tea", "imageurl"));
        highestDatas.add(new CarData("Tea", "imageurl"));
        highestDatas.add(new CarData("Tea", "imageurl"));
        highestDatas.add(new CarData("Tea", "imageurl"));
        highestDatas.add(new CarData("Tea", "imageurl"));
        highestDatas.add(new CarData("Tea", "imageurl"));
        highestDatas.add(new CarData("Tea", "imageurl"));
        highestDatas.add(new CarData("Tea", "imageurl"));
        highestDatas.add(new CarData("Tea", "imageurl"));
        highestDatas.add(new CarData("Tea", "imageurl"));


    }


    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.date_text:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(CarFragment.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.travel_from:

                Intent google_intent = new Intent(getActivity(), GoogleMapActivity.class);
                google_intent.putExtra("choose_source",true);
                startActivity(google_intent);

            case R.id.travel_to:

                Intent google_intent2 = new Intent(getActivity(), GoogleMapActivity.class);
                google_intent2.putExtra("choose_source",false);
                startActivity(google_intent2);


        }


    }

    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second)
    {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = "You picked the following time: "+hourString+"h"+minuteString+"m"+secondString+"s";
        date_txt.setText(time);
    }


    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {
        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        Date date2 = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            date2 = date_format.parse(year+"-"+monthOfYear+"-"+dayOfMonth);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        System.out.println("date======" + date2.toString());

        String day_txt = date2.toString().substring(0,3);

        String month_txt = date2.toString().substring(4, 7);
        date_txt.setText(day_txt + " " + dayOfMonth +" "+month_txt);

    }

}