package com.datalife.datalife.bean;

/**
 * Created by LG on 2018/5/4.
 */

public class DateBean {
    private String year;
    private String month;
    private String day;
    private String cal;

    public DateBean(String cal) {
        this.year = cal.substring(0,cal.indexOf("-"));
        this.month = cal.substring(cal.indexOf("-")+1,cal.lastIndexOf("-"));
        this.day = cal.substring(cal.lastIndexOf("-")+1,cal.length());
        this.cal = cal;
    }

    public String onKey(){
        return year+"-"+month+"-"+day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    @Override
    public String toString() {
        return "DateBean{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", cal='" + cal + '\'' +
                '}';
    }
}
