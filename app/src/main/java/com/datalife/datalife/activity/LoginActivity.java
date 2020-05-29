package com.datalife.datalife.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.RegisterUserInfo;
import com.datalife.datalife.contract.LoginContract;
import com.datalife.datalife.dao.WxUserInfo;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.presenter.LoginPresenter;
import com.datalife.datalife.util.Code;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.widget.RoundImageView;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/1/15.
 */

public class LoginActivity extends BaseActivity implements LoginContract.LoginView {

    @BindView(R.id.et_account)//账号
            EditText inputEmail;
    @BindView(R.id.et_psd)//密码
            EditText inputPassword;
    @BindView(R.id.ic_vcode)//验证码图片
            ImageView iv_vcode;
    @BindView(R.id.et_vcode)
            EditText et_vcode;
    @BindView(R.id.ll_vcode)
            LinearLayout ll_vcode;//验证码layout
    @BindView(R.id.ic_psd)
            ImageView iv_psd;
    @BindView(R.id.btn_login)
            Button loginButton;
    @BindView(R.id.login_layout)
            LinearLayout loginlayout;
    @BindView(R.id.ic_login_back)
            ImageView mLoginBack;
    @BindView(R.id.line_login_account)
            View lineAccount;
    @BindView(R.id.line_login_psd)
            View linePsd;
    @BindView(R.id.line_login_vcode)
            View lineVcode;
    @BindView(R.id.tv_login_account)
            TextView mTvLoginAccout;
    @BindView(R.id.tv_login_psd)
            TextView mTvLoginPsd;
    @BindView(R.id.iv_wx_login)
            ImageView wxLoginImage;
    @BindView(R.id.tv_hello)
            TextView wxTvHello;
    @BindView(R.id.tv_content)
            TextView wxTvContent;
    @BindView(R.id.tv_register_wx)
            TextView tv_wx_register;
    @BindView(R.id.tv_register)
            TextView tv_register;
    @BindView(R.id.ll_third_login)
            LinearLayout llThirdLogin;
    @BindView(R.id.riv_head)
            RoundImageView mRivHead;

    LoginPresenter mLoginPresenter = new LoginPresenter(this);

    private boolean isClose = true;

    private String realCode;

    private String SESSIONID = "";
    private ProgressDialog progressDialog;

    IWXAPI iwxapi = null;
    private WxUserInfo wxUserInfo = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initEventAndData() {
        mLoginPresenter.onCreate();
        mLoginPresenter.attachView(this);

        iwxapi = WXAPIFactory.createWXAPI(this,IDatalifeConstant.APP_ID,true);
        iwxapi.registerApp(IDatalifeConstant.APP_ID);

        if (getIntent().getBooleanExtra("restart",false)){
            SharedPreferences sharedPreferences = getSharedPreferences(DataLifeUtil.LOGIN, Context.MODE_PRIVATE);
            if (sharedPreferences != null && !sharedPreferences.getString("account","").equals("")) {
                loginlayout.setVisibility(View.GONE);
                mLoginPresenter.login(sharedPreferences.getString("account", ""), sharedPreferences.getString("password", ""), DeviceData.getUniqueId(this));
            }
        }

        iv_vcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.login_loading));
        progressDialog.setCancelable(true);

        SESSIONID = DeviceData.getUniqueId(this);
        if (MainActivity.loginNum != 0){
            ll_vcode.setVisibility(View.VISIBLE);
        }

        inputPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                linePsd.setBackgroundColor(getResources().getColor(R.color.bg_toolbar_title));
                lineAccount.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                lineVcode.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                mTvLoginPsd.setVisibility(View.VISIBLE);
                inputPassword.setHint("");
                inputEmail.setHint(R.string.hint_login_name);
                if (inputEmail.getText().toString().trim().length() == 0){
                    mTvLoginAccout.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(inputPassword.getText()) && !TextUtils.isEmpty(inputEmail.getText())) {
                    loginButton.setBackgroundResource(R.mipmap.ic_login_click);
                }else {
                    loginButton.setBackgroundResource(R.mipmap.ic_login_unclick);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputPassword.setHint(R.string.hint_psd_name);
            }
        });

        inputEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lineAccount.setBackgroundColor(getResources().getColor(R.color.bg_toolbar_title));
                linePsd.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                lineVcode.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                mTvLoginAccout.setVisibility(View.VISIBLE);
                inputEmail.setHint("");
                inputPassword.setHint(R.string.hint_psd_name);
                if (inputPassword.getText().toString().trim().length() == 0){
                    mTvLoginPsd.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lineAccount.setBackgroundColor(getResources().getColor(R.color.bg_toolbar_title));
                linePsd.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                lineVcode.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                mTvLoginAccout.setVisibility(View.VISIBLE);
//                inputEmail.setHint(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(inputPassword.getText()) && !TextUtils.isEmpty(inputEmail.getText())) {
                    loginButton.setBackgroundResource(R.mipmap.ic_login_click);
                }else {
                    loginButton.setBackgroundResource(R.mipmap.ic_login_unclick);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputEmail.setHint(R.string.hint_login_name);
            }
        });

        et_vcode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lineVcode.setBackgroundColor(getResources().getColor(R.color.bg_toolbar_title));
                lineAccount.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                linePsd.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                if (inputEmail.getText().toString().trim().length() == 0){
                    mTvLoginAccout.setVisibility(View.INVISIBLE);
                }
                if (inputPassword.getText().toString().trim().length() == 0){
                    mTvLoginPsd.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

    }

    @OnClick({R.id.btn_login,R.id.tv_register,R.id.ic_vcode,R.id.ic_psd,R.id.ic_login_back,R.id.iv_wx_login,R.id.tv_register_wx,R.id.et_vcode})
    protected void otherViewClick(View view) {
        switch (view.getId()){

            case R.id.tv_register:
                if (wxUserInfo != null) {
                    mLoginPresenter.registerText(this, SESSIONID, wxUserInfo.getOpenid(),wxUserInfo.getUnionid());
                }else {
                    mLoginPresenter.registerText(this, SESSIONID, "","");
                }
                break;

            case R.id.tv_register_wx:
                if (wxUserInfo != null) {
                    mLoginPresenter.registerText(this, SESSIONID, wxUserInfo.getOpenid(),wxUserInfo.getUnionid());
                }else {
                    mLoginPresenter.registerText(this, SESSIONID, "","");
                }
                break;
            case R.id.btn_login:

                if (loginButton.getText().toString().equals(getResources().getString(R.string.bind))){
                    if (wxUserInfo != null){
                        mLoginPresenter.bindUser(getAccount(), getPwd(),wxUserInfo.getOpenid(),wxUserInfo.getUnionid(), SESSIONID);
                    }
                }else {
                    if (MainActivity.loginNum >= 1) {
                        String phoneCode = et_vcode.getText().toString().toLowerCase();
                        if (phoneCode.trim().length() == 0) {
                            toast(R.string.input_vcode);
                        } else if (phoneCode.equals(realCode)) {
                            progressDialog.show();
                            mLoginPresenter.login(getAccount(), getPwd(), SESSIONID);
                        } else {
                            iv_vcode.setImageBitmap(Code.getInstance().createBitmap());
                            realCode = Code.getInstance().getCode().toLowerCase();
                            toast(R.string.toast_vcode_error);
                        }
                    } else {
                        progressDialog.show();
                        mLoginPresenter.login(getAccount(), getPwd(), SESSIONID);
                    }
                }

                break;

            case R.id.ic_vcode:
                iv_vcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;

            case R.id.ic_psd:
                if (isClose){
                    iv_psd.setImageResource(R.mipmap.ic_open_psd);
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    inputPassword.setSelection(inputPassword.getText().toString().length());
                    isClose = false;
                }else{
                    iv_psd.setImageResource(R.mipmap.ic_close_psd);
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    inputPassword.setSelection(inputPassword.getText().toString().length());
                    isClose = true;
                }
                break;

            case R.id.ic_login_back:
                Intent intent = new Intent();
                intent.setClass(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.iv_wx_login:

                if (!iwxapi.isWXAppInstalled()) {
                    toast("您没有安装微信");
                } else {
                    DBManager.getInstance(this).deleteWxInfo();
                    final SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_微信登录";
                    iwxapi.sendReq(req);
                }

                break;

            case R.id.et_vcode:

                break;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("LG","onrestart");

        wxUserInfo = DBManager.getInstance(this).queryWxInfo();
        if (wxUserInfo != null && wxUserInfo.getOpenid() != null && wxUserInfo.getOpenid().trim().length() > 0){
//            mLoginPresenter.onCreate();
//            mLoginPresenter.attachView(this);
            mLoginPresenter.wxlogin(wxUserInfo.getOpenid(),wxUserInfo.getUnionid(),"android", ProApplication.SESSIONID);
//            wxLoginFail("sdfsfd");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.onStop();
    }

    @Override
    public String getPwd() {
        return inputPassword.getText().toString().trim();
    }

    @Override
    public void loginSuccess(LoginBean mLoginBean) {
        LoginBean loginBean = mLoginBean;
        String datalife = null;
        try {
            datalife = DataLifeUtil.serialize(loginBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(DataLifeUtil.LOGIN, MODE_PRIVATE);
        sharedPreferences.edit().putString("sessionid",SESSIONID).putBoolean(DataLifeUtil.LOGIN,true).putString("logininfo",datalife).putString("account",getAccount()).putString("password",getPwd()).commit();

        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        finish();
        progressDialog.dismiss();

    }

    @Override
    public String getAccount() {
        return inputEmail.getText().toString().trim();
    }

    @Override
    public void loginFail(String failMsg) {
        toast(failMsg + "");
        MainActivity.loginNum++;
        ll_vcode.setVisibility(View.VISIBLE);
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void wxLoginSuccess() {

    }

    @Override
    public void wxLoginFail(String msg) {
        toast(msg);

        wxTvHello.setText(R.string.bind_user);
        wxTvContent.setText(R.string.bind_content);
        loginButton.setText(R.string.bind);
        tv_wx_register.setVisibility(View.VISIBLE);
        tv_register.setVisibility(View.GONE);
        llThirdLogin.setVisibility(View.GONE);
        wxLoginImage.setVisibility(View.GONE);
        mRivHead.setVisibility(View.GONE);
    }

    public boolean checkNull() {
        boolean isNull = false;
        if (TextUtils.isEmpty(getAccount())) {
            inputEmail.setError(getResources().getString(R.string.account_is_null));
            MainActivity.loginNum++;
            isNull = true;
        } else if (TextUtils.isEmpty(getPwd())) {
            inputPassword.setError(getResources().getString(R.string.psw_is_null));
            MainActivity.loginNum++;
            isNull = true;
        }
        return isNull;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
//            setResult(RESULT_OK);
//            finish();
            Intent intent = new Intent();
            intent.setClass(this,MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            Log.e("111111111requestCode:" , resultCode+"");
            if (requestCode == IDatalifeConstant.RESULT_REGISTER){
                RegisterUserInfo registerUserInfo = (RegisterUserInfo) data.getSerializableExtra(IDatalifeConstant.REGISTERBEAN);
                mLoginPresenter.login(registerUserInfo.getUsername(), registerUserInfo.getPsw(),SESSIONID);
            }
        }
    }

    @Override
    public void showPromptMessage(int resId) {
        toast(resId);
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void showPromptMessage(String message) {
        toast(message);
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
