package com.netforceinfotech.tripsplit.Search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.netforceinfotech.tripsplit.Search.searchfragment.SearchFragment;

/**
 * Created by John on 8/29/2016.
 */

public class SearchPagerAdapter extends FragmentStatePagerAdapter {


    int mNumOfTabs;
    private SearchFragment searchFragment;
    public int currentpage = 0;
    private Bundle bundle;

    public SearchPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        bundle = new Bundle();
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                searchFragment = new SearchFragment();
                bundle.putString("type", "aeroplane");
                searchFragment.setArguments(bundle);
                return searchFragment;
            case 1:
                searchFragment = new SearchFragment();
                bundle.putString("type", "car");
                searchFragment.setArguments(bundle);
                return searchFragment;
            case 2:
                searchFragment = new SearchFragment();
                bundle.putString("type", "bus");
                searchFragment.setArguments(bundle);
                return searchFragment;
            case 3:
                searchFragment = new SearchFragment();
                bundle.putString("type", "ship");
                searchFragment.setArguments(bundle);
                return searchFragment;
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void clicked(int currentItem) {
        Log.i("kclicked", "clicked" + currentpage);
        searchFragment.clicked();
    }


}