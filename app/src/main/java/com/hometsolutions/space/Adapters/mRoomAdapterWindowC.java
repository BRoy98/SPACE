/*****************************************************************************************
 *
 *                       Copyright (C) 2016 Bishwajyoti Roy
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 ****************************************************************************************/

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
