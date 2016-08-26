package com.netforceinfotech.tripsplit.Review;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.netforceinfotech.tripsplit.R;

/**
 * Created by John on 8/26/2016.
 */
public class ReviewHolder extends RecyclerView.ViewHolder {


    TextView textViewTitle, textViewCategory, textViewPros;

    MaterialRippleLayout materialRippleLayout;
    View view;

    public ReviewHolder(View itemView) {
        super(itemView);
        //implementing onClickListener
        view = itemView;

        materialRippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);

    }
}