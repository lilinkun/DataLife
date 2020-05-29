package com.datalife.datalife.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.datalife.datalife.R;
import com.datalife.datalife.dao.BrushBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LG on 2018/1/17.
 */

public class IDatalifeConstant {


    public static final boolean ANTI_ALIAS = true;

    public static final int DEFAULT_SIZE = 150;
    public static final int DEFAULT_START_ANGLE = 270;
    public static final int DEFAULT_SWEEP_ANGLE = 360;

    public static final int DEFAULT_ANIM_TIME = 1000;

    public static final int DEFAULT_MAX_VALUE = 100;
    public static final int DEFAULT_VALUE = 50;

    public static final int DEFAULT_HINT_SIZE = 15;
    public static final int DEFAULT_UNIT_SIZE = 30;
    public static final int DEFAULT_VALUE_SIZE = 15;

    public static final int DEFAULT_ARC_WIDTH = 15;

    public static final int DEFAULT_WAVE_HEIGHT = 40;

    public static final String TYPE = "type";

    public static final int TYPE_BACK = 0x0003;

    public static final int INTENT_LOGIN =0x0012;

    public static final int MESSAGEWHAT_CHART = 0x112;

    public static final int MESSAGEMACHINE = 0x1122;

    public static final int MESSAGESERVICE = 0x123;

    public static final int VISIBLEHEALTH = 0x12341;


    public static final int SETTINGREQUESTCODE = 0x232;

    /**
     * 登陆跳转注册页面
     */
    public static final int RESULT_REGISTER = 0x1234;

    public static final String REGISTERBEAN = "registerbean";

    /**
     * 蓝牙返回值
     */
    public static final int REQUEST_OPEN_BT = 0x23;

    public static String RESULT_SUCCESS = "success";
    public static String RESULT_FAIL = "fail";

    public static final int BTINT = 2;//体温的project_id
    public static final int SPO2HINT = 3;//血氧的project_id
    public static final int ECGINT = 5;//心电图的project_id
    public static final int BPINT = 1;//血压的project_id

    public static final int BRUSHINT = 7;

    //刷牙上传数据
    public static final int BRUSHSINGLE = 1;
    public static final int BRUSHMUL = 2;


    //检测历史记录
    public static final int DAY = 0;
    public static final int WEEK = 1;
    public static final int MONTH = 2;
    public static final int YEAR = 3;

    //历史记录获取的条数
    public static final String ShowCount = "100";


    public static final String APP_ID = "wx7b154709878a1cbe";
    public static final String SECRET = "b92b65a7e3452eb4714685abd1ad3b73";
//    public static final String APP_ID = "wxdb538af5f5e2fcab";
//    public static final String SECRET = "c8d1fdec79dfabb5a0722500907e4046";


    /**
     * 获取某月的最后一天
     * @Title:getLastDayOfMonth
     * @Description:
     * @param:@param year
     * @param:@param month
     * @param:@return
     * @return:String
     * @throws
     */
    public static String getLastDayOfMonth(int year,int month)
    {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    /**
     * 年月日得到某月最后一天
     */
    public static String getLastDayOfMonth(String str){
        int month = Integer.valueOf(str.substring(str.indexOf("-")+1,str.lastIndexOf("-")));
        int year = Integer.valueOf(str.substring(0,str.indexOf("-")));
        String lastDayOfMonth = getLastDayOfMonth(year,month);
        return lastDayOfMonth;
    }


    //进度表的宽度
    public static int getWM(Context context){
        int wm_width = IDatalifeConstant.display(context).getWidth();
        wm_width = wm_width - DensityUtil.dip2px(context, 60);
        return wm_width;
    }

    //血压进度表的宽度
    public static int getSmallWM(Context context){

        WindowManager wm = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int wm_width = wm.getDefaultDisplay().getWidth();
        wm_width = (wm_width - DensityUtil.dip2px(context, 60))/2;
        return wm_width;
    }


    public static final Display display(Context context){
        WindowManager wm = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        Display defaultDisplay = wm.getDefaultDisplay();
        return defaultDisplay;
    }


    public static final int getCalculation(Context context ,BrushBean brushBean){

        int leftBrush = brushBean.getLeftBrushTime();
        int rightBrush = brushBean.getRightBrushTime();
        int big = leftBrush>=rightBrush ? leftBrush:rightBrush;
        int small = leftBrush>=rightBrush ? rightBrush:leftBrush;
        int overPowerCount =brushBean.getOverPowerCount();
        int settingTime = brushBean.getSettingTime();
        int workTime = brushBean.getWorkTime();

        /*if (settingTime == 13){
            settingTime = 180;
        }else if (settingTime == 10){
            settingTime = 150;
        }else if (settingTime == 16){
            settingTime = 210;
        }*/

        String[] strings = context.getResources().getStringArray(R.array.arr_duration);
        if (settingTime < 27) {
            settingTime = Integer.valueOf(strings[settingTime - 1]);
        }


        double sa = (double)workTime/settingTime * 40;
        if (overPowerCount >= 10){
            overPowerCount = 10;
        }

        double rangeCount = (double)(leftBrush + rightBrush)/workTime*100;

        if (rangeCount > 80){
            rangeCount = (rangeCount-80) * 0.5;
        }else if (rangeCount < 80){
            rangeCount = (80 - rangeCount) * 0.5;
        }else if (rangeCount == 80){
            rangeCount = 0;
        }

        if (rangeCount > 20){
            rangeCount = 20;
        }else {
            rangeCount = 20 - rangeCount;
        }

        if (small == 0){
            return 0;
        }else {
            double sas = (double)small/big * 40;
            int fraction =  (int)sas + (int)sa;

            fraction = fraction + (int)rangeCount;

            /*if (fraction >= 10){
                fraction -= overPowerCount;
            }*/

            return fraction;
        }
    }



}
