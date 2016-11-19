package com.hometsolutions.space.Fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.hometsolutions.space.Adapters.*;
import com.hometsolutions.space.Activitys.*;
import com.hometsolutions.space.Utils.DatabaseHelper;
import com.hometsolutions.space.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetupFragment extends Fragment  implements View.OnClickListener {
    private FloatingActionButton fabSetup;
    public mRoomAdapterEditRooms adapter;
    public RecyclerView recyclerView;
    MainActivity mainActivity;
    DatabaseHelper databaseHelper;

    public SetupFragment(MainActivity mainActivity) {
        // Required empty public constructor
        this.mainActivity = mainActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_setup, container, false);
        fabSetup = (FloatingActionButton) layout.findViewById(R.id.fab_setup);
        fabSetup.setOnClickListener(this);
        databaseHelper = new DatabaseHelper(mainActivity.getApplicationContext());
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_edit);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadAdapter();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i("RE SCROLL STATE", Integer.toString(newState));
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                Log.i("RE SCROLL", Integer.toString(dy));
            }
        });

        /*recyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(getActivity(), new
                DefaultItemClickListener() {
                    @Override
                    public boolean onItemClick(final View view, final int position) {
                        Log.i("WindowF","Click: " + position);
                        return false;
                    }

                    @Override
                    public void onItemLongPress(View view, int position) {
                        Log.i("WindowF","LongClick: " + position);
                        mainActivity.editRoomLongPress(position);
                    }
                }));*/
        return layout;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab_setup:
                NewConnectionActivity nc = new NewConnectionActivity();
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                Intent intent = new Intent(getContext(),nc.getClass());
                getContext().startActivity(intent, bndlanimation);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        loadAdapter();
        super.onHiddenChanged(hidden);
    }

    public void loadAdapter() {
        adapter = new mRoomAdapterEditRooms(getContext(), databaseHelper.getRoomsArray(), mainActivity, SetupFragment.this);
        recyclerView.setAdapter(adapter);
    }
}
