package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.bean.GeneBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.ReportContract;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/3/27.
 */

public class ReportPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private ReportContract.ReportView reportView = null;

    public ReportPresenter(Context mContext) {
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
        reportView = (ReportContract.ReportView)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void sendList(String PageIndex,String PageCount,String sessionid){
        HashMap<String, String> params = new HashMap<>();
        params.put("fun", "GeneCollectionList");
        params.put("cls", "GeneCollection");
        params.put("PageIndex", PageIndex);
        params.put("PageCount", PageCount);
        params.put("SessionId", sessionid);
        mCompositeSubscription.add(manager.sendlist(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<GeneBean>,Object>(){

                    @Override
                    public void onResponse(ArrayList<GeneBean> geneBean, String status) {
                        reportView.BackValueSuccess(geneBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        reportView.BackValueFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean resultBean) {
                        super.onNext(resultBean);
                    }
                })
        );
    }


}
