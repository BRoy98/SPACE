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
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hometsolutions.space.Utils.DatabaseHelper;
import com.hometsolutions.space.Activitys.MainActivity;
import com.hometsolutions.space.R;
import com.etiennelawlor.discreteslider.library.ui.DiscreteSlider;

/**
 * Created by Bishwajyoti Roy on 11/9/2016.
 */

public class mLightAdapterLightNext extends RecyclerView.Adapter<mLightAdapterLightNext.mViewHolder> {

    private Context context;
    private String[] data;
    private LayoutInflater inflator;
    private DatabaseHelper databaseHelper;
    private MainActivity mainActivity;

    public mLightAdapterLightNext(Context context, String[] data, MainActivity mainActivity) {

        this.context = context;
        this.data = data;
        inflator = LayoutInflater.from(context);
        this.databaseHelper = new DatabaseHelper(context);
        this.mainActivity = mainActivity;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = inflator.inflate(R.layout.recycler_layout_light_dim_control, parent, false);
        return new mViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(mLightAdapterLightNext.mViewHolder holder, int position) {
        holder.lightName.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return databaseHelper.LIGHT_getCount(mainActivity.lastRoomSelected);
    }

    class mViewHolder extends RecyclerView.ViewHolder{

        CardView card;
        TextView lightName;
        DiscreteSlider discreteSlider;
        TextView lightState;

        mViewHolder(View itemView) {
            super(itemView);

            lightName = (TextView) itemView.findViewById(R.id.Recycler_text_light_name);
            card = (CardView) itemView.findViewById(R.id.Recycler_card_light_Control);
            //discreteSlider = (DiscreteSlider) itemView.findViewById(R.id.Recycler_slider_light_discreteSlider);
            //lightState = (TextView) itemView.findViewById(R.id.Recycler_text_lightState);
            discreteSlider.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
                @Override
                public void onPositionChanged(int position) {
                    String[] lights = databaseHelper.LIGHT_getNames(mainActivity.lastRoomSelected);
                    int id = getAdapterPosition();
                    String state = null;
                    switch (position) {
                        case 0:
                            state = "0";
                            lightState.setText("OFF");
                            lightState.setTextColor(Color.RED);
                            break;
                        case 1:
                            lightState.setText("Brightness: 20%");
                            lightState.setTextColor(Color.GREEN);
                            state = "1";
                            break;
                        case 2:
                            lightState.setText("Brightness: 40%");
                            lightState.setTextColor(Color.GREEN);
                            state = "2";
                            break;
                        case 3:
                            lightState.setText("Brightness: 60%");
                            lightState.setTextColor(Color.GREEN);
                            state = "3";
                            break;
                        case 4:
                            lightState.setText("Brightness: 80%");
                            lightState.setTextColor(Color.GREEN);
                            state = "4";
                            break;
                        case 5:
                            lightState.setText("Brightness: 100%");
                            lightState.setTextColor(Color.GREEN);
                            state = "5";
                            break;
                    }
                    //mainActivity.bluetoothSerial.write("**LL**" + databaseHelper.LIGHT_getID(lights[id]) + "**" + state + "**", true);
                    Log.i("SPACE - Light Check","**LL**" + databaseHelper.LIGHT_getID(lights[id]) + "**" + state + "**");
                }
            });
        }
    }
}
