package com.datalife.datalife.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.WebViewActivity;
import com.datalife.datalife.adapter.ReportAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.bean.GeneBean;
import com.datalife.datalife.contract.ReportContract;
import com.datalife.datalife.presenter.ReportPresenter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by LG on 2018/3/27.
 */

public class ElectronicReportFragment extends BaseFragment implements ReportContract.ReportView{

    @BindView(R.id.lv_report)
    ListView mReportLv;
    ReportAdapter reportAdapter = null;
    ArrayList<GeneBean> geneBeans = null;

    ReportPresenter reportPresenter = new ReportPresenter(getActivity());

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_report;
    }

    @Override
    protected void initEventAndData() {

        reportPresenter.onCreate();
        reportPresenter.attachView(this);

        reportPresenter.sendList("1","20", ProApplication.SESSIONID);
        mReportLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (geneBeans.get(position).getGCStatus().equals("4")){
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WebViewActivity.class);
                    intent.putExtra("type","gene");
                    intent.putExtra("url",geneBeans.get(position).getUrl());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void BackValueSuccess(ArrayList<GeneBean> geneBeans) {
        this.geneBeans = geneBeans;
        reportAdapter = new ReportAdapter(getActivity(),geneBeans);
        mReportLv.setAdapter(reportAdapter);
    }

    @Override
    public void BackValueFail(String msg) {
//        toast(msg+"");
    }
}
