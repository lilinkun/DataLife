package com.datalife.datalife.bean;

import android.databinding.BaseObservable;
import android.util.Log;

import lib.linktop.obj.LoadBtBean;

/**
 * Created by LG on 2018/1/18.
 */

public class Bt extends BaseObservable  implements LoadBtBean {

    private long ts = 0;
    private double temp = 0.0d;

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
        notifyChange();
    }

    public void reset() {
        Log.e("BT", "reset");
        temp = 0.0d;
        ts = 0L;
        notifyChange();
    }

    public boolean isEmptyData() {
        return temp == 0.0d || ts == 0L;
    }

    @Override
    public String toString() {
        return "Bt{" +
                "ts=" + ts +
                ", temp=" + temp +
                '}';
    }
}
