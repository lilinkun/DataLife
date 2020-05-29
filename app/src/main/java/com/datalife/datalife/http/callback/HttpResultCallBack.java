package com.datalife.datalife.http.callback;

import android.util.Log;

import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.http.factory.ResultException;
import com.datalife.datalife.util.IDatalifeConstant;
import com.google.gson.Gson;

import rx.Subscriber;

public abstract class HttpResultCallBack<M,T> extends Subscriber<ResultBean<M,T>> {

    /**
     * 请求返回
     */
    public abstract void onResponse(M m, String status);
    public abstract void onErr(String msg, String status);

    /**
     * 请求完成
     */
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if(e != null){
            if(e instanceof ResultException){
                ResultException err = (ResultException) e;
                onErr(err.getErrMsg(), IDatalifeConstant.RESULT_FAIL);
            }else{
                onErr("网络异常，请检查网络", IDatalifeConstant.RESULT_FAIL);
                Log.d("HttpResultCallBack","解析失败==：" + e.getMessage());
            }
        }
        onCompleted();
    }

    /**
     * Http请求失败
     */
    private void onHttpFail(String msg, String status){
        onErr(msg, status);
    }

    @Override
    public void onNext(ResultBean<M,T> result) {
        String jsonResponse = new Gson().toJson(result);
        Log.d("HttpResultCallBack", "返回ok==：" + jsonResponse);
        if (result.getStatus().equals(IDatalifeConstant.RESULT_SUCCESS)) {
            onResponse(result.getData(), IDatalifeConstant.RESULT_SUCCESS);
        } else {
            onHttpFail(result.getDesc(), IDatalifeConstant.RESULT_FAIL + result.getCode());
        }
    }
}
