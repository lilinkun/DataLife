package com.datalife.datalife.base;

import com.datalife.datalife.mvp.IModel;
import com.datalife.datalife.mvp.IPresenter;
import com.datalife.datalife.mvp.IView;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/1/15.
 */

public abstract class BasePresenter<T extends IView> implements IPresenter {
    private WeakReference actReference;
    protected T iView;
    protected CompositeSubscription mSubscriptions;

    public abstract HashMap<String, IModel> getiModelMap();

    @Override
    public void attachView(IView iView) {
        actReference = new WeakReference(iView);
    }

    protected void addSubscription(Subscription subscribe) {
        if (mSubscriptions == null)
            mSubscriptions = new CompositeSubscription();
        mSubscriptions.add(subscribe);
    }

    @Override
    public void detachView() {
        if (actReference != null) {
            actReference.clear();
            actReference = null;
        }
    }

    @Override
    public T getIView() {
        return (T) actReference.get();
    }

    /**
     * @param models
     * @return
     * 添加多个model,如有需要
     */
    public abstract HashMap<String, IModel> loadModelMap(IModel... models);

}
