package com.datalife.datalife.bean;

/**
 * Created by LG on 2018/7/27.
 */

public class LastFatMeasureDataBean<T> {
    private T Project6;

    public T getProject6() {
        return Project6;
    }

    public void setProject6(T project6) {
        Project6 = project6;
    }

    @Override
    public String toString() {
        return "LastFatMeasureDataBean{" +
                "Project6=" + Project6 +
                '}';
    }
}
