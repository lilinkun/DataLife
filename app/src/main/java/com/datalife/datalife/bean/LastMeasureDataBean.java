package com.datalife.datalife.bean;

/**
 * Created by LG on 2018/3/17.
 */

public class LastMeasureDataBean<T> {
    private T Project1;
    private T Project2;
    private T Project3;
    private T Project5;

    public T getProject1() {
        return Project1;
    }

    public void setProject1(T project1) {
        Project1 = project1;
    }

    public T getProject2() {
        return Project2;
    }

    public void setProject2(T project2) {
        Project2 = project2;
    }

    public T getProject3() {
        return Project3;
    }

    public void setProject3(T project3) {
        Project3 = project3;
    }

    public T getProject5() {
        return Project5;
    }

    public void setProject5(T project5) {
        Project5 = project5;
    }

    @Override
    public String toString() {
        return "LastMeasureDataBean{" +
                "Project1=" + Project1 +
                ", Project2=" + Project2 +
                ", Project3=" + Project3 +
                ", Project5=" + Project5 +
                '}';
    }
}
