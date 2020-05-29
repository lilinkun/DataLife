package com.datalife.datalife.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.bean.DataTestBean;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DefaultPicEnum;
import com.datalife.datalife.widget.RoundImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/8/21.
 */

public class DatatestDetailActivity extends BaseActivity {

    @BindView(R.id.tv_type_name)
    TextView mTypeName;
    @BindView(R.id.tv_datatest_date)
    TextView mDataTestDate;
    @BindView(R.id.datatest_message)
    TextView mMessage;
    @BindView(R.id.riv_face)
    RoundImageView roundImageView;
    @BindView(R.id.tv_name)
    TextView mTvName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_datatest_details;
    }

    @Override
    protected void initEventAndData() {
        ArrayList<FamilyUserInfo> familyUserInfo = (ArrayList<FamilyUserInfo>) DBManager.getInstance(this).queryFamilyUserInfoList();

        if (getIntent() != null) {
            DataTestBean dataTestBean = (DataTestBean) getIntent().getSerializableExtra("datatest");

            mDataTestDate.setText(dataTestBean.getCreateDate());

            mMessage.setText(dataTestBean.getMessage());

            getProject(dataTestBean.getServer_Id(),mTypeName);

            for (int i = 0 ; i < familyUserInfo.size(); i++){
                if (familyUserInfo.get(i).getMember_Id() == dataTestBean.getMember_Id()){
                    mTvName.setText(familyUserInfo.get(i).getMember_Name());
                    roundImageView.setImageResource(DefaultPicEnum.getPageByValue(String.valueOf(familyUserInfo.get(i).getMember_Portrait())).getResPic());
                };
            }
        }
    }

    private void getProject(int serviceId,TextView mType){
        if(serviceId == 211){
            mType.setText(getString(R.string.bp));
        }else if(serviceId == 214){
            mType.setText(getString(R.string.bfr_value));
        }else if(serviceId == 213){
            mType.setText(getString(R.string.my_weight));
        }else if(serviceId == 217){
            mType.setText(getString(R.string.spo2h_value));
        }
    }

    @OnClick({R.id.iv_head_left})
    public void onclick(View view){
        switch (view.getId()){
            case R.id.iv_head_left:

                finish();

                break;
        }
    }

}
