package com.datalife.datalife.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.RegisterActivity;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.LoginContract;
import com.datalife.datalife.contract.RegisterContract;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.PhoneFormatCheckUtils;

import java.util.HashMap;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/1/31.
 */

public class LoginPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private ResultBean<LoginBean,Object> mLoginBean;
    private LoginContract.LoginView mLoginView;

    public LoginPresenter(Context mContext){
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
        mLoginView = (LoginContract.LoginView)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    /**
     * 注册
     * @param mContext
     */
    public void registerText(Activity mContext,String sessionid,String openid,String unionid){
        Intent intent = new Intent();
        intent.setClass(mContext, RegisterActivity.class);
        intent.putExtra(DataLifeUtil.SESSIONID, sessionid);
        intent.putExtra(DataLifeUtil.OPENID, openid);
        intent.putExtra(DataLifeUtil.UNIONID, unionid);
        mContext.startActivityForResult(intent, IDatalifeConstant.RESULT_REGISTER);
    }

    /**
     * 登陆
     */
    public void login(String username,String psw,String sessionId){
        if(username == null || username.isEmpty()){
            mLoginView.showPromptMessage(R.string.prompt_login_name_not_empty);
            return;
        }

        if(psw == null || psw.isEmpty()){
            mLoginView.showPromptMessage(R.string.prompt_login_passwrod_not_empty);
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "userbase");
        params.put("fun", "Login");
        params.put("UserName", username);
        params.put("PassWord", psw);
        params.put("SessionId", sessionId);
        params.put("MobileType","android");
        mCompositeSubscription.add(manager.login(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<LoginBean,Object>() {
                    @Override
                    public void onResponse(LoginBean loginBean, String status) {
                        mLoginView.loginSuccess(loginBean);
                    }
                    @Override
                    public void onErr(String msg, String status) {
                        mLoginView.loginFail(msg);
                    }
                    @Override
                    public void onNext(ResultBean<LoginBean,Object> ResultBean) {
                        super.onNext(ResultBean);
                    }
                })
        );
    }

    /**
     * 微信登陆
     */
    public void wxlogin(String openid,String unionid,String MobileType,String sessionId){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "userbase");
        params.put("fun", "WxLogin");
        params.put("openid", openid);
        params.put("unionid",unionid);
        params.put("MobileType", MobileType);
        params.put("SessionId", sessionId);
        params.put("MobileType","android");
        mCompositeSubscription.add(manager.wxlogin(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<LoginBean,Object>() {
                    @Override
                    public void onResponse(LoginBean loginBean, String status) {
                        mLoginView.loginSuccess(loginBean);
                    }
                    @Override
                    public void onErr(String msg, String status) {
                        mLoginView.wxLoginFail(msg);
                    }
                    @Override
                    public void onNext(ResultBean<LoginBean,Object> ResultBean) {
                        super.onNext(ResultBean);
                    }
                })
        );
    }

    /**
     * 绑定账号
     */
    public void bindUser(String user_name,String user_password,String openid,String unionid,String sessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "userbase");
        params.put("fun", "UserBindWx");
        params.put("user_name", user_name);
        params.put("openid", openid);
        params.put("unionid",unionid);
        params.put("user_password", user_password);
        params.put("SessionId", sessionId);
        params.put("MobileType","android");
        mCompositeSubscription.add(manager.bindUser(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<LoginBean,Object>() {
                    @Override
                    public void onResponse(LoginBean loginBean, String status) {
                        mLoginView.loginSuccess(loginBean);
                    }
                    @Override
                    public void onErr(String msg, String status) {
                        mLoginView.loginFail(msg);
                    }
                    @Override
                    public void onNext(ResultBean<LoginBean,Object> ResultBean) {
                        super.onNext(ResultBean);
                    }
                })
        );
    }

}
