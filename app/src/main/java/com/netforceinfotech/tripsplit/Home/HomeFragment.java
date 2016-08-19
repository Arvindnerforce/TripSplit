package com.netforceinfotech.tripsplit.Home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.posttrip.PostTripActivity;


public class HomeFragment extends Fragment {


    ImageView post_trip,search_trip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view= inflater.inflate(R.layout.activity_home_content, container, false);

        setuplayout(view);

        return view;
    }

    public void setuplayout(View v)
    {

        post_trip = (ImageView) v.findViewById(R.id.post_trip_image);

        search_trip = (ImageView) v.findViewById(R.id.search_split_image);


        post_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), PostTripActivity.class);

                startActivity(i);
            }
        });


        search_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), PostTripActivity.class);

                startActivity(i);
            }
        });
    }

}
