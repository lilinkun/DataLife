package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.bean.BrushUseCount;
import com.datalife.datalife.bean.DataTestBean;
import com.datalife.datalife.bean.RedEnvelopeEntity;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.BrushSaveContract;
import com.datalife.datalife.dao.BrushBean;
import com.datalife.datalife.dao.BrushManyBean;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.UToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/10/10.
 */

public class BrushPresenter extends BasePresenter {

    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private BrushSaveContract.BrushView brushView;
    private ResultBean mResultBean;

    public BrushPresenter(Context context){
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
        brushView = (BrushSaveContract.BrushView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    /**
     * 上传刷牙数据
     * @param Member_Id
     * @param MachineBindId
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
    public void putBrushValue(String Member_Id ,String MachineBindId ,String Project_Id,String Machine_Sn,String SessionId,String CreateDate,String CheckValue1,String CheckValue2,String CheckValue3,String CheckValue4,String CheckValue5,String CheckValue6,String CheckValue7){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ProjectCheckCreate");
        params.put("Member_Id", Member_Id);
        params.put("MachineBindId",MachineBindId);
        params.put("Machine_Id", DataLifeUtil.MACHINE_BRUSH);
        params.put("Project_Id", Project_Id);
        params.put("Machine_Sn",Machine_Sn);
        params.put("CreateDate",CreateDate);
        params.put("CheckValue1",CheckValue1);
        params.put("CheckValue2",CheckValue2);
        params.put("CheckValue3",CheckValue3);
        params.put("CheckValue4",CheckValue4);
        params.put("CheckValue5",CheckValue5);
        params.put("CheckValue6",CheckValue6);
        params.put("CheckValue7",CheckValue7);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.addFamilyUser(params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResultBean>() {
                            @Override
                            public void onCompleted() {
                                if (mResultBean != null){
                                    if (mResultBean.getStatus().equals("success")){
                                        brushView.success();
                                    }else{
//                                        brushView.fail("aaa");
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
//                                  addFamilyView.onfail("请求失败！！");
                            }

                            @Override
                            public void onNext(ResultBean ResultBean) {
                                mResultBean = ResultBean;
                            }
                        })
        );

    }

    public void getBrushCount(String MachineBindId,String SessionId,String MemberId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ProjectCheckToothbrush");
        params.put("MachineBindId", MachineBindId);
        params.put("MemberId",MemberId);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getUseCount(params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new HttpResultCallBack<BrushUseCount,Object>() {

                            @Override
                            public void onResponse(BrushUseCount newsInfos, String status) {
                                brushView.successCount(newsInfos);
                            }

                            @Override
                            public void onErr(String msg, String status) {
                                brushView.failCount(msg);
                            }

                            @Override
                            public void onNext(ResultBean<BrushUseCount,Object> resultNews) {
                                super.onNext(resultNews);
                            }
                        })
        );

    }

    /**
     * 上传刷牙数据
     * @param SessionId
     */
    public void putBrushValues(String SessionId, String str, final int type, final List<BrushManyBean> brushManyBeans){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ProjectCheckMoreCreate");
        params.put("CheckValue", str);
        params.put("SessionId", SessionId);

        mCompositeSubscription.add(manager.getHistory(params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new HttpResultCallBack<String,Object>() {
                            @Override
                            public void onResponse(String newsInfos, String status) {
//                                dataTestView.success(newsInfos);
//                                UToast.show(mContext,"success上传");
                                if (type == IDatalifeConstant.BRUSHMUL){
                                    brushView.successMul();
                                }else{
                                    brushView.success();
                                }
                            }

                            @Override
                            public void onErr(String msg, String status) {
                                brushView.fail(msg,brushManyBeans);
//                                UToast.show(mContext,"fail上传");
                            }

                            @Override
                            public void onNext(ResultBean<String,Object> resultNews) {
                                super.onNext(resultNews);
                            }
                        })
        );

    }

    /**
     * 判断这个用户是否有红包
     * @param SessionId
     * @param MM_Id
     */
    public void isHaveRedEnvelope(String SessionId, String MM_Id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "RedEnvelopes");
        params.put("fun", "RedEnvelopesExists");
        params.put("MM_Id", MM_Id);
        params.put("ReceiveType","1");
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.getIsHaveRedEnvelope(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<RedEnvelopeEntity, Object>() {
                    @Override
                    public void onResponse(RedEnvelopeEntity newsInfos, String status) {
                        brushView.successRedEnvelope(newsInfos);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        brushView.failRedEnvelope(status);
                    }

                    @Override
                    public void onNext(ResultBean<RedEnvelopeEntity, Object> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
    }


    /**
     * 判断这个用户打开红包
     * @param SessionId
     * @param MM_Id
     */
    public void isOpenRedEnvelope(String SessionId, String MM_Id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "RedEnvelopes");
        params.put("fun", "RedEnvelopesOpen");
        params.put("MM_Id", MM_Id);
        params.put("ReceiveType","1");
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.getIsOpenRedEnvelope(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<String, Object>() {
                    @Override
                    public void onResponse(String newsInfos, String status) {
                        brushView.successOpenRedEnvelope();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        brushView.failOpenRedEnvelope(msg,status);
                    }

                    @Override
                    public void onNext(ResultBean<String, Object> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
    }
}
