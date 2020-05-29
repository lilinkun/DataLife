package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.BrushContract;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/10/9.
 */

public class BrushBindPresenter extends BasePresenter{

    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private BrushContract.BrushBindView brushBindView;

    public BrushBindPresenter(Context context){
        this.mContext = context;
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
        brushBindView = (BrushContract.BrushBindView)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void sendDeviceInfo(String MachineName,String MachineSn,String SessionId,String Member_Id){

        Log.v("HealthMonitorPresenter:" , "MachineName:" + MachineName + ",MachineSn:" + MachineSn + ",SessionId:"+SessionId);
        HashMap<String , String> hashMap = new HashMap<>();
        hashMap.put("cls","MachineBind");
        hashMap.put("fun","MachineBindCreate");
        hashMap.put("MachineName",MachineName);
        hashMap.put("Member_Id",Member_Id);
        hashMap.put("MachineSn",MachineSn);
        hashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.sendDeviceInfo(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {
                    @Override
                    public void onResponse(Object o, String status) {
                        brushBindView.success();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        brushBindView.fail(msg);
                    }

                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }

                })
        );

    }

}
