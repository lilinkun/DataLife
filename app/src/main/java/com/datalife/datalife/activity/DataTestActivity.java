package com.datalife.datalife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.DataTestAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.bean.Data;
import com.datalife.datalife.bean.DataTestBean;
import com.datalife.datalife.bean.TestHistoryBean;
import com.datalife.datalife.contract.DataTestContract;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.presenter.DataTestPresenter;
import com.datalife.datalife.widget.DataTestTitle;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/8/20.
 */

public class DataTestActivity extends BaseActivity implements DataTestContract.DataTestView, DataTestAdapter.OnItemClickListener {

    @BindView(R.id.rv_data_test)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_head_left)
    ImageView backLeft;
    @BindView(R.id.datatest_title)
    DataTestTitle dataTestTitle;

    private LinearLayoutManager linearLayoutManager;
    private DataTestPresenter dataTestPresenter = new DataTestPresenter(this);
    private ArrayList<DataTestBean> testHistoryBeans = null;
    private ArrayList<DataTestBean> datalist = null;
    private  DataTestAdapter dataTestAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x322:

                    Bundle bundle = msg.getData();
                    FamilyUserInfo familyUserInfo = (FamilyUserInfo) bundle.getSerializable("Member");
                    int serviceid =  bundle.getInt("typeid");
                    if (testHistoryBeans != null) {

                        if (datalist == null) {
                            datalist = new ArrayList<>();
                        }else {
                            datalist.clear();
                        }
                        for (int i = 0; i < testHistoryBeans.size(); i++) {
                            if (testHistoryBeans.get(i).getMember_Id() == familyUserInfo.getMember_Id()) {
                                if (serviceid != 0) {
                                    if (testHistoryBeans.get(i).getServer_Id() == serviceid) {
                                        datalist.add(testHistoryBeans.get(i));
                                    }
                                }else {
                                    datalist.add(testHistoryBeans.get(i));
                                }
                            }
                        }
                        if (dataTestAdapter != null){
                            dataTestAdapter.setData(datalist);
                        }
                    }

                    break;

                case 0x323:

                    Bundle bundle1 = msg.getData();
                    int serviceid1 =  bundle1.getInt("typeid");
                    FamilyUserInfo familyUserInfo1 = (FamilyUserInfo) bundle1.getSerializable("Member");
                    if (testHistoryBeans != null) {

                        if (datalist == null) {
                            datalist = new ArrayList<>();
                        }else {
                            datalist.clear();
                        }
                        for (int i = 0; i < testHistoryBeans.size(); i++) {
                            if (testHistoryBeans.get(i).getServer_Id() == serviceid1) {
                                if (familyUserInfo1 != null){
                                    if (testHistoryBeans.get(i).getMember_Id() == familyUserInfo1.getMember_Id()){
                                        datalist.add(testHistoryBeans.get(i));
                                    }
                                }else {
                                    datalist.add(testHistoryBeans.get(i));
                                }
                            }
                        }



                        if (dataTestAdapter != null){
                            dataTestAdapter.setData(datalist);
                        }
                    }



                    break;

            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_datatest;
    }

    @Override
    protected void initEventAndData() {

        dataTestPresenter.onCreate();
        dataTestPresenter.attachView(this);

        dataTestTitle.setLayout(mRecyclerView);
        dataTestTitle.setHandler(handler);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        dataTestPresenter.getTestData("1","50", ProApplication.SESSIONID);

    }

    @OnClick({R.id.iv_head_left})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_head_left:

                finish();

                break;
        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    protected void onDestroy() {
        dataTestPresenter.onStop();
        super.onDestroy();
    }

    @Override
    public void success(ArrayList<DataTestBean> dataTestBeans) {
        testHistoryBeans = dataTestBeans;
        dataTestAdapter = new DataTestAdapter(this,testHistoryBeans);

        dataTestAdapter.setItemClickListener(this);

        mRecyclerView.setAdapter(dataTestAdapter);
    }

    @Override
    public void fail(String msg) {

    }

    @Override
    public void onItemClick(int position) {

    }
}
