package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.datalife.datalife.bean.LastMeasureDataBean;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.HealthMonitorContract;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/2/8.
 */

public class HealthMonitorPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private HealthMonitorContract.HealthMonitorView mHealthMonitorView;
    private ResultBean resultBean;

    public HealthMonitorPresenter(Context context){
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
        mHealthMonitorView = (HealthMonitorContract.HealthMonitorView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

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
                        mHealthMonitorView.sendSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        mHealthMonitorView.sendFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }

                })
        );
    }

    public void getMachineInfo(String PageIndex,String PageCount,String SessionId){

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
                        mHealthMonitorView.onSuccess(machineBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        mHealthMonitorView.onfail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>, PageBean> arrayListPageBeanResultFamily) {
                        super.onNext(arrayListPageBeanResultFamily);
                    }
                }));
    }


    /**
     * 机器项目检测最后一次
     */
    public void getNewMeasureInfo(String Member_Id,String Machine_Id,String SessionId){
       HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("cls","ProjectCheck");
        hashMap.put("fun","ProjectCheckLast");
        hashMap.put("Member_Id",Member_Id);
        hashMap.put("Machine_Id",Machine_Id);
        hashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getLastInfo(hashMap)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new HttpResultCallBack<LastMeasureDataBean<MeasureRecordBean>,Object>() {
            @Override
            public void onNext(ResultBean o) {
                super.onNext(o);
            }

            @Override
            public void onResponse(LastMeasureDataBean<MeasureRecordBean> o, String status) {
                mHealthMonitorView.getSuccess(o);
            }

            @Override
            public void onErr(String msg, String status) {
                mHealthMonitorView.getFail(msg);
            }
        })
        );
    }

    public void getBindMemberList(String PageIndex,String PageCount,String MachineBindId,String SessionId){
        HashMap<String,String> mHashMap = new HashMap<>();
        mHashMap.put("cls","MachineMemberBind");
        mHashMap.put("fun","MachineMemberBindList");
        mHashMap.put("PageIndex",PageIndex);
        mHashMap.put("PageCount",PageCount);
        mHashMap.put("MachineBindId",MachineBindId);
        mHashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getMachineMemberInfo(mHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<List<MachineBindMemberBean>, PageBean>() {
                    @Override
                    public void onResponse(List<MachineBindMemberBean> machineBindMemberBeans, String status) {
                        mHealthMonitorView.getBindListSuccess(machineBindMemberBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        mHealthMonitorView.getBindListFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<List<MachineBindMemberBean>,PageBean> resultBean) {
                        super.onNext(resultBean);
                    }
                }));
    }

}
