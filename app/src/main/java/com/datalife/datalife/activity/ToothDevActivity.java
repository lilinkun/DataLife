package com.datalife.datalife.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.ToothDevAdapter;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.bean.SimpleBackPage;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.UIHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/6/25.
 */

public class ToothDevActivity extends BaseActivity implements ToothDevAdapter.OnItemClickListener{

    @BindView(R.id.rv_history)
    RecyclerView recyclerView;

    List<FamilyUserInfo> familyUserInfos = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_history;
    }

    @Override
    protected void initEventAndData() {
        familyUserInfos = DBManager.getInstance(this).queryFamilyUserInfoList();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ToothDevAdapter toothDevAdapter = new ToothDevAdapter(this);
        toothDevAdapter.setItemClickListener(this);
        recyclerView.setAdapter(toothDevAdapter);
    }

    @OnClick({R.id.iv_head_left})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_head_left:
                finish();
                break;

            /*case R.id.rl_health_fat:

                onJumpTest(DecoratedActivity.class,333);

                break;

            case R.id.rl_health_monitor:

                onJumpTest(RecordActivity.class,334);

                break;*/
        }
    }

    private void onJumpTest(Class<?> targetActivity,int requestCode){
        SharedPreferences mySharedPreferences = getSharedPreferences(DataLifeUtil.LOGIN, MODE_PRIVATE);
        if (mySharedPreferences.getBoolean(DataLifeUtil.LOGIN,false) == false){
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if (familyUserInfos == null || familyUserInfos.size() == 0) {
            // 创建构建器
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_add_user)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            UIHelper.showSimpleBackForResult(ToothDevActivity.this, SimplebackActivity.RESULT_ADDUSER, SimpleBackPage.ADDUSER);
                        }
                    }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,
                                    int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }else {
            UIHelper.launcherForResult(this, targetActivity,requestCode);
        }
    }

    @Override
    public void onItemClick(int position) {
        switch (position){

            case 0:

                onJumpTest(RecordActivity.class,334);

                break;

            case 1:

                onJumpTest(FatCalendarActivity.class,333);

                break;

            case 2:

                onJumpTest(ToothHistoryActivity.class,335);

                break;
        }
    }
}
