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


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hometsolutions.space.Adapters.mRoomAdapterWindowC;
import com.hometsolutions.space.Activitys.MainActivity;
import com.hometsolutions.space.Utils.DatabaseHelper;
import com.dd.CircularProgressButton;
import com.hometsolutions.space.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WindowFragment extends Fragment {

    private mRoomAdapterWindowC adapter;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;
    private DatabaseHelper databaseHelper;

    public WindowFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        //this.databaseHelper = new DatabaseHelper(mainActivity.getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.wizard_fragment_authentication, container, false);
        final CircularProgressButton circularButton1 = (CircularProgressButton) layout.findViewById(R.id.btn_authenticate);
        circularButton1.setIndeterminateProgressMode(true);
        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton1.getProgress() == 0) {
                    circularButton1.setProgress(50);
                } else if (circularButton1.getProgress() == 100) {
                    circularButton1.setProgress(0);
                } else {
                    circularButton1.setProgress(100);
                }
            }
        });

        /*sucBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb.loadingSuccessful();
                Log.i("SPACE - LB ST", String.valueOf(lb.getCurrentState()));
            }
        });

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb.loadingFailed();
                Log.i("SPACE - LB ST", String.valueOf(lb.getCurrentState()));
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                lb.reset();
                Log.i("SPACE - LB ST", String.valueOf(lb.getCurrentState()));
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb.cancelLoading();
                Log.i("SPACE - LB ST", String.valueOf(lb.getCurrentState()));
            }
        });*/

        return layout;
    }

    /*@Override
    public void onHiddenChanged(boolean hidden) {
        adapter = new mRoomAdapterWindowC(getContext(), databaseHelper.getRoomsArray());
        recyclerView.setAdapter(adapter);
        //super.onHiddenChanged(hidden);
    }*/

}
