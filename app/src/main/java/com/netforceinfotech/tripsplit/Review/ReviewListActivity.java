package com.netforceinfotech.tripsplit.Review;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.Search.SearchAdapter;
import com.netforceinfotech.tripsplit.Search.SearchData;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class ReviewListActivity extends AppCompatActivity implements View.OnClickListener {


    RecyclerView recyclerView;

    LinearLayoutManager layoutManager;
    ReviewAdapter adapter;
    ArrayList<ReviewData> highestDatas = new ArrayList<ReviewData>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupRecyclerView();

    }



    private void setupRecyclerView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);


        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReviewAdapter(getApplicationContext(), highestDatas);
        recyclerView.setAdapter(adapter);
        setupFinsihedDatas();
        adapter.notifyDataSetChanged();


    }

    private void setupFinsihedDatas()
    {
        try {
            highestDatas.clear();
        } catch (Exception ex) {

        }
        highestDatas.add(new ReviewData("Tea", "imageurl"));
        highestDatas.add(new ReviewData("Tea", "imageurl"));
        highestDatas.add(new ReviewData("Tea", "imageurl"));
        highestDatas.add(new ReviewData("Tea", "imageurl"));
        highestDatas.add(new ReviewData("Tea", "imageurl"));
        highestDatas.add(new ReviewData("Tea", "imageurl"));
        highestDatas.add(new ReviewData("Tea", "imageurl"));
        highestDatas.add(new ReviewData("Tea", "imageurl"));
        highestDatas.add(new ReviewData("Tea", "imageurl"));
        highestDatas.add(new ReviewData("Tea", "imageurl"));
        highestDatas.add(new ReviewData("Tea", "imageurl"));
        highestDatas.add(new ReviewData("Tea", "imageurl"));


    }

    public void  onClick(View view)
    {




    }



}
