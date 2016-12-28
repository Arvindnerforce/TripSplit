package com.netforceinfotech.tripsplit.message.message_title;


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

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.netforceinfotech.tripsplit.Home.HomeFragment;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageTitleFragment extends Fragment {


    ArrayList<MyData> myDatas = new ArrayList<>();
    MyAdapter myAdapter;
    Context context;
    UserSessionManager userSessionManager;
    DatabaseReference _chatTitle, _chatTitle_id, _unseen;
    TextView textViewNoMessage, textViewTitle;
    private MaterialDialog progressDialog;

    public MessageTitleFragment() {
        // Required empty public constructor
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_title, container, false);
        context = getActivity();
        setuptoolbar();
        userSessionManager = new UserSessionManager(context);
        progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0).build();
        textViewNoMessage = (TextView) view.findViewById(R.id.textViewNoMessage);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        setupRecyclerView(view);
        return view;
    }

    @Override
    public void onResume() {
        setupFirebase();
        super.onResume();
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

    private void setupFirebase() {
        progressDialog.show();
        getUnseenMessage();
        _chatTitle = FirebaseDatabase.getInstance().getReference().child("chat_title");
        //check User History
        _chatTitle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userSessionManager.getUserId()).exists()) {
                    _chatTitle_id = _chatTitle.child(userSessionManager.getUserId());
                    _chatTitle_id.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            appendList(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    textViewNoMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getUnseenMessage() {
        _unseen = FirebaseDatabase.getInstance().getReference().child("unseen");
        _unseen.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userSessionManager.getUserId()).exists()) {
                    progressDialog.dismiss();
                    long count = dataSnapshot.child(userSessionManager.getUserId()).getValue(Long.class);
                    textViewTitle.setText("Message (" + count + ")");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();

            }
        });
    }

    private void appendList(DataSnapshot dataSnapshot) {
        long size = dataSnapshot.getChildrenCount();
        if (size <= 0) {
            textViewNoMessage.setVisibility(View.VISIBLE);
            return;
        }
        textViewNoMessage.setVisibility(View.GONE);
        for (DataSnapshot singleNode : dataSnapshot.getChildren()) {
            String key = singleNode.getKey();
            String last_message = singleNode.child("last_message").getValue(String.class);
            String chat_id = singleNode.child("chat_id").getValue(String.class);

            String name = singleNode.child("name").getValue(String.class);
            String image_url = singleNode.child("image_url").getValue(String.class);
            boolean seen = singleNode.child("seen").getValue(Boolean.class);
            long timestamp = singleNode.child("timestamp").getValue(Long.class);
            long unseen_count = singleNode.child("unseen_count").getValue(Long.class);
            MyData myData = new MyData(key, chat_id, image_url, last_message, name, timestamp, seen, unseen_count);
            Log.i("mydata", myData.key);
            if (!myDatas.contains(myData)) {
                myDatas.add(myData);
            }

        }
        myAdapter.notifyDataSetChanged();

    }

    private void setupRecyclerView(View view) {
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recycler);
        myAdapter = new MyAdapter(context, myDatas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(myAdapter);
    }

}
