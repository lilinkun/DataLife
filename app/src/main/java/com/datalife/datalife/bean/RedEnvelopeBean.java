package com.datalife.datalife.bean;

/**
 * Created by LG on 2018/10/18.
 */

public class RedEnvelopeBean {

    String redEnvelopeType;
    String redEnvelopeTime;
    int redEnvelopeSum;

    public RedEnvelopeBean(){

    }

    public RedEnvelopeBean(String redEnvelopeType, String redEnvelopeTime, int redEnvelopeSum) {
        this.redEnvelopeType = redEnvelopeType;
        this.redEnvelopeTime = redEnvelopeTime;
        this.redEnvelopeSum = redEnvelopeSum;
    }

    public String getRedEnvelopeType() {
        return redEnvelopeType;
    }

    public void setRedEnvelopeType(String redEnvelopeType) {
        this.redEnvelopeType = redEnvelopeType;
    }

    public String getRedEnvelopeTime() {
        return redEnvelopeTime;
    }

    public void setRedEnvelopeTime(String redEnvelopeTime) {
        this.redEnvelopeTime = redEnvelopeTime;
    }

    public int getRedEnvelopeSum() {
        return redEnvelopeSum;
    }

    public void setRedEnvelopeSum(int redEnvelopeSum) {
        this.redEnvelopeSum = redEnvelopeSum;
    }
}
