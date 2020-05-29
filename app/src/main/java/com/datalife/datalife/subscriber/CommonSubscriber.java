package com.datalife.datalife.subscriber;

import android.content.Context;

import com.datalife.datalife.base.BaseSubscriber;
import com.datalife.datalife.exception.ApiException;
import com.datalife.datalife.util.LogUtils;
import com.datalife.datalife.util.NetworkUtil;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import rx.Subscriber;

/**
 * Created by LG on 2018/1/15.
 */

public abstract class CommonSubscriber<T> extends Subscriber<T> {

    private Context context;

    public CommonSubscriber(){

    }

    public CommonSubscriber(Context context) {
        this.context = context;
    }

    private static final String TAG = "CommonSubscriber";

    @Override
    public void onStart() {

        if (!NetworkUtil.isNetworkAvailable(context)) {
            LogUtils.e(TAG, "网络不可用");
        } else {
            LogUtils.e(TAG, "网络可用");
        }
    }



    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            //服务器返回的错误
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            LogUtils.e("commonSubscriber","网络异常，请检查网络");
        } else if (e instanceof TimeoutException || e instanceof SocketTimeoutException) {
            LogUtils.e("commonSubscriber","网络不畅，请稍后再试！");
        } else if (e instanceof JsonSyntaxException) {
            LogUtils.e("commonSubscriber","数据解析异常");
        } else {
            LogUtils.e("commonSubscriber","服务端错误");
        }
        e.printStackTrace();
    }
    @Override
    public void onCompleted() {
        LogUtils.e(TAG, "成功了");
    }

}
