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
import com.hometsolutions.space.Adapters.mLightAdapterEditLights;
import com.hometsolutions.space.Activitys.EditComponentsActivity;
import com.hometsolutions.space.Utils.DatabaseHelper;
import com.hometsolutions.space.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class addLightFragment extends Fragment implements View.OnClickListener {

    DatabaseHelper databaseHelper;
    public mLightAdapterEditLights adapter;
    EditComponentsActivity editComponentsActivity;
    public RecyclerView recyclerView;
    public String[] lightNames = new String[999];

    public addLightFragment(EditComponentsActivity editComponentsActivity) {
        this.editComponentsActivity = editComponentsActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_add_light, container, false);
        databaseHelper = new DatabaseHelper(editComponentsActivity.getApplicationContext());
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_add_light);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadAdapter(editComponentsActivity.roomID);
        FloatingActionButton fab = (FloatingActionButton) layout.findViewById(R.id.add_light_fab);
        fab.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add_light_fab:
                new MaterialDialog.Builder(getContext())
                        .title("Add Light")
                        .content("Please enter the name of the light")
                        .inputType(InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .inputRange(4,15)
                        .positiveText("Add")
                        .negativeText("Cancel")
                        .input("Eg.: Bedroom light", "", false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                try {
                                    //databaseHelper.addRoom(input.toString());
                                    databaseHelper.LIGHT_AddLight(input.toString(), editComponentsActivity.roomID);
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
        lightNames = databaseHelper.LIGHT_getNames(roomID);
        adapter = new mLightAdapterEditLights(getContext(), lightNames, editComponentsActivity, addLightFragment.this);
        recyclerView.setAdapter(adapter);
    }
}
