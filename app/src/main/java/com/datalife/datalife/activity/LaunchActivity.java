package com.datalife.datalife.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.base.BasePresenter;
import com.datalife.datalife.util.DataLifeUtil;

import java.util.concurrent.TimeUnit;

import lib.linktop.sev.CssServerApi;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by Liguo on 2018/1/12.
 * <p>
 * APP启动页
 * <p>
 * 获取服务器动态IP和端口。
 * <p>
 * 正式服务器需要调用{@link CssServerApi#checkHostPorts},确认成功才能进行服务器交互。
 * 建议写在第一屏（即启动页）， 成功则进入主界面，不成功则提示用户重试。
 * 切記不可在不成功的情況下調用服務器相關接口，雖然大部分接口仍然可用，但
 * 可能會出現部分接口調用不成功的情況，這是由於默認IP或端口與服務器實際IP和端口匹配不準確造成的。
 * <p>
 * 测试服务器不需要调用{@link CssServerApi#checkHostPorts}。
 * <p>
 * 不打算使用Linktop服務器作為數據同步手段的直接忽視就行。
 */
public class LaunchActivity extends BaseActivity {

    /**
     * 是否正式服务器 ，true：需要checkHostPorts，否：不需要
     * 切換正式測試服務器IP時記得在此切換Boolean值。
     */
    private final static boolean isReleaseServer = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initEventAndData() {
        doJump();
    }

    /**
     * 执行跳转页面动作
     * */
    private void doJump() {
        final Observable<Boolean> observable;
        final Observable<Long> timer = Observable.timer(2300L, TimeUnit.MILLISECONDS);
        if (isReleaseServer) {
            //2秒计时的同时确认服务器IP和端口，若2秒内确认服务器IP和端口完成，则等待到2秒时执行下一步。成功跳转界面并关掉本界面，失败则提示
            //若无法2秒确认服务器IP和端口完成，则等到何时确认完成，何时执行下一步。
            //10秒无法响应任何数据，认为是超时。
            final Observable<Boolean> checkHostPorts = CssServerApi.checkHostPorts(getApplication());
            final Observable<Boolean> error = Observable.error(new Throwable("Time out"));
            observable = Observable.zip(timer, checkHostPorts,
                    new Func2<Long, Boolean, Boolean>() {
                        @Override
                        public Boolean call(Long aLong, Boolean aBoolean) {
                            return aLong == 0L && aBoolean;
                        }
                    })
                    .timeout(10000L, TimeUnit.MILLISECONDS, error);
        } else {
            observable = timer.map(new Func1<Long, Boolean>() {
                @Override
                public Boolean call(Long aLong) {
                    return aLong == 0;
                }
            });
        }
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {

                    private String message;
                    private boolean isSuccess = false;

                    @Override
                    public void onCompleted() {
                        if (isSuccess) {
                            Intent intent = null;
                            SharedPreferences sharedPreferences = getSharedPreferences(DataLifeUtil.LOGIN, MODE_PRIVATE);
                            if (sharedPreferences.getBoolean(DataLifeUtil.LOGIN,false) == true){
                               intent = new Intent(getBaseContext(), MainActivity.class);
                            }else{
                               intent = new Intent(getBaseContext(), MainActivity.class);
//                               intent = new Intent(getBaseContext(), LoginActivity.class);
                            }

                            startActivity(intent);
                            finish();
                        } else {
//                            toast(TextUtils.isEmpty(message) ? "IP确认失败" : message);
                            Toast.makeText(getApplicationContext(),"IP确认失败",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        message = e.getMessage();
                        onCompleted();
                    }

                    @Override
                    public void onNext(Boolean o) {
                        isSuccess = o;
                    }
                });
    }

}
