package com.netforceinfotech.tripsplit.tutorial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netforceinfotech.tripsplit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppIntroSecond extends Fragment {


    public AppIntroSecond() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_app_intro_fourth, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        Glide.with(getActivity()).load(R.drawable.fourth_tutorial).into(imageView);
        return view;
    }

}
