package com.netforceinfotech.tripsplit.Profile.flight;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.netforceinfotech.tripsplit.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class FlightFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    FlightAdapter adapter;
    RecyclerView recyclerView;
    Context context;
    TextView date_txt, pass_txt, space_txt;
    Button increamentPass, decreamentPass, increamentSpace, decreamentSpace, male, female, mixed;
    int pass_number = 1;
    int space_number = 1;


    public FlightFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_flight, container, false);
        context = getActivity();

        date_txt = (TextView) view.findViewById(R.id.date_text);

        date_txt.setOnClickListener(this);
        setupLayout(view);

        return view;
    }

    private void setupLayout(View view) {

        increamentPass = (Button) view.findViewById(R.id.increament_pass);

        decreamentPass = (Button) view.findViewById(R.id.decreament_pass);

        increamentSpace = (Button) view.findViewById(R.id.increament_Space);

        decreamentSpace = (Button) view.findViewById(R.id.decreament_Space);

        male = (Button) view.findViewById(R.id.maleButton);

        female = (Button) view.findViewById(R.id.femaleButton);

        mixed = (Button) view.findViewById(R.id.mixedButton);
        mixed.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        mixed.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

        pass_txt = (TextView) view.findViewById(R.id.pass_txt);

        space_txt = (TextView) view.findViewById(R.id.space_txt);

        increamentPass.setOnClickListener(this);
        decreamentPass.setOnClickListener(this);
        increamentSpace.setOnClickListener(this);
        decreamentSpace.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        mixed.setOnClickListener(this);

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
            case R.id.increament_pass:

                pass_number = pass_number + 1;
                String p_n = String.valueOf(pass_number);
                pass_txt.setText(p_n);


                break;

            case R.id.decreament_pass:

                if (pass_number >= 1) {
                    pass_number = pass_number - 1;
                    String dp_n = String.valueOf(pass_number);
                    pass_txt.setText(dp_n);
                }
                break;

            case R.id.increament_Space:

                space_number = space_number + 1;
                String sp_n = String.valueOf(space_number);
                space_txt.setText(sp_n);

                break;

            case R.id.decreament_Space:
                if (space_number >= 1) {
                    space_number = space_number - 1;
                    String dsp_n = String.valueOf(space_number);
                    space_txt.setText(dsp_n);
                }

                break;

            case R.id.maleButton:

                female.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_image));
                mixed.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_image2));
                male.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                male.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                female.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                mixed.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                break;

            case R.id.femaleButton:

                male.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_image));
                mixed.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_image2));
                female.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                female.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                male.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                mixed.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));


                break;

            case R.id.mixedButton:

                male.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_image));
                female.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_image));
                mixed.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                mixed.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                male.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                female.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));


                break;


        }


    }

    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";
        date_txt.append("  " + hourString + ":" + minuteString);
    }


    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: " + dayOfMonth + "/" + (++monthOfYear) + "/" + year;
        Date date2 = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date2 = date_format.parse(year + "-" + monthOfYear + "-" + dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("date======" + date2.toString());

        String day_txt = date2.toString().substring(0, 3);

        String month_txt = date2.toString().substring(4, 7);
        date_txt.setText(day_txt + " " + dayOfMonth + " " + month_txt);
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), true
        );
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }

}
