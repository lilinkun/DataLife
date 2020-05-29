package com.datalife.datalife.dao;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by LG on 2018/2/10.
 */
@Entity
public class Spo2hDao {
    @Id
    private Long id;
    private String name;
    private String createTime;
    private String spo2hValue;

    @Generated(hash = 1855166576)
    public Spo2hDao(Long id, String name, String createTime, String spo2hValue) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.spo2hValue = spo2hValue;
    }

    public Spo2hDao() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSpo2hValue() {
        return spo2hValue;
    }

    public void setSpo2hValue(String spo2hValue) {
        this.spo2hValue = spo2hValue;
    }

    @Override
    public String toString() {
        return "Spo2hDao{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createTime='" + createTime + '\'' +
                ", spo2hValue='" + spo2hValue + '\'' +
                '}';
    }
}
