package com.datalife.datalife.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.R;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.RegisterContract;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;
import com.datalife.datalife.util.PhoneFormatCheckUtils;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/1/17.
 */

public class RegisterPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private RegisterContract.View mRegisterView;

    public RegisterPresenter (Context mContext){
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
        mRegisterView = (RegisterContract.View) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void register(String username,String name, String psw, String mobile, String code, String sessionId,final ProgressDialog progressDialog,String openid,String unionid){

        if(username == null || username.isEmpty()){
            mRegisterView.showPromptMessage(R.string.prompt_login_name_not_empty);
            return;
        }

        if(username.toString().trim().length() > 15 || username.toString().trim().length() < 2){
            mRegisterView.showPromptMessage(R.string.prompt_login_name_not_allow);
            return;
        }

        if(username == null || username.isEmpty()){
            mRegisterView.showPromptMessage(R.string.prompt_register_name_not_allow);
            return;
        }

        if(mobile == null || mobile.isEmpty() || !PhoneFormatCheckUtils.isChinaPhoneLegal(mobile)){
            mRegisterView.showPromptMessage(R.string.prompt_phone_number_invalid);
            return;
        }

        if(code == null || code.isEmpty()){
            mRegisterView.showPromptMessage(R.string.prompt_verification_code_not_empty);
            return;
        }

        if(psw == null || psw.isEmpty()){
            mRegisterView.showPromptMessage(R.string.prompt_login_passwrod_not_empty);
            return;
        }

        if(psw.toString().trim().length() > 20 || psw.toString().trim().length() < 6){
            mRegisterView.showPromptMessage(R.string.prompt_passwrod_not_allow);
            return;
        }


        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "userbase");
        params.put("fun", "Register");
        params.put("user_name", username);
        params.put("Name",name);
        params.put("user_password", psw);
        params.put("mobile", mobile);
        params.put("Code", code);
        params.put("SessionId", sessionId);
        params.put("openid", openid);
        params.put("unionid", unionid);
        mCompositeSubscription.add(manager.register(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack(){

                    @Override
                    public void onResponse(Object o, String status) {
                        mRegisterView.onSuccess();
                        progressDialog.show();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        mRegisterView.onError(msg);
                    }

                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }

                })
        );
    }

    public void SendSms(String mobile,String sessionId){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "SendSms");
        params.put("fun", "RegisteredSmsCode");
        params.put("mobile", mobile);
        params.put("SessionId", sessionId);
        mCompositeSubscription.add(manager.register(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack(){

                    @Override
                    public void onResponse(Object o, String status) {
                        mRegisterView.sendCodeSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        mRegisterView.sendFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }

                })
        );
    }

    /**
     * 登陆
     */
    public void login(String username,String psw,String sessionId){
        if(username == null || username.isEmpty()){
            mRegisterView.showPromptMessage(R.string.prompt_login_name_not_empty);
            return;
        }

        if(psw == null || psw.isEmpty()){
            mRegisterView.showPromptMessage(R.string.prompt_login_passwrod_not_empty);
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
                        mRegisterView.loginSuccess(loginBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        mRegisterView.loginFail(msg);
                    }


                    @Override
                    public void onNext(ResultBean<LoginBean,Object> ResultBean) {
                        super.onNext(ResultBean);
                    }
                })
        );
    }
}
