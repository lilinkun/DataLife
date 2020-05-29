package com.datalife.datalife.interf;

/**
 * Created by LG on 2018/3/16.
 */

public interface OnBtRecordLisener {

    public void onBtIntent(int Date,int pageIndex,String startDate,String finishDate);

    public void onSpo2hIntent(int Date,int pageIndex,String startDate,String finishDate);

    public void onHrIntent(int Date,int pageIndex,String startDate,String finishDate);

    public void onEcgIntent(int date , int pageIndex);

    public void onBpIntent(int Date,int pageIndex,String startDate,String finishDate);

}
