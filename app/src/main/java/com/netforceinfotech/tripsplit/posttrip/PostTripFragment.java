package com.netforceinfotech.tripsplit.posttrip;

import android.content.Context;
import android.graphics.PorterDuff;

import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import android.support.v4.widget.DrawerLayout;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.netforceinfotech.tripsplit.Profile.PagerAdapter;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.WrapContentViewPager;

public class PostTripFragment extends Fragment
{

    WrapContentViewPager viewPager;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    Context context;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        context = getActivity();

        setuptoolbar();

        setupTab(view);

        return view;

    }

    private void setuptoolbar()
    {
        Toolbar toolbar = (Toolbar)  getActivity().findViewById(R.id.toolbar);

        ImageView home = (ImageView) getActivity().findViewById(R.id.homeButton);

        ImageView icon = (ImageView)  getActivity().findViewById(R.id.image_appicon);

        ImageView logout = (ImageView) getActivity().findViewById(R.id.lagouttxt);

        home.setVisibility(View.VISIBLE);
        icon.setVisibility(View.VISIBLE);
        logout.setVisibility(View.INVISIBLE);

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }

    private void setupTab(View v)
    {

        viewPager = (WrapContentViewPager) v.findViewById(R.id.pager);
        viewPager.setPagingEnabled(true);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_plane)));
        tabLayout.addTab(tabLayout.newTab().setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_car)));
        tabLayout.addTab(tabLayout.newTab().setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_bus)));
        tabLayout.addTab(tabLayout.newTab().setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_build)));

        final int tabIconColor = ContextCompat.getColor(getActivity(), R.color.black);
        final int tabIconSelectedColor = ContextCompat.getColor(getActivity(), R.color.red);


        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconSelectedColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final PagerAdapter adapter = new PagerAdapter(getChildFragmentManager() , tabLayout.getTabCount());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {

            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(tabIconSelectedColor, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case android.R.id.home:
              ///  finish();

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }




}
