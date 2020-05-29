package com.datalife.datalife.bean;

import java.io.Serializable;

/**
 * Created by LG on 2018/1/31.
 * 注册信息
 */
public class RegisterUserInfo implements Serializable {

    String username;
    String psw;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
}
