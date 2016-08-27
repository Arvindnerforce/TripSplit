package com.netforceinfotech.tripsplit.Profile.flight;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.netforceinfotech.tripsplit.R;


public class FlightFragment extends Fragment
{

    FlightAdapter adapter;
    RecyclerView  recyclerView;
   Context context;


    public FlightFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view= inflater.inflate(R.layout.fragment_flight, container, false);
        context=getActivity();





        return view;
    }


}
