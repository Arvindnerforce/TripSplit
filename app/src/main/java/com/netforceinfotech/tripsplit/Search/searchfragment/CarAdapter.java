package com.netforceinfotech.tripsplit.Search.searchfragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.Search.SearchData;
import com.netforceinfotech.tripsplit.Search.SearchHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 8/29/2016.
 */
public class CarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int SIMPLE_TYPE = 0;
    private static final int IMAGE_TYPE = 1;
    private final LayoutInflater inflater;
    private List<CarData> itemList;
    private Context context;
    ArrayList<Boolean> booleanGames = new ArrayList<>();



    public CarAdapter(Context context, List<CarData> itemList)
    {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.row_search, parent, false);
        SearchHolder viewHolder = new SearchHolder(view);
        for (int i = 0; i < itemList.size(); i++) {
            if (i == 0) {
                booleanGames.add(true);
            } else {
                booleanGames.add(false);
            }
            Log.i("looppp", "" + i);
        }

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {

    }

    private void showMessage(String s)
    {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        return 12;
//        return itemList.size();
    }


}