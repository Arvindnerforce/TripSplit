package com.netforceinfotech.tripsplit.message.message_detail;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

/*
Variable Used->*****************************************
***********************************************
boolean
----------------------------------------------------
  myUserIdFlag -> true if user node is present;
  myUserId_id_Flag-> true if user is already chatting with other user
  --------------------------------------------------
*****************************************************
 Database Reference
 -------------------------------
 _chat_title-> "chat_title" node
 _my_userId -> node of user's user id in "chat_title" node
 _my_userId_id-> node of other user id in User's id node. (Exist if they chat)
 _chat-> root node of "chat"
 _chat_id-> node of there conversation
 --------------------------------
 ************************************************************
  Function()
 -----------------------------------------
 setupMyUserId() ->set up _my_userId node
 setupMyChatIdId() -> setup _my_userId_id node
 --------------------------------------
  */
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
    private DatabaseReference _my_userId, _their_userId;
    private boolean myUserIdFlag = false, theirUserIdFlag;
    String id;
    private boolean myUserId_id_flag = false, theirUserId_id_flag = false;
    private DatabaseReference _my_userId_id, _their_userId_id;
    private DatabaseReference _chat_id;
    private DatabaseReference _chat;
    String name, image_url;
    private DatabaseReference _unseen;
    private String chat_id;
    boolean chat_id_created_flag = false, seen = false;
    private String last_message;
    long count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        context = this;
        userSessionManager = new UserSessionManager(context);
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        id = bundle.getString("id");
        image_url = bundle.getString("image_url");
        try {

            seen = bundle.getBoolean("seen");
            count = bundle.getLong("count");
            if (!seen) {
                updateSeen(count);
            }

        } catch (Exception ex) {

        }
        setupToolBar(name);
        initView();
        setupRecyclerView();
        setupFirebase();
    }

    private void updateSeen(final long count1) {
        final DatabaseReference _unseen = FirebaseDatabase.getInstance().getReference().child("unseen");
        _unseen.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.child(userSessionManager.getUserId()).getValue(Long.class);
                count = count - count1;
                _unseen.child(userSessionManager.getUserId()).setValue(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference _chat_titleMyId_id = FirebaseDatabase.getInstance().getReference().child("chat_title").child(userSessionManager.getUserId()).child(id);
        _chat_titleMyId_id.child("seen").setValue(true);
        _chat_titleMyId_id.child("unseen_count").setValue(0);

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
        _unseen = FirebaseDatabase.getInstance().getReference().child("unseen");
        _chat = FirebaseDatabase.getInstance().getReference().child("chat");
        _chat_title = FirebaseDatabase.getInstance().getReference().child("chat_title");
        _chat_title.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                setupMyUserId(dataSnapshot);
                setupTheirUserId(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setupTheirUserId(DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChild(id)) {
            // run some code
            _their_userId = _chat_title.child(id);
            theirUserIdFlag = true;
            _their_userId.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    setupTheirChatIdId(dataSnapshot, userSessionManager.getUserId());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            theirUserIdFlag = false;
        }
    }


    private void setupMyUserId(DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChild(userSessionManager.getUserId())) {
            // run some code
            _my_userId = _chat_title.child(userSessionManager.getUserId());
            myUserIdFlag = true;
            _my_userId.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    setupMyChatIdId(dataSnapshot, id);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } else {
            myUserIdFlag = false;
            textViewNoMessage.setVisibility(View.VISIBLE);
        }
    }

    private void setupMyChatIdId(DataSnapshot dataSnapshot, String id) {
        if (dataSnapshot.hasChild(id)) {
            myUserId_id_flag = true;
            _my_userId_id = _my_userId.child(id);
            _my_userId_id.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    setupChatId(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            myUserId_id_flag = false;
        }
    }

    private void setupTheirChatIdId(DataSnapshot dataSnapshot, String userId) {
        if (dataSnapshot.hasChild(userId)) {
            theirUserId_id_flag = true;
            _their_userId_id = _their_userId.child(userId);

        } else {
            theirUserId_id_flag = false;
        }
    }

    private void setupChatId(DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChild("chat_id")) {
            String chat_id = dataSnapshot.child("chat_id").getValue(String.class);
            _chat_id = _chat.child(chat_id);
            _chat_id.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    appendMessage(dataSnapshot);
                    Log.i("unseenfunction","called");
                    updateSeen(1);

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
                    showMessage("Check Internet Connection");
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        try{
            if(_unseen!=null){

            }
        }catch (Exception ex){

        }
        super.onDestroy();
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
                last_message = et_message.getText().toString();
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        progressDialog.show();
        if (myUserId_id_flag) {
            pushMessage();
            upDateMyUserId_id_DATA();
        } else {
            if (myUserIdFlag) {
                createMyUserId_IdNode();

            } else {
                createMyUserIdNode();
            }
        }


        if (theirUserId_id_flag) {
            upDateTheirUserId_id_DATA();
        } else {
            if (theirUserIdFlag) {
                createTheirUserId_IdNode();

            } else {
                createTheirUserIdNode();
            }
        }

    }

    private void upDateTheirUserId_id_DATA() {
        _their_userId_id.child("timestamp").setValue(ServerValue.TIMESTAMP);
        _their_userId_id.child("seen").setValue(false);
        _their_userId_id.child("you").setValue(false);
        _their_userId_id.child("last_message").setValue(last_message);
        _their_userId_id.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = dataSnapshot.child("unseen_count").getValue(Integer.class);
                count++;
                _their_userId_id.child("unseen_count").setValue(count);
                updateTheirUnseenCount(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateTheirUnseenCount(final int count) {
        _unseen.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(id).exists()) {
                    int unseenCount = dataSnapshot.child(id).getValue(Integer.class);
                    unseenCount = unseenCount + 1;
                    _unseen.child(id).setValue(unseenCount);
                } else {

                    HashMap<String, Object> unseentheir_map = new HashMap<String, Object>();
                    unseentheir_map.put(id, count);
                    _unseen.updateChildren(unseentheir_map);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateMyUnseenCount(final int unseenCount1) {
        _unseen.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userSessionManager.getUserId()).exists()) {
                    int unseenCount = dataSnapshot.child(userSessionManager.getUserId()).getValue(Integer.class);
                    unseenCount = unseenCount - unseenCount1;
                    _unseen.child(userSessionManager.getUserId()).setValue(unseenCount);
                } else {

                    HashMap<String, Object> unseentheir_map = new HashMap<String, Object>();
                    unseentheir_map.put(userSessionManager.getUserId(), 0);
                    _unseen.updateChildren(unseentheir_map);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void upDateMyUserId_id_DATA() {
        _my_userId_id.child("timestamp").setValue(ServerValue.TIMESTAMP);
        _my_userId_id.child("seen").setValue(true);
        _my_userId_id.child("you").setValue(true);
        _my_userId_id.child("last_message").setValue(last_message);

        _my_userId_id.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int unseenCount = dataSnapshot.child("unseen_count").getValue(Integer.class);
                updateMyUnseenCount(unseenCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        _my_userId_id.child("unseen_count").setValue(0);
        _my_userId_id.child("last_message").setValue(et_message.getText().toString());
    }

    private void createMyUserIdNode() {
        HashMap<String, Object> chat_title_userid_map = new HashMap<String, Object>();
        chat_title_userid_map.put(userSessionManager.getUserId(), "");
        _chat_title.updateChildren(chat_title_userid_map);
        _chat_title.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myUserIdFlag = true;
                _my_userId = _chat_title.child(userSessionManager.getUserId());
                createMyUserId_IdNode();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void createMyUserId_IdNode() {
        HashMap<String, Object> chat_title_userid_id_map = new HashMap<String, Object>();
        chat_title_userid_id_map.put(id, "");
        _my_userId.updateChildren(chat_title_userid_id_map);
        _my_userId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                _my_userId_id = _my_userId.child(id);
                myUserId_id_flag = true;
                HashMap<String, Object> chat_title_userid_id_detailmap = new HashMap<String, Object>();
                chat_title_userid_id_detailmap.put("name", name);
                chat_title_userid_id_detailmap.put("image_url", image_url);
                chat_title_userid_id_detailmap.put("timestamp", ServerValue.TIMESTAMP);
                chat_title_userid_id_detailmap.put("seen", true);
                chat_title_userid_id_detailmap.put("unseen_count", 0);
                chat_title_userid_id_detailmap.put("last_message", et_message.getText().toString());
                chat_title_userid_id_detailmap.put("you", true);

                if (!chat_id_created_flag) {
                    chat_id = _my_userId_id.push().getKey();
                    chat_id_created_flag = true;
                    chat_title_userid_id_detailmap.put("chat_id", chat_id);
                } else {
                    chat_title_userid_id_detailmap.put("chat_id", chat_id);

                }
                _my_userId_id.updateChildren(chat_title_userid_id_detailmap);
                _my_userId_id.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        _chat_id = _chat.child(chat_id);
                        pushMessage();
                        upDateMyUserId_id_DATA();
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

    private void createTheirUserIdNode() {
        HashMap<String, Object> chat_title_userid_map = new HashMap<String, Object>();
        chat_title_userid_map.put(id, "");
        _chat_title.updateChildren(chat_title_userid_map);
        _chat_title.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                theirUserIdFlag = true;
                _their_userId = _chat_title.child(id);
                createTheirUserId_IdNode();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void createTheirUserId_IdNode() {
        HashMap<String, Object> chat_title_userid_id_map = new HashMap<String, Object>();
        chat_title_userid_id_map.put(userSessionManager.getUserId(), "");
        _their_userId.updateChildren(chat_title_userid_id_map);
        _their_userId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                _their_userId_id = _their_userId.child(userSessionManager.getUserId());
                theirUserId_id_flag = true;
                HashMap<String, Object> chat_title_userid_id_detailmap = new HashMap<String, Object>();
                chat_title_userid_id_detailmap.put("name", userSessionManager.getName());
                chat_title_userid_id_detailmap.put("image_url", userSessionManager.getProfileImage());
                chat_title_userid_id_detailmap.put("timestamp", ServerValue.TIMESTAMP);
                chat_title_userid_id_detailmap.put("seen", false);
                chat_title_userid_id_detailmap.put("unseen_count", 0);
                chat_title_userid_id_detailmap.put("last_message", et_message.getText().toString());
                chat_title_userid_id_detailmap.put("you", false);

                if (!chat_id_created_flag) {
                    chat_id = _their_userId_id.push().getKey();
                    chat_id_created_flag = true;
                    chat_title_userid_id_detailmap.put("chat_id", chat_id);
                } else {
                    chat_title_userid_id_detailmap.put("chat_id", chat_id);

                }
                _their_userId_id.updateChildren(chat_title_userid_id_detailmap);

                _their_userId_id.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        _chat_id = _chat.child(chat_id);
                        upDateTheirUserId_id_DATA();

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
        message_root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                appendMessage(dataSnapshot);
                progressDialog.dismiss();
                last_message = et_message.getText().toString();
                et_message.setText("");
                textViewNoMessage.setVisibility(View.GONE);
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