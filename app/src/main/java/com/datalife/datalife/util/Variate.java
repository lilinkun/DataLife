package com.datalife.datalife.util;

import com.datalife.datalife.bean.UserData;

import java.sql.Date;

/**
 * Created by LG on 2018/2/6.
 */

public class Variate {

    /**
     * 手机频�?�?��像素
     */
    public static int screenWidth;
    public static int screenHeight;
    /**
     * 手机唯一�?��识码
     */
    public static String code = "";
    /**
     * 用户登陆后的账号ＩＤ
     */
    public static String accountNumID = "";

    public static String user_gm_id;

    public static UserData user = null;

    public static int analyze_project_count = 0;//保存上次显示的项目

    public static Date analyze_homepage_count = null;//保存上次显示的项目


    public static String user_sendDivData = "83010122a0";//发送给脂肪秤的命令

    public static double user_goal_weight = 0;//


    public static UserData getUser() {
        return user;
    }

    public static void setUser(UserData user) {
        Variate.user = user;
    }

}
