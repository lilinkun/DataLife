package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.dao.FlashListBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.dao.NewsInfo;
import com.datalife.datalife.contract.HomePageContract;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/3/2.
 */

public class HomePagePresenter extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private HomePageContract.HomePageView homePageView;
    private ResultBean<ArrayList<NewsInfo>,Object> mResultNews;

    public HomePagePresenter(Context mContext){
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
        homePageView = (HomePageContract.HomePageView)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void getNewsInfo(String PageIndex,String PageCount) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "article");
        params.put("fun", "articleList");
        params.put("PageIndex", PageIndex);
        params.put("PageCount", PageCount);
        params.put("cat_id", "");
        params.put("article_title", "");
        mCompositeSubscription.add(manager.getNewsInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<NewsInfo>,Object>() {
                    @Override
                    public void onResponse(ArrayList<NewsInfo> newsInfos, String status) {
                        homePageView.onSuccess(newsInfos);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        homePageView.onfail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<NewsInfo>,Object> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
    }

    public void onFlashPage(){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "Flash");
        params.put("fun", "FlashList");
        params.put("F_Style", "5");
        mCompositeSubscription.add(manager.flashList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<FlashListBean>,Object>() {
                    @Override
                    public void onResponse(ArrayList<FlashListBean> flashListBeans, String status) {
                        homePageView.onFlashSuccess(flashListBeans);
                        DBManager.getInstance(mContext).deleteFlash();
                        for (int i = 0;i<flashListBeans.size();i++) {
                            DBManager.getInstance(mContext).insertFlash(flashListBeans.get(i));
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        homePageView.onFlashFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<FlashListBean>,Object> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
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
                        homePageView.onEquipSuccess(machineBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        homePageView.onEquipFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>, PageBean> arrayListPageBeanResultFamily) {
                        super.onNext(arrayListPageBeanResultFamily);
                    }
                }));
    }
}
