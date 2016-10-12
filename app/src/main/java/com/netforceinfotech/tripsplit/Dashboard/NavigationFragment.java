package com.netforceinfotech.tripsplit.Dashboard;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netforceinfotech.tripsplit.Home.HomeFragment;
import com.netforceinfotech.tripsplit.NavigationView.Message.MessageFragment;
import com.netforceinfotech.tripsplit.Profile.editprofile.EditPofileFragment;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.Search.SearchSplitFragment;
import com.netforceinfotech.tripsplit.general.UserSessionManager;
import com.netforceinfotech.tripsplit.posttrip.PostTripFragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.techery.properratingbar.ProperRatingBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends Fragment implements RecyclerAdapterDrawer.clickListner, View.OnClickListener {

    public static final String preFile = "textFile";
    public static final String userKey = "key";
    private static final String TAG = "gcm_tag";
    public static int POSITION = 0;
    public static ActionBarDrawerToggle mDrawerToggle;
    public static DrawerLayout mDrawerLayout;
    boolean mUserLearnedDrawer;
    boolean mFromSavedInstance;
    View view;
    RecyclerView recyclerView;
    public static RecyclerAdapterDrawer adapter;
    public static final String PREFS_NAME = "call_recorder";
    private SharedPreferences loginPreferences;
    public static SharedPreferences.Editor loginPrefsEditor;
    public static List<RowDataDrawer> list = new LinkedList<>();
    private Context context;
    ImageView imageViewDp;
    TextView textViewName, textviewCountry;
    ProperRatingBar properRatingBar;

    public NavigationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_navigation, container, false);
        initView();
        setupHomeFragment();
        return view;
    }

    private void initView() {
        imageViewDp = (ImageView) view.findViewById(R.id.imageViewDp);
        textviewCountry = (TextView) view.findViewById(R.id.textviewCountry);
        textViewName = (TextView) view.findViewById(R.id.textviewName);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        list = setDrawer();
        adapter = new RecyclerAdapterDrawer(context, list);
        adapter.setClickListner(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);
        //sharedprefrance
        loginPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

    }


    private List<RowDataDrawer> setDrawer() {
        List<RowDataDrawer> list = new ArrayList<>();

        String title[] = {"Home", "Preferences", "Edit Profile", "Invite Friends", "Search Split", "Create Trip", "Messages", "dummy", "Group", "How it works", "Support"};
        int drawableId[];


        drawableId = new int[]
                {
                        R.drawable.ic_home_menu, R.drawable.ic_prefrence, R.drawable.ic_edit_profile, R.drawable.ic_invite_frnd, R.drawable.ic_search, R.drawable.ic_create_trip, R.drawable.ic_message,
                        R.drawable.ic_group, R.drawable.ic_group, R.drawable.ic_help, R.drawable.ic_community
                };


        for (int i = 0; i < title.length; i++) {
            RowDataDrawer current = new RowDataDrawer();
            current.text = title[i];
            current.id = drawableId[i];
            list.add(current);

        }


        Log.i("TAG count", list.size() + "");
        return list;
    }


    public static void openDrawer() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), userKey, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstance = true;
        }
    }

    public void setup(int id, final DrawerLayout drawer, Toolbar toolbar) {
        view = getActivity().findViewById(id);

        mDrawerLayout = drawer;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.drawer_open, R
                .string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                hideSoftKeyboard(getActivity());

                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    savedInstances(getActivity(), userKey, mUserLearnedDrawer + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();

            }

        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDrawerLayout.closeDrawers();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public static void savedInstances(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharePreference = context.getSharedPreferences(preFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePreference.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharePreference = context.getSharedPreferences(preFile, Context.MODE_PRIVATE);
        return sharePreference.getString(preferenceName, defaultValue);
    }

    @Override
    public void itemClicked(int position) {
        POSITION = position;
        RecyclerAdapterDrawer.selected_item = position;
        adapter.notifyDataSetChanged();
        mDrawerLayout.closeDrawers();
        switch (RecyclerAdapterDrawer.selected_item) {
            case 0:
                setupHomeFragment();
                break;
            case 1:
                setupPreferenceFragment();
                break;
            case 2:
                setupEditProfileFragment();
                break;
            case 3:
                setupInviteFriendFragment();
                break;
            case 4:
                setupSearchSplitFramgent();
                break;
            case 5:
                setupCreateTripFragment();
                break;
            case 6:
                setupMessageFragment();
                break;
            case 7:
                break;
            case 8:
                setupGroupFragment();
                break;
            case 9:
                setupHiWFragment();
                break;
            case 10:
                setupSupportFragment();
                break;
        }


    }

    private void setupSupportFragment() {
        showMessage("set up support");
    }

    private void setupHiWFragment() {
        showMessage("set up How it work Fragment");
    }

    private void setupGroupFragment() {
        showMessage("set up Group Fragment");
    }

    private void setupMessageFragment() {
        MessageFragment searchSplitFragment = new MessageFragment();
        String tag = searchSplitFragment.getClass().getName();
        replaceFragment(searchSplitFragment, tag);
    }

    private void setupCreateTripFragment() {
        PostTripFragment searchSplitFragment = new PostTripFragment();
        String tag = searchSplitFragment.getClass().getName();
        replaceFragment(searchSplitFragment, tag);
    }

    private void setupSearchSplitFramgent() {
        SearchSplitFragment searchSplitFragment = new SearchSplitFragment();
        String tag = searchSplitFragment.getClass().getName();
        replaceFragment(searchSplitFragment, tag);
    }

    private void setupInviteFriendFragment() {
        shareData();
    }

    private void setupEditProfileFragment() {
        EditPofileFragment editPofileFragment = new EditPofileFragment();
        String tagName = editPofileFragment.getClass().getName();
        replaceFragment(editPofileFragment, tagName);
    }

    public static void hideSoftKeyboard(Activity activity)

    {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.header:
                showMessage("header");
                break;
        }
    }

    private void showMessage(String clicked) {
        Toast.makeText(context, clicked, Toast.LENGTH_SHORT).show();
    }


    private void replaceFragment(Fragment newFragment, String tag) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, newFragment, tag);
        transaction.commit();
    }

    public void setupHomeFragment() {
        HomeFragment dashboardFragment = new HomeFragment(adapter);
        String tagName = dashboardFragment.getClass().getName();
        replaceFragment(dashboardFragment, tagName);
    }

    private void setupPreferenceFragment() {
        showMessage("set up preference");
    }


    private void shareData() {
        String shareBody = "Trip split lorem ispsum";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.shareit)));
    }
}