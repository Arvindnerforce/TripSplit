package com.netforceinfotech.tripsplit.profile.myprofile;

import android.content.Context;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.hedgehog.ratingbar.RatingBar;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    String userId;
    Context context;
    UserSessionManager userSessionManager;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    ArrayList<MyData> myDatas = new ArrayList<>();
    private float reviewRating = -1;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Bundle bundle = getIntent().getExtras();
        context = this;
        userSessionManager = new UserSessionManager(context);
        userName = bundle.getString("name");
        userId = bundle.getString("user_id");
        setupToolBar(userName);

        setupRecyclerView();
        initView();
    }

    private void initView() {
        findViewById(R.id.buttonWriteReview).setOnClickListener(this);
    }

    private void setupToolBar(String app_name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(app_name);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        myAdapter = new MyAdapter(context, myDatas);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
        recyclerView.smoothScrollToPosition(myDatas.size());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonWriteReview:
                showReviewPopUp(userSessionManager.getName(), "12-dec-1990", userSessionManager.getProfileImage());
                break;

        }
    }

    private void showReviewPopUp(String name, String dob, String imageUrl) {
        final RatingBar ratingBar;

        final MaterialDialog reviewBox = new MaterialDialog.Builder(this)
                .customView(R.layout.review_pop_up, false)
                .show();

        reviewBox.setCanceledOnTouchOutside(false);

        reviewBox.findViewById(R.id.imageViewClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        final EditText editText = (EditText) reviewBox.findViewById(R.id.editText);
        CircleImageView imageViewDp = (CircleImageView) reviewBox.findViewById(R.id.imageViewDp);
        TextView textViewName, textViewDob;
        textViewDob = (TextView) reviewBox.findViewById(R.id.textViewDob);
        textViewName = (TextView) reviewBox.findViewById(R.id.textViewName);
        ratingBar = (RatingBar) reviewBox.findViewById(R.id.ratingbar);
        Glide.with(getApplicationContext()).load(imageUrl).error(R.drawable.ic_error).into(imageViewDp);
        textViewDob.setText(getFormattedDob(dob));
        textViewName.setText(name);
        ratingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                reviewRating = RatingCount;
            }
        });
        reviewBox.findViewById(R.id.imageViewClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewBox.dismiss();
            }
        });
        reviewBox.findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reviewRating == -1) {
                    showMessage("Please Rate this trip first!!!");
                    return;
                }
                if (editText.length() <= 0) {
                    showMessage("Please write few word about trip !!!");
                }
            }
        });
    }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    private String getFormattedDob(String dob) {
        return dob;
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
}
