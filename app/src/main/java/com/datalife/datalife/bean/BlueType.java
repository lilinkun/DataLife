package com.datalife.datalife.bean;

import android.bluetooth.BluetoothDevice;

/**
 * Created by LG on 2018/9/13.
 */

public class BlueType {

    private String blueAddress;
    private int blueType;
    private BluetoothDevice bluetoothDevice;

    public BlueType(String blueAddress, int blueType, BluetoothDevice bluetoothDevice) {
        this.blueAddress = blueAddress;
        this.blueType = blueType;
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getBlueAddress() {
        return blueAddress;
    }

    public void setBlueAddress(String blueAddress) {
        this.blueAddress = blueAddress;
    }

    public int getBlueType() {
        return blueType;
    }

    public void setBlueType(int blueType) {
        this.blueType = blueType;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
}
