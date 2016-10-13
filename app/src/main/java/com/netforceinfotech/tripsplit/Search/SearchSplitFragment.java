package com.netforceinfotech.tripsplit.Search;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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

import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.WrapContentViewPager;

public class SearchSplitFragment extends Fragment {

    WrapContentViewPager viewPager;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_split, container, false);
        context = getActivity();
        setuptoolbar();
        setupTab(view);

        return view;

    }

    private void setuptoolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        ImageView home = (ImageView) toolbar.findViewById(R.id.homeButton);

        ImageView icon = (ImageView) toolbar.findViewById(R.id.image_appicon);
        TextView textViewLogout = (TextView) toolbar.findViewById(R.id.textviewLogout);
        textViewLogout.setVisibility(View.GONE);
        home.setVisibility(View.VISIBLE);
        icon.setVisibility(View.VISIBLE);

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }


    private void setupTab(View view) {

        viewPager = (WrapContentViewPager) view.findViewById(R.id.pager);
        viewPager.setPagingEnabled(true);
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


        final SearchPagerAdapter adapter = new SearchPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());

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


}
