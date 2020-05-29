package com.datalife.datalife.bean;

import com.datalife.datalife.R;

/**
 * Created by LG on 2018/6/12.
 */

public enum TestEnum {
    BODYFAT(R.string.home_body_fat,R.mipmap.ic_health_bodyfat),
    HEALTHTEST(R.string.health_testing,R.mipmap.ic_health_testing),
    TOOTH(R.string.health_tooth,R.mipmap.ic_health_testing);

    private int testName;
    private int testRes;

    private TestEnum(int testName,int testRes){
        this.testName = testName;
        this.testRes = testRes;
    }

    public int getTestName() {
        return testName;
    }

    public void setTestName(int testName) {
        this.testName = testName;
    }

    public int getTestRes() {
        return testRes;
    }

    public void setTestRes(int testRes) {
        this.testRes = testRes;
    }

}
