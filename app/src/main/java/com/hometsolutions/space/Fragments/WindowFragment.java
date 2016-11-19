package com.hometsolutions.space.Fragments;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hometsolutions.space.Adapters.mRoomAdapterWindowC;
import com.hometsolutions.space.Activitys.MainActivity;
import com.hometsolutions.space.Utils.DatabaseHelper;
import com.hometsolutions.space.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WindowFragment extends Fragment {

    private mRoomAdapterWindowC adapter;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;
    private DatabaseHelper databaseHelper;

    public WindowFragment() {
        this.mainActivity = mainActivity;
        //this.databaseHelper = new DatabaseHelper(mainActivity.getApplicationContext());
    }

    public static WindowFragment newInstance(@LayoutRes int layoutResId) {
        Bundle args = new Bundle();
        //args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId);
        WindowFragment fragment = new WindowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_under_development, container, false);
        /*final View layout = inflater.inflate(R.layout.fragment_window, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_window);
        adapter = new mRoomAdapterWindowC(getContext(), databaseHelper.getRoomsArray());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(getActivity(), new
                DefaultItemClickListener() {
                    @Override
                    public boolean onItemClick(final View view, final int position) {
                        Log.i("WindowF","Click: " + position);
                        return false;
                    }

                    @Override
                    public void onItemLongPress(View view, int position) {
                        Log.i("WindowF","LongClick: " + position);
                    }
                }));*/
        return layout;
    }

    /*@Override
    public void onHiddenChanged(boolean hidden) {
        adapter = new mRoomAdapterWindowC(getContext(), databaseHelper.getRoomsArray());
        recyclerView.setAdapter(adapter);
        //super.onHiddenChanged(hidden);
    }*/

}
