package com.netforceinfotech.tripsplit.Search.searchfragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.netforceinfotech.tripsplit.R;

/**
 * Created by John on 9/7/2016.
 */
public class CarHolder extends RecyclerView.ViewHolder {


    TextView textViewTitle, textViewCategory, textViewPros;
   LinearLayout linear;
    MaterialRippleLayout materialRippleLayout;
    View view;


    public CarHolder(View itemView) {
        super(itemView);
        //implementing onClickListener
        view = itemView;

        materialRippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);

    }
}
