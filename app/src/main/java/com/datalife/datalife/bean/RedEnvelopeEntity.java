package com.datalife.datalife.bean;

import java.io.Serializable;

/**
 * Created by LG on 2018/10/23.
 */

public class RedEnvelopeEntity implements Serializable{

    private String ReceiveId;
    private String User_Id;
    private String User_Name;
    private String MM_Id;
    private String Amount;
    private String ReceiveState;
    private String ValidityStartTime;
    private String ValidityEndTime;
    private String ReceiveDateTime;
    private String Batch;
    private String Order_Sn;
    private String openid;
    private String MakeAmountDateTime;
    private String MakeAmountInfo;
    private String CreateTime;
    private String ReceiveType;
    private String DistributionType;

    public String getReceiveId() {
        return ReceiveId;
    }

    public void setReceiveId(String receiveId) {
        ReceiveId = receiveId;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getMM_Id() {
        return MM_Id;
    }

    public void setMM_Id(String MM_Id) {
        this.MM_Id = MM_Id;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getReceiveState() {
        return ReceiveState;
    }

    public void setReceiveState(String receiveState) {
        ReceiveState = receiveState;
    }

    public String getValidityStartTime() {
        return ValidityStartTime;
    }

    public void setValidityStartTime(String validityStartTime) {
        ValidityStartTime = validityStartTime;
    }

    public String getValidityEndTime() {
        return ValidityEndTime;
    }

    public void setValidityEndTime(String validityEndTime) {
        ValidityEndTime = validityEndTime;
    }

    public String getReceiveDateTime() {
        return ReceiveDateTime;
    }

    public void setReceiveDateTime(String receiveDateTime) {
        ReceiveDateTime = receiveDateTime;
    }

    public String getBatch() {
        return Batch;
    }

    public void setBatch(String batch) {
        Batch = batch;
    }

    public String getOrder_Sn() {
        return Order_Sn;
    }

    public void setOrder_Sn(String order_Sn) {
        Order_Sn = order_Sn;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getMakeAmountDateTime() {
        return MakeAmountDateTime;
    }

    public void setMakeAmountDateTime(String makeAmountDateTime) {
        MakeAmountDateTime = makeAmountDateTime;
    }

    public String getMakeAmountInfo() {
        return MakeAmountInfo;
    }

    public void setMakeAmountInfo(String makeAmountInfo) {
        MakeAmountInfo = makeAmountInfo;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getReceiveType() {
        return ReceiveType;
    }

    public void setReceiveType(String receiveType) {
        ReceiveType = receiveType;
    }

    public String getDistributionType() {
        return DistributionType;
    }

    public void setDistributionType(String distributionType) {
        DistributionType = distributionType;
    }
}
