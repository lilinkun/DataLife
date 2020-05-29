package com.datalife.datalife.util;

/**
 * Created by LG on 2018/9/12.
 */

public enum AllToothTime {
    TOOTH_1("2'30''",2.5,(byte) 10,1,"150"),
    TOOTH_2("3'",3.0,(byte)13,2,"180"),
    TOOTH_3("3'30''",3.5,(byte)16,3,"210");

    private String timeStr;
    private double toothTime;
    private byte toothId;
    private int id;
    private String second;

    private AllToothTime(String str,double toothTime,byte toothId,int id,String second){
        this.timeStr = str;
        this.toothTime = toothTime;
        this.toothId = toothId;
        this.id = id;
        this.second = second;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public double getToothTime() {
        return toothTime;
    }

    public void setToothTime(double toothTime) {
        this.toothTime = toothTime;
    }

    public byte getToothId() {
        return toothId;
    }

    public void setToothId(byte toothId) {
        this.toothId = toothId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public static AllToothTime getPageByValue(double type) {
        for (AllToothTime p : values()) {
            if (p.getToothTime() == type)
                return p;
        }
        return null;
    }

    public static AllToothTime getPageByValue(String type) {
        for (AllToothTime p : values()) {
            if (p.getTimeStr().equals(type))
                return p;
        }
        return null;
    }

    public static AllToothTime getSecondByValue(String second) {
        for (AllToothTime p : values()) {
            if (p.getSecond().equals(second))
                return p;
        }
        return null;
    }
}
