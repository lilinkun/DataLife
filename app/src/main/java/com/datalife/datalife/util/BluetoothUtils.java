package com.datalife.datalife.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.widget.Toast;

import java.util.List;

/**
 * Created by LG on 2018/1/28.
 */

public class BluetoothUtils {

    private static BluetoothUtils instance;

    /**
     * 单一实例
     */
    public static BluetoothUtils getBlutoothUtils() {
        if (instance == null) {
            instance = new BluetoothUtils();
        }

        return instance;
    }

    //检查已连接的蓝牙设备
    public void getConnectBt(final Context context,BluetoothAdapter bluetoothAdapter) {
        int a2dp = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
        int headset = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
        int health = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);
        int flag = -1;
        if (a2dp == BluetoothProfile.STATE_CONNECTED) {
            flag = a2dp;
        } else if (headset == BluetoothProfile.STATE_CONNECTED) {
            flag = headset;
        } else if (health == BluetoothProfile.STATE_CONNECTED) {
            flag = health;
        }
        if (flag != -1) {
            bluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {
                @Override
                public void onServiceDisconnected(int profile) {
                    Toast.makeText(context,profile+"", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onServiceConnected(int profile, BluetoothProfile proxy) {
                    List<BluetoothDevice> mDevices = proxy.getConnectedDevices();
                    if (mDevices != null && mDevices.size() > 0) {
                        for (BluetoothDevice device : mDevices) {
                            Toast.makeText(context, device.getName() + "," + device.getAddress(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "mDevices is null", Toast.LENGTH_SHORT).show();
                    }
                }
            }, flag);
        }
    }
}
