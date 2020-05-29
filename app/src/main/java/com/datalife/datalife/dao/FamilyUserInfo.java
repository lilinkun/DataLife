package com.datalife.datalife.dao;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by LG on 2018/2/4.
 */
@Entity
public class FamilyUserInfo implements Serializable{

    private int Member_Id;
    private int user_id;
    private String user_name;
    private String Member_Sex;
    private String Member_Name;//姓名
    private String Member_Portrait;//头像
    private double Member_Stature;//身高
    private double Member_Weight;//体重
    private String Member_DateOfBirth;//出生日期
    private int Member_Status;//状态，0为正常，1为异常
    private boolean Member_IsDefault;//是否默认
    private String CreateDate;
    public static final long serialVersionUID = 123224;

    @Generated(hash = 814909213)
    public FamilyUserInfo(int Member_Id, int user_id, String user_name,
            String Member_Sex, String Member_Name, String Member_Portrait,
            double Member_Stature, double Member_Weight, String Member_DateOfBirth,
            int Member_Status, boolean Member_IsDefault, String CreateDate) {
        this.Member_Id = Member_Id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.Member_Sex = Member_Sex;
        this.Member_Name = Member_Name;
        this.Member_Portrait = Member_Portrait;
        this.Member_Stature = Member_Stature;
        this.Member_Weight = Member_Weight;
        this.Member_DateOfBirth = Member_DateOfBirth;
        this.Member_Status = Member_Status;
        this.Member_IsDefault = Member_IsDefault;
        this.CreateDate = CreateDate;
    }

    @Generated(hash = 232979000)
    public FamilyUserInfo() {
    }

    public String getMember_Sex() {
        return Member_Sex;
    }

    public void setMember_Sex(String member_Sex) {
        Member_Sex = member_Sex;
    }

    public int getMember_Id() {
        return Member_Id;
    }

    public void setMember_Id(int member_Id) {
        Member_Id = member_Id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getMember_Stature() {
        return Member_Stature;
    }

    public void setMember_Stature(double member_Stature) {
        Member_Stature = member_Stature;
    }

    public double getMember_Weight() {
        return Member_Weight;
    }

    public void setMember_Weight(double member_Weight) {
        Member_Weight = member_Weight;
    }

    public int getMember_Status() {
        return Member_Status;
    }

    public void setMember_Status(int member_Status) {
        Member_Status = member_Status;
    }

    public boolean isMember_IsDefault() {
        return Member_IsDefault;
    }

    public void setMember_IsDefault(boolean member_IsDefault) {
        Member_IsDefault = member_IsDefault;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMember_Name() {
        return Member_Name;
    }

    public void setMember_Name(String member_Name) {
        Member_Name = member_Name;
    }

    public String getMember_Portrait() {
        return Member_Portrait;
    }

    public void setMember_Portrait(String member_Portrait) {
        Member_Portrait = member_Portrait;
    }


    public String getMember_DateOfBirth() {
        return Member_DateOfBirth;
    }

    public void setMember_DateOfBirth(String member_DateOfBirth) {
        Member_DateOfBirth = member_DateOfBirth;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public boolean getMember_IsDefault() {
        return this.Member_IsDefault;
    }
}
