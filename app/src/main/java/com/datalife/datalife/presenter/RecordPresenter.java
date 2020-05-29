package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.bean.BtRecordBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.LoginContract;
import com.datalife.datalife.contract.RecordContract;
import com.datalife.datalife.dao.NewsInfo;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.IDatalifeConstant;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/3/14.
 */

public class RecordPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private RecordContract.View mView = null;

    public RecordPresenter(Context mContext){
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
        mView = (RecordContract.View)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    /**
     * 获取图表值
     * @param Member_Id
     * @param Machine_Id
     * @param Project_Id
     * @param SessionId
     */
    public void onGetBtRecordValue(String Member_Id, String Machine_Id, final String Project_Id, String SessionId){
        HashMap<String,String> mHashMap = new HashMap<>();
        mHashMap.put("fun","ProjectCheckTotal");
        mHashMap.put("cls","ProjectCheck");
        mHashMap.put("Member_Id",Member_Id);
        mHashMap.put("Machine_Id",Machine_Id);
        mHashMap.put("Project_Id",Project_Id);
        mHashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getBtRecord(mHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<BtRecordBean>,Object>() {
                    @Override
                    public void onResponse(ArrayList<BtRecordBean> newsInfos, String status) {
                        mView.onSuccess(newsInfos);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        if (Project_Id.equals(IDatalifeConstant.BTINT)) {
                            mView.onBtFail(msg);
                        }else if (Project_Id.equals(IDatalifeConstant.BPINT + "")){
                            mView.onBpFail(msg);
                        }else if (Project_Id.equals(IDatalifeConstant.SPO2HINT + "")){
                            mView.onSpo2hFail(msg);
                        }else if (Project_Id.equals(IDatalifeConstant.ECGINT + "")){
                            mView.onEcgFail(msg);
                            mView.onHrFail(msg);
                        }
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<BtRecordBean>,Object> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
    }

    /**
     * 获取列表值
     * @param PageIndex
     * @param PageCount
     * @param Member_Id
     * @param Machine_Id
     * @param Project_Id
     * @param BeginDate
     * @param EndDate
     * @param SessionId
     */
    public void onGetListValue(String PageIndex,String PageCount,String Member_Id,String Machine_Id,final String Project_Id,String BeginDate,String EndDate,String SessionId){
        HashMap<String,String> mHashMap = new HashMap<>();
        mHashMap.put("fun","ProjectCheckList");
        mHashMap.put("cls","ProjectCheck");
        mHashMap.put("PageIndex",PageIndex);
        mHashMap.put("PageCount",PageCount);
        mHashMap.put("Member_Id",Member_Id);
        mHashMap.put("Machine_Id",Machine_Id);
        mHashMap.put("Project_Id",Project_Id);
        mHashMap.put("BeginDate",BeginDate);
        mHashMap.put("EndDate",EndDate);
        mHashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getBtListRecord(mHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<MeasureRecordBean>,PageBean>() {
                    @Override
                    public void onResponse(ArrayList<MeasureRecordBean> measureRecordBeans, String status) {
                        mView.onSuccess(measureRecordBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        if (Project_Id.equals(IDatalifeConstant.BTINT + "")) {
                            mView.onBtFail(msg);
                        }else if (Project_Id.equals(IDatalifeConstant.BPINT + "")){
                            mView.onBpFail(msg);
                        }else if (Project_Id.equals(IDatalifeConstant.SPO2HINT + "")){
                            mView.onSpo2hFail(msg);
                        }else if (Project_Id.equals(IDatalifeConstant.ECGINT + "")){
                            mView.onEcgFail(msg);
                            mView.onHrFail(msg);
                        }
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MeasureRecordBean>,PageBean> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
    }
}
