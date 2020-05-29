package com.datalife.datalife.base;

import com.datalife.datalife.http.Http;
import com.datalife.datalife.http.HttpService;
import com.datalife.datalife.mvp.IModel;

/**
 * Created by LG on 2018/1/15.
 */

public class BaseModel implements IModel{
    protected static HttpService httpService;

    //初始化httpService
    static {
        httpService = Http.getHttpService();
    }

}