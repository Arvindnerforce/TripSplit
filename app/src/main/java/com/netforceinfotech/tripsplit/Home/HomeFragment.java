package com.netforceinfotech.tripsplit.Home;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netforceinfotech.tripsplit.Dashboard.NavigationFragment;
import com.netforceinfotech.tripsplit.Dashboard.RecyclerAdapterDrawer;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.Search.SearchSplitFragment;
import com.netforceinfotech.tripsplit.posttrip.PostTripFragment;


public class HomeFragment extends Fragment {
    Context context;
    ImageView post_trip, search_trip;
    RecyclerAdapterDrawer adapterDrawer;

    public HomeFragment(RecyclerAdapterDrawer adapter) {
        this.adapterDrawer=adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_home_content, container, false);
        context = getActivity();
        setuptoolbar(view);
        setuplayout(view);

        return view;
    }

    private void setuptoolbar(View view) {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ImageView home = (ImageView) toolbar.findViewById(R.id.homeButton);
        ImageView icon = (ImageView) toolbar.findViewById(R.id.image_appicon);
        TextView textView = (TextView) toolbar.findViewById(R.id.textviewLogout);
        textView.setVisibility(View.VISIBLE);
        home.setVisibility(View.GONE);
        icon.setVisibility(View.INVISIBLE);
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryTransparent));
    }

    public void setuplayout(View v) {
        post_trip = (ImageView) v.findViewById(R.id.post_trip_image);

        search_trip = (ImageView) v.findViewById(R.id.search_split_image);

        post_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterDrawer.click.itemClicked(5);
                NavigationFragment.POSITION=5;
             /*   PostTripFragment postTripActivity = new PostTripFragment();
                android.support.v4.app.FragmentTransaction message_fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                message_fragmentTransaction.replace(R.id.frame, postTripActivity);
                message_fragmentTransaction.commit();
*/
            }
        });


        search_trip.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                NavigationFragment.POSITION=4;
                adapterDrawer.click.itemClicked(4);
              /*  SearchSplitFragment searchSplitFragment = new SearchSplitFragment();
                android.support.v4.app.FragmentTransaction message_fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                message_fragmentTransaction.replace(R.id.frame, searchSplitFragment);
                message_fragmentTransaction.commit();*/

            }
        });


    }

}
