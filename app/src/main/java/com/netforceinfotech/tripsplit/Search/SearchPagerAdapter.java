package com.netforceinfotech.tripsplit.Search;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.netforceinfotech.tripsplit.Profile.flight.FlightFragment;
import com.netforceinfotech.tripsplit.Search.searchfragment.CarFragment;

/**
 * Created by John on 8/29/2016.
 */

public class SearchPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private CarFragment carFragment;

    public SearchPagerAdapter(FragmentManager fm, int NumOfTabs)
    {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }



    @Override
    public Fragment getItem(int position)
    {

        switch (position)
        {
            case 0:
                carFragment = new CarFragment();
                return carFragment;
            case 1:
                carFragment = new CarFragment();
                return carFragment;
            default:
                carFragment = new CarFragment();
                return carFragment;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}