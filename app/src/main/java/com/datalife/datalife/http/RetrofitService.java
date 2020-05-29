package com.datalife.datalife.http;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by LG on 2018/1/31.
 */

public interface RetrofitService {

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<LoginBean,Object>> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<LoginBean,Object>> wxlogin(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<LoginBean,Object>> bindUser(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> register(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> loginout(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> addfamilyuser(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<FamilyUserInfo>,PageBean>> getfamilylist(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> sendDeviceInfo(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<NewsInfo>,Object>> getNewsInfo(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>,PageBean>> getMachineInfo(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<List<MachineBindMemberBean>,PageBean>> getMachineMemberInfo(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> putMachineMember(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<BtRecordBean>,Object>> getBtRecord(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<MeasureRecordBean>,PageBean>> getBtListRecord(@FieldMap Map<String,String> params);

    //获取健康监测仪最新一次测量数据
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<LastMeasureDataBean<MeasureRecordBean>,Object>> getLastInfo(@FieldMap Map<String,String> params);

    //获取体脂称最新一次测量数据
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<LastFatMeasureDataBean<MeasureRecordBean>,Object>> getFatLastInfo(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<DownloadBean,Object>> update(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<GeneBean>,Object>> sendlist(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<FlashListBean>,Object>> flashList(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<String>,Object>> getDayDate(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<DataTestBean>,Object>> getDataTest(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<BrushUseCount,Object>> getUseCount(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<BrushUseCount,Object>> getClearCount(@FieldMap Map<String,String> params);


    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<String,Object>> getHistory(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<RedEnvelopeEntity,Object>> getIsHaveRedEnvelope(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<String,Object>> getIsOpenRedEnvelope(@FieldMap Map<String, String> params);
}
