package com.datalife.datalife.util;

/**
 * Created by LG on 2018/9/12.
 */

public enum AllToothMode {
    MODE_CLEAN("清洁模式",(byte)0x01),
    MODE_WHITE("美白模式",(byte)0x02),
    MODE_GUMCARE("抛光模式",(byte)0x07),
    MODE_SENSITIVE("牙龈护理",(byte)0x03);

    private String modeName;
    private byte modeId;

    private AllToothMode(String modename,byte modeId){
        this.modeName = modename;
        this.modeId = modeId;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public byte getModeId() {
        return modeId;
    }

    public void setModeId(byte modeId) {
        this.modeId = modeId;
    }


    public static AllToothMode getPageByValue(String type) {
        for (AllToothMode p : values()) {
            if (p.getModeName().equals(type))
                return p;
        }
        return null;
    }
}
