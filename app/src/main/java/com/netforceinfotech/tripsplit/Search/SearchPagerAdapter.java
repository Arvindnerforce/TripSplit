package com.netforceinfotech.tripsplit.Search;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.netforceinfotech.tripsplit.Profile.flight.FlightFragment;
import com.netforceinfotech.tripsplit.Search.searchfragment.CarFragment;

/**
 * Created by John on 8/29/2016.
 */

public class SearchPagerAdapter extends FragmentStatePagerAdapter {


    int mNumOfTabs;
    private CarFragment carFragment1, carFragment2, carFragment3, carFragment4;
    public int currentpage = 0;

    public SearchPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                carFragment1 = new CarFragment();
                return carFragment1;
            case 1:
                carFragment2 = new CarFragment();
                return carFragment2;
            case 2:
                carFragment3 = new CarFragment();
                return carFragment3;
            case 3:
                carFragment4 = new CarFragment();
                return carFragment4;
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
        switch (currentItem) {

            case 0:

                carFragment1.clicked();
                break;
            case 1:
                carFragment2.clicked();
                break;
            case 2:
                carFragment3.clicked();
                break;
            case 3:
                carFragment4.clicked();
                break;
        }
    }


}