package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.BtContract;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;
import com.datalife.datalife.util.DataLifeUtil;

import java.util.HashMap;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/2/28.
 */

public class BtPresenter extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private BtContract.BtView mBtView;;
    private ResultBean mResultBean;

    public BtPresenter(Context mContext){
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
        mBtView = (BtContract.BtView)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    /**
     * 上传温度
     * @param Member_Id
     * @param Project_Id
     * @param Machine_Sn
     * @param SessionId
     * @param CheckValue1
     */
    public void putBtValue(String Member_Id ,String MachineBindId ,String Project_Id,String Machine_Sn,String SessionId,String CheckValue1){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ProjectCheckCreate");
        params.put("Member_Id", Member_Id);
        params.put("MachineBindId",MachineBindId);
        params.put("Machine_Id", DataLifeUtil.MACHINE_HEALTH);
        params.put("Project_Id", Project_Id);
        params.put("Machine_Sn",Machine_Sn);
        params.put("CheckValue1",CheckValue1);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.addFamilyUser(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean>() {
                    @Override
                    public void onCompleted() {
                        if (mResultBean != null){
                            if (mResultBean.getStatus().equals("success")){
                                mBtView.onSuccess();
                            }else{
                                mBtView.onFail();
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
//                        addFamilyView.onfail("请求失败！！");
                    }

                    @Override
                    public void onNext(ResultBean ResultBean) {
                        mResultBean = ResultBean;
                    }
                })
        );

    }

    /**
     * 上传血压
     * @param Member_Id
     * @param Project_Id
     * @param Machine_Sn
     * @param SessionId
     * @param CheckValue1
     * @param CheckValue2
     * @param CheckValue3
     */
    public void putBpValue(String Member_Id ,String MachineBindId, String Project_Id,String Machine_Sn,String SessionId,String CheckValue1,String CheckValue2,String CheckValue3){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ProjectCheckCreate");
        params.put("Member_Id", Member_Id);
        params.put("MachineBindId",MachineBindId);
        params.put("Machine_Id", DataLifeUtil.MACHINE_HEALTH);
        params.put("Project_Id", Project_Id);
        params.put("Machine_Sn",Machine_Sn);
        params.put("CheckValue1",CheckValue1);
        params.put("CheckValue2",CheckValue2);
        params.put("CheckValue3",CheckValue3);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.addFamilyUser(params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResultBean>() {
                            @Override
                            public void onCompleted() {
                                if (mResultBean != null){
                                    if (mResultBean.getStatus().equals("success")){
                                        mBtView.onSuccess();
                                    }else{
                                        mBtView.onFail();
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
//                        addFamilyView.onfail("请求失败！！");
                            }

                            @Override
                            public void onNext(ResultBean ResultBean) {
                                mResultBean = ResultBean;
                            }
                        })
        );
    }

    /**
     * 上传血氧
     * @param Member_Id
     * @param Project_Id
     * @param Machine_Sn
     * @param SessionId
     * @param CheckValue1
     * @param CheckValue2
     */
    public void putSpo2hValue(String Member_Id , String MachineBindId ,String Project_Id,String Machine_Sn,String SessionId,String CheckValue1,String CheckValue2){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ProjectCheckCreate");
        params.put("Member_Id", Member_Id);
        params.put("MachineBindId",MachineBindId);
        params.put("Machine_Id", DataLifeUtil.MACHINE_HEALTH);
        params.put("Project_Id", Project_Id);
        params.put("Machine_Sn",Machine_Sn);
        params.put("CheckValue1",CheckValue1);
        params.put("CheckValue2",CheckValue2);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.addFamilyUser(params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResultBean>() {
                            @Override
                            public void onCompleted() {
                                if (mResultBean != null){
                                    if (mResultBean.getStatus().equals("success")){
                                        mBtView.onSuccess();
                                    }else{
                                        mBtView.onFail();
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
     * 上上传血氧
     * @param Member_Id
     * @param Project_Id
     * @param Machine_Sn
     * @param SessionId
     * @param CheckValue1
     * @param CheckValue2
     * @param CheckValue3
     * @param CheckValue4
     * @param CheckValue5
     * @param CheckValue6
     */
    public void putEcgValue(String Member_Id , String MachineBindId ,String Project_Id,String Machine_Sn,String SessionId,String CheckValue1,String CheckValue2,String CheckValue3,String CheckValue4,String CheckValue5,String CheckValue6){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ProjectCheckCreate");
        params.put("MachineBindId",MachineBindId);
        params.put("Member_Id", Member_Id);
        params.put("Machine_Id", DataLifeUtil.MACHINE_HEALTH);
        params.put("Project_Id", Project_Id);
        params.put("Machine_Sn",Machine_Sn);
        params.put("CheckValue1",CheckValue1);
        params.put("CheckValue2",CheckValue2);
        params.put("CheckValue3",CheckValue3);
        params.put("CheckValue4",CheckValue4);
        params.put("CheckValue5",CheckValue5);
        params.put("CheckValue6",CheckValue6);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.addFamilyUser(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean>() {
                    @Override
                    public void onCompleted() {
                        if (mResultBean != null){
                            if (mResultBean.getStatus().equals("success")){
                                mBtView.onSuccess();
                            }else{
                                mBtView.onFail();
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

}
