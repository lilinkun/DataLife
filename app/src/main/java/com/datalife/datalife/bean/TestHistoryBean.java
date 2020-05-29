package com.datalife.datalife.bean;

/**
 * Created by LG on 2018/8/20.
 */
public class TestHistoryBean {

    private String message;
    private String time;
    private String date;
    private String name;
    private String type;

    public TestHistoryBean(String message, String time, String date, String name, String type) {
        this.message = message;
        this.time = time;
        this.date = date;
        this.name = name;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
