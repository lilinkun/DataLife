package com.datalife.datalife.base;

import com.datalife.datalife.exception.ApiException;

import rx.Subscriber;

/**
 * Created by LG on 2018/1/15.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        ApiException apiException = (ApiException) e;
        onError(apiException);
    }


    /**
     * @param e 错误的一个回调
     */
    protected abstract void onError(ApiException e);

}
