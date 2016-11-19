package com.hometsolutions.space.Fragments;


import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hometsolutions.space.Utils.DatabaseHelper;
import com.hometsolutions.space.Activitys.MainActivity;
import com.hometsolutions.space.Adapters.mRoomAdapterLightC;
import com.hometsolutions.space.R;
import com.thesurix.gesturerecycler.DefaultItemClickListener;
import com.thesurix.gesturerecycler.RecyclerItemTouchListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class LightControlFragment extends  Fragment{

    mRoomAdapterLightC adapter;
    RecyclerView recyclerView;
    private MainActivity mainActivity;
    private DatabaseHelper databaseHelper;

    public LightControlFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.databaseHelper = new DatabaseHelper(mainActivity.getApplicationContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_light_control, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_light);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadAdapter();
        recyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(getActivity(), new
                DefaultItemClickListener() {
                    @Override
                    public boolean onItemClick(final View view, final int position) {
                        Log.i("LightF","Click: " + position);
                        mainActivity.lastRoomSelected = position;
                        mainActivity.hideAllFragment();
                        mainActivity.lightNextFragment(true);
                        return false;
                    }

                    @Override
                    public void onItemLongPress(View view, int position) {
                        Log.i("LightF","LongClick: " + position);
                    }
                }));
        return layout;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        loadAdapter();
        super.onHiddenChanged(hidden);
    }

    public void loadAdapter() {
        adapter = new mRoomAdapterLightC(getContext(), databaseHelper.getRoomsArray());
        recyclerView.setAdapter(adapter);
    }

}
