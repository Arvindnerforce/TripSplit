package com.netforceinfotech.tripsplit.NavigationView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.netforceinfotech.tripsplit.R;

import java.util.ArrayList;


public class MessageFragment extends Fragment

{



    private RecyclerView recyclerView;
    Context context;
    private LinearLayoutManager layoutManager;
    private MessageAdapter adapter;
    FrameLayout messageLayout;
    int theme;



    ArrayList<MessageFragmentData> highestDatas = new ArrayList<MessageFragmentData>();

    public MessageFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        context = getActivity();


        setupRecyclerView(view);
        return view;
    }

    private void setupRecyclerView(View view)
    {
        messageLayout = (FrameLayout) view.findViewById(R.id.messagelayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);



        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(context, highestDatas);
        recyclerView.setAdapter(adapter);
        setupFinsihedDatas();
        adapter.notifyDataSetChanged();
    }

    private void setupFinsihedDatas()
    {
        try
        {
            highestDatas.clear();
        } catch (Exception ex) {

        }
        highestDatas.add(new MessageFragmentData("Tea", "imageurl"));
        highestDatas.add(new MessageFragmentData("Tea", "imageurl"));
        highestDatas.add(new MessageFragmentData("Tea", "imageurl"));
        highestDatas.add(new MessageFragmentData("Tea", "imageurl"));
        highestDatas.add(new MessageFragmentData("Tea", "imageurl"));
        highestDatas.add(new MessageFragmentData("Tea", "imageurl"));
        highestDatas.add(new MessageFragmentData("Tea", "imageurl"));
        highestDatas.add(new MessageFragmentData("Tea", "imageurl"));
        highestDatas.add(new MessageFragmentData("Tea", "imageurl"));
        highestDatas.add(new MessageFragmentData("Tea", "imageurl"));
        highestDatas.add(new MessageFragmentData("Tea", "imageurl"));
        highestDatas.add(new MessageFragmentData("Tea", "imageurl"));
    }
}