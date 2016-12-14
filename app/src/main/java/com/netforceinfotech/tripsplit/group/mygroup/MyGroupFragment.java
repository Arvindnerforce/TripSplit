package com.netforceinfotech.tripsplit.group.mygroup;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroupFragment extends Fragment {

    Context context;
    LinearLayout linearlayoutNoGroup;
    ArrayList<MyData> myDatas = new ArrayList<>();
    private MyAdapter myAdapter;
    UserSessionManager userSessionManager;
    DatabaseReference _user_group;

    public MyGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_group, container, false);
        context = getActivity();
        userSessionManager = new UserSessionManager(context);
        setupRecyclerView(view);
        initView(view);
        setupFirebase();
        setupData();
        return view;
    }

    private void initView(View view) {
        linearlayoutNoGroup = (LinearLayout) view.findViewById(R.id.linearlayoutNoGroup);

    }

    private void setupFirebase() {
        FirebaseDatabase _root = FirebaseDatabase.getInstance();
        _user_group = _root.getReference().child("user_group");
        _user_group.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userSessionManager.getUserId())) {
                    // run some code
                    linearlayoutNoGroup.setVisibility(View.GONE);
                } else {
                    linearlayoutNoGroup.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setupData() {
        MyData myData = new MyData("", "Test1", "Kunsang", "test message", "Sun 23 Nov 2016 15:13", "12");
        if (!myDatas.contains(myData)) {
            myDatas.add(myData);
        }
        MyData myData1 = new MyData("", "Test1", "Kunsang", "test message", "Sun 23 Nov 2016 15:13", "13");
        if (!myDatas.contains(myData)) {
            myDatas.add(myData);
        }
        MyData myData2 = new MyData("", "Test1", "Kunsang", "test message", "Sun 23 Nov 2016 15:13", "14");
        if (!myDatas.contains(myData)) {
            myDatas.add(myData);
        }
        MyData myData3 = new MyData("", "Test1", "Kunsang", "test message", "Sun 23 Nov 2016 15:13", "15");
        if (!myDatas.contains(myData)) {
            myDatas.add(myData);
        }
        MyData myData4 = new MyData("", "Test1", "Kunsang", "test message", "Sun 23 Nov 2016 15:13", "16");
        if (!myDatas.contains(myData)) {
            myDatas.add(myData);
        }
        MyData myData5 = new MyData("", "Test1", "Kunsang", "test message", "Sun 23 Nov 2016 15:13", "17");
        if (!myDatas.contains(myData)) {
            myDatas.add(myData);
        }
        MyData myData6 = new MyData("", "Test1", "Kunsang", "test message", "Sun 23 Nov 2016 15:13", "18");
        if (!myDatas.contains(myData)) {
            myDatas.add(myData);
        }
        myAdapter.notifyDataSetChanged();

    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerMyGroup);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        myAdapter = new MyAdapter(context, myDatas);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }

}
