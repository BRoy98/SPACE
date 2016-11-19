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
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.hometsolutions.space.BuildConfig;
import com.hometsolutions.space.Fragments.*;
import com.hometsolutions.space.R;
import com.hometsolutions.space.Utils.DatabaseHelper;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.TypiconsIcons;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.macroyau.blue2serial.BluetoothDeviceListDialog;
import com.macroyau.blue2serial.BluetoothSerial;
import com.macroyau.blue2serial.BluetoothSerialListener;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements BluetoothSerialListener, BluetoothDeviceListDialog.OnDeviceSelectedListener {

    private Drawer drawer;
    private int drawerSelected;
    private boolean doubleBackToExitPressedOnce = false;
    private boolean BLUTOOTH_SETUP = false;
    private boolean fragmentHome = false;
    private boolean fragmentDashboard = false;
    private boolean fragmentFan = false;
    private boolean fragmentLight = false;
    private boolean fragmentLightNext = false;
    private boolean fragmentDoor = false;
    private boolean fragmentWindow = false;
    private boolean fragmentSetup = false;
    private boolean fragmentSettings = false;
    private boolean fragmentTrouble = false;
    private boolean fragmentLicences = false;
    private boolean fragmentConnectionWizerd = false;
    private boolean firstEnebleBTSnackBar = false;
    private boolean menuCreated = false;
    public boolean fragmentLightNextON = false;

    private Snackbar btSnackbar;
    public Menu menu;

    public String deviceID;
    public boolean connectedBeforeMinimize = false;
    public boolean connected = false;
    public FrameLayout cLayout;
    public BluetoothSerial bluetoothSerial;
    public BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public Context context;
    public int lastRoomSelected = 9999;
    EditComponentsActivity EditComponentsActivity;

    final ConnectionFragment hf = new ConnectionFragment();
    final DashBoaedFragment df = new DashBoaedFragment();
    private LightControlFragment lc = null;//new LightControlFragment();
    private LightControlNextFragment lnc = null;//new LightControlNextFragment();
    private FanControlFragment fc = null;//new FanControlFragment(MainActivity.this);
    private WindowFragment wf = null;//new WindowFragment(MainActivity.this);
    final DoorLockFragment dl = new DoorLockFragment();
    final SetupFragment erf = new SetupFragment(MainActivity.this);
    final SettingsFragment sf = new SettingsFragment(MainActivity.this, EditComponentsActivity);
    final TroubleshootingFragment tsf = new TroubleshootingFragment();
    final LibsSupportFragment libf = new LibsBuilder()
            .withAboutIconShown(true)
            .withAboutAppName("SPACE")
            .withAboutDescription("SPACE uses following open source libraries")
            //.withAutoDetect(false)
            .supportFragment();
    TextView toastText;


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

    public void updatePairButton() {
        if (bluetoothSerial == null) {
            return;
        } else if (bluetoothSerial.isConnected()) {
            if (ConnectionFragment.connect != null)
                ConnectionFragment.connect.setVisibility(cLayout.GONE);
            if (ConnectionFragment.disconnect != null)
                ConnectionFragment.disconnect.setVisibility(cLayout.VISIBLE);
        } else {
            if (ConnectionFragment.connect != null)
                ConnectionFragment.connect.setVisibility(cLayout.VISIBLE);
            if (ConnectionFragment.disconnect != null)
                ConnectionFragment.disconnect.setVisibility(cLayout.GONE);
        }
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
                break;
            default:
                subtitle = getString(R.string.status_disconnected);
                break;
        }
        if (ConnectionFragment.btPairState != null)
            ConnectionFragment.btPairState.setText(subtitle);

        if (getSupportActionBar() != null) {
            //getSupportActionBar().setSubtitle(subtitle);
        }
    }

    public void btCardClick(View view) {
        ConnectionFragment.btSwitch.toggle();
    }

    public void showToast(String toastString, int toastLength) {
        LayoutInflater inflater = getLayoutInflater();
        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        toastText = (TextView) toastRoot.findViewById(R.id.toastText);
        Toast toast = new Toast(context);
        toastText.setText(toastString);
        toast.setView(toastRoot);
        if (toastLength == 0)
            toast.setDuration(Toast.LENGTH_SHORT);
        if (toastLength == 1)
            toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public void connectionFragment(boolean show) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (show) {
            if (!fragmentHome) {
                tx.add(R.id.mainFrame, hf);
                tx.commit();
                getSupportActionBar().setTitle(R.string.action_connection);
                fragmentHome = true;
            } else if (fragmentHome) {
                tx.show(hf);
                tx.commit();
                getSupportActionBar().setTitle(R.string.action_connection);
            }
        }
        if (!show) {
            if (fragmentHome) {
                tx.hide(hf);
                tx.commit();
            }
        }
    }

    public void dashboardFragment(boolean a) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (a) {
            if (!fragmentDashboard) {
                tx.add(R.id.mainFrame, df);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_home);
                fragmentDashboard = true;
            } else if (fragmentDashboard) {
                tx.show(df);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_home);
            }
        }
        if (!a) {
            if (fragmentDashboard) {
                tx.hide(df);
                tx.commit();
            }
        }
    }

    public void lightFragment(boolean a) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (a) {
            if (!fragmentLight) {
                tx.add(R.id.mainFrame, lc);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_lightC);
                fragmentLight = true;
            } else if (fragmentLight) {
                tx.show(lc);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_lightC);
            }
        }
        if (!a) {
            if (fragmentLight) {
                tx.hide(lc);
                tx.commit();
            }
        }
    }

    public void lightNextFragment(boolean a) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (a) {
            if (!fragmentLightNext) {
                tx.add(R.id.mainFrame, lnc);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_lightC);
                fragmentLightNext = true;
                fragmentLightNextON = true;
            } else if (fragmentLightNext) {
                tx.show(lnc);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_lightC);
                fragmentLightNextON = true;
            }
        }
        if (!a) {
            if (fragmentLightNext) {
                tx.hide(lnc);
                tx.commit();
                fragmentLightNextON = false;
            }
        }
    }

    public void fanFragment(boolean a) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (a) {
            if (!fragmentFan) {
                tx.add(R.id.mainFrame, fc);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_fanC);
                fragmentFan = true;
            } else if (fragmentFan) {
                tx.show(fc);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_fanC);
            }
        }
        if (!a) {
            if (fragmentFan) {
                tx.hide(fc);
                tx.commit();
            }
        }
    }

    public void doorLockFragment(boolean a) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (a) {
            if (!fragmentDoor) {
                tx.add(R.id.mainFrame, dl);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_door_lockC);
                fragmentDoor = true;
            } else if (fragmentDoor) {
                tx.show(dl);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_door_lockC);
            }
        }
        if (!a) {
            if (fragmentDoor) {
                tx.hide(dl);
                tx.commit();
            }
        }
    }

    public void windowFragment(boolean a) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (a) {
            if (!fragmentWindow) {
                tx.add(R.id.mainFrame, wf);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_windowC);
                fragmentWindow = true;
            } else if (fragmentWindow) {
                tx.show(wf);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_windowC);
            }
        }
        if (!a) {
            if (fragmentWindow) {
                tx.hide(wf);
                tx.commit();
            }
        }
    }

    public void SetupFragment(boolean a) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (a) {
            if (!fragmentSetup) {
                tx.add(R.id.mainFrame, erf);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_device_setup);
                fragmentSetup = true;
            } else if (fragmentSetup) {
                tx.show(erf);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_device_setup);
            }
        }
        if (!a) {
            if (fragmentSetup) {
                tx.hide(erf);
                tx.commit();
            }
        }
    }

    public void settingsFragment(boolean a) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (a) {
            if (!fragmentSettings) {
                tx.add(R.id.mainFrame, sf);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_settings);
                fragmentSettings = true;
            } else if (fragmentSettings) {
                tx.show(sf);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_settings);
            }
        }
        if (!a) {
            if (fragmentSettings) {
                tx.hide(sf);
                tx.commit();
            }
        }
    }

    public void troubleFragment(boolean a) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (a) {
            if (!fragmentTrouble) {
                tx.add(R.id.mainFrame, tsf);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_trouble);
                fragmentTrouble = true;
            } else if (fragmentTrouble) {
                tx.show(tsf);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_trouble);
            }
        }
        if (!a) {
            if (fragmentTrouble) {
                tx.hide(tsf);
                tx.commit();
            }
        }
    }

    public void licenceFragment(boolean a) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (a) {
            if (!fragmentLicences) {
                tx.add(R.id.mainFrame, libf);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_licence);
                fragmentLicences = true;
                showToast("Made with ❤ in Kolkata", 1);
            } else if (fragmentLicences) {
                tx.show(libf);
                tx.commit();
                getSupportActionBar().setTitle(R.string.title_licence);
                showToast("Made with ❤ in Kolkata", 1);
            }
        }
        if (!a) {
            if (fragmentLicences) {
                tx.hide(libf);
                tx.commit();
            }
        }
    }

    public void hideAllFragment() {
        connectionFragment(false);
        dashboardFragment(false);
        lightFragment(false);
        lightNextFragment(false);
        fanFragment(false);
        doorLockFragment(false);
        windowFragment(false);
        settingsFragment(false);
        troubleFragment(false);
        licenceFragment(false);
        SetupFragment(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new MaterialModule())
                .with(new TypiconsModule());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        context = getApplicationContext();

        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withProfileImagesVisible(false)
                .withHeaderBackground(R.drawable.navigation_back)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("SPACE Beta")
                                .withEmail("Version: " + "PRE-RELEASE"/*BuildConfig.VERSION_NAME*/ + " • Build: " + BuildConfig.VERSION_CODE))
                .withCurrentProfileHiddenInList(true)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .withSavedInstance(savedInstanceState)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.nav_dashboard).withIdentifier(1).withIcon(new IconDrawable(this, MaterialIcons.md_dashboard))
                                .withSelectedIcon(new IconDrawable(this, MaterialIcons.md_dashboard).colorRes(R.color.colorAccent)),
                        new ExpandableDrawerItem().withName(R.string.nav_controls).withIdentifier(2).withIcon(new IconDrawable(this, MaterialIcons.md_accessibility)).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName(R.string.nav_lightC).withIdentifier(201).withLevel(2).withIcon(new IconDrawable(this, MaterialIcons.md_wb_incandescent))
                                        .withSelectedIcon(new IconDrawable(this, MaterialIcons.md_wb_incandescent).colorRes(R.color.colorAccent)),
                                new SecondaryDrawerItem().withName(R.string.nav_fanC).withIdentifier(202).withLevel(2).withIcon(new IconDrawable(this, MaterialIcons.md_toys))
                                        .withSelectedIcon(new IconDrawable(this, MaterialIcons.md_toys).colorRes(R.color.colorAccent)),
                                new SecondaryDrawerItem().withName(R.string.nav_plugC).withIdentifier(203).withLevel(2).withIcon(new IconDrawable(this, MaterialIcons.md_power))
                                        .withSelectedIcon(new IconDrawable(this, MaterialIcons.md_power).colorRes(R.color.colorAccent))
                                /*new SecondaryDrawerItem().withName(R.string.nav_windowC).withIdentifier(204).withLevel(2).withIcon(new IconDrawable(this, MaterialIcons.md_picture_in_picture))
                                        .withSelectedIcon(new IconDrawable(this, MaterialIcons.md_picture_in_picture).colorRes(R.color.colorAccent)),
                                new SecondaryDrawerItem().withName(R.string.nav_door_lockC).withIdentifier(205).withLevel(2).withIcon(new IconDrawable(this, MaterialIcons.md_vpn_key))
                                        .withSelectedIcon(new IconDrawable(this, MaterialIcons.md_vpn_key).colorRes(R.color.colorAccent))*/
                        ),
                        new PrimaryDrawerItem().withName(R.string.nav_device_setup).withIdentifier(3).withIcon(new IconDrawable(this, MaterialIcons.md_edit))
                                .withSelectedIcon(new IconDrawable(this, MaterialIcons.md_edit).colorRes(R.color.colorAccent)),

                        new SectionDrawerItem().withName(R.string.nav_section_header_others).withTextColor(Color.rgb(184, 184, 184)),
                        new PrimaryDrawerItem().withName(R.string.nav_settings).withIdentifier(4).withIcon(new IconDrawable(this, MaterialIcons.md_settings))
                                .withSelectedIcon(new IconDrawable(this, MaterialIcons.md_settings).colorRes(R.color.colorAccent)),
                        new PrimaryDrawerItem().withName(R.string.nav_trouble).withIdentifier(5).withIcon(new IconDrawable(this, MaterialIcons.md_help_outline))
                                .withSelectedIcon(new IconDrawable(this, MaterialIcons.md_help_outline).colorRes(R.color.colorAccent)).withEnabled(false),
                        new PrimaryDrawerItem().withName(R.string.nav_licence).withIdentifier(6).withIcon(new IconDrawable(this, TypiconsIcons.typcn_social_github_circular))
                                .withSelectedIcon(new IconDrawable(this, TypiconsIcons.typcn_social_github_circular).colorRes(R.color.colorAccent))
                )
                .addStickyDrawerItems(
                        //new SecondaryDrawerItem().withName("Powered by Homet Solutions").withEnabled(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem
                            drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == 1 && drawerSelected != drawerItem.getIdentifier()) {
                                hideAllFragment();
                                dashboardFragment(true);
                                drawerSelected = 1;
                            } else if (drawerItem.getIdentifier() == 201 && drawerSelected != drawerItem.getIdentifier()) {
                                hideAllFragment();
                                lightFragment(true);
                                drawerSelected = 201;
                            } else if (drawerItem.getIdentifier() == 202 && drawerSelected != drawerItem.getIdentifier()) {
                                hideAllFragment();
                                fanFragment(true);
                                drawerSelected = 202;
                            } else if (drawerItem.getIdentifier() == 203 && drawerSelected != drawerItem.getIdentifier()) {
                                hideAllFragment();

                                drawerSelected = 203;
                            } else if (drawerItem.getIdentifier() == 204 && drawerSelected != drawerItem.getIdentifier()) {
                                hideAllFragment();
                                windowFragment(true);
                                drawerSelected = 204;
                            } else if (drawerItem.getIdentifier() == 205 && drawerSelected != drawerItem.getIdentifier()) {
                                hideAllFragment();
                                doorLockFragment(true);
                                drawerSelected = 205;
                            } else if (drawerItem.getIdentifier() == 3 && drawerSelected != drawerItem.getIdentifier()) {
                                hideAllFragment();
                                SetupFragment(true);
                                drawerSelected = 3;
                            } else if (drawerItem.getIdentifier() == 4 && drawerSelected != drawerItem.getIdentifier()) {
                                hideAllFragment();
                                settingsFragment(true);
                                drawerSelected = 4;
                            } else if (drawerItem.getIdentifier() == 5 && drawerSelected != drawerItem.getIdentifier()) {
                                hideAllFragment();
                                troubleFragment(true);
                                drawerSelected = 5;
                            } else if (drawerItem.getIdentifier() == 6 && drawerSelected != drawerItem.getIdentifier()) {
                                hideAllFragment();
                                licenceFragment(true);
                                drawerSelected = 6;
                            }
                        }
                        return false;
                    }
                })
                .withSelectedItem(1)
                //.withSelectedItemByPosition(1)
                .build();
        initialize();
    }

    public void initialize() {
        bluetoothSerial = new BluetoothSerial(this, this);
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        cLayout = (FrameLayout) findViewById(R.id.mainFrame);
        lc = new LightControlFragment(MainActivity.this);
        lnc = new LightControlNextFragment(MainActivity.this);
        fc = new FanControlFragment(MainActivity.this);
        wf = new WindowFragment();
        connectionFragment(true);
        hideAllFragment();
        dashboardFragment(true);
        drawerSelected = 1;
        DatabaseHelper dbh = new DatabaseHelper(context);
        try {
            dbh.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isEnabled()) {
                bluetoothSerial.setup();
                BLUTOOTH_SETUP = true;
            } else {
                if(!firstEnebleBTSnackBar) {
                    btSnackbar = Snackbar.make(cLayout, "Bluetooth is disabled", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ENABLE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (mBluetoothAdapter != null) {
                                        mBluetoothAdapter.enable();
                                    }
                                }
                            });
                    btSnackbar.show();
                    firstEnebleBTSnackBar = true;
                }
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
        if(menuCreated){
            if (mBluetoothAdapter != null) {
                if (mBluetoothAdapter.isEnabled()) {
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_enebled));
                } else {
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_disabled));
                }
            }
        }
        if (connectedBeforeMinimize) {
            if (bluetoothSerial.getState() == 0) {
                bluetoothSerial.start();
                bluetoothSerial.connect(deviceID);
                connected = true;
            }
        }
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
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else if (fragmentLightNextON) {
            hideAllFragment();
            lightFragment(true);
        } else if (drawerSelected == 1) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            showToast("Press back again to exit", 0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            drawer.setSelection(1);
            drawerSelected = 1;
            if(drawer.isDrawerOpen()) {
                drawer.closeDrawer();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isEnabled()) {
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_enebled));
            } else {
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_disabled));
            }
        }
        menu.getItem(0).setIcon((new IconDrawable(this, MaterialIcons.md_dashboard).sizeDp(24).color(Color.WHITE)));
        menuCreated = true;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.change_log) {
            return true;
        }

        if (id == R.id.action_connection) {
            if (drawerSelected != 0) {
                hideAllFragment();
                connectionFragment(true);
                drawer.deselect();
                drawerSelected = 0;
            }
            return true;
        }

        if (id == R.id.action_dashboard) {
            if (drawerSelected != 2) {
                hideAllFragment();
                dashboardFragment(true);
                drawerSelected = 2;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                        menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_disabled));
                        ConnectionFragment.btState.setText(R.string.btState_Text_off);
                        ConnectionFragment.btState.setTextColor(Color.RED);
                        showToast("Bluetooth is disabled", 0);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        ConnectionFragment.btSwitch.setChecked(false);
                        ConnectionFragment.btState.setText(R.string.btState_Text_Toff);
                        ConnectionFragment.btState.setTextColor(Color.GRAY);
                        ConnectionFragment.btPairState.setEnabled(false);
                        ConnectionFragment.connect.setEnabled(false);
                        ConnectionFragment.disconnect.setEnabled(false);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_enebled));
                        ConnectionFragment.btSwitch.setChecked(true);
                        ConnectionFragment.btState.setText(R.string.btState_Text_on);
                        ConnectionFragment.btState.setTextColor(Color.GREEN);
                        ConnectionFragment.btPairState.setEnabled(true);
                        ConnectionFragment.connect.setEnabled(true);
                        ConnectionFragment.disconnect.setEnabled(true);
                        if (!BLUTOOTH_SETUP) {
                            bluetoothSerial.setup();
                            BLUTOOTH_SETUP = true;
                        }
                        if (btSnackbar != null) {
                            if (btSnackbar.isShown())
                                btSnackbar.dismiss();
                        }
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        ConnectionFragment.btState.setText(R.string.btState_Text_Ton);
                        ConnectionFragment.btState.setTextColor(Color.GRAY);
                        showToast("Bluetooth is enabled", 0);
                        break;
                }
            }
        }
    };

    @Override
    public void onBluetoothNotSupported() {
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

    @Override
    public void onBluetoothDisabled() {
    }

    @Override
    public void onBluetoothDeviceDisconnected() {
        menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_enebled));
        updatePairButton();
        updateBluetoothState();
        connected = false;
    }

    @Override
    public void onConnectingBluetoothDevice() {
        updateBluetoothState();
    }

    @Override
    public void onBluetoothDeviceConnected(String name, String address) {
        Log.i("BT_ON_CONNECT","1");
        if (!connectedBeforeMinimize) {
            Log.i("BT_ON_CONNECT","2");
            connectedBeforeMinimize = true;
            Log.i("BT_ON_CONNECT","3");
            deviceID = bluetoothSerial.getConnectedDeviceAddress();
            Log.i("BT_ON_CONNECT","4");
        }
        menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bluetooth_connected));
        updatePairButton();
        updateBluetoothState();
        connected = true;
    }

    @Override
    public void onBluetoothSerialRead(String message) {
        // Print the incoming message on the terminal screen
        //tvTerminal.append(getString(R.string.terminal_message_template,
        //        bluetoothSerial.getConnectedDeviceName(),
        //        message));
        //svTerminal.post(scrollTerminalToBottom);
    }

    @Override
    public void onBluetoothSerialWrite(String message) {
        // Print the outgoing message on the terminal screen
        // tvTerminal.append(getString(R.string.terminal_message_template,
        //         bluetoothSerial.getLocalAdapterName(),
        //         message));
        // svTerminal.post(scrollTerminalToBottom);
    }

    @Override
    public void onBluetoothDeviceSelected(BluetoothDevice device) {
        // Connect to the selected remote Bluetooth device
        bluetoothSerial.connect(device);
    }

}
