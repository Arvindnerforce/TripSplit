package com.netforceinfotech.tripsplit.Search.searchfragment.subfragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netforceinfotech.tripsplit.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private CarAdapter adapter;
    ArrayList<CarData> highestDatas = new ArrayList<CarData>();

    public RecyclerViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        setupRecyclerView(view);
        return view;
    }

    private void setupRecyclerView(View v) {

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CarAdapter(getActivity(), highestDatas);
        recyclerView.setAdapter(adapter);
        //  setupDummyDatas();
        adapter.notifyDataSetChanged();
    }

    public void updateTextValue(String source, String destination) {
        setupDummyDatas();
    }

    private void setupDummyDatas() {
        try {
            highestDatas.clear();
        } catch (Exception ex) {

        }
        //CarData(String stringDate, String travelCost, String ETD, String ETA, String source, String destination, String travelTime, String carType) {
        highestDatas.add(new CarData("", "", "", "", "", "", "", ""));
        highestDatas.add(new CarData("", "", "", "", "", "", "", ""));
        highestDatas.add(new CarData("", "", "", "", "", "", "", ""));
        highestDatas.add(new CarData("", "", "", "", "", "", "", ""));
        highestDatas.add(new CarData("", "", "", "", "", "", "", ""));
        highestDatas.add(new CarData("", "", "", "", "", "", "", ""));
        highestDatas.add(new CarData("", "", "", "", "", "", "", ""));
        highestDatas.add(new CarData("", "", "", "", "", "", "", ""));
        highestDatas.add(new CarData("", "", "", "", "", "", "", ""));
        highestDatas.add(new CarData("", "", "", "", "", "", "", ""));
        highestDatas.add(new CarData("", "", "", "", "", "", "", ""));
        highestDatas.add(new CarData("", "", "", "", "", "", "", ""));
        adapter.notifyDataSetChanged();

    }
}
