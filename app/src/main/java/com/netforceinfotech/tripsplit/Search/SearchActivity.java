package com.netforceinfotech.tripsplit.Search;

import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.netforceinfotech.tripsplit.NavigationView.Message.MessageAdapter;
import com.netforceinfotech.tripsplit.NavigationView.Message.MessageFragmentData;
import com.netforceinfotech.tripsplit.R;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyFadeInAnimation;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private Calendar calendar;
    SearchData searchdata;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SearchAdapter adapter;
    Button sort_button;
    ArrayList<SearchData> highestDatas = new ArrayList<SearchData>();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        sort_button = (Button) findViewById(R.id.sortbutton);

        setSupportActionBar(toolbar);

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
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchAdapter(getApplicationContext(), highestDatas);
        recyclerView.setAdapter(adapter);
        setupFinsihedDatas();
        adapter.notifyDataSetChanged();

    }

    private void setupFinsihedDatas()
    {
        try
        {
            highestDatas.clear();
        }
        catch (Exception ex) {

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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDatePicker:
                DatePickerDialog.newInstance(SearchActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
            case R.id.btnTimePicker:
                TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;
        }
    }


    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        update();
    }


    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();
    }
    private void update() {
        /*lblDate.setText(dateFormat.format(calendar.getTime()));
        lblTime.setText(timeFormat.format(calendar.getTime()));*/
    }

}
