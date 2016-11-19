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
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hometsolutions.space.Adapters.mFanAdapterEditFans;
import com.hometsolutions.space.Activitys.EditComponentsActivity;
import com.hometsolutions.space.Utils.DatabaseHelper;
import com.hometsolutions.space.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class addFanFragment extends Fragment implements View.OnClickListener {

    DatabaseHelper databaseHelper;
    public mFanAdapterEditFans adapter;
    EditComponentsActivity editComponentsActivity;
    public RecyclerView recyclerView;
    public String[] fanNames = new String[999];

    public addFanFragment(EditComponentsActivity editComponentsActivity) {
        // Required empty public constructor
        this.editComponentsActivity = editComponentsActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_add_fan, container, false);

        databaseHelper = new DatabaseHelper(editComponentsActivity.getApplicationContext());
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_add_fan);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadAdapter(editComponentsActivity.roomID);
        FloatingActionButton fab = (FloatingActionButton) layout.findViewById(R.id.add_fan_fab);
        fab.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add_fan_fab:
                new MaterialDialog.Builder(getContext())
                        .title("Add Fan")
                        .content("Please enter the name of the fan")
                        .inputType(InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .inputRange(4,15)
                        .positiveText("Add")
                        .negativeText("Cancel")
                        .input("Eg.: Bedroom fan", "", false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                try {
                                    //databaseHelper.addRoom(input.toString());
                                    databaseHelper.FAN_AddFan(input.toString(), editComponentsActivity.roomID);
                                    loadAdapter(editComponentsActivity.roomID);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //loadAdapter();
                                //recyclerView.invalidate();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //((EditComponentsActivity) getActivity()).showToast("Fuck! Its a negative click! :-(" ,0);
                            }
                        }).show();
        }
    }

    public void loadAdapter(int roomID) {
        fanNames = databaseHelper.FAN_getNames(roomID);
        adapter = new mFanAdapterEditFans(getContext(), fanNames, editComponentsActivity, addFanFragment.this);
        recyclerView.setAdapter(adapter);
    }
}
