package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.bean.LastFatMeasureDataBean;
import com.datalife.datalife.bean.LastMeasureDataBean;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.FatContract;
import com.datalife.datalife.dao.MachineBindMemberBean;
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
 * Created by LG on 2018/5/25.
 */

public class FatPresenter extends BasePresenter {

    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private FatContract.FatView mFatView;
    private ResultBean mResultBean;

    public FatPresenter(Context mContext){
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
        mFatView = (FatContract.FatView)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

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
                        mFatView.onSuccess(machineBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        mFatView.onfail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>, PageBean> arrayListPageBeanResultFamily) {
                        super.onNext(arrayListPageBeanResultFamily);
                    }
                }));
    }

    /**
     * 上传体脂称的值
     * @param Member_Id
     * @param Machine_Sn
     * @param SessionId
     * @param CheckValue1
     * @param CheckValue2
     * @param CheckValue3
     * @param CheckValue4
     * @param CheckValue5
     * @param CheckValue6
     * @param CheckValue7
     * @param CheckValue8
     * @param CheckValue9
     * @param CheckValue10
     * @param CheckValue11
     * @param CheckValue12
     */
    public void putfattest(String Member_Id,
                           String MachineBindId,String Machine_Sn,String SessionId,String CheckValue1,String CheckValue2,String CheckValue3,String CheckValue4,
                           String CheckValue5,String CheckValue6,String CheckValue7,String CheckValue8,String CheckValue9,String CheckValue10,String CheckValue11,String CheckValue12){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ProjectCheckCreate");
        params.put("Member_Id", Member_Id);
        params.put("Machine_Id", DataLifeUtil.MACHINE_FAT);
        params.put("Project_Id", "6");
        params.put("Machine_Sn",Machine_Sn);
        params.put("MachineBindId",MachineBindId);
        params.put("CheckValue1",CheckValue1);
        params.put("CheckValue2",CheckValue2);
        params.put("CheckValue3",CheckValue3);
        params.put("CheckValue4",CheckValue4);
        params.put("CheckValue5",CheckValue5);
        params.put("CheckValue6",CheckValue6);
        params.put("CheckValue7",CheckValue7);
        params.put("CheckValue8",CheckValue8);
        params.put("CheckValue9",CheckValue9);
        params.put("CheckValue10",CheckValue10);
        params.put("CheckValue11",CheckValue11);
        params.put("CheckValue12",CheckValue12);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.addFamilyUser(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean>() {
                    @Override
                    public void onCompleted() {
                        if (mResultBean != null){
                            if (mResultBean.getStatus().equals("success")){
                                mFatView.onSuccess();
                            }else{
                                mFatView.onFail();
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResultBean ResultBean) {
                        mResultBean = ResultBean;
                    }
                })
        );
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
        mCompositeSubscription.add(manager.getFatLastInfo(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<LastFatMeasureDataBean<MeasureRecordBean>,Object>() {
                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }

                    @Override
                    public void onResponse(LastFatMeasureDataBean<MeasureRecordBean> o, String status) {
                        mFatView.onSuccess(o);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        mFatView.onfail(msg);
                    }
                })
        );
    }

}
