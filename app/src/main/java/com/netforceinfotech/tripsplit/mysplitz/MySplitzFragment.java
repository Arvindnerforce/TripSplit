package com.netforceinfotech.tripsplit.mysplitz;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netforceinfotech.tripsplit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MySplitzFragment extends Fragment {


    public MySplitzFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_splitz, container, false);
    }

}
