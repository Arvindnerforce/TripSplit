package com.netforceinfotech.tripsplit.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netforceinfotech.tripsplit.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by John on 9/14/2016.
 */
public class RecyclerAdapterDrawer extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int ALERT_DRAWER = 4;
    public static int selected_item = 0;
    private final LayoutInflater inflater;
    List<RowDataDrawer> data = Collections.emptyList();
    Context context;
    private clickListner click = null;
    private final int NORMAL_DRAWER = 2;
    private Intent intent;
    int drawableId[];

    RecyclerAdapterDrawer(Context context, List<RowDataDrawer> data)
    {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;

        drawableId = new int[]{

                R.drawable.ic_home_red,  R.drawable.ic_prefrence_red,R.drawable.ic_edit_profile_red, R.drawable.ic_invite_frnd_red, R.drawable.ic_search_red, R.drawable.ic_create_trip_red, R.drawable.ic_message_red
        };



    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_navigation_drawer, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;

    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position)
    {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        if(position == selected_item)
        {

            myViewHolder.textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            myViewHolder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.selected_menu_back));
            int cImage = drawableId[position];
            myViewHolder.imageView.setImageResource(cImage);


        }
        else
        {

            myViewHolder.textView.setTextColor(ContextCompat.getColor(context, R.color.black));
            myViewHolder.linearLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
            myViewHolder.imageView.setImageResource(data.get(position).id);
        }

        myViewHolder.textView.setText(data.get(position).text);



    }

    private void showMessage(String s) {

        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        Log.i("TAG2 count", data.size() + "");
        return data.size();
    }

    public void setClickListner(clickListner click) {

        this.click = click;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;



        public MyViewHolder(final View itemView)
        {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView1);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.header);

            linearLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    click.itemClicked(itemView, getAdapterPosition());

                }
            });
        }


    }

    public interface clickListner
    {
        void itemClicked(View view, int position);
    }


}
