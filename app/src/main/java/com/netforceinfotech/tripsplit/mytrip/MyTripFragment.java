package com.netforceinfotech.tripsplit.mytrip;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.tripsplit.home.HomeFragment;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTripFragment extends Fragment {

    private RecyclerView recyclerView;
    UserSessionManager userSessionManager;
    Context context;
    private MyAdapter myAdapter;
    ArrayList<MyData> myDatas = new ArrayList<>();
    private MaterialDialog progressDialog;

    public MyTripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_splitz, container, false);
        context = getActivity();
        userSessionManager = new UserSessionManager(context);
        setuptoolbar();
        initView(view);
        setupRecyclerView(view);
        getData();
        return view;
    }

    private void initView(View view) {
        progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0).build();
    }

    private void getData() {
        //http://netforce.biz/tripesplit/mobileApp/api/services.php?opt=my_splitz
        progressDialog.show();

        String baseUrl = getString(R.string.url);
        //http://netforce.biz/tripesplit/mobileApp/api/services.php?opt=search_trip
        String url = baseUrl + "services.php?opt=search_trip";
        Log.i("url", url);
        Log.i("type1", "type");
        Ion.with(context)
                .load("POST", url)
                .setBodyParameter("user_id", userSessionManager.getUserId())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressDialog.dismiss();
                        // do stuff with the result or error
                        if (result == null) {
                            showMessage("something wrong");
                        } else {
                            if (result.get("status").getAsString().equalsIgnoreCase("success")) {
                                Log.i("result", result.toString());

                                JsonArray data = result.getAsJsonArray("data");
                                setupData(data);
                            }

                        }
                    }
                });
    }

    private void setupData(JsonArray data) {

    }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        myAdapter = new MyAdapter(context, myDatas);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
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

}
