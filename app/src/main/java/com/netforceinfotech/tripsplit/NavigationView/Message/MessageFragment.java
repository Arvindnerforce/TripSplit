package com.netforceinfotech.tripsplit.NavigationView.Message;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.netforceinfotech.tripsplit.NavigationView.Message.writemessage.WriteAdapter;
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

        setuptoolbar(view);

        setupRecyclerView(view);
        return view;
    }


    private void setuptoolbar(View view)
    {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        FrameLayout home = (FrameLayout) getActivity().findViewById(R.id.homebutton);

        ImageView icon = (ImageView) getActivity().findViewById(R.id.image_appicon);

        FrameLayout logout = (FrameLayout) getActivity().findViewById(R.id.logoutbutton);

        home.setVisibility(View.VISIBLE);
        icon.setVisibility(View.VISIBLE);
        logout.setVisibility(View.INVISIBLE);

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

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



    public void onClick(View view, int position)
    {
        // The onClick implementation of the RecyclerView item click

    }

    private void setupFinsihedDatas()
    {
        try
        {
            highestDatas.clear();
        }
        catch (Exception ex) {

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