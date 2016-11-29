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

package com.hometsolutions.space.Activitys;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hometsolutions.space.Adapters.mBT_DevicesListsAdapter;
import com.hometsolutions.space.R;
import com.hometsolutions.space.Utils.RecyclerItemClickListener;
import com.hometsolutions.space.Wizard.Model.ConnectionWizardModel;
import com.hometsolutions.space.Wizard.UI.AuthenticationFragment;
import com.hometsolutions.space.Wizard.UI.PairDeviceFragment;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;
import com.tech.freak.wizardpager.ui.ReviewFragment;
import com.tech.freak.wizardpager.ui.StepPagerStrip;
import com.rey.material.widget.Button;
import io.palaima.smoothbluetooth.SmoothBluetooth;
import io.palaima.smoothbluetooth.Device;

import java.util.ArrayList;
import java.util.List;

public class NewConnectionActivity extends AppCompatActivity implements
        PageFragmentCallbacks, ReviewFragment.Callbacks, ModelCallbacks,
        View.OnClickListener, ViewPager.OnPageChangeListener,
        StepPagerStrip.OnPageSelectedListener{


    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;
    private String subtitle;
    private boolean mEditingAfterReview;
    private boolean paused;

    public String deviceID_NEW;
    public int devicePort_NEW;
    public boolean deviceFirstSetupCheck_NEW;

    private AbstractWizardModel mWizardModel;
    private boolean mConsumePageSelectedEvent;

    private Button mNextButton;
    private Button mPrevButton;



    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;

    public SmoothBluetooth mSmoothBluetooth;
    public BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private List<Integer> mBuffer = new ArrayList<>();
    MaterialDialog btOffDialog;
    public CountDownTimer connectionDownTimer;
    long time = 10 * 1000;
    long interval = 1000;

    public NewConnectionActivity() {
    }

    private void updateBottomBar() {
        int position = mPager.getCurrentItem();

        if (position == mCurrentPageSequence.size()) {
            mNextButton.setText(R.string.tab_finish);
            mNextButton.setTextColor(Color.WHITE);
            ColorDrawable cd = new ColorDrawable(0xFF7cb342);
            mNextButton.setBackgroundDrawable(cd);
        } else {
            mNextButton.setText(mEditingAfterReview ? R.string.review
                    : R.string.tab_next);
            TypedValue v = new TypedValue();
            ColorDrawable cd = new ColorDrawable(0xFFf5f5f5);
            mPrevButton.setBackgroundDrawable(cd);
            mNextButton.setBackgroundDrawable(cd);
            mNextButton.setTextColor(Color.BLACK);
            mNextButton.setTextAppearance(this, v.resourceId);
            mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());
            mNextButton.setTextColor(mNextButton.isEnabled() ?
                    getResources().getColor(R.color.colorAccent) : getResources().getColor(R.color.grey_400));
        }
        mPrevButton.setVisibility(position <= 0 ?
                View.INVISIBLE : View.VISIBLE);
        mPrevButton.setTextColor(mPrevButton.isEnabled() ?
                getResources().getColor(R.color.grey_800) : getResources().getColor(R.color.grey_800));
    }

    public void timerStart() {
        connectionDownTimer = new MyCountDownTimer(time, interval).start();
    }

    public void timerStop() {
        if(connectionDownTimer != null)
            connectionDownTimer.cancel();

    }

    private void closeActivity() {
        finish();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_connection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.new_conn_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Connection Wizard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSmoothBluetooth = new SmoothBluetooth(this);
        mSmoothBluetooth.setListener(mListener);
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        mWizardModel = new ConnectionWizardModel(this);
        if (savedInstanceState != null) {
            mWizardModel.load(savedInstanceState.getBundle("model"));
        }

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mStepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);

        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);

        mWizardModel.registerListener(this);
        mStepPagerStrip.setOnPageSelectedListener(this);
        mPager.setOnPageChangeListener(this);
        mNextButton.setOnClickListener(this);
        mPrevButton.setOnClickListener(this);
        onPageTreeChanged();
        updateBottomBar();

        //region bluetoothOffDialog
        MaterialDialog.Builder builderBTOFF = new MaterialDialog.Builder(NewConnectionActivity.this)
                .title("Enable Bluetooth")
                .content("Bluetooth is turned off. In order to continue the wizard you need to " +
                        "enable bluetooth. Do you want to enable bluetooth now?")
                .positiveText("Enable")
                .negativeText("No, Quit Wizard")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        switch (which.toString()) {
                            case "POSITIVE":
                                if (mBluetoothAdapter != null) {
                                    Log.i("mBluetoothAdapter", "!null");
                                    mBluetoothAdapter.enable();
                                }
                                if (PairDeviceFragment.connect_text != null) {
                                    mStepPagerStrip.setCurrentPage(2);
                                    PairDeviceFragment.updateConnectState(false);
                                }
                                if (PairDeviceFragment.connect_text != null) {
                                    mStepPagerStrip.setCurrentPage(2);
                                    PairDeviceFragment.updateConnectState(false);
                                }
                                break;
                            case "NEGATIVE":
                                closeActivity();
                                break;
                        }
                    }
                })
                .cancelable(false);
        btOffDialog = builderBTOFF.build();
        //endregion
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        paused = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSmoothBluetooth.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prev_button:
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                break;
            case R.id.next_button:
                if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {

                } else {
                    if (mEditingAfterReview) {
                        mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
                    } else {
                        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                    }
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mStepPagerStrip.setCurrentPage(position);
        if (mConsumePageSelectedEvent) {
            mConsumePageSelectedEvent = false;
            return;
        }

        mEditingAfterReview = false;
        updateBottomBar();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageStripSelected(int position) {
        position = Math.min(mPagerAdapter.getCount() - 1,
                position);
        if (mPager.getCurrentItem() != position) {
            mPager.setCurrentItem(position);
        }
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                if(mPagerAdapter != null)
                    mPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 =
        // review
        // step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    @Override
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String pageKey) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(pageKey)) {
                mConsumePageSelectedEvent = true;
                mEditingAfterReview = true;
                mPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        btOffDialog.show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        btOffDialog.dismiss();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }
        }
    };

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= mCurrentPageSequence.size()) {
                return new ReviewFragment();
            }

            return mCurrentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position,
                                   Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            return Math.min(mCutOffPage + 1, mCurrentPageSequence == null ? 1
                    : mCurrentPageSequence.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            Log.i("SPACE - TIMER", "Time's up!");
            //if(timerTick > 9)
                AuthenticationFragment.circularButton.setProgress(-1);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //timerTick++;
            Log.i("SPACE - TIMER", "" + millisUntilFinished / 1000);
        }
    }

    private SmoothBluetooth.Listener mListener = new SmoothBluetooth.Listener() {

        @Override
        public void onBluetoothNotSupported() {

        }

        @Override
        public void onBluetoothNotEnabled() {

        }

        @Override
        public void onConnecting(Device device) {
            subtitle = getString(R.string.status_connecting);
            if (PairDeviceFragment.connect_text != null)
                PairDeviceFragment.connect_text.setText(subtitle);
        }

        @Override
        public void onConnected(Device device) {
            subtitle = getString(R.string.status_connected, device.getName());
            if (PairDeviceFragment.connect_text != null)
                PairDeviceFragment.updateConnectState(true);
            if (PairDeviceFragment.connect_text != null)
                PairDeviceFragment.connect_text.setText(subtitle);
        }

        @Override
        public void onDisconnected() {
            if(!paused) {
                subtitle = getString(R.string.status_disconnected);
                if (PairDeviceFragment.connect_text != null)
                    PairDeviceFragment.updateConnectState(false);
                if (PairDeviceFragment.connect_text != null)
                    PairDeviceFragment.connect_text.setText(subtitle);
            }
        }

        @Override
        public void onConnectionFailed(Device device) {
            subtitle = getString(R.string.status_disconnected);
            if (PairDeviceFragment.connect_text != null)
                PairDeviceFragment.updateConnectState(false);
            if (PairDeviceFragment.connect_text != null)
                PairDeviceFragment.connect_text.setText(subtitle);
        }

        @Override
        public void onDiscoveryStarted() {

        }

        @Override
        public void onDiscoveryFinished() {

        }

        @Override
        public void onNoDevicesFound() {

        }

        @Override
        public void onDevicesFound(final List<Device> deviceList,
                                   final SmoothBluetooth.ConnectionCallback connectionCallback) {

            final MaterialDialog dialog = new MaterialDialog.Builder(NewConnectionActivity.this)
                    .title("Paired Devices")
                    .adapter(new mBT_DevicesListsAdapter(NewConnectionActivity.this, deviceList), null)
                    .build();

            RecyclerView listView = dialog.getRecyclerView();
            if (listView != null) {
                listView.addOnItemTouchListener(new RecyclerItemClickListener(NewConnectionActivity.this, listView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        connectionCallback.connectTo(deviceList.get(position));
                        dialog.dismiss();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        // ...
                    }
                }));
            }

            dialog.show();
        }

        @Override
        public void onDataReceived(int data) {
            mBuffer.add(data);
            if (data == 64 && !mBuffer.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (int integer : mBuffer) {
                    sb.append( Character.toString ((char) integer));
                }
                mBuffer.clear();
                //Log.i("SMOOTH - DATA_R", sb.toString());
                int i = 0;
                String[] values = new String[10];
                for (String value: sb.toString().split("__")) {
                    values[i] = value;
                    Log.i("SPACE - SPLIT", String.valueOf(i) +" : " + values[i]);
                    i++;
                }
                if(values[1] != null) {
                    /*
                    * ARDUINO SENDING DATA:
                    * mySerial.println("__CHK-AUT__" + DEVICE_ID + "__8__" + FIRST_SETUP + "__@");
                    */
                    if(values[1].contains("CHK-AUT") && values[2] != null && values[3] != null && values[4] != null ) {
                            deviceID_NEW = values[2];
                            devicePort_NEW = Integer.parseInt(values[3]);
                            if(values[4].contains("FALSE"))
                                deviceFirstSetupCheck_NEW = false;
                            else if(values[4].contains("TRUE"))
                                deviceFirstSetupCheck_NEW = true;
                            timerStop();
                            AuthenticationFragment.circularButton.setProgress(100);
                        if(AuthenticationFragment.circularButton != null) {
                            AuthenticationFragment.mPage.isAuthenticated = true;
                            AuthenticationFragment.mPage.notifyDataChanged();
                        }
                    }
                }
            }
        }
    };
}
