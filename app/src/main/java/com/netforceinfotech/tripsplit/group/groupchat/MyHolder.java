package com.netforceinfotech.tripsplit.group.groupchat;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.VerticalTextView;

/**
 * Created by John on 9/7/2016.
 */
public class MyHolder extends RecyclerView.ViewHolder {


    VerticalTextView textViewTime;
    View view;

    public MyHolder(View itemView) {
        super(itemView);
        //implementing onClickListener
        view = itemView;
        textViewTime = (VerticalTextView) view.findViewById(R.id.textViewTime);
      /*  RotateAnimation ranim = (RotateAnimation) AnimationUtils.loadAnimation(view.getContext(), R.anim.rotate);
        ranim.setFillAfter(true); //For the textview to remain at the same place after the rotation
        textViewTime.setAnimation(ranim);*/

    }
}
