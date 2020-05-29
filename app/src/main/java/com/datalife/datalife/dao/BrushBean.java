package com.datalife.datalife.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by LG on 2018/9/21.
 */
@Entity
public class BrushBean {

    String address;
    String startTime;
    int workTime;
    int overPowerCount;
    int overPowerTime;
    int leftBrushTime;
    int rightBrushTime;
    int settingTime;

    @Keep
    public BrushBean(String address,String startTime, int workTime, int overPowerCount, int overPowerTime, int leftBrushTime, int rightBrushTime,int settingTime) {
        this.address = address;
        this.startTime = startTime;
        this.workTime = workTime;
        this.overPowerCount = overPowerCount;
        this.overPowerTime = overPowerTime;
        this.leftBrushTime = leftBrushTime;
        this.rightBrushTime = rightBrushTime;
        this.settingTime =settingTime;
    }

    @Keep
    public BrushBean() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getOverPowerCount() {
        return overPowerCount;
    }

    public void setOverPowerCount(int overPowerCount) {
        this.overPowerCount = overPowerCount;
    }

    public int getOverPowerTime() {
        return overPowerTime;
    }

    public void setOverPowerTime(int overPowerTime) {
        this.overPowerTime = overPowerTime;
    }

    public int getLeftBrushTime() {
        return leftBrushTime;
    }

    public void setLeftBrushTime(int leftBrushTime) {
        this.leftBrushTime = leftBrushTime;
    }

    public int getRightBrushTime() {
        return rightBrushTime;
    }

    public void setRightBrushTime(int rightBrushTime) {
        this.rightBrushTime = rightBrushTime;
    }

    public int getSettingTime() {
        return settingTime;
    }

    public void setSettingTime(int settingTime) {
        this.settingTime = settingTime;
    }
}
