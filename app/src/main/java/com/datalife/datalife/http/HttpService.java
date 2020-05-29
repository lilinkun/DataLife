package com.datalife.datalife.http;


import com.datalife.datalife.base.BaseHttpResult;
import com.datalife.datalife.bean.DataEntity;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.ResultBean;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by LG on 2018/1/15.
 * 网络请求的接口都在这里
 */

public interface HttpService {

    /*@FormUrlEncoded
    @POST("demo/login")
    Observable<BaseHttpResult<LoginBean>> login(@Field("userName") String username, @Field
            ("passWord") String pwd);


    /**
     * 注册
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<RegisterBean> register(@FieldMap Map<String, String> params);
}
