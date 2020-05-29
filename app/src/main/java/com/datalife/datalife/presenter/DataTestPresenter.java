package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.bean.DataTestBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.DataTestContract;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;
import com.datalife.datalife.util.DataLifeUtil;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/8/20.
 */

public class DataTestPresenter extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private DataTestContract.DataTestView dataTestView;

    public DataTestPresenter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        manager = new DataManager(mContext);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void attachView(IView view) {
        dataTestView = (DataTestContract.DataTestView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }


    public void getTestData(String PageIndex,String PageCount,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "CheckServerResult");
        params.put("fun", "CheckServerResultList");
        params.put("PageIndex", PageIndex);
        params.put("PageCount",PageCount);
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.getDataTest(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<DataTestBean>,Object>() {
                    @Override
                    public void onResponse(ArrayList<DataTestBean> newsInfos, String status) {
                        dataTestView.success(newsInfos);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        dataTestView.fail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<DataTestBean>,Object> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
    }
}
