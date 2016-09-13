package com.netforceinfotech.tripsplit.Search;

import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.netforceinfotech.tripsplit.Profile.PagerAdapter;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.WrapContentViewPager;

public class SearchSplitActivity extends AppCompatActivity
{

    WrapContentViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_split);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setuptoolbar();

        setupTab();

    }

    private void setuptoolbar()
    {
        Toolbar toolbar = (Toolbar)  findViewById(R.id.toolbar);

        FrameLayout home = (FrameLayout) findViewById(R.id.homebutton);

        ImageView icon = (ImageView)  findViewById(R.id.image_appicon);

        FrameLayout logout = (FrameLayout) findViewById(R.id.logoutbutton);

        home.setVisibility(View.VISIBLE);
        icon.setVisibility(View.VISIBLE);
        logout.setVisibility(View.INVISIBLE);

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }


    private void setupTab()
    {

        viewPager = (WrapContentViewPager) findViewById(R.id.pager);
        viewPager.setPagingEnabled(false);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setIcon(ContextCompat.getDrawable(this, R.drawable.ic_plane)));
        tabLayout.addTab(tabLayout.newTab().setIcon(ContextCompat.getDrawable(this, R.drawable.ic_car)));
        tabLayout.addTab(tabLayout.newTab().setIcon(ContextCompat.getDrawable(this, R.drawable.ic_bus)));
        tabLayout.addTab(tabLayout.newTab().setIcon(ContextCompat.getDrawable(this, R.drawable.ic_build)));

        final int tabIconColor = ContextCompat.getColor(this, R.color.black);
        final int tabIconSelectedColor = ContextCompat.getColor(this, R.color.red);

        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconSelectedColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final SearchPagerAdapter adapter = new SearchPagerAdapter(getSupportFragmentManager() , tabLayout.getTabCount());

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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

}
