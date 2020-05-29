package com.datalife.datalife.bean;

import com.datalife.datalife.dao.FamilyUserInfo;

/**
 * Created by LG on 2018/3/13.
 */
public class MemberListBean {
    private FamilyUserInfo familyUserInfo;
    private boolean isSelector ;

    public FamilyUserInfo getFamilyUserInfo() {
        return familyUserInfo;
    }

    public void setFamilyUserInfo(FamilyUserInfo familyUserInfo) {
        this.familyUserInfo = familyUserInfo;
    }

    public boolean isSelector() {
        return isSelector;
    }

    public void setSelector(boolean selector) {
        isSelector = selector;
    }

    @Override
    public String toString() {
        return "MemberListBean{" +
                "familyUserInfo=" + familyUserInfo +
                ", isSelector=" + isSelector +
                '}';
    }
}
