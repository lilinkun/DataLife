package com.datalife.datalife.bean;

/**
 * Created by LG on 2018/2/6.
 */

public class Data {
    private long id;
    private String u_id;
    private String gm_id = "";
    private String bmi = "";
    private String weight = "";
    private String fat = "";
    private String water = "";
    private String muscle = "";
    private String bmr = "";
    private String bone = "";
    private String visceralfat = "";
    private String height = "";
    private String bodyage = "";
    private String is_delete = "0";//0表示可以显示，1表示已经删除
    private String type = "0";//0表示没有上传，1表示已经上传
    private String m_time = "";
    private String device_info = "";
    private long time;
    private boolean isShowDelete = false;

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGm_id() {
        return gm_id;
    }

    public void setGm_id(String gm_id) {
        this.gm_id = gm_id;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }


    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public String getBone() {
        return bone;
    }

    public void setBone(String bone) {
        this.bone = bone;
    }

    public String getVisceralfat() {
        return visceralfat;
    }

    public void setVisceralfat(String visceralfat) {
        this.visceralfat = visceralfat;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isShowDelete() {
        return isShowDelete;
    }

    public void setShowDelete(boolean isShowDelete) {
        this.isShowDelete = isShowDelete;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getM_time() {
        return m_time;
    }

    public void setM_time(String m_time) {
        this.m_time = m_time;
    }


    public String getBodyage() {
        return bodyage;
    }

    public void setBodyage(String bodyage) {
        this.bodyage = bodyage;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getBmr() {
        return bmr;
    }

    public void setBmr(String bmr) {
        this.bmr = bmr;
    }
}
