package com.datalife.datalife.contract;

import android.content.Context;

import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.mvp.IView;

import javax.crypto.spec.IvParameterSpec;

/**
 * Created by LG on 2018/1/15.
 * 契约类,定义登录用到的一些接口方法
 */

public interface LoginContract {

    public interface LoginView extends IView{
        String getAccount();
        String getPwd();
        void loginSuccess(LoginBean mRegisterBean);
        void loginFail(String failMsg);
        void wxLoginSuccess();
        void wxLoginFail(String msg);
    }

    public interface LoginPresenter{
        void login(String name,String pwd,String SessionId);
        void registerText(Context context);
    }

}
