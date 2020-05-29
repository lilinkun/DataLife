package com.datalife.datalife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.widget.CommonLayout;

import aicare.net.cn.iweightlibrary.entity.BodyFatData;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/5/3.
 */

public class FatTestHistoryActivity extends BaseActivity {

    @BindView(R.id.commonlayout)
    CommonLayout mMeasureLayout;
    @BindView(R.id.tv_test_time)
    TextView mTvTestTime;

    private BodyFatData bodyFatData = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_testfat;
    }

    @Override
    protected void initEventAndData() {
        Bundle bundle = getIntent().getBundleExtra(DataLifeUtil.BUNDLEMEMBER);
        if (bundle != null){
            bodyFatData = new BodyFatData();
            MeasureRecordBean measureRecordBean = (MeasureRecordBean)bundle.getSerializable(DataLifeUtil.BUNDLEMEASURE);
            FamilyUserInfo familyUserInfo = (FamilyUserInfo)bundle.getSerializable(DataLifeUtil.BUNDLEMACHINEMEMBER);
            if (measureRecordBean != null) {
                mTvTestTime.setText(measureRecordBean.getCreateDate());
                bodyFatData.setWeight(Double.parseDouble(measureRecordBean.getCheckValue3()));
                bodyFatData.setBmi(Double.parseDouble(measureRecordBean.getCheckValue4()));
                bodyFatData.setBfr(Double.parseDouble(measureRecordBean.getCheckValue5()));
                bodyFatData.setRom(Double.parseDouble(measureRecordBean.getCheckValue6()));
                bodyFatData.setVwc(Double.parseDouble(measureRecordBean.getCheckValue7()));
                bodyFatData.setDbw(Double.parseDouble(measureRecordBean.getCheckValue8()));
                bodyFatData.setBm(Double.parseDouble(measureRecordBean.getCheckValue9()));
                bodyFatData.setSex(Integer.valueOf(familyUserInfo.getMember_Sex()));
                bodyFatData.setHeight((int)(familyUserInfo.getMember_Stature()));
//                bodyFatData.setWeight(familyUserInfo.getMember_Weight());
                bodyFatData.setAge(DataLifeUtil.getAgeByBirthDay(familyUserInfo.getMember_DateOfBirth()));
                if (measureRecordBean.getCheckValue10().equals("0.0")) {
                    bodyFatData.setUvi(0);
                } else {
                    double v = Double.parseDouble(measureRecordBean.getCheckValue10());
                    bodyFatData.setUvi((int)v);
                }
                bodyFatData.setBmr(Double.parseDouble(measureRecordBean.getCheckValue11()));
                mMeasureLayout.onGetFatData(bodyFatData);
            }
        }
    }

    @OnClick({R.id.iv_head_left})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_head_left:
                finish();
                break;
        }
    }

}
