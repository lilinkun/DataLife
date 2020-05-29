package com.datalife.datalife.transformer;

import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.exception.ApiException;
import com.datalife.datalife.exception.ApiException1;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by LG on 2018/1/30.
 */

public class DefaultTransformer<T> implements Observable.Transformer<T, T> {
    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<T, T>() {// 通用错误处理，判断code
                    @Override
                    public T call(T t) {
                        if (((RegisterBean)t).getStatus().equals("error")) {
                            throw new ApiException1(Integer.parseInt(((RegisterBean)t).getCode()), ((RegisterBean)t).getCode());
                        }
                        return t;
                    }
                });
    }

    public static <T> DefaultTransformer<T> create() {
        return new DefaultTransformer<>();
    }
}