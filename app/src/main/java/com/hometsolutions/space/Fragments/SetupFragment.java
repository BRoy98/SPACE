/*****************************************************************************************
 * Copyright (C) 2016 Bishwajyoti Roy
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ****************************************************************************************/

package com.hometsolutions.space.Fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.hometsolutions.space.Adapters.*;
import com.hometsolutions.space.Activitys.*;
import com.hometsolutions.space.Utils.DatabaseHelper;
import com.hometsolutions.space.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetupFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton fabSetup;
    public mRoomAdapterEditRooms adapter;
    public RecyclerView recyclerView;
    MainActivity mainActivity;
    DatabaseHelper databaseHelper;

    public SetupFragment(MainActivity mainActivity) {
        // Required empty public constructor
        this.mainActivity = mainActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_setup, container, false);
        fabSetup = (FloatingActionButton) layout.findViewById(R.id.fab_setup);
        fabSetup.setOnClickListener(this);
        databaseHelper = new DatabaseHelper(mainActivity.getApplicationContext());
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_edit);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadAdapter();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i("RE SCROLL STATE", Integer.toString(newState));
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.i("RE SCROLL", Integer.toString(dy));
            }
        });

        /*recyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(getActivity(), new
                DefaultItemClickListener() {
                    @Override
                    public boolean onItemClick(final View view, final int position) {
                        Log.i("WindowF","Click: " + position);
                        return false;
                    }

                    @Override
                    public void onItemLongPress(View view, int position) {
                        Log.i("WindowF","LongClick: " + position);
                        mainActivity.editRoomLongPress(position);
                    }
                }));*/
        return layout;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab_setup:
                if (mainActivity.mBluetoothAdapter != null) {
                    if (mainActivity.mBluetoothAdapter.isEnabled()) {
                        NewConnectionActivity nc = new NewConnectionActivity();
                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                        Intent intent = new Intent(getContext(), nc.getClass());
                        getContext().startActivity(intent, bndlanimation);
                    } else {
                        new MaterialDialog.Builder(mainActivity)
                                .title("Enable Bluetooth")
                                .content("In order to connect to a device, you need to " +
                                        "enable bluetooth. Do you want to enable bluetooth now?")
                                .positiveText("Enable")
                                .negativeText("No, Thanks")
                                .onAny(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Log.i("Press", which.toString());
                                        switch (which.toString()) {
                                            case "POSITIVE":
                                                try {
                                                    if (!mainActivity.mBluetoothAdapter.isEnabled()) {
                                                        mainActivity.mBluetoothAdapter.enable();
                                                    }
                                                    do {
                                                        NewConnectionActivity nc = new NewConnectionActivity();
                                                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                                                        Intent intent = new Intent(getContext(), nc.getClass());
                                                        getContext().startActivity(intent, bndlanimation);
                                                    }
                                                        while (mainActivity.mBluetoothAdapter.isEnabled());

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "NEGATIVE":
                                                break;
                                        }
                                    }
                                })
                                .show();
                    }
                }
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        loadAdapter();
        super.onHiddenChanged(hidden);
    }

    public void loadAdapter() {
        adapter = new mRoomAdapterEditRooms(getContext(), databaseHelper.getRoomsArray(), mainActivity, SetupFragment.this);
        recyclerView.setAdapter(adapter);
    }
}
