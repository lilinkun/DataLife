package com.datalife.datalife.util;

import com.datalife.datalife.R;

/**
 * Created by LG on 2018/9/17.
 */

public enum AllDevEnum {
    HEALTH(R.mipmap.ic_history_health_monitor,R.string.multifunction_health_monitor),
    FAT(R.mipmap.ic_history_fat,R.string.datalife_body_fat);

//    TOOTH(R.mipmap.ic_history_tooth,R.string.datalife_blue_tooth)

    private int srcImage;
    private int typeName;

    private AllDevEnum(int srcImage,int typeName){
        this.srcImage = srcImage;
        this.typeName = typeName;
    }

    public int getSrcImage() {
        return srcImage;
    }

    public void setSrcImage(int srcImage) {
        this.srcImage = srcImage;
    }

    public int getTypeName() {
        return typeName;
    }

    public void setTypeName(int typeName) {
        this.typeName = typeName;
    }
}
