package com.datalife.datalife.bean;

/**
 * Created by LG on 2018/8/13.
 */

public class ToothTestBean {

    private String testDate;
    private String testTime;
    private int testCount;

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public int getTestCount() {
        return testCount;
    }

    public void setTestCount(int testCount) {
        this.testCount = testCount;
    }

    @Override
    public String toString() {
        return "ToothTestBean{" +
                "testDate='" + testDate + '\'' +
                ", testTime='" + testTime + '\'' +
                ", testCount='" + testCount + '\'' +
                '}';
    }

}
