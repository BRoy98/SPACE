package com.hometsolutions.space.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hometsolutions.space.Adapters.mLightAdapterLightNext;
import com.hometsolutions.space.Activitys.MainActivity;
import com.hometsolutions.space.Utils.DatabaseHelper;
import com.hometsolutions.space.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

/**
 * A simple {@link Fragment} subclass.
 */
public class LightControlNextFragment extends Fragment {

    MainActivity mainActivity;
    mLightAdapterLightNext adapter;
    RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;

    public LightControlNextFragment(MainActivity mainActivity) {
        this.mainActivity= mainActivity;
        this.databaseHelper = new DatabaseHelper(mainActivity.getApplicationContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_light_control_next, container, false);
        //final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_light_control_next, null);
        Toolbar toolbar = (Toolbar) layout.findViewById(R.id.Light_next_toolbar);
        toolbar.setTitle("BACK TO ROOMS");
        toolbar.setNavigationIcon(new IconDrawable(mainActivity, MaterialIcons.md_arrow_back).sizeDp(24).color(Color.WHITE));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.hideAllFragment();
                mainActivity.lightFragment(true);
            }
        });
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_light_next);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadAdapter();
        return layout;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        loadAdapter();
        super.onHiddenChanged(hidden);
    }

    public void loadAdapter() {
        adapter = new mLightAdapterLightNext(getContext(), databaseHelper.LIGHT_getNames(mainActivity.lastRoomSelected), mainActivity);
        recyclerView.setAdapter(adapter);
    }
}
