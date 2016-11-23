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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hometsolutions.space.R;

import java.util.List;

import io.palaima.smoothbluetooth.Device;

public class mDevicesAdapter extends RecyclerView.Adapter<mDevicesAdapter.mViewHolder> {

    private final Context mContext;
    private final List<Device> mDevices;

    public mDevicesAdapter(Context context, List<Device> devices) {
        this.mContext = context;
        this.mDevices = devices;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_list_device, parent, false);
        mViewHolder holder = new mViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        Device device = mDevices.get(position);
        holder.mNameTV.setText(device.getName());
        holder.mMacTV.setText(device.getAddress());
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder{

        public TextView mNameTV;
        public TextView mMacTV;

        public mViewHolder(View itemView) {
            super(itemView);

            mNameTV = (TextView) itemView.findViewById(R.id.device_name);
            mMacTV = (TextView) itemView.findViewById(R.id.device_address);
        }
    }

}
