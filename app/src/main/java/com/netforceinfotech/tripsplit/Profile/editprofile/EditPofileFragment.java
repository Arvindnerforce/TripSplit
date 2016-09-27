package com.netforceinfotech.tripsplit.Profile.editprofile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netforceinfotech.tripsplit.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditPofileFragment extends Fragment implements  View.OnClickListener,TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener
{


    TextView dob_txt;
    LinearLayout linearlayoutdob;
    View view;;

    public EditPofileFragment()
    {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
       view =inflater.inflate(R.layout.fragment_edit_profile, container, false);

        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        linearlayoutdob = (LinearLayout) view.findViewById(R.id.linearlayoutdob);

        linearlayoutdob.setOnClickListener(this);

        dob_txt = (TextView) view.findViewById(R.id.dob_txt);
        setuptoolbar(view);
        return view;
    }



    private void setuptoolbar(View view)
    {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        ImageView home = (ImageView) getActivity().findViewById(R.id.homeButton);

        ImageView icon = (ImageView) getActivity().findViewById(R.id.image_appicon);

        ImageView logout = (ImageView) getActivity().findViewById(R.id.lagouttxt);

        home.setVisibility(View.VISIBLE);
        icon.setVisibility(View.VISIBLE);
        logout.setVisibility(View.INVISIBLE);

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }

    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = "You picked the following time: "+hourString+"h"+minuteString+"m"+secondString+"s";
        dob_txt.setText(time);
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
        dob_txt.setText(day_txt + " " + dayOfMonth +" "+month_txt);

    }


    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.linearlayoutdob:

               /* Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(getActivity(),
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
*/

                break;

        }


    }


}
