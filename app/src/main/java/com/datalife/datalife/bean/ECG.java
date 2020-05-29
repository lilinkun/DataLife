package com.datalife.datalife.bean;

import android.databinding.BaseObservable;
import android.text.TextUtils;

import java.io.Serializable;

import lib.linktop.obj.LoadECGBean;

/**
 * Created by LG on 2018/1/18.
 */

public class ECG extends BaseObservable implements Serializable ,LoadECGBean{

    private int rrMax;
    private int rrMin;
    private int hr;
    private int hrv;
    private int mood;
    private int br;
    private long duration;
    private long ts;
    private String wave;

    public int getRrMax() {
        return rrMax;
    }

    public void setRrMax(int rrMax) {
        this.rrMax = rrMax;
        notifyChange();
    }

    public int getRrMin() {
        return rrMin;
    }

    public void setRrMin(int rrMin) {
        this.rrMin = rrMin;
        notifyChange();
    }

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
        notifyChange();
    }

    public int getHrv() {
        return hrv;
    }

    public void setHrv(int hrv) {
        this.hrv = hrv;
        notifyChange();
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
        notifyChange();
    }

    public int getBr() {
        return br;
    }

    public void setBr(int br) {
        this.br = br;
        notifyChange();
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setWave(String wave) {
        this.wave = wave;
    }

    public String getWave() {
        return wave;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public void reset() {
        rrMax = 0;
        rrMin = 0;
        hr = 0;
        hrv = 0;
        mood = 0;
        br = 0;
        duration = 0L;
        ts = 0L;
        wave = "";
        notifyChange();
    }

    public boolean isEmptyData() {
        return rrMax == 0 ||
                rrMin == 0 ||
                hr == 0 ||
                hrv == 0 ||
                mood == 0 ||
                br == 0 ||
                duration == 0L ||
                ts == 0L ||
                TextUtils.isEmpty(wave);
    }

    @Override
    public String toString() {
        return "ECG{" +
                "rrMax=" + rrMax +
                ", rrMin=" + rrMin +
                ", hr=" + hr +
                ", hrv=" + hrv +
                ", mood=" + mood +
                ", br=" + br +
                ", duration=" + duration +
                ", ts=" + ts +
                ", wave='" + wave + '\'' +
                '}';
    }
}