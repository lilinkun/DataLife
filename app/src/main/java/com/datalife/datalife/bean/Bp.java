package com.datalife.datalife.bean;

import android.databinding.BaseObservable;
import android.util.Log;

import java.io.Serializable;

import lib.linktop.obj.LoadBpBean;

/**
 * Created by LG on 2018/1/18.
 */

public class Bp extends BaseObservable implements LoadBpBean,Serializable {

    /**
     *舒张压
     */
    private int sbp;
    /**
     *收缩压
     */
    private int dbp;
    /**
     * 心率
     */
    private int hr;
    private long ts;

    public int getSbp() {
        return sbp;
    }

    public void setSbp(int sbp) {
        this.sbp = sbp;
        notifyChange();
    }

    public int getDbp() {
        return dbp;
    }

    public void setDbp(int dbp) {
        this.dbp = dbp;
        notifyChange();
    }

    public int getHr() {
        return hr;
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
        Log.e("BP", "reset");
        sbp = 0;
        dbp = 0;
        hr = 0;
        ts = 0L;
        notifyChange();
    }

    public boolean isEmptyData() {
        return sbp == 0 || dbp == 0 || hr == 0 || ts == 0L;
    }

    @Override
    public String toString() {
        return "Bp{" +
                "sbp=" + sbp +
                ", dbp=" + dbp +
                ", hr=" + hr +
                '}';
    }
}
