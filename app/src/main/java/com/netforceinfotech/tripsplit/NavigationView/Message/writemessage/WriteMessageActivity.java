package com.netforceinfotech.tripsplit.NavigationView.Message.writemessage;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.netforceinfotech.tripsplit.NavigationView.Message.MessageAdapter;
import com.netforceinfotech.tripsplit.NavigationView.Message.MessageFragmentData;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.Search.SearchAdapter;

import java.util.ArrayList;

public class WriteMessageActivity extends AppCompatActivity
{


    private RecyclerView recyclerView;
    Context context;
    private LinearLayoutManager layoutManager;
    private WriteAdapter adapter;
    FrameLayout messageLayout;
    int theme;

    ArrayList<WriteData> highestDatas = new ArrayList<WriteData>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        setupRecyclerView();
    }

    private void setupRecyclerView()
    {

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new WriteAdapter(getApplicationContext(), highestDatas);
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
        highestDatas.add(new WriteData("Tea", "imageurl"));
        highestDatas.add(new WriteData("Tea", "imageurl"));
        highestDatas.add(new WriteData("Tea", "imageurl"));
        highestDatas.add(new WriteData("Tea", "imageurl"));
        highestDatas.add(new WriteData("Tea", "imageurl"));
        highestDatas.add(new WriteData("Tea", "imageurl"));
        highestDatas.add(new WriteData("Tea", "imageurl"));
        highestDatas.add(new WriteData("Tea", "imageurl"));
        highestDatas.add(new WriteData("Tea", "imageurl"));
        highestDatas.add(new WriteData("Tea", "imageurl"));
        highestDatas.add(new WriteData("Tea", "imageurl"));
        highestDatas.add(new WriteData("Tea", "imageurl"));


    }



}