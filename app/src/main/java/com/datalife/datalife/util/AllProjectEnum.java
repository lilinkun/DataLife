package com.datalife.datalife.util;

/**
 * Created by LG on 2018/8/21.
 */

public enum AllProjectEnum {
    TYPE_BP("血压",211),
    TYPE_SPO2H("血氧",217),
    TYPE_BFR("体脂率",214),
    TYPE_WEIGHT("体重",213);

    private String type;
    private int typeid;

    private AllProjectEnum(String type,int typeid){
        this.type = type;
        this.typeid =typeid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public static AllProjectEnum getPageByValue(String type) {
        for (AllProjectEnum p : values()) {
            if (p.getType().equals(type))
                return p;
        }
        return null;
    }
}
