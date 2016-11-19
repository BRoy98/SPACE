package com.hometsolutions.space.Activitys;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hometsolutions.space.Adapters.*;
import com.hometsolutions.space.R;

public class EditComponentsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    public String roomName;
    public int roomID;

    private ViewPager viewPager;

    public EditComponentsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room_components);

        String INTENT_ROOM_NAME = "roomName";
        roomName = getIntent().getStringExtra(INTENT_ROOM_NAME);
        String INTENT_ROOM_ID = "roomID";
        roomID = getIntent().getIntExtra(INTENT_ROOM_ID, 9999);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(roomName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabAdapter adapter = new mTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), EditComponentsActivity.this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
