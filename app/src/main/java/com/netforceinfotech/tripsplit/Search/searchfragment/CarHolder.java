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


   /*
   * textViewPax
textCarType
textViewJourneyTime
textViewETA
textViewDestination
textViewSource
textviewETD_time
textViewPrice
   * */

    View view;
    TextView textViewPax, textCarType, textViewJourneyTime, textViewETA,
            textViewDestination, textViewSource, textviewETD_time, textViewPrice;

    public CarHolder(View itemView) {
        super(itemView);
        //implementing onClickListener
        view = itemView;
        textViewPax = (TextView) view.findViewById(R.id.textViewPax);
        textCarType = (TextView) view.findViewById(R.id.textCarType);
        textViewJourneyTime = (TextView) view.findViewById(R.id.textViewJourneyTime);
        textViewETA = (TextView) view.findViewById(R.id.textViewETA);
        textViewDestination = (TextView) view.findViewById(R.id.textViewDestination);
        textViewSource = (TextView) view.findViewById(R.id.textViewSource);
        textviewETD_time = (TextView) view.findViewById(R.id.textviewETD_time);
        textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);
    }
}
