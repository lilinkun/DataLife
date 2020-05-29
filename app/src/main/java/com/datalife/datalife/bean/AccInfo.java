package com.datalife.datalife.bean;

import android.databinding.BaseObservable;

/**
 * Created by LG on 2018/1/18.
 */

public class AccInfo extends BaseObservable {

    private String mobileNo = "";

    private String password = "";

    private String countryCode = "";

    private String verificationCode = "";

    public AccInfo() {
    }

    public void clear() {
        mobileNo = "";
        password = "";
        countryCode = "";
        verificationCode = "";
        notifyChange();
    }


    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
        notifyChange();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyChange();
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    public String toString() {
        return "AccInfo{" +
                "mobileNo='" + mobileNo + '\'' +
                ", password='" + password + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }
}