package com.netforceinfotech.tripsplit.message.message_detail;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
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

public class MessageDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    UserSessionManager userSessionManager;
    ArrayList<MyData> myDatas = new ArrayList<>();
    EditText et_message;
    private MaterialDialog progressDialog;
    TextView textViewNoMessage;
    private DatabaseReference _chat_title;
    private DatabaseReference _chat_title_userid;
    private boolean chat_title_userid_flag = false;
    String id;
    private boolean chat_title_userid_id_flag = false;
    private DatabaseReference _chat_title_userid_id;
    private DatabaseReference _chat_id;
    private DatabaseReference _chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        context = this;
        userSessionManager = new UserSessionManager(context);
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        id = bundle.getString("id");
        setupToolBar(name);
        initView();
    }

    private void initView() {
        progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0).build();
        findViewById(R.id.buttonSend).setOnClickListener(this);
        et_message = (EditText) findViewById(R.id.et_message);
        textViewNoMessage = (TextView) findViewById(R.id.textViewNoMessage);
        textViewNoMessage.setVisibility(View.GONE);
    }

    private void setupToolBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String teams = title;
        getSupportActionBar().setTitle(teams);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        myAdapter = new MyAdapter(context, myDatas);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    private void setupFirebase() {
        progressDialog.show();
        _chat = FirebaseDatabase.getInstance().getReference().child("chat");

        _chat_title = FirebaseDatabase.getInstance().getReference().child("chat_title");
        _chat_title.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.hasChild(userSessionManager.getUserId())) {
                    // run some code
                    _chat_title_userid = _chat_title.child(userSessionManager.getUserId());
                    chat_title_userid_flag = true;
                    _chat_title_userid.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(id)) {
                                chat_title_userid_id_flag = true;
                                _chat_title_userid_id = _chat_title_userid.child(id);
                                _chat_title_userid_id.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild("chat_id")) {
                                            String chat_id = dataSnapshot.child("chat_id").getValue(String.class);
                                            _chat_id = _chat.child(chat_id);
                                            _chat_id.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                    appendMessage(dataSnapshot);
                                                }

                                                @Override
                                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                                }

                                                @Override
                                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                                }

                                                @Override
                                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                chat_title_userid_id_flag = false;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } else {
                    chat_title_userid_flag = false;
                    textViewNoMessage.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void appendMessage(DataSnapshot dataSnapshot) {

        Log.i("datasnampshot", dataSnapshot.toString());
        String id = dataSnapshot.child("user_id").getValue(String.class);
        String image_url = dataSnapshot.child("image").getValue(String.class);
        String message = dataSnapshot.child("message").getValue(String.class);
        String name = dataSnapshot.child("name").getValue(String.class);
        Long timestamp = dataSnapshot.child("timestamp").getValue(Long.class);
        String key = dataSnapshot.getKey();
        boolean you = false;
        if (userSessionManager.getUserId().equalsIgnoreCase(id)) {
            you = true;
        } else {
            you = false;
        }
        Log.i("message", dataSnapshot.child("message").getValue(String.class) + "");
        MyData myData = new MyData(key, id, image_url, message, name, timestamp, you);
        if (!myDatas.contains(myData)) {
            myDatas.add(myData);
        }
        myAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(myDatas.size());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSend:
                if (et_message.length() <= 0) {
                    showMessage("Input Message");
                    return;
                }
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        if (!chat_title_userid_id_flag) {
            if (!chat_title_userid_flag) {

            } else {
                HashMap<String, Object> chat_title_userid_map = new HashMap<String, Object>();
                chat_title_userid_map.put(userSessionManager.getUserId(), "");
                _chat_title.updateChildren(chat_title_userid_map);
                _chat_title.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        _chat_title_userid = _chat_title.child(userSessionManager.getUserId());
                        HashMap<String, Object> chat_title_userid_id_map = new HashMap<String, Object>();
                        chat_title_userid_id_map.put(id, "");
                        _chat_title_userid.updateChildren(chat_title_userid_id_map);
                        _chat_title_userid.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                _chat_title_userid_id = _chat_title_userid.child(id);
                                HashMap<String, Object> chat_title_userid_id_detailmap = new HashMap<String, Object>();
                                chat_title_userid_id_detailmap.put("name", "name");
                                chat_title_userid_id_detailmap.put("image_url", "imageurl");
                                chat_title_userid_id_detailmap.put("timestamp", ServerValue.TIMESTAMP);
                                String key = _chat_title_userid_id.push().getKey();
                                chat_title_userid_id_detailmap.put("chat_id", key);

                                _chat_title_userid_id.updateChildren(chat_title_userid_id_detailmap);
                                _chat_title_userid_id.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        _chat_id = _chat_title_userid_id.child("chat_id");
                                        pushMessage();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        } else {
            pushMessage();
        }
    }

    private void pushMessage() {
        Map<String, Object> map = new HashMap<String, Object>();
        String tempKey = _chat_id.push().getKey();
        _chat_id.updateChildren(map);
        DatabaseReference message_root = _chat_id.child(tempKey);
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("image", userSessionManager.getProfileImage());
        map1.put("message", et_message.getText().toString());
        map1.put("name", userSessionManager.getName());
        map1.put("user_id", userSessionManager.getUserId());
        map1.put("timestamp", ServerValue.TIMESTAMP);
        message_root.updateChildren(map1);
        progressDialog.show();
        message_root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                appendMessage(dataSnapshot);
                progressDialog.dismiss();
                et_message.setText("");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                showMessage("Could not send");
            }
        });
    }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
