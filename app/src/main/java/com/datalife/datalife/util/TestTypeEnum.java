package com.datalife.datalife.util;

/**
 * Created by LG on 2018/3/14.
 */
public enum TestTypeEnum {
    TYPE_DAY("day_type","1"),
    TYPE_YEAR("year_type","2");

    private String type;
    private String typeid;

    private TestTypeEnum(String type,String typeid){
        this.type = type;
        this.typeid =typeid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }
}
