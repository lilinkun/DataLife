package com.datalife.datalife.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.bean.LoginBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LG on 2018/2/1.
 */

public class DataLifeUtil {

    public static final int PAGE_HOME = 0;
    public static final int PAGE_BP = 1;
    public static final int PAGE_TEMP = 2;
    public static final int PAGE_SPO2H = 3;
    public static final int PAGE_ECG = 4;
    public static final int PAGE_EQUIT = 5;

    public static final int HEALTH_HISTORY_PAGE_HEART = 1;
    public static final int HEALTH_HISTORY_PAGE_BP = 2;
    public static final int HEALTH_HISTORY_PAGE_TEMP = 3;
    public static final int HEALTH_HISTORY_PAGE_SPO2H = 0;
    public static final int HEALTH_HISTORY_PAGE_ECG = 4;

    public static final String MACHINE_FAT = "1";
    public static final String MACHINE_HEALTH = "2";
    public static final String MACHINE_BRUSH = "3";

    public static final int PAGE_HOMEPAGE = 0;
    public static final int PAGE_MALL = 1;
    public static final int PAGE_HEALTHHOME = 2;
    public static final int PAGE_ME = 3;

    public static final int TOOTH_MODE = 1;
    public static final int TOOTH_TIME = 2;

    public static final int TYPE_HEALTH = 1;
    public static final int TYPE_FAT = 2;
    public static final int TYPE_TOOTH = 3;


    public static final int COMMOMHANDLERMACHINE = 0x112234;
    public static final int COMMOMHANDLERMEMBER = 0x112244;


    public static final String BUNDLEMEMBER = "bundle";
    public static final String BUNDLEMEMBERID = "memberid";
    public static final String BUNDLEMEASURE = "Measure";
    public static final String BUNDLEMACHINEMEMBER = "MACHINEMEMBER";
    public static final String SESSIONID = "SESSIONID";
    public static final String FAMILYUSERINFO = "familyUserInfo";
    public static final String OPENID = "OPENID";
    public static final String UNIONID = "UNIONID";
    public static final String DAY = "DAY";

    public static final String PAGE = "page";

    public static final String LOGIN = "login";

    public static final String SWAN = "SWAN";

    public static final String ZSONIC = "ZSONIC";

    public static final String USAGEPROTOCOL_URL = "https://auth.100zt.com/News/showAgreement/52";

    /**
     * 序列化对象
     * @param loginBean
     * @return
     * @throws IOException
     */
    public static String serialize(LoginBean loginBean) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(loginBean);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.d("serial", "serialize str =" + serStr);
        return serStr;
    }

    /**
     * 序列化对象
     * @return
     * @throws IOException
     */
    public static String serialize(Serializable t) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(t);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.d("serial", "serialize str =" + serStr);
        return serStr;
    }

    /**
     * 反序列化对象
     * @param str
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static LoginBean deSerialization(String str) throws IOException,
            ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        LoginBean loginBean = (LoginBean) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return loginBean;
    }

    /**
     * 反序列化对象
     * @param str
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Serializable unSerialization(String str) throws IOException,
            ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Serializable loginBean = (Serializable) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return loginBean;
    }

    /**
     * 保存数据
     * @param context
     * @param str
     */
    public static void saveData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("logininfo",str).commit();
    }

    /**
     * 保存最近一次温度
     */
    public static void saveBtData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("bt",str).commit();
    }

    /**
     * 保存最近一次温度
     */
    public static void saveBpData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("bp",str).commit();
    }

    /**
     * 保存最近一次温度
     */
    public static void saveSpo2hData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("spo2h",str).commit();
    }

    /**
     * 保存最近一次温度
     */
    public static void saveEcgData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("ecg",str).commit();
    }

    /**
     * 获取测量数据
     */
    public  static String getTestData(Context context,String title){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        String str = sharedPreferences.getString(title,"");
        return  str;
    }

    /**
     * 获取登录信息
     * @param context
     * @return
     */
    public static LoginBean getLoginData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            String str = sharedPreferences.getString("logininfo", "");
            try {
                LoginBean loginBean = (LoginBean)DataLifeUtil.deSerialization(str);

                return loginBean;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 得到数据
     * @param context
     * @return
     */
    public static String getData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        String str = sharedPreferences.getString("logininfo","");
        return  str;
    }

    /**
     * 去除单位的方法
     */
    public static final String getPlusUnit(String str){

        String resultStr = str.substring(0,str.length()-2);
        return resultStr;

    }


    /**
     * 获取头像
     * @param str
     * @param iv
     */
    public static void GetPIC(Context context ,String str , ImageView iv){
        if (str.equals(DefaultPicEnum.PIC1.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC1.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC2.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC2.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC3.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC3.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC4.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC4.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC5.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC5.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC6.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC6.getResPic()));
        }
    }

    /**
     *
     * @param context
     */
    public static void startAlarm(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (notification == null) return;
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

    /**
     * 获取当前时间
     * @return
     */
    public  static String getCurrentTime(){
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(day);
    }


    //出生日期字符串转化成Date对象
    public static Date parse(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(strDate);
    }

    //由出生日期获得年龄
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }


    public static final int[] getColor(Context context){
        int[] colors = new int[]{context.getResources().getColor(R.color.dial_blue), context.getResources().getColor(R.color.dial_green), context.getResources().getColor(R.color.dial_red)};
        return colors;
    }

    public static int getAgeByBirthDay(String birthDay){
        if (birthDay == null || birthDay.length()<4) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //得到当前的年份
        String cYear = sdf.format(new Date()).substring(0,4);
        String cMouth = sdf.format(new Date()).substring(5,7);
        String cDay = sdf.format(new Date()).substring(8,10);
        //得到生日年份
        String birth_Year = birthDay .substring(0,4);
        String birth_Mouth = birthDay .substring(5,7);
        String birth_Day = birthDay .substring(8,10);
        int age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
        if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth))<0) {
            age=age-1;
        }else if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth))==0) {
            if ( (Integer.parseInt(cDay) - Integer.parseInt(birth_Day))>0) {
                age=age-1;
            }else {
                age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
            }
        }else if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth))>0) {
            age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
        }
        return age;
    }

    public static int getInt(String num){
        if (num == null){
            return 0;
        }
        double number = Double.valueOf(num);
        BigDecimal bd=new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(bd.toString());
    }


}
