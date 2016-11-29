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

package com.hometsolutions.space.Wizard.UI;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hometsolutions.space.Activitys.NewConnectionActivity;
import com.hometsolutions.space.R;
import com.rey.material.widget.Spinner;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

public class SetupPortFragment extends Fragment {

    private static final String ARG_KEY = "key2";
    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private static SetupPortPage mPage;
    Spinner deviceType1, deviceType2, deviceType3, deviceType4, deviceType5,
            deviceType6, deviceType7, deviceType8, deviceType9, deviceType10;
    CardView card1, card2, card3, card4, card5, card6, card7, card8, card9, card10;
    private String[] PortType = new String[] {"None", "Light","Fan","Plug"};
    TextView title;
    int devicePorts;

    public SetupPortFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        SetupPortFragment fragment = new SetupPortFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (SetupPortPage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.wizard_fragment_setup_port, container, false);

        title = ((TextView) rootView.findViewById(R.id.setupPort_title));title.setText(mPage.getTitle());
        title.setText(mPage.getTitle());
        title.setTextColor(getResources().getColor(com.tech.freak.wizardpager.R.color.step_pager_selected_tab_color));

        deviceType1 = (Spinner) rootView.findViewById(R.id.Device_type_list1);
        deviceType2 = (Spinner) rootView.findViewById(R.id.Device_type_list2);
        deviceType3 = (Spinner) rootView.findViewById(R.id.Device_type_list3);
        deviceType4 = (Spinner) rootView.findViewById(R.id.Device_type_list4);
        deviceType5 = (Spinner) rootView.findViewById(R.id.Device_type_list5);
        deviceType6 = (Spinner) rootView.findViewById(R.id.Device_type_list6);
        deviceType7 = (Spinner) rootView.findViewById(R.id.Device_type_list7);
        deviceType8 = (Spinner) rootView.findViewById(R.id.Device_type_list8);
        deviceType9 = (Spinner) rootView.findViewById(R.id.Device_type_list9);
        deviceType10 = (Spinner) rootView.findViewById(R.id.Device_type_list10);

        card1 = (CardView) rootView.findViewById(R.id.Recycler_card_setup_port1);
        card2 = (CardView) rootView.findViewById(R.id.Recycler_card_setup_port2);
        card3 = (CardView) rootView.findViewById(R.id.Recycler_card_setup_port3);
        card4 = (CardView) rootView.findViewById(R.id.Recycler_card_setup_port4);
        card5 = (CardView) rootView.findViewById(R.id.Recycler_card_setup_port5);
        card6 = (CardView) rootView.findViewById(R.id.Recycler_card_setup_port6);
        card7 = (CardView) rootView.findViewById(R.id.Recycler_card_setup_port7);
        card8 = (CardView) rootView.findViewById(R.id.Recycler_card_setup_port8);
        card9 = (CardView) rootView.findViewById(R.id.Recycler_card_setup_port9);
        card10 = (CardView) rootView.findViewById(R.id.Recycler_card_setup_port10);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.layout_port_type_row, PortType);
        adapter.setDropDownViewResource(R.layout.layout_port_type_dropdown);

        deviceType1.setAdapter(adapter);
        deviceType2.setAdapter(adapter);
        deviceType3.setAdapter(adapter);
        deviceType4.setAdapter(adapter);
        deviceType5.setAdapter(adapter);
        deviceType6.setAdapter(adapter);
        deviceType7.setAdapter(adapter);
        deviceType8.setAdapter(adapter);
        deviceType9.setAdapter(adapter);
        deviceType10.setAdapter(adapter);


        deviceType1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {

            }
        });
        NewConnectionActivity activity = (NewConnectionActivity) getActivity();
        devicePorts = activity.devicePort_NEW;

        for(int i = 10; i > devicePorts; i--) {
            if (i == 10)
                card10.setVisibility(View.GONE);
            if (i == 9)
                card9.setVisibility(View.GONE);
            if (i == 8)
                card8.setVisibility(View.GONE);
            if (i == 7)
                card7.setVisibility(View.GONE);
            if (i == 6)
                card6.setVisibility(View.GONE);
            if (i == 5)
                card5.setVisibility(View.GONE);
            if (i == 4)
                card4.setVisibility(View.GONE);
            if (i == 3)
                card3.setVisibility(View.GONE);
            if (i == 2)
                card2.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }
        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
