package com.netforceinfotech.tripsplit.posttrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import xyz.santeri.wvp.WrappingViewPager;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private int mCurrentPosition = -1; // Keep track of the current position

    private TypeFragment typeFragment;
    Bundle bundle;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        bundle = new Bundle();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if (!(container instanceof WrappingViewPager)) {
            return; // Do nothing if it's not a compatible ViewPager
        }

        if (position != mCurrentPosition) { // If the position has changed, tell WrappingViewPager
            Fragment fragment = (Fragment) object;
            WrappingViewPager pager = (WrappingViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.onPageChanged(fragment.getView());
            }
        }
    }
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                typeFragment = new TypeFragment();
                bundle.putString("type", "aeroplane");//aeroplane,car,bus,ship
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