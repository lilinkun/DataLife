package com.datalife.datalife.fragment;

import android.view.View;
import android.widget.ListView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.RecordActivity;
import com.datalife.datalife.adapter.EcgRecordAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseRecordFragment;
import com.datalife.datalife.bean.BtRecordBean;
import com.datalife.datalife.bean.ECG;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.widget.LX_LoadListView;

import java.security.PrivilegedExceptionAction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by LG on 2018/2/5.
 */

public class EcgRecordFragment extends BaseRecordFragment implements LX_LoadListView.OnLoadMoreListener {

    @BindView(R.id.lv_ecg_record)
    LX_LoadListView mEcgListView;

    EcgRecordAdapter ecgRecordAdapter;

    int pageIndex = 1;

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_ecgrecord;
    }

    @Override
    protected void initEventAndData() {
//        mEcgListView.setAdapter(new EcgRecordAdapter(getActivity(),ecgs));
        mEcgListView.setLoadMoreListener(this);
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public String getTitle() {
        return "心电";
    }


    public void onSuccess(Object o,int index) {
        mEcgListView.setVisibility(View.VISIBLE);
        ArrayList<MeasureRecordBean> measureRecordBeans = (ArrayList<MeasureRecordBean>)o;
        if (ecgRecordAdapter == null) {
            ecgRecordAdapter = new EcgRecordAdapter(getActivity(), measureRecordBeans);
            mEcgListView.setAdapter(ecgRecordAdapter);
        }else {
            ecgRecordAdapter.setValue(measureRecordBeans);
            ecgRecordAdapter.notifyDataSetChanged();
        }
    }

    public void onFail(String str){
        if (str.contains("查无数据")) {
            mEcgListView.setVisibility(View.GONE);
//            ArrayList<MeasureRecordBean> measureRecordBeans = new ArrayList<>();
//            ecgRecordAdapter.setValue(measureRecordBeans);
//            ecgRecordAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadMore() {

        pageIndex++;
        onBtRecordLisener.onEcgIntent(1,pageIndex);

    }
}
