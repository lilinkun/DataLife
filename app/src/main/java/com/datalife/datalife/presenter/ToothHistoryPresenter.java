package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.bean.BrushUseCount;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.BrushHistoryContract;
import com.datalife.datalife.dao.BrushBean;
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
 * Created by LG on 2018/10/11.
 */

public class ToothHistoryPresenter extends BasePresenter {

    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private BrushHistoryContract.BrushHistoryView brushHistoryView;

    public ToothHistoryPresenter(Context context){
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
        brushHistoryView = (BrushHistoryContract.BrushHistoryView)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

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
    public void onGetListValue(String PageIndex,String PageCount,String MachineBindId,String Member_Id,String Machine_Id,final String Project_Id,String BeginDate,String EndDate,String SessionId){
        HashMap<String,String> mHashMap = new HashMap<>();
        mHashMap.put("fun","ProjectCheckList");
        mHashMap.put("cls","ProjectCheck");
        mHashMap.put("PageIndex",PageIndex);
        mHashMap.put("PageCount",PageCount);
        mHashMap.put("Member_Id",Member_Id);
        mHashMap.put("MachineBindId",MachineBindId);
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
                        ArrayList<BrushBean> brushBeans = new ArrayList<>();
                        for (int i = 0;i<measureRecordBeans.size();i++){
                            BrushBean brushBean = new BrushBean();
                            brushBean.setStartTime(measureRecordBeans.get(i).getCreateDate());
                            brushBean.setLeftBrushTime(DataLifeUtil.getInt(measureRecordBeans.get(i).getCheckValue1()));
                            brushBean.setRightBrushTime(DataLifeUtil.getInt(measureRecordBeans.get(i).getCheckValue2()));
                            brushBean.setWorkTime(DataLifeUtil.getInt(measureRecordBeans.get(i).getCheckValue3()));
                            brushBean.setSettingTime(DataLifeUtil.getInt(measureRecordBeans.get(i).getCheckValue6()));
                            brushBean.setOverPowerCount(DataLifeUtil.getInt(measureRecordBeans.get(i).getCheckValue4()));
                            brushBean.setOverPowerTime(DataLifeUtil.getInt(measureRecordBeans.get(i).getCheckValue5()));

                            brushBeans.add(brushBean);
                        }

                        brushHistoryView.onSuccess(brushBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        brushHistoryView.onFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MeasureRecordBean>,PageBean> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
    }

}
