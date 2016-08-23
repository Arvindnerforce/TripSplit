package com.netforceinfotech.tripsplit.Search;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.netforceinfotech.tripsplit.R;


/**1
 * Created by asdf on 7/21/2016.
 */
public class SearchHolder extends RecyclerView.ViewHolder {


    TextView textViewTitle, textViewCategory, textViewPros;

    MaterialRippleLayout materialRippleLayout;
    View view;


    public SearchHolder(View itemView) {
        super(itemView);
        //implementing onClickListener
        view = itemView;

        materialRippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);

    }
}