package com.datalife.datalife.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.bean.DownloadBean;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.SettingContract;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.http.callback.HttpResultCallBack;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;

import java.util.HashMap;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/1/31.
 */

public class SettingPresenter extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private SettingContract.SettingView settingView;


    public SettingPresenter(Context mContext) {
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
        settingView = (SettingContract.SettingView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void loginout(String sessionid){
        HashMap<String, String> params = new HashMap<>();
        params.put("fun", "Logout");
        params.put("cls", "userbase");
        params.put("SessionId", sessionid);
        mCompositeSubscription.add(manager.loginout(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {
                    @Override
                    public void onResponse(Object o, String status) {
                        DBManager.getInstance(mContext).deleteWxInfo();
                        settingView.loginoutSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        settingView.loginoutFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean resultBean) {
                        super.onNext(resultBean);
                    }
                })
        );
    }

    public void update(String sessionid){
        HashMap<String, String> params = new HashMap<>();
        params.put("fun", "Update");
        params.put("cls", "Home");
        params.put("SessionId", sessionid);
        mCompositeSubscription.add(manager.update(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<DownloadBean , Object>() {

                    @Override
                    public void onResponse(DownloadBean downloadBean, String status) {
                        settingView.updateSuccess(downloadBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        settingView.updateFail(msg);
                    }


                    @Override
                    public void onNext(ResultBean resultBean) {
                        super.onNext(resultBean);
                    }
                })
        );

    }


    public void download(){
        mCompositeSubscription.add(manager.download()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadBean>() {
                    @Override
                    public void onCompleted() {
//                        Toast.makeText(mContext,"",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Toast.makeText(mContext,"",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(DownloadBean downloadBean) {

                        Toast.makeText(mContext,downloadBean.toString(),Toast.LENGTH_LONG).show();
                    }
                })
        );
    };

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
                        settingView.bindSuccess();
                    }
                    @Override
                    public void onErr(String msg, String status) {
                        settingView.bindFail(msg);
                    }
                    @Override
                    public void onNext(ResultBean<LoginBean,Object> ResultBean) {
                        super.onNext(ResultBean);
                    }
                })
        );
    }
}
