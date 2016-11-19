package com.hometsolutions.space.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hometsolutions.space.Activitys.MainActivity;
import com.hometsolutions.space.Activitys.EditComponentsActivity;

import com.hometsolutions.space.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    MainActivity mainActivity;
    EditComponentsActivity EditComponentsActivity;


    public SettingsFragment(MainActivity mainActivity, EditComponentsActivity EditComponentsActivity) {
        // Required empty public constructor
        this.mainActivity = mainActivity;
        this.EditComponentsActivity = EditComponentsActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layout = inflater.inflate(R.layout.fragment_under_development, container, false);
        //final View layout = inflater.inflate(R.layout.fragment_settings, container, false);
        return layout;
    }

}
