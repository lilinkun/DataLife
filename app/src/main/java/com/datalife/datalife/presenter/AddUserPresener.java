package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.R;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.AddFamilyUserContract;
import com.datalife.datalife.contract.LoginContract;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/2/4.
 */

public class AddUserPresener extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private ResultBean mResultBean;
    private AddFamilyUserContract.AddFamilyView addFamilyView;


    public AddUserPresener(Context mContext){
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
        addFamilyView = (AddFamilyUserContract.AddFamilyView)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {
    }


    public void addFamilyUser(String Member_Name ,String Member_Portrait,String Member_Stature,String Member_Weight,String Member_DateOfBirth,String Member_Sex,String Member_Status,String Member_IsDefault,String SessionId){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "BigDataMember");
        params.put("fun", "BigDataMemberCreate");
        params.put("Member_Name", Member_Name);
        params.put("Member_Portrait", Member_Portrait);
        params.put("Member_Stature", Member_Stature);
        params.put("Member_Weight",Member_Weight);
        params.put("Member_DateOfBirth",Member_DateOfBirth);
        params.put("Member_Status",Member_Status);
        params.put("Member_Sex",Member_Sex);
        params.put("Member_IsDefault",Member_IsDefault);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.addFamilyUser(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {
                    @Override
                    public void onResponse(Object o, String status) {
                        addFamilyView.onsuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        addFamilyView.onfail(msg);
                    }

                    @Override
                    public void onNext(ResultBean resultBean) {
                        super.onNext(resultBean);
                    }

                })
        );
    }

    public void getFamilyDataList(String sessionId,String PageIndex,String PageCount){

        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put("SessionId",sessionId);
        hashMap.put("cls","BigDataMember");
        hashMap.put("fun","BigDataMemberList");
        hashMap.put("PageCount",PageCount);
        hashMap.put("PageIndex",PageIndex);

        mCompositeSubscription.add(manager.getFamilyList(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<FamilyUserInfo>, PageBean>() {
                    @Override
                    public void onResponse(ArrayList<FamilyUserInfo> familyUserInfos, String status) {
                        addFamilyView.onBackFamilyListDataSuccess(familyUserInfos);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        addFamilyView.onBackFamilyListDataFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<FamilyUserInfo>,PageBean> resultBean) {
                        super.onNext(resultBean);
                    }
                }));
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
                        addFamilyView.onSuccess(machineBindMemberBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        addFamilyView.onFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<List<MachineBindMemberBean>,PageBean> resultBean) {
                        super.onNext(resultBean);
                    }
                }));
    }


}
