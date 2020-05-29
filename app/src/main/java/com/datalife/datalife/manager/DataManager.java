package com.datalife.datalife.manager;

import android.content.Context;

import com.datalife.datalife.bean.BrushUseCount;
import com.datalife.datalife.bean.BtRecordBean;
import com.datalife.datalife.bean.DataTestBean;
import com.datalife.datalife.bean.DownloadBean;
import com.datalife.datalife.bean.LastFatMeasureDataBean;
import com.datalife.datalife.bean.RedEnvelopeEntity;
import com.datalife.datalife.dao.FlashListBean;
import com.datalife.datalife.bean.GeneBean;
import com.datalife.datalife.bean.LastMeasureDataBean;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.dao.NewsInfo;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.http.DownloadService;
import com.datalife.datalife.http.RetrofitDownload;
import com.datalife.datalife.http.RetrofitHelper;
import com.datalife.datalife.http.RetrofitService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observer;

import rx.Observable;

/**
 * Created by LG on 2018/1/31.
 */

public class DataManager {
    private RetrofitService mRetrofitService;
    private DownloadService downloadService;
    public DataManager(Context context){
        this.mRetrofitService = RetrofitHelper.getInstance(context).getServer();
        DataManager1(context);
    }

    public void DataManager1(Context context){
        this.downloadService = RetrofitDownload.getInstance(context).getServer();
    }

    public Observable<DownloadBean> download(){
        return downloadService.download();
    }

    public Observable<ResultBean> register(HashMap<String, String> mHashMap){
        return mRetrofitService.register(mHashMap);
    }

    public Observable<ResultBean<LoginBean,Object>> login(HashMap<String, String> mHashMap){
        return mRetrofitService.login(mHashMap);
    }

    public Observable<ResultBean<LoginBean,Object>> wxlogin(HashMap<String, String> mHashMap){
        return mRetrofitService.wxlogin(mHashMap);
    }

    public Observable<ResultBean<LoginBean,Object>> bindUser(HashMap<String, String> mHashMap){
        return mRetrofitService.bindUser(mHashMap);
    }

    public Observable<ResultBean> loginout(HashMap<String, String> mHashMap){
        return mRetrofitService.loginout(mHashMap);
    }

    public Observable<ResultBean> addFamilyUser(HashMap<String, String> mHashMap){
        return mRetrofitService.addfamilyuser(mHashMap);
    }

    public Observable<ResultBean<ArrayList<FamilyUserInfo>,PageBean>> getFamilyList(HashMap<String,String> mHashMap){
        return mRetrofitService.getfamilylist(mHashMap);
    }

    public Observable<ResultBean> sendDeviceInfo(HashMap<String ,String> mHashMap){
        return mRetrofitService.sendDeviceInfo(mHashMap);
    }

    public Observable<ResultBean<ArrayList<NewsInfo>,Object>> getNewsInfo(HashMap<String ,String> mHashMap){
        return mRetrofitService.getNewsInfo(mHashMap);
    }

    public Observable<ResultBean<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>,PageBean>> getMachineInfo(HashMap<String ,String> mHashMap){
        return mRetrofitService.getMachineInfo(mHashMap);
    }

    public Observable<ResultBean<List<MachineBindMemberBean>,PageBean>> getMachineMemberInfo(HashMap<String ,String> mHashMap){
        return mRetrofitService.getMachineMemberInfo(mHashMap);
    }

    public Observable<ResultBean> putMachineMember(HashMap<String,String> mHashMap){
        return mRetrofitService.putMachineMember(mHashMap);
    }

    public Observable<ResultBean<ArrayList<BtRecordBean>,Object>> getBtRecord(HashMap<String,String> mHashMap){
        return mRetrofitService.getBtRecord(mHashMap);
    }

    public Observable<ResultBean<ArrayList<MeasureRecordBean>,PageBean>> getBtListRecord(HashMap<String,String> mHashMap){
        return mRetrofitService.getBtListRecord(mHashMap);
    }

    public Observable<ResultBean<LastMeasureDataBean<MeasureRecordBean>,Object>> getLastInfo(HashMap<String,String> mHashMap){
        return mRetrofitService.getLastInfo(mHashMap);
    }

    public Observable<ResultBean<LastFatMeasureDataBean<MeasureRecordBean>,Object>> getFatLastInfo(HashMap<String,String> mHashMap){
        return mRetrofitService.getFatLastInfo(mHashMap);
    }

    public Observable<ResultBean<DownloadBean,Object>> update(HashMap<String,String> mHashMap){
        return mRetrofitService.update(mHashMap);
    }

    public Observable<ResultBean<ArrayList<GeneBean>,Object>> sendlist(HashMap<String,String> mHashMap){
        return mRetrofitService.sendlist(mHashMap);
    }

    public Observable<ResultBean<ArrayList<FlashListBean>,Object>> flashList(HashMap<String,String> mHashMap){
        return mRetrofitService.flashList(mHashMap);
    }

    public Observable<ResultBean<ArrayList<String>,Object>> getDayDate(HashMap<String,String> mHashMap){
        return mRetrofitService.getDayDate(mHashMap);
    }

    public Observable<ResultBean<ArrayList<DataTestBean>,Object>> getDataTest(HashMap<String,String> mHashMap){
        return mRetrofitService.getDataTest(mHashMap);
    }

    public Observable<ResultBean<BrushUseCount,Object>> getUseCount(HashMap<String, String> mHashMap){
        return mRetrofitService.getUseCount(mHashMap);
    }

    public Observable<ResultBean<BrushUseCount,Object>> getClearCount(HashMap<String, String> mHashMap){
        return mRetrofitService.getClearCount(mHashMap);
    }

    public Observable<ResultBean<String,Object>> getHistory(HashMap<String, String> hashMaps){
        return mRetrofitService.getHistory(hashMaps);
    }

    public Observable<ResultBean<RedEnvelopeEntity,Object>> getIsHaveRedEnvelope(HashMap<String,String> mHashMap){
        return mRetrofitService.getIsHaveRedEnvelope(mHashMap);
    }

    public Observable<ResultBean<String,Object>> getIsOpenRedEnvelope(HashMap<String,String> mHashMap){
        return mRetrofitService.getIsOpenRedEnvelope(mHashMap);
    }

//    public Observable<>
}
