package com.datalife.datalife.bean;

import java.io.Serializable;

/**
 * Created by LG on 2018/8/21.
 */

public class DataTestBean implements Serializable{

    private String CheckServerId;
    private int Member_Id;
    private String user_id;
    private int ProjectId;
    private int server_Id;
    private String testRecordID;
    private String subject;
    private String introduction;
    private String remind;
    private String message;
    private String detail;
    private boolean wxPropelling;
    private boolean appPropelling;
    private String wxPropellingDate;
    private String appPropellingDate;
    private String CreateDate;

    public String getCheckServerId() {
        return CheckServerId;
    }

    public void setCheckServerId(String checkServerId) {
        CheckServerId = checkServerId;
    }

    public int getMember_Id() {
        return Member_Id;
    }

    public void setMember_Id(int member_Id) {
        Member_Id = member_Id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getProjectId() {
        return ProjectId;
    }

    public void setProjectId(int projectId) {
        ProjectId = projectId;
    }

    public int getServer_Id() {
        return server_Id;
    }

    public void setServer_Id(int server_Id) {
        this.server_Id = server_Id;
    }

    public String getTestRecordID() {
        return testRecordID;
    }

    public void setTestRecordID(String testRecordID) {
        this.testRecordID = testRecordID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isWxPropelling() {
        return wxPropelling;
    }

    public void setWxPropelling(boolean wxPropelling) {
        this.wxPropelling = wxPropelling;
    }

    public boolean isAppPropelling() {
        return appPropelling;
    }

    public void setAppPropelling(boolean appPropelling) {
        this.appPropelling = appPropelling;
    }

    public String getWxPropellingDate() {
        return wxPropellingDate;
    }

    public void setWxPropellingDate(String wxPropellingDate) {
        this.wxPropellingDate = wxPropellingDate;
    }

    public String getAppPropellingDate() {
        return appPropellingDate;
    }

    public void setAppPropellingDate(String appPropellingDate) {
        this.appPropellingDate = appPropellingDate;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }
}
