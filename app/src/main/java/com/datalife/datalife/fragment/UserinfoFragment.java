package com.datalife.datalife.fragment;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.widget.RoundImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/1/17.
 */

public class UserinfoFragment extends BaseFragment{

    @BindView(R.id.rl_user_birthday)
    RelativeLayout mBirthLayout;
    @BindView(R.id.tv_userinfo_area)
    TextView mAreaTv;
    @BindView(R.id.tv_userinfo_local)
    TextView mLocalTv;
    @BindView(R.id.tv_userinfo_date)
    TextView mDateTv;
    @BindView(R.id.tv_userinfo_height)
    TextView mHeightTv;
    @BindView(R.id.tv_userinfo_mobile)
    TextView mMobileTv;
    @BindView(R.id.tv_userinfo_name)
    TextView mNameTv;
    @BindView(R.id.tv_userinfo_sex)
    TextView mSexTv;
    @BindView(R.id.ic_userinfo_face)
    RoundImageView mFaceIv;

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_userinfo;
    }

    @Override
    protected void initEventAndData() {
        String str = DataLifeUtil.getData(getActivity());
        try {
            LoginBean loginBean = (LoginBean)DataLifeUtil.deSerialization(str);

            mNameTv.setText(loginBean.getUser_name());
            mMobileTv.setText(loginBean.getMobile());
            mDateTv.setText(loginBean.getLastLogin_date());
            Picasso.with(mFaceIv.getContext()).load(loginBean.getHeadPic()).into(mFaceIv);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    @OnClick({R.id.rl_user_birthday})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_user_birthday:

                break;

        }
    }


    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }
}
