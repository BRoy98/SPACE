package com.hometsolutions.space.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.hometsolutions.space.Activitys.MainActivity;
import com.hometsolutions.space.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionFragment extends Fragment {

    public static TextView btState, btPairState;
    public static Button connect, disconnect;
    public static Switch btSwitch;

    public ConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_connection, container, false);
        connect = (Button) layout.findViewById(R.id.pairCnnct);
        disconnect = (Button) layout.findViewById(R.id.pairDcnnct);
        btState = (TextView) layout.findViewById(R.id.btState);
        btPairState = (TextView) layout.findViewById(R.id.btPairState);
        btSwitch = (Switch) layout.findViewById(R.id.btSwitch);
        initialize();
        btSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {
                if (on) {
                    if (!((MainActivity) getActivity()).mBluetoothAdapter.isEnabled()) {
                        ((MainActivity) getActivity()).mBluetoothAdapter.enable();
                        // onBluetooth();
                    }
                } else {
                    if (((MainActivity) getActivity()).mBluetoothAdapter.isEnabled()) {
                        ((MainActivity) getActivity()).mBluetoothAdapter.disable();
                    }
                }
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showDeviceListDialog(true);
            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).bluetoothSerial.stop();
                ((MainActivity) getActivity()).connected =false;
            }
        });
        return layout;
    }

    private void initialize() {
        if (((MainActivity) getActivity()).mBluetoothAdapter != null) {
            if (((MainActivity) getActivity()).mBluetoothAdapter.isEnabled()) {
                btState.setText(R.string.btState_Text_on);
                btState.setTextColor(Color.GREEN);
                btSwitch.setChecked(true);
                btPairState.setEnabled(true);
                connect.setEnabled(true);
                disconnect.setEnabled(true);
            } else {
                btState.setText(R.string.btState_Text_off);
                btState.setTextColor(Color.RED);
                btSwitch.setChecked(false);
                btPairState.setEnabled(false);
                connect.setEnabled(false);
                disconnect.setEnabled(false);
            }
        } else {
        }
        disconnect.setVisibility(((MainActivity) getActivity()).cLayout.GONE);
    }
}
