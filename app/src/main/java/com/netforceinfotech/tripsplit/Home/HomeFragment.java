package com.netforceinfotech.tripsplit.Home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.Search.SearchSplitFragment;
import com.netforceinfotech.tripsplit.posttrip.PostTripFragment;


public class HomeFragment extends Fragment {


    ImageView post_trip,search_trip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view= inflater.inflate(R.layout.activity_home_content, container, false);

        setuptoolbar(view);

        setuplayout(view);

        return view;
    }

    private void setuptoolbar(View view)
    {

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        ImageView home = (ImageView) getActivity().findViewById(R.id.homeButton);

        ImageView icon = (ImageView) getActivity().findViewById(R.id.image_appicon);

        ImageView logout = (ImageView) getActivity().findViewById(R.id.lagouttxt);

        home.setVisibility(View.INVISIBLE);
        icon.setVisibility(View.INVISIBLE);
        logout.setVisibility(View.VISIBLE);

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTransparent));

    }

    public void setuplayout(View v)
    {

        post_trip = (ImageView) v.findViewById(R.id.post_trip_image);

        search_trip = (ImageView) v.findViewById(R.id.search_split_image);

        post_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PostTripFragment postTripActivity = new PostTripFragment();
                android.support.v4.app.FragmentTransaction message_fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                message_fragmentTransaction.replace(R.id.frame, postTripActivity);
                message_fragmentTransaction.commit();

            }
        });


        search_trip.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                SearchSplitFragment searchSplitFragment = new SearchSplitFragment();
                android.support.v4.app.FragmentTransaction message_fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                message_fragmentTransaction.replace(R.id.frame, searchSplitFragment);
                message_fragmentTransaction.commit();

            }
        });





    }

}
