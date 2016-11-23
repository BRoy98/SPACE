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
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hometsolutions.space.R;
import com.hometsolutions.space.Wizard.Model.ConnectionWizardModel;
import com.hometsolutions.space.Wizard.UI.AuthenticationFragment;
import com.hometsolutions.space.Wizard.UI.PairDeviceFragment;
import com.macroyau.blue2serial.BluetoothDeviceListDialog;
import com.macroyau.blue2serial.BluetoothSerial;
import com.macroyau.blue2serial.BluetoothSerialListener;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;
import com.tech.freak.wizardpager.ui.ReviewFragment;
import com.tech.freak.wizardpager.ui.StepPagerStrip;
import com.rey.material.widget.Button;

import java.util.List;

public class NewConnectionActivity extends AppCompatActivity implements
        PageFragmentCallbacks, ReviewFragment.Callbacks, ModelCallbacks, BluetoothSerialListener,
        BluetoothDeviceListDialog.OnDeviceSelectedListener, View.OnClickListener, ViewPager.OnPageChangeListener,
        StepPagerStrip.OnPageSelectedListener{


    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;

    private boolean mEditingAfterReview;

    private AbstractWizardModel mWizardModel;
    private boolean mConsumePageSelectedEvent;

    private Button mNextButton;
    private Button mPrevButton;

    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;

    public boolean connected = false;
    private boolean BLUTOOTH_SETUP = false;
    public BluetoothSerial bluetoothSerial;
    public BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private MaterialDialog.Builder builder;
    MaterialDialog btOffDialog;
    public CountDownTimer connectionDownTimer;
    long time = 10 * 1000;
    long interval = 1 * 1000;

    public NewConnectionActivity() {
    }

    public void showDeviceListDialog(boolean a) {
        BluetoothDeviceListDialog dialog = new BluetoothDeviceListDialog(this);
        dialog.setOnDeviceSelectedListener(this);
        dialog.setTitle("Paired Devices");
        dialog.setDevices(bluetoothSerial.getPairedDevices());
        if (a)
            dialog.showAddress(true);
        else
            dialog.showAddress(false);
        dialog.show();
        //startActivityForResult(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS), 0);
    }

    private void updateBluetoothState() {
        final int state;
        if (bluetoothSerial != null)
            state = bluetoothSerial.getState();
        else
            state = BluetoothSerial.STATE_DISCONNECTED;

        String subtitle;
        switch (state) {
            case BluetoothSerial.STATE_CONNECTING:
                subtitle = getString(R.string.status_connecting);
                break;
            case BluetoothSerial.STATE_CONNECTED:
                subtitle = getString(R.string.status_connected, bluetoothSerial.getConnectedDeviceName());
                if (PairDeviceFragment.connect_text != null)
                    PairDeviceFragment.updateConnectState(true);
                break;
            case BluetoothSerial.STATE_DISCONNECTED:
                subtitle = getString(R.string.status_disconnected);
                if (PairDeviceFragment.connect_text != null)
                    PairDeviceFragment.updateConnectState(false);
                break;
            default:
                subtitle = getString(R.string.status_disconnected);
                break;
        }
        if (PairDeviceFragment.connect_text != null)
            PairDeviceFragment.connect_text.setText(subtitle);
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
        AuthenticationFragment.circularButton.setProgress(-1);
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

        bluetoothSerial = new BluetoothSerial(this, this);
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
        builder = new MaterialDialog.Builder(NewConnectionActivity.this)
                .title("Enable Bluetooth")
                .content("Bluetooth is turned off. In order to continue the wizard you need to " +
                        "enable bluetooth. Do you want to enable bluetooth now?")
                .positiveText("Enable")
                .negativeText("No, Quit Wizard")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.i("Press", which.toString());
                        switch (which.toString()) {
                            case "POSITIVE":

                                break;
                            case "NEGATIVE":
                                closeActivity();
                                break;
                        }
                    }
                })
                .cancelable(false);
        btOffDialog = builder.build();
        //endregion
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isEnabled()) {
                bluetoothSerial.setup();
                BLUTOOTH_SETUP = true;
            }

        } else {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(R.string.no_bluetooth)
                    .setPositiveButton(R.string.action_quit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBluetoothState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetoothSerial.stop();
        connected = false;
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

                    //On finish
/*
                    1. Fixed license files
                    2. Get 'Display Dame' data at the end of the wizard (found how to get data at the end of the wizard)*/

                    String dpName = mWizardModel.findByKey("Six ports:Display Name").getData().getString("dp_name");

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
            AuthenticationFragment.circularButton.setProgress(-1);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i("SPACE - TIMER", "" + millisUntilFinished / 1000);
        }
    }

    @Override
    public void onBluetoothDeviceSelected(BluetoothDevice device) {
        bluetoothSerial.connect(device);
    }

    @Override
    public void onBluetoothNotSupported() {

    }

    @Override
    public void onBluetoothDisabled() {

    }

    @Override
    public void onBluetoothDeviceDisconnected() {
        updateBluetoothState();
        connected = false;
    }

    @Override
    public void onConnectingBluetoothDevice() {
        updateBluetoothState();
    }

    @Override
    public void onBluetoothDeviceConnected(String name, String address) {
        updateBluetoothState();
        connected = true;
    }

    @Override
    public void onBluetoothSerialRead(String message) {
        int i = 0;
        String auth = message;
        Log.i("SPACE - MESSAGE", auth);
        String[] values = new String[10];
        //Log.i("SPACE - SPLIT", String.valueOf(values.length));
        if(auth.startsWith("_")) {
            for (String data: auth.split("__")) {
                values[i] = data;
                Log.i("SPACE - SPLIT", data);
                i++;
            }
        }
    }

    @Override
    public void onBluetoothSerialWrite(String message) {

    }
}
