package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.DecoratedContract;
import com.datalife.datalife.dao.NewsInfo;
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

public class DecoratedPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private DecoratedContract.DecoratedView mDecoratedView;


    public DecoratedPresenter(Context mContext){
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
        mDecoratedView = (DecoratedContract.DecoratedView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void onTestDay(String Member_Id,String Project_Id,String date,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ProjectDayDate");
        params.put("Member_Id", Member_Id);
        params.put("Machine_Id", DataLifeUtil.MACHINE_FAT);
        params.put("Project_Id", Project_Id);
        params.put("Date",date);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getDayDate(params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new HttpResultCallBack<ArrayList<String>,Object>() {
                                    @Override
                                    public void onResponse(ArrayList<String> newsInfos, String status) {
                                        mDecoratedView.onSuccess(newsInfos);
                                    }

                                    @Override
                                    public void onErr(String msg, String status) {
                                        mDecoratedView.onfail(msg);
                                    }

                                    @Override
                                    public void onNext(ResultBean<ArrayList<String>,Object> resultNews) {
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
    public void onGetListValue(String PageIndex,String PageCount,String Member_Id,String Machine_Id,String Project_Id,String BeginDate,String EndDate,String SessionId){
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
                        mDecoratedView.onDataSuccess(measureRecordBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        mDecoratedView.onDataFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MeasureRecordBean>,PageBean> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
    }

}
