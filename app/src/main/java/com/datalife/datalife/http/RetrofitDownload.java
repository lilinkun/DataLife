package com.datalife.datalife.http;

import android.content.Context;

import com.datalife.datalife.http.factory.GsonDConverterFactory;
import com.datalife.datalife.util.LoggerInterceptor;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LG on 2018/3/18.
 */

public class RetrofitDownload {
    private Context mCntext;
    //接口根地址
    public static final String BASE_URL = "http://192.168.0.126:8080/liguo/";
//    public static final String BASE_URL = "http://192.168.0.168:81/Update/";
    //设置超时时间
    private static final long DEFAULT_TIMEOUT = 10_000L;

    OkHttpClient client = null;
    GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
    private static RetrofitDownload instance = null;
    private Retrofit mRetrofit = null;
    public static RetrofitDownload getInstance(Context context){
        if (instance == null){
            instance = new RetrofitDownload(context);
        }
        return instance;
    }
    private RetrofitDownload(Context mContext){
        mCntext = mContext;
        init();
    }

    private void init() {
        resetApp();
    }

    private void resetApp() {

        client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                //添加请求头
                //.addInterceptor(new HeaderInterceptor())
                //添加日志打印拦截器
                .addInterceptor(new LoggerInterceptor("===", true))
                .build();


        mRetrofit = new Retrofit.Builder()
//                .baseUrl("https://api.douban.com/v2/")
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
    public DownloadService getServer(){
        return mRetrofit.create(DownloadService.class);
    }
}