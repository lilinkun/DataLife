package com.datalife.datalife.model;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.activity.RegisterActivity;
import com.datalife.datalife.base.BaseModel;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.exception.ApiException;
import com.datalife.datalife.subscriber.CommonSubscriber;
import com.datalife.datalife.transformer.CommonTransformer;

/**
 * Created by LG on 2018/1/15.
 */

public class LoginModel extends BaseModel {
    private boolean isLogin = false;

    public boolean login(String account, String pwd,String SessionId, final InfoHint infoHint){

        if (infoHint == null)
            throw new RuntimeException("InfoHint不能为空");

        /*httpService.login(account,pwd).compose(new CommonTransformer<LoginBean>()).subscribe(new CommonSubscriber<LoginBean>(ProApplication.getmContext()) {
            @Override
            public void onNext(LoginBean loginBean) {
                isLogin = true;
                infoHint.successInfo(loginBean.getToken());
            }

            protected void onError(ApiException e) {
                super.onError(e);
                isLogin = false;
                infoHint.failInfo(e.message);
            }
        });*/
        return isLogin;
    }

    public void register(Context context){
        Intent intent = new Intent();
        intent.setClass(context, RegisterActivity.class);
        context.startActivity(intent);

    }


    //通过接口产生信息回调
    public interface InfoHint {
        void successInfo(String str);

        void failInfo(String str);

    }

}
