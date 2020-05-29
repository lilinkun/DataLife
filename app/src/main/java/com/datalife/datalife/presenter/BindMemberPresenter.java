package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.bean.ResultFamily;
import com.datalife.datalife.contract.BindMemberContract;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;

import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/3/6.
 */

public class BindMemberPresenter extends BasePresenter {

    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private BindMemberContract.BindMemberView bindMemberView;
    private ResultFamily<List<MachineBindMemberBean>,PageBean> arrayListResultBean;
    private ResultBean resultBean;


    public BindMemberPresenter(Context context){
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
        bindMemberView = (BindMemberContract.BindMemberView)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

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
                        bindMemberView.onSuccess(machineBindMemberBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        bindMemberView.onFail();
                    }

                    @Override
                    public void onNext(ResultBean<List<MachineBindMemberBean>,PageBean> resultBean) {
                        super.onNext(resultBean);
                    }
                }));
    }

    public void putBindMember(String MachineBindId,String Machine_Id,String Member_Id,String SessionId){
        HashMap<String,String> mHashMap = new HashMap<>();
        mHashMap.put("cls","MachineMemberBind");
        mHashMap.put("fun","MachineMemberBindCreate");
        mHashMap.put("MachineBindId",MachineBindId);
        mHashMap.put("Machine_Id",Machine_Id);
        mHashMap.put("Member_Id",Member_Id);
        mHashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.putMachineMember(mHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {
                    @Override
                    public void onResponse(Object o, String status) {
                        bindMemberView.onSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        bindMemberView.onFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean resultBeans) {
                        super.onNext(resultBeans);
                    }
                }));
    }

}
