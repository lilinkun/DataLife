package com.datalife.datalife.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.MyChartAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.bean.ToothTestBean;
import com.datalife.datalife.contract.BrushHistoryContract;
import com.datalife.datalife.dao.BrushBean;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.presenter.ToothHistoryPresenter;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.UToast;
import com.datalife.datalife.widget.RoundImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/6/19.
 */

public class ToothHistoryActivity extends BaseActivity implements MyChartAdapter.OnItemClickListener,BrushHistoryContract.BrushHistoryView {

    @BindView(R.id.lv_mychart)
    RecyclerView recyclerView;
    @BindView(R.id.pb_total_time)
    ProgressBar mTotalProgressBar;
    @BindView(R.id.pb_brush_even)
    ProgressBar mEvenProgressBar;
    @BindView(R.id.pb_range)
    ProgressBar mRangeProgressBar;
    @BindView(R.id.tv_total_time)
    TextView mTotalTime;
    @BindView(R.id.tv_even)
    TextView mTvEven;
    @BindView(R.id.tv_range)
    TextView mTvRange;

    LinearLayoutManager mLinearLayoutManager;
    List<BrushBean> brushBeans;
    MyChartAdapter myChartAdapter;

    private int pageId = 1;
    private int pageCount = 20;

    private String[] durationArr;

    private String memberid = "";

    private ToothHistoryPresenter toothHistoryPresenter = new ToothHistoryPresenter(this);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tooth_history;
    }

    @Override
    protected void initEventAndData() {

        toothHistoryPresenter.attachView(this);
        toothHistoryPresenter.onCreate();


        memberid = getIntent().getBundleExtra(DataLifeUtil.BUNDLEMEMBER).getString("memberid");
        String machinebindid = getIntent().getBundleExtra(DataLifeUtil.BUNDLEMEMBER).getString("machineid");

        durationArr = getResources().getStringArray(R.array.arr_duration);

        toothHistoryPresenter.onGetListValue(pageId+"",pageCount+"","",memberid,DataLifeUtil.MACHINE_BRUSH, IDatalifeConstant.BRUSHINT + "", "","", ProApplication.SESSIONID);


        initMyChart();
    }

    @OnClick({R.id.iv_head_left})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_head_left:
                finish();
                break;
        }
    }

    private void initMyChart(){

        brushBeans = DBManager.getInstance(this).queryBrushList("01:B4:EC:B9:C7:C8");

        myChartAdapter = new MyChartAdapter(this,brushBeans);
        myChartAdapter.setItemClickListener(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLinearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(myChartAdapter);
//        chartView = (ChartView) findViewById(R.id.my_chart);
//        chartView.setList(80f);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lcp = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lcp == mLinearLayoutManager.getItemCount() - 2){
                    int fcp= mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    View child = mLinearLayoutManager.findViewByPosition(lcp);
                    int deltaY = recyclerView.getLeft() - recyclerView.getPaddingLeft() -
                            child.getLeft();
                    // fcp为0时说明列表滚动到了顶部, 不再滚动
                    if (deltaY > 0 && fcp!= 0) {
                        recyclerView.smoothScrollBy(0, -deltaY);
                    }
                } else if (lcp== mLinearLayoutManager.getItemCount() - 1) {
                    // 最后一项完全显示, 触发操作, 执行加载更多操作

                    if (brushBeans != null && brushBeans.size() > 0) {
                        pageId++;
                        toothHistoryPresenter.onGetListValue(pageId+"",pageCount+"","",memberid,DataLifeUtil.MACHINE_BRUSH, IDatalifeConstant.BRUSHINT + "", "","", ProApplication.SESSIONID);
                    }
                }
            }
        });


    }


    @Override
    public void onItemClick(int position) {
        if (brushBeans.get(position).getSettingTime() > 0){
            if ((brushBeans.get(position).getWorkTime() / brushBeans.get(position).getSettingTime()) * 40 < 30) {
                mTotalProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bg_red));
            } else {
                mTotalProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bg_blue));
            }
        }

        int longtime = brushBeans.get(position).getWorkTime();
        String strTime = longtime/60 + "分" + longtime%60 + "秒";

        mTotalTime.setText("已刷牙" + strTime);

        int bigtime = brushBeans.get(position).getLeftBrushTime()>=brushBeans.get(position).getRightBrushTime()? brushBeans.get(position).getLeftBrushTime() : brushBeans.get(position).getRightBrushTime();
        int smalltime = brushBeans.get(position).getLeftBrushTime()>=brushBeans.get(position).getRightBrushTime()? brushBeans.get(position).getRightBrushTime() : brushBeans.get(position).getLeftBrushTime();

        if ((double)smalltime/bigtime*40 < 20) {
            mEvenProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bg_red));
            mTvEven.setText("未达标");
        }else {
            mEvenProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bg_blue));
            mTvEven.setText("标准");
        }

        if (brushBeans.get(position).getSettingTime()>20) {
            if (brushBeans.get(position).getSettingTime()-brushBeans.get(position).getWorkTime() <= 3){
                mTotalProgressBar.setProgress(40);
            }else {
                mTotalProgressBar.setProgress((int) ((double) brushBeans.get(position).getWorkTime() / brushBeans.get(position).getSettingTime() * 40));
            }
        }else if (brushBeans.get(position).getSettingTime()>0 && brushBeans.get(position).getSettingTime()<20){
            mTotalProgressBar.setProgress((int)((double)brushBeans.get(position).getWorkTime()/Integer.valueOf(durationArr[brushBeans.get(position).getSettingTime()-1])*40));
        }else {
            mTotalProgressBar.setProgress(0);
        }

        double rangeCount = 0;
        if (brushBeans.get(position).getWorkTime() > 0) {
            rangeCount = (double) (brushBeans.get(position).getLeftBrushTime() + brushBeans.get(position).getRightBrushTime()) / brushBeans.get(position).getWorkTime() * 100;
        }

        if (rangeCount > 80){
            rangeCount = (rangeCount-80) * 0.5;
        }else if (rangeCount < 80){
            rangeCount = (80 - rangeCount) * 0.5;
        }else if (rangeCount == 80){
            rangeCount = 0;
        }

        if (rangeCount > 20){
            rangeCount = 20;
        }else {
            rangeCount = 20 - rangeCount;
        }
        if (rangeCount < 12){
            mRangeProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bg_red));
            mTvRange.setText("未达标");
        }else {
            mRangeProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bg_blue));
            mTvRange.setText("标准");
        }

        mRangeProgressBar.setProgress((int)rangeCount);

        if (bigtime > 0) {
            double aa = (double)smalltime / bigtime * 40;
            mEvenProgressBar.setProgress((int)aa);
        }else {
            mEvenProgressBar.setProgress(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toothHistoryPresenter.onStop();
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void onSuccess(ArrayList<BrushBean> brushBeans) {

        /*if (refreshLayout != null && refreshLayout.isShown()){
            refreshLayout.setRefreshing(false);
        }*/

        DBManager.getInstance(this).deleteBrushList();

        for (BrushBean brushBean : brushBeans) {
            DBManager.getInstance(this).insertBrushList(brushBean);
        }

        Collections.reverse(brushBeans);

        if (this.brushBeans == null || this.brushBeans.size() == 0) {
            this.brushBeans = brushBeans;
        }else {
            this.brushBeans.addAll(brushBeans);
        }

        if(myChartAdapter != null){
            if (brushBeans.size() == pageCount){
                myChartAdapter.setBrush(this.brushBeans,0);
            }else {
                myChartAdapter.setBrush(this.brushBeans,1);
            }

        }else {
            myChartAdapter = new MyChartAdapter(this,this.brushBeans);
            recyclerView.setAdapter(myChartAdapter);
        }

        if (this.brushBeans.size() > 0) {
            onItemClick(0);
        }

    }

    @Override
    public void onFail(String msg) {
        Log.e("ToothHistoryActivity","fail:" + msg);
        toast(msg);
    }


}
