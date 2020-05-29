package com.datalife.datalife.util;

/**
 * Created by LG on 2018/10/24.
 */

public enum BrushLevelEnum {

    level1(0,10,"护齿新秀"),
    level2(11,50,"护齿达人"),
    level3(51,100,"护齿专家"),
    level4(101,101,"护齿大师");

    private int minValue;
    private int maxValue;
    private String levelName;

    private BrushLevelEnum(int min,int max,String levelName){
        this.minValue = min;
        this.maxValue = max;
        this.levelName = levelName;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }


    public static BrushLevelEnum getBalueByLevel(int value){
        for (BrushLevelEnum brushLevelEnum : values()){

            if (brushLevelEnum.maxValue >= value  && brushLevelEnum.minValue <= value){
                return brushLevelEnum;
            }

        }

        return null;
    }

}
