package com.netforceinfotech.tripsplit.message.message_title;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.message.message_detail.MessageDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by John on 8/29/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int YOU = 0;
    private static final int THEM = 1;
    private final LayoutInflater inflater;
    private List<MyData> itemList;
    private Context context;
    ArrayList<Boolean> booleanGames = new ArrayList<>();
    MyHolder viewHolder;


    public MyAdapter(Context context, List<MyData> itemList) {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_chat_title, parent, false);
        viewHolder = new MyHolder(view);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        final MyData myData = itemList.get(position);
        myHolder.textViewTime.setText(getFormattedDate(myData.timestamp));
        try {
            Glide.with(context).load(myData.image_url).error(R.drawable.ic_error).into(myHolder.imageViewDp);
        } catch (Exception ex) {

        }
        if (myData.seen) {
            myHolder.textViewCount.setVisibility(View.GONE);
            myHolder.textViewLastMessage.setTextColor(ContextCompat.getColor(context, R.color.ampm_text_color));
        } else {
            myHolder.textViewCount.setVisibility(View.VISIBLE);
            myHolder.textViewLastMessage.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
        myHolder.textViewLastMessage.setText(myData.last_message);
        myHolder.textViewName.setText(myData.name);
        myHolder.textViewCount.setText(myData.count + "");
        myHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", myData.name);
                bundle.putString("image_url", myData.image_url);
                bundle.putString("id", myData.key);
                bundle.putBoolean("seen", myData.seen);
                bundle.putLong("count",myData.count);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }


    public String getFormattedDate(long timestamp) {
        Date date = new Date(timestamp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.DATE) == cal.get(Calendar.DATE)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
            return dateFormat.format(cal.getTime());
        } else if (now.get(Calendar.DATE) - cal.get(Calendar.DATE) == 1) {
            return "Yesterday ";
        } else {
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
            return sfd.format(new Date(timestamp));
        }


    }

    private void showMessage(String s) {

        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        return itemList.size();
//        return itemList.size();
    }

    private String getFormetedTime(Long timestamp) {
        SimpleDateFormat sfd = new SimpleDateFormat("hh:mm a");
        return sfd.format(new Date(timestamp));
    }


}