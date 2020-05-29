package com.datalife.datalife.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.BlueToothDevActivity;
import com.datalife.datalife.activity.DataTestActivity;
import com.datalife.datalife.activity.MyRedEnvelopeListActivity;
import com.datalife.datalife.activity.RedEnvelopeActivity;
import com.datalife.datalife.activity.SettingActivity;
import com.datalife.datalife.activity.SimplebackActivity;
import com.datalife.datalife.activity.ToothDevActivity;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.SimpleBackPage;
import com.datalife.datalife.interf.OnBackListener;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.Eyes;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.widget.RoundImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/12.
 */

public class MeFragment extends BaseFragment {

    @BindView(R.id.tv_user_name)
    TextView mUserNameTv;
    @BindView(R.id.tv_user_tel)
    TextView mTelTv;
    @BindView(R.id.ic_user)
    RoundImageView roundImageView;

    OnBackListener onBackListener = null;

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initEventAndData() {
        Eyes.setStatusBarColor(getActivity(), ContextCompat.getColor(getActivity(), R.color.bg_toolbar_title));
//        Eyes.translucentStatusBar(getActivity());
        onBackListener = (OnBackListener) getActivity();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(DataLifeUtil.LOGIN,Context.MODE_PRIVATE);

        if (sharedPreferences != null) {
            String str = sharedPreferences.getString("logininfo", "");
            try {
                LoginBean loginBean = (LoginBean)DataLifeUtil.deSerialization(str);

                mUserNameTv.setText(loginBean.getUser_name());
                mTelTv.setText(loginBean.getMobile());
                if (loginBean != null && loginBean.getHeadPic() != null){
                    if (loginBean.getHeadPic() != ""){
                        Picasso.with(roundImageView.getContext()).load(loginBean.getHeadPic()).into(roundImageView);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick({R.id.rl_userinfo,R.id.rl_shoppingcar,R.id.rl_history,R.id.rl_equipmanager,R.id.rl_famalymanager,R.id.me_setting,R.id.ll_ele_report,R.id.rl_datetest,R.id.rl_red_envelope})
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.rl_userinfo:

//                UIHelper.showSimpleBackForResult(getActivity(),SimplebackActivity.RESULT_USERINFO,SimpleBackPage.USERINFO);

                break;

            case R.id.rl_equipmanager:

//                Bundle bundle = new Bundle();
//                bundle.putString("page","page");
//                UIHelper.launcherForResultBundle(getActivity(), HealthMonitorActivity.class, 322,bundle);

                Intent intent = new Intent();
                intent.setClass(getActivity(), BlueToothDevActivity.class);
                startActivity(intent);

                break;


            case R.id.me_setting:

//              UIHelper.showSimpleBackForResult(getActivity(),SimplebackActivity.RESULT_SETTING,SimpleBackPage.SYSTEMSETTING);

                UIHelper.launcherForResult(getActivity(), SettingActivity.class, IDatalifeConstant.SETTINGREQUESTCODE);

                break;

            case R.id.rl_famalymanager:

                UIHelper.showSimpleBackForResult(getActivity(),SimplebackActivity.RESULT_MEMBER,SimpleBackPage.FAMILYMANAGER);
                break;

            case R.id.rl_bindmember:

                UIHelper.showSimpleBackForResult(getActivity(),SimplebackActivity.RESULT_BINDMEMBER,SimpleBackPage.BINDMEMBER);
                break;

            case R.id.ll_ele_report:

                UIHelper.showSimpleBackForResult(getActivity(),SimplebackActivity.RESULT_REPORT,SimpleBackPage.REPORT);
                break;

            case R.id.rl_history:

                UIHelper.launcherForResult(getActivity(), ToothDevActivity.class, 333);

                break;

            case R.id.rl_datetest:
                UIHelper.launcherForResult(getActivity(), DataTestActivity.class, 444);
                break;

            case R.id.rl_red_envelope:
                UIHelper.launcherForResult(getActivity(), MyRedEnvelopeListActivity.class, 454);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == SimplebackActivity.RESULT_SETTING){
                    getActivity().finish();
            }

            if (requestCode == IDatalifeConstant.INTENT_LOGIN){
                onBackListener.onBack();
            }

        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }
}
