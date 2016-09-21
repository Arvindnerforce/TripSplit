package com.netforceinfotech.tripsplit.Profile.editprofile;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class EditProfileActivity extends AppCompatActivity implements  View.OnClickListener,TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener
{

    EditText editAge ;
    TextView dob_txt;
    LinearLayout linearlayoutdob;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        linearlayoutdob = (LinearLayout) findViewById(R.id.linearlayoutdob);

        linearlayoutdob.setOnClickListener(this);

        dob_txt = (TextView) findViewById(R.id.dob_txt);


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

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        EditProfileActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");


                break;

        }


    }





}
