package com.datalife.datalife.presenter;

import android.content.Intent;

import com.datalife.datalife.mvp.IView;

/**
 * Created by LG on 2018/1/31.
 */

public interface Presenter {
    void onCreate();

    void onStart();

    void onStop();

    void pause();

    void attachView(IView view);

    void attachIncomingIntent(Intent intent);
}