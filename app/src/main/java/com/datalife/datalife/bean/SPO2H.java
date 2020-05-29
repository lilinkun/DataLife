package com.datalife.datalife.bean;

import android.databinding.BaseObservable;

import lib.linktop.obj.LoadSPO2HBean;

/**
 * Created by LG on 2018/1/18.
 */

public class SPO2H extends BaseObservable implements LoadSPO2HBean{

    private int value = 0;
    private int hr = 0;
    private long ts = 0L;

    public int getHr() {
        return hr;
    }

    @Override
    public void setValue(int i) {
        this.value = i;
        notifyChange();
    }

    @Override
    public int getValue() {
        return value;
    }

    public void setHr(int hr) {
        this.hr = hr;
        notifyChange();
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public void reset() {
        value = 0;
        hr = 0;
        ts = 0L;
        notifyChange();
    }

    @Override
    public String toString() {
        return "SPO2H{" +
                "spo2h=" + value +
                ", hr=" + hr +
                ", ts=" + ts +
                '}';
    }

    public boolean isEmptyData() {
        return value == 0 || hr == 0 || ts == 0L;
    }
}