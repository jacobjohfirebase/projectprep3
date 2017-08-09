package com.examplemobileappcompany.projectprep3bleaidl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DeviceScanActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    private BluetoothGatt mBluetoothGatt;

    private Context context = this;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }
    //private LeDeviceListAdapter mLeDeviceListAdapter;
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {


                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // mLeDeviceListAdapter.addDevice(device);
                        //    mLeDeviceListAdapter.notifyDataSetChanged();
                            mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
                        }
                    });
                }
            };

            private final String TAG = "mylog";

    // Various callback methods defined by the BLE API.
    private final BluetoothGattCallback mGattCallback =
            new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                    int newState) {
                    String intentAction;
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        //  intentAction = ACTION_GATT_CONNECTED;
                        //  mConnectionState = STATE_CONNECTED;
                        //  broadcastUpdate(intentAction);
                        Log.d(TAG, "Connected to GATT server.");
                        Log.d(TAG, "Attempting to start service discovery:" +
                                mBluetoothGatt.discoverServices());

                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        //   intentAction = ACTION_GATT_DISCONNECTED;
                        //   mConnectionState = STATE_DISCONNECTED;
                        Log.d(TAG, "Disconnected from GATT server.");
                        //   broadcastUpdate(intentAction);
                    }
                }
            };
}
