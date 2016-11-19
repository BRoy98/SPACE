package com.hometsolutions.space.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hometsolutions.space.Utils.DatabaseHelper;
import com.hometsolutions.space.R;

/**
 * Created by Bishwajyoti Roy on 13-10-2016.
 */

public class mRoomAdapterWindowC extends RecyclerView.Adapter<mRoomAdapterWindowC.mViewHolder> {

    private Context context;
    private String[] data;
    private LayoutInflater inflator;
    private DatabaseHelper databaseHelper;

    public mRoomAdapterWindowC(Context context, String[] data) {

        this.context = context;
        this.data = data;
        inflator = LayoutInflater.from(context);
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layout = inflator.inflate(R.layout.recycler_layout_rooms, parent, false);
        mViewHolder holder = new mViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, final int position) {

        holder.roomName.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return databaseHelper.getRoomCount();
    }

    class mViewHolder extends RecyclerView.ViewHolder{

        TextView roomName;
        CardView card;

        public mViewHolder(View itemView) {
            super(itemView);

            roomName = (TextView) itemView.findViewById(R.id.Recycler_text_name_rooms);
            card = (CardView) itemView.findViewById(R.id.Recycler_card_rooms);
        }
    }
}
