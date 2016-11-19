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

package com.hometsolutions.space.Fragments;


import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hometsolutions.space.Utils.DatabaseHelper;
import com.hometsolutions.space.Activitys.MainActivity;
import com.hometsolutions.space.Adapters.mRoomAdapterLightC;
import com.hometsolutions.space.R;
import com.thesurix.gesturerecycler.DefaultItemClickListener;
import com.thesurix.gesturerecycler.RecyclerItemTouchListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class LightControlFragment extends  Fragment{

    mRoomAdapterLightC adapter;
    RecyclerView recyclerView;
    private MainActivity mainActivity;
    private DatabaseHelper databaseHelper;

    public LightControlFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.databaseHelper = new DatabaseHelper(mainActivity.getApplicationContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_light_control, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_light);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadAdapter();
        recyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(getActivity(), new
                DefaultItemClickListener() {
                    @Override
                    public boolean onItemClick(final View view, final int position) {
                        Log.i("LightF","Click: " + position);
                        mainActivity.lastRoomSelected = position;
                        mainActivity.hideAllFragment();
                        mainActivity.lightNextFragment(true);
                        return false;
                    }

                    @Override
                    public void onItemLongPress(View view, int position) {
                        Log.i("LightF","LongClick: " + position);
                    }
                }));
        return layout;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        loadAdapter();
        super.onHiddenChanged(hidden);
    }

    public void loadAdapter() {
        adapter = new mRoomAdapterLightC(getContext(), databaseHelper.getRoomsArray());
        recyclerView.setAdapter(adapter);
    }

}
