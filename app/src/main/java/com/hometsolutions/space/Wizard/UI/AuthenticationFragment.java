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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.hometsolutions.space.Activitys.NewConnectionActivity;
import com.hometsolutions.space.R;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthenticationFragment extends Fragment {


    private static final String ARG_KEY = "key2";
    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    public static AuthenticationPage mPage;
    public static CircularProgressButton circularButton;
    TextView title;

    public AuthenticationFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        AuthenticationFragment fragment = new AuthenticationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (AuthenticationPage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.wizard_fragment_authentication, container, false);

        title = ((TextView) layout.findViewById(R.id.auth_title));title.setText(mPage.getTitle());
        title.setText(mPage.getTitle());
        title.setTextColor(getResources().getColor(com.tech.freak.wizardpager.R.color.step_pager_selected_tab_color));

        circularButton = (CircularProgressButton) layout.findViewById(R.id.btn_authenticate);
        circularButton.setIndeterminateProgressMode(true);
        circularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton.getProgress() == 0) {
                    circularButton.setProgress(50);
                    ((NewConnectionActivity) getActivity ()).mSmoothBluetooth.send("CHK-AUT", false);//bluetoothSerial.write("CHK-AUT");
                    ((NewConnectionActivity) getActivity ()).timerStart();
                } else if (circularButton.getProgress() == -1) {
                    circularButton.setProgress(0);
                }
            }
        });

        return layout;
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
