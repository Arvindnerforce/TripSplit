package com.netforceinfotech.tripsplit.Search;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netforceinfotech.tripsplit.Home.HomeFragment;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.WrapContentViewPager;

import xyz.santeri.wvp.WrappingViewPager;

public class SearchSplitFragment extends Fragment implements View.OnClickListener {

    WrappingViewPager viewPager;
    Context context;
    SearchPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_split, container, false);
        context = getActivity();
        setuptoolbar();
        initView(view);
        setupTab(view);

        return view;

    }

    private void initView(View view) {
        view.findViewById(R.id.linearlayoutSearch).setOnClickListener(this);
    }

    private void setuptoolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        ImageView home = (ImageView) toolbar.findViewById(R.id.homeButton);

        ImageView icon = (ImageView) toolbar.findViewById(R.id.image_appicon);
        TextView textViewLogout = (TextView) toolbar.findViewById(R.id.textviewLogout);
        textViewLogout.setVisibility(View.GONE);
        home.setVisibility(View.VISIBLE);
        icon.setVisibility(View.VISIBLE);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupHomeFragment();
            }
        });

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }


    private void replaceFragment(Fragment newFragment, String tag) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, newFragment, tag);
        transaction.commit();
    }

    public void setupHomeFragment() {
        HomeFragment dashboardFragment = new HomeFragment();
        String tagName = dashboardFragment.getClass().getName();
        replaceFragment(dashboardFragment, tagName);
    }

    private void setupTab(View view) {

        viewPager = (WrappingViewPager) view.findViewById(R.id.pager);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager.getLayoutParams().height = deviceHeight;

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


        adapter = new SearchPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearlayoutSearch:
                adapter.clicked(viewPager.getCurrentItem());
                break;
        }
    }

    private void showMessage(String clicked) {
        Toast.makeText(context, clicked, Toast.LENGTH_SHORT).show();
    }
}
