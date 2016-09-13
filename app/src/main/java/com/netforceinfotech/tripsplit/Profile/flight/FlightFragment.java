package com.netforceinfotech.tripsplit.Profile.flight;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.netforceinfotech.tripsplit.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class FlightFragment extends Fragment  implements View.OnClickListener, TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener
{

    FlightAdapter adapter;
    RecyclerView  recyclerView;
    Context context;
    TextView date_txt;

    public FlightFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view= inflater.inflate(R.layout.fragment_flight, container, false);
        context=getActivity();

        date_txt = (TextView) view.findViewById(R.id.date_text);

        date_txt.setOnClickListener(this);

        return view;
    }


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.date_text:

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        FlightFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");


                break;

        }


    }

    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
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
        try {
            date2 = date_format.parse(year+"-"+monthOfYear+"-"+dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("date======" + date2.toString());

        String day_txt = date2.toString().substring(0,3);

        String month_txt = date2.toString().substring(4, 7);
        date_txt.setText(day_txt + " " + dayOfMonth +" "+month_txt);

    }

}
