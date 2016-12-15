package com.netforceinfotech.tripsplit.group.groupchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener {

    UserSessionManager userSessionManager;
    String group_id = "", title = "No title";
    Context context;
    DatabaseReference _group, _group_child;
    TextView textViewNoMessage;
    private boolean child_group_exist = false;
    ArrayList<MyData> myDatas = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    EditText et_message;
    private String tempKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        context = this;
        userSessionManager = new UserSessionManager(context);
        try {
            Bundle bundle = getIntent().getExtras();
            title = bundle.getString("title");
            group_id = bundle.getString("group_id");
        } catch (Exception ex) {
            showMessage("Bundle Error");
        }
        setupToolBar(title);
        initView();
        setupFirebase();
        setupRecyclerView();


    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        myAdapter = new MyAdapter(context, myDatas);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    private void initView() {
        findViewById(R.id.buttonSend).setOnClickListener(this);
        et_message = (EditText) findViewById(R.id.et_message);
        textViewNoMessage = (TextView) findViewById(R.id.textViewNoMessage);
        textViewNoMessage.setVisibility(View.GONE);
    }

    private void setupFirebase() {
        _group = FirebaseDatabase.getInstance().getReference().child("group");
        _group.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(group_id)) {
                    // run some code
                    _group_child = _group.child(group_id);
                    textViewNoMessage.setVisibility(View.GONE);
                    _group_child.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            appendMessage(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    child_group_exist = false;
                    textViewNoMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void appendMessage(DataSnapshot dataSnapshot) {

        String id = dataSnapshot.child("id").getValue(String.class);
        String image_url = dataSnapshot.child("image_url").getValue(String.class);
        String message = dataSnapshot.child("message").getValue(String.class);
        String name = dataSnapshot.child("name").getValue(String.class);
        Long timestamp = dataSnapshot.child("timestamp").getValue(Long.class);
        timestamp = dataSnapshot.child("timestamp").getValue(Long.class);
        String key = dataSnapshot.getKey();
        MyData myData = new MyData(key, id, image_url, message, name, timestamp);
        if (myDatas.contains(myData)) {
            myDatas.add(myData);
        }
        myAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(myDatas.size());

    }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    private void setupToolBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String teams = title;
        getSupportActionBar().setTitle(teams);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSend:
                if (et_message.length() <= 0) {
                    showMessage("Enter Message");
                    return;
                }
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        if (!child_group_exist) {
            showMessage("Creating group");
            HashMap<String, Object> child_group = new HashMap<String, Object>();
            child_group.put(group_id, "");
            _group.updateChildren(child_group).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    _group_child = _group.child(group_id);
                    tempKey = _group_child.push().getKey();
                    _group_child.updateChildren(map);
                    DatabaseReference message_root = _group_child.child(tempKey);
                    Map<String, Object> map1 = new HashMap<String, Object>();
                    map1.put("image", userSessionManager.getProfileImage());
                    map1.put("message", et_message.getText().toString());
                    map1.put("name", userSessionManager.getName());
                    map1.put("user_id", userSessionManager.getUserId());
                    map1.put("timestamp", ServerValue.TIMESTAMP);
                    message_root.updateChildren(map1);

                }
            });
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            tempKey = _group_child.push().getKey();
            _group_child.updateChildren(map);
            DatabaseReference message_root = _group_child.child(tempKey);
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("image", userSessionManager.getProfileImage());
            map1.put("message", et_message.getText().toString());
            map1.put("name", userSessionManager.getName());
            map1.put("user_id", userSessionManager.getUserId());
            map1.put("timestamp", ServerValue.TIMESTAMP);
            message_root.updateChildren(map1);
        }
    }
}
