package com.datalife.datalife.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.BindToothAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.contract.BrushContract;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.presenter.BrushBindPresenter;
import com.datalife.datalife.util.DataLifeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/10/8.
 */

public class ToothBindActivity extends BaseActivity implements BindToothAdapter.OnItemClickListener ,BrushContract.BrushBindView{

    @BindView(R.id.rv_tooth_bind)
    RecyclerView recyclerView;

    private BindToothAdapter bindToothAdapter;
    private int posId = 0;
    private List<FamilyUserInfo> familyUserInfos = null;
    private String mMachineName = "";
    private String mSn = "";

    private BrushBindPresenter brushBindPresenter = new BrushBindPresenter(this);

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {



        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tooth_bind;
    }

    @Override
    protected void initEventAndData() {

        brushBindPresenter.attachView(this);
        brushBindPresenter.onCreate();

        Bundle bundle = getIntent().getBundleExtra(DataLifeUtil.BUNDLEMEMBER);
        mMachineName = bundle.getString("MachineName");
        mSn = bundle.getString("sn");

        familyUserInfos = DBManager.getInstance(this).queryFamilyUserInfoList();

        bindToothAdapter = new BindToothAdapter(this,handler,posId,familyUserInfos);
        bindToothAdapter.setItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(bindToothAdapter);

    }

    @OnClick({R.id.iv_head_left,R.id.btn_bindtooth})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_head_left:
                setResult(Activity.RESULT_OK);
                finish();
                break;

            case R.id.btn_bindtooth:

                View contentView = LayoutInflater.from(this).inflate(R.layout.common_brushhead,null);
                LinearLayout linearLayout = (LinearLayout) contentView.findViewById(R.id.ll_bind_brush);
                Button sureBtn = (Button) contentView.findViewById(R.id.btn_bg_left);
                Button exitBtn = (Button) contentView.findViewById(R.id.btn_bg_right);
                TextView textView = (TextView) contentView.findViewById(R.id.tv_bindmember_brush);

                textView.setText(getResources().getString(R.string.brush_select));

                linearLayout.setBackgroundResource(R.drawable.brushbind);

                final AlertDialog builder = new AlertDialog.Builder(this).setView(contentView).create();

                builder.show();

                sureBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        brushBindPresenter.sendDeviceInfo(mMachineName,mSn, ProApplication.SESSIONID,familyUserInfos.get(posId).getMember_Id()+"");
                        builder.dismiss();
                    }
                });
                exitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
                /*ton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        brushBindPresenter.sendDeviceInfo(mMachineName,mSn, ProApplication.SESSIONID,familyUserInfos.get(posId).getMember_Id()+"");
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();*/



                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        brushBindPresenter.onStop();
    }

    @Override
    public void onItemClick(int position) {
        if (bindToothAdapter != null){
            bindToothAdapter.setPosId(position);
            posId = position;
        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void success() {
//        toast("success");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void fail(String msg) {
        toast("fail" + msg);
    }
}
