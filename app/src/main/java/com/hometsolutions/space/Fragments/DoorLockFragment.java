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
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.hometsolutions.space.R;

import com.etiennelawlor.discreteslider.library.ui.*;
import com.etiennelawlor.discreteslider.library.utilities.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoorLockFragment extends Fragment {

    public Switch lightSw;
    public DiscreteSlider discreteSlider;
    public TextView stat;
    private ConstraintLayout cLayout;

    public DoorLockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_under_development, container, false);
        /*final View layout = inflater.inflate(R.layout.fragment_door_lock, container, false);
        discreteSlider = (DiscreteSlider) layout.findViewById(R.id.discreteSlider);
        lightSw = (Switch) layout.findViewById(R.id.lightSw);
        stat = (TextView) layout.findViewById(R.id.stat);
        if (lightSw.isChecked()) {
        }*/
        return layout;
    }

    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Detect when slider position changes
        discreteSlider.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
            @Override
            public void onPositionChanged(int position) {
                if(lightSw.isChecked()) {
                    switch (position) {
                        case 0:
                            stat.setText("Fan ID: 2 | Status: " + "OFF");
                            break;
                        case 1:
                            stat.setText("Fan ID: 2 | Status: " + "Speed 1");
                            break;
                        case 2:
                            stat.setText("Fan ID: 2 | Status: " + "Speed 2");
                            break;
                        case 3:
                            stat.setText("Fan ID: 2 | Status: " + "Speed 3");
                            break;
                        case 4:
                            stat.setText("Fan ID: 2 | Status: " + "Speed 4");
                            break;
                        case 5:
                            stat.setText("Fan ID: 2 | Status: " + "Speed 5");
                            break;
                    }
                } else {
                }
            }
        });
    }*/

}
