package com.netforceinfotech.tripsplit.posttrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private TypeFragment typeFragment;
    Bundle bundle;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        bundle = new Bundle();
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                typeFragment = new TypeFragment();
                bundle.putString("type", "aeroplane");
                typeFragment.setArguments(bundle);
                return typeFragment;
            case 1:
                typeFragment = new TypeFragment();
                bundle.putString("type", "car");
                typeFragment.setArguments(bundle);
                return typeFragment;
            case 2:
                typeFragment = new TypeFragment();
                bundle.putString("type", "bus");
                typeFragment.setArguments(bundle);
                return typeFragment;
            case 3:
                typeFragment = new TypeFragment();
                bundle.putString("type", "ship");
                typeFragment.setArguments(bundle);
                return typeFragment;

            default:
                typeFragment = new TypeFragment();
                bundle.putString("type", "car");
                typeFragment.setArguments(bundle);
                return typeFragment;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}