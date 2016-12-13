package com.netforceinfotech.tripsplit.Search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.netforceinfotech.tripsplit.R;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.Date;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,  TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener
{

    private Calendar calendar;
    SearchData searchdata;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SearchAdapter adapter;
    Button sort_button;
    ArrayList<SearchData> highestDatas = new ArrayList<SearchData>();
    TextView date_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sort_button = (Button) findViewById(R.id.sortbutton);
        Calendar now;

        setupRecyclerView();

        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, sort_button);

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
                Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
            }
        });
        droppyBuilder.build();


    }


    private void setupRecyclerView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerMyGroup);

        date_txt = (TextView) findViewById(R.id.textviewETD);

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchAdapter(getApplicationContext(), highestDatas);
        recyclerView.setAdapter(adapter);
        setupFinsihedDatas();
        adapter.notifyDataSetChanged();

        date_txt.setOnClickListener(this);

    }

    private void setupFinsihedDatas()
    {
        try {
            highestDatas.clear();
        } catch (Exception ex) {

        }
        highestDatas.add(new SearchData("Tea", "imageurl"));
        highestDatas.add(new SearchData("Tea", "imageurl"));
        highestDatas.add(new SearchData("Tea", "imageurl"));
        highestDatas.add(new SearchData("Tea", "imageurl"));
        highestDatas.add(new SearchData("Tea", "imageurl"));
        highestDatas.add(new SearchData("Tea", "imageurl"));
        highestDatas.add(new SearchData("Tea", "imageurl"));
        highestDatas.add(new SearchData("Tea", "imageurl"));
        highestDatas.add(new SearchData("Tea", "imageurl"));
        highestDatas.add(new SearchData("Tea", "imageurl"));
        highestDatas.add(new SearchData("Tea", "imageurl"));
        highestDatas.add(new SearchData("Tea", "imageurl"));


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();


                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

   public void  onClick(View view)
{

    switch (view.getId())
    {
        case R.id.textviewETD:

            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    SearchActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.show(getFragmentManager(), "Datepickerdialog");


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
