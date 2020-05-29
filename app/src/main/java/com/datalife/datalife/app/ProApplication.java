package com.datalife.datalife.app;

import android.app.Application;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.multidex.MultiDex;

import com.datalife.datalife.util.DeviceData;
import com.linktop.MonitorDataTransmissionManager;

import lib.linktop.sev.CssServerApi;

/**
 * Created by LG on 2018/1/15.
 * 应用,主要用来做一下初始化的操作
 */

public class ProApplication extends Application {
    private static Context mContext;
    private static ProApplication instance;

    @Override
    public void onCreate() {
        //是否打开日志，true 打开，false关闭，默认打开
        MonitorDataTransmissionManager.isDebug(true);
        //在Application初始化CssServerApi，不使用凌拓服务器进行数据同步的话，就不需要在此初始化CssServerAp。
//   accPrefix= "zydb_"： AndroidManifest.xml中设置的appKey&appSecret所对应的账号前缀
//        每组appKey&appSecret都有自己对应的accPrefix,具体什么值由凌拓服务器组决定并定义。
//        务必填对，否则账号系统相关接口无法响应成功。
        CssServerApi.init(this, "zydb_", true);
        isLogin.set(CssServerApi.isLogin());
        super.onCreate();
        mContext = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static ProApplication getInstance() {
        return instance;
    }



    public static synchronized ProApplication context() {
        return (ProApplication) mContext;
    }

    /**
     * @return
     * 全局的上下文
     */
    public static Context getmContext() {
        return mContext;
    }

    public final static ObservableBoolean isLogin = new ObservableBoolean(true);
    public final static ObservableBoolean isShowUploadButton = new ObservableBoolean(false);
    public final static boolean isUseCustomBleDevService = true;
    public static String SESSIONID = "";
}