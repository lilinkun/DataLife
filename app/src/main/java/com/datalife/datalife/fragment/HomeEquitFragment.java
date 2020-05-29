package com.datalife.datalife.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.BlueToothDevActivity;
import com.datalife.datalife.activity.FatActivity;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.activity.LoginActivity;
import com.datalife.datalife.activity.MainActivity;
import com.datalife.datalife.activity.SettingActivity;
import com.datalife.datalife.activity.SimplebackActivity;
import com.datalife.datalife.activity.ToothActivity;
import com.datalife.datalife.activity.ToothDevActivity;
import com.datalife.datalife.adapter.MainMachineAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.bean.SimpleBackPage;
import com.datalife.datalife.contract.HomePageContract;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.FlashListBean;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.dao.NewsInfo;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.presenter.HomePagePresenter;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.Eyes;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.util.UToast;
import com.datalife.datalife.widget.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by LG on 2018/10/17.
 */

public class HomeEquitFragment extends BaseFragment implements HomePageContract.HomePageView, MainMachineAdapter.OnItemClickListener {


    @BindView(R.id.rv_equit)
    RecyclerView recyclerView;
    @BindView(R.id.ll_no_net)
    LinearLayout mNoNetLayout;


    HomePagePresenter homePagePresenter = new HomePagePresenter(getActivity());
    public List<FamilyUserInfo> familyUserInfos= null;
    private List<MachineBean> machineBeans = null;
    private MainMachineAdapter mainMachineAdapter = null;
    private int bundle_result = 0x0123;
    private AlertDialog alertDialog;

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    protected int getlayoutId() {
        return R.layout.activity_main_rechange;
    }

    @Override
    protected void initEventAndData() {
        Eyes.translucentStatusBar(getActivity());
//        Eyes.setStatusBarColor(getActivity(), ContextCompat.getColor(getActivity(), R.color.bg_toolbar_title));

        homePagePresenter.onCreate();
        homePagePresenter.attachView(this);

        familyUserInfos = DBManager.getInstance(getActivity()).queryFamilyUserInfoList();

        machineBeans = DBManager.getInstance(getActivity()).queryMachineBeanList();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        int spanCount = 2; // 2 columns
        int spacing = 20; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        recyclerView.setLayoutManager(gridLayoutManager);

        mainMachineAdapter = new MainMachineAdapter(getActivity(),machineBeans);
        mainMachineAdapter.setItemClickListener(this);
        recyclerView.setAdapter(mainMachineAdapter);

        homePagePresenter.getDevMachineInfo("1","20", ProApplication.SESSIONID);

        if (machineBeans != null){
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void setRestartApp(){
//        recyclerView.setVisibility(View.INVISIBLE);
    }

    public void notnet(){
        if (mNoNetLayout != null) {
            mNoNetLayout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.iv_head_right})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_head_right:

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(DataLifeUtil.LOGIN, Context.MODE_PRIVATE);
                if (sharedPreferences.getBoolean(DataLifeUtil.LOGIN,false) == false){

                    if(alertDialog == null) {
                        alertDialog = new AlertDialog.Builder(getActivity()).setMessage("您还没有登录，请先登录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), LoginActivity.class);
                                startActivityForResult(intent, IDatalifeConstant.INTENT_LOGIN);
                                getActivity().finish();
                                alertDialog = null;
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();

                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }else {
                        alertDialog.show();
                    }

                    return;
                }else {
                    UIHelper.launcherForResult(this, BlueToothDevActivity.class, bundle_result);
                }

                break;
        }
    }


    //点击健康监测仪
    private void healthTest(String machineName){
        onJumpTest(HealthMonitorActivity.class, 111,null);
    }


    //点击体脂称
    private void fatTest(String machineName){
        Bundle bundle = new Bundle();
        bundle.putString("machinename",machineName);
        onJumpTest( FatActivity.class, 121,bundle);
    }

    private void toothTest(Bundle bundle){
        onJumpTest(ToothActivity.class,131,bundle);
    }

    private void onJumpTest(Class<?> targetActivity, int requestCode, Bundle bundle){
        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(DataLifeUtil.LOGIN, MODE_PRIVATE);
        if (mySharedPreferences.getBoolean(DataLifeUtil.LOGIN,false) == false){
            Intent intent = new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            return;
        }

        if (familyUserInfos == null || familyUserInfos.size() == 0) {
            // 创建构建器
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.no_add_user)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            UIHelper.showSimpleBackForResult(getActivity(), SimplebackActivity.RESULT_ADDUSER, SimpleBackPage.ADDUSER);
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
            UIHelper.launcherForResultBundle(getActivity(), targetActivity, requestCode,bundle);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == bundle_result){
                machineBeans = DBManager.getInstance(getActivity()).queryMachineBeanList();
                mainMachineAdapter.setValues(machineBeans);
            }
        }
    }

    @Override
    public void onSuccess(ArrayList<NewsInfo> resultNews) {

    }

    @Override
    public void onfail(String str) {

    }

    @Override
    public void onFlashSuccess(ArrayList<FlashListBean> flashListBeans) {

    }

    @Override
    public void onFlashFail(String str) {

    }

    @Override
    public void onEquipSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews) {

        if (mainMachineAdapter != null) {
            Collections.reverse(machineBeans);
            mainMachineAdapter.setValues(machineBeans);
        }
    }

    public void onGetEquitSuccess(){
        machineBeans = DBManager.getInstance(getActivity()).queryMachineBeanList();

        if (mainMachineAdapter != null) {
            Collections.reverse(machineBeans);
            mainMachineAdapter.setValues(machineBeans);
        }
    }

    public void getMember(){
        familyUserInfos = DBManager.getInstance(getActivity()).queryFamilyUserInfoList();
    }

    @Override
    public void onEquipFail(String msg) {

    }

    @Override
    public void onItemClick(int position) {
        if (machineBeans.get(position).getMachineName().startsWith("HC02")){
            healthTest(machineBeans.get(position).getMachineName());
        }else if (machineBeans.get(position).getMachineName().startsWith("SWAN")){
            fatTest(machineBeans.get(position).getMachineName());
        }else if (machineBeans.get(position).getMachineName().startsWith("ZSON")){
            Bundle bundle = new Bundle();
            bundle.putSerializable("machine",machineBeans.get(position));
            toothTest(bundle);
        }
    }
}
