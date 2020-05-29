package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.activity.BrushHeadActivity;
import com.datalife.datalife.bean.BrushUseCount;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.BrushHeadContract;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;
import com.datalife.datalife.util.DataLifeUtil;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/10/12.
 */

public class BrushHeadPresenter extends BasePresenter {

    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private BrushHeadContract.BrushHeadView brushHeadView;

    public BrushHeadPresenter(Context context){
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
        brushHeadView = (BrushHeadContract.BrushHeadView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }


    public void getChangeBrushHead(String MachineBindId,String SessionId,String MemberId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ToothbrushClear");
        params.put("MachineBindId", MachineBindId);
        params.put("MemberId",MemberId);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getClearCount(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<BrushUseCount,Object>() {

                    @Override
                    public void onResponse(BrushUseCount newsInfos, String status) {
                        brushHeadView.onSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        brushHeadView.onFail(status);
                    }

                    @Override
                    public void onNext(ResultBean<BrushUseCount,Object> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );

    }
}
