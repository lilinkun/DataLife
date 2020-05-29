package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.ResultFamily;
import com.datalife.datalife.contract.EquipmentContract;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/3/5.
 */

public class EquipmentPresenter extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private EquipmentContract.EquipmentView equipmentView;
    private ResultBean<ArrayList<MachineBean>, PageBean> mMachineList;
    private ResultBean resultBean;

    public EquipmentPresenter(Context mContext){
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
        equipmentView = (EquipmentContract.EquipmentView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {
    }

    public void getDevMachineInfo(String PageIndex,String PageCount,String SessionId){

        HashMap<String,String> mHashMap = new HashMap<>();
        mHashMap.put("cls","MachineBind");
        mHashMap.put("fun","MachineBindList");
        mHashMap.put("PageIndex",PageIndex);
        mHashMap.put("PageCount",PageCount);
        mHashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getMachineInfo(mHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>, PageBean>() {
                    @Override
                    public void onResponse(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> machineBeans, String status) {
                        equipmentView.onEquipSuccess(machineBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        equipmentView.onEquipFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>, PageBean> arrayListPageBeanResultFamily) {
                        super.onNext(arrayListPageBeanResultFamily);
                    }
                }));
    }

    public void sendDeviceInfo(String MachineName,String MachineSn,String SessionId){

        Log.v("HealthMonitorPresenter:" , "MachineName:" + MachineName + ",MachineSn:" + MachineSn + ",SessionId:"+SessionId);
        HashMap<String , String> hashMap = new HashMap<>();
        hashMap.put("cls","MachineBind");
        hashMap.put("fun","MachineBindCreate");
        hashMap.put("MachineName",MachineName);
        hashMap.put("MachineSn",MachineSn);
        hashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.sendDeviceInfo(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {
                    @Override
                    public void onResponse(Object o, String status) {
                        equipmentView.sendSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        equipmentView.sendFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }

                })
        );

    }

}
