package com.datalife.datalife.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.MainActivity;
import com.datalife.datalife.activity.SimplebackActivity;
import com.datalife.datalife.adapter.FamilyAdapter;
import com.datalife.datalife.adapter.FamilyRecyclerAdapter;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.bean.SimpleBackPage;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.util.UIHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/3/2.
 */

public class FamilyFragment extends BaseFragment {

//    @BindView(R.id.lv_family)
//    ListView mLvFamily;
    @BindView(R.id.lv_family)
    RecyclerView mLvFamily;

    List<FamilyUserInfo> familyUserInfos = null;
    FamilyAdapter familyAdapter = null;
    FamilyRecyclerAdapter familyRecyclerAdapter = null;

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_family;
    }

    @Override
    protected void initEventAndData() {
        familyUserInfos = DBManager.getInstance(getActivity()).queryFamilyUserInfoList();
        familyAdapter = new FamilyAdapter(getActivity(),familyUserInfos);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLvFamily.setLayoutManager(linearLayoutManager);
        familyRecyclerAdapter = new FamilyRecyclerAdapter(familyUserInfos);
//        mLvFamily.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mLvFamily.setAdapter(familyRecyclerAdapter);
    }

    @OnClick({R.id.ll_add_member})
     public void OnClick(View view){
        switch (view.getId()){
            case R.id.ll_add_member:
                UIHelper.showSimpleBackForResult2(this, SimplebackActivity.RESULT_ADDUSER, SimpleBackPage.ADDUSER);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == SimplebackActivity.RESULT_ADDUSER){
                FamilyUserInfo familyUserInfo = (FamilyUserInfo)data.getSerializableExtra("familyUserInfo");
                familyUserInfos.add(familyUserInfo);
                familyAdapter.notifyDataSetChanged();
                familyRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }
}
