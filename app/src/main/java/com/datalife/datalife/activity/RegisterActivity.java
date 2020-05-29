package com.datalife.datalife.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.RegisterUserInfo;
import com.datalife.datalife.contract.RegisterContract;
import com.datalife.datalife.presenter.RegisterPresenter;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.PhoneFormatCheckUtils;
import com.datalife.datalife.util.UToast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by LG on 2018/1/16.
 */

public class RegisterActivity extends BaseActivity implements RegisterContract.View{

    @BindView(R.id.iv_leftback)
    ImageView mLeftBack;
    @BindView(R.id.et_register_account)
    EditText mRegisterAccount;
    @BindView(R.id.et_register_name)
    EditText mRegisterName;
    @BindView(R.id.et_register_code)
    EditText mRegisterCode;
    @BindView(R.id.et_register_phone)
    EditText mRegisterPhone;
    @BindView(R.id.et_register_psw)
    EditText mRegisterPsw;
    @BindView(R.id.ic_psd)
    ImageView iv_psd;
    @BindView(R.id.tv_send_vcode)
    TextView sendCode;

    private boolean isClose = true;

    ProgressDialog progressDialog = null;

    private RegisterPresenter mRegisterPresenter = new RegisterPresenter(this);

    private RegisterUserInfo registerUserInfo = new RegisterUserInfo();
    private String username;
    private String psw;
    String sessionid = "";
    String openid = "";
    String unionid = "";
    MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initEventAndData() {

        if (getIntent() != null){
            sessionid  = getIntent().getStringExtra(DataLifeUtil.SESSIONID);
            openid = getIntent().getStringExtra(DataLifeUtil.OPENID);
            unionid = getIntent().getStringExtra(DataLifeUtil.UNIONID);
        }else{
            sessionid = DeviceData.getUniqueId(this);
        }

        mRegisterPresenter.onCreate();
        mRegisterPresenter.attachView(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.login_loading));
        progressDialog.setCancelable(true);
    }

    @OnClick({R.id.tv_usage_protocol,R.id.iv_leftback,R.id.tv_send_vcode,R.id.btn_register_over,R.id.ic_psd})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_usage_protocol:
//                UToast.show(this,);
                Intent intent = new Intent();
                intent.setClass(this,WebViewActivity.class);
                intent.putExtra("url", DataLifeUtil.USAGEPROTOCOL_URL);
                intent.putExtra("type","register");
                startActivity(intent);
                break;

            case R.id.iv_leftback:
                finish();
                break;

            case R.id.tv_send_vcode:

                if(mRegisterPhone == null || mRegisterPhone.getText().toString().isEmpty() || !PhoneFormatCheckUtils.isChinaPhoneLegal(mRegisterPhone.getText().toString())){
                    toast(R.string.prompt_phone_number_invalid);
                    return;
                }

                mRegisterPresenter.SendSms(mRegisterPhone.getText().toString(),sessionid);
                sendCode.setBackgroundResource(R.mipmap.ic_message_unclick_vcode);
                myCountDownTimer.start();
                break;

            case R.id.btn_register_over:

                username = mRegisterAccount.getText().toString();
                psw = mRegisterPsw.getText().toString();
                String mobile = mRegisterPhone.getText().toString();
                String Code = mRegisterCode.getText().toString();
                String name = mRegisterName.getText().toString();

                mRegisterPresenter.register(username,name,psw,mobile,Code, sessionid,progressDialog,openid,unionid);

//                progressDialog.show();

                break;

            case R.id.ic_psd:

                if (isClose){
                    iv_psd.setImageResource(R.mipmap.ic_open_psd);
                    mRegisterPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mRegisterPsw.setSelection(mRegisterPsw.getText().toString().length());
                    isClose = false;
                }else{
                    iv_psd.setImageResource(R.mipmap.ic_close_psd);
                    mRegisterPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mRegisterPsw.setSelection(mRegisterPsw.getText().toString().length());
                    isClose = true;
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRegisterPresenter.onStop();
    }

    @Override
    public void showPromptMessage(String message) {
        UToast.show(this,message);
    }

    @Override
    public void showPromptMessage(int resId) {
        UToast.show(this,resId);
    }

    @Override
    public void onSuccess() {
        mRegisterPresenter.login(username,psw,sessionid);
    }

    @Override
    public void onError(String result) {
        Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendCodeSuccess() {
        toast("发送验证码成功");
    }

    @Override
    public void sendFail(String str) {
        toast(str);
    }

    @Override
    public void loginSuccess(LoginBean loginBean) {
        String datalife = null;
        try {
            datalife = DataLifeUtil.serialize(loginBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(DataLifeUtil.LOGIN, MODE_PRIVATE);
        sharedPreferences.edit().putString("sessionid",sessionid).putBoolean(DataLifeUtil.LOGIN,true).putString("logininfo",datalife).commit();
        progressDialog.dismiss();

        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFail(String str) {
        toast(str + "");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            sendCode.setClickable(false);
            sendCode.setText(l/1000+"s");
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            sendCode.setText(R.string.recapture_vcode);
            //设置可点击
            sendCode.setClickable(true);

            sendCode.setBackgroundResource(R.mipmap.ic_message_click_vcode);
        }
    }
}
