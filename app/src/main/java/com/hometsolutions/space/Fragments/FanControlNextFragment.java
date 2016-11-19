package com.hometsolutions.space.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hometsolutions.space.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FanControlNextFragment extends Fragment {


    public FanControlNextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fan_control_next, container, false);
    }

}
