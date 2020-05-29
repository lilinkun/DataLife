package com.datalife.datalife.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.RecordActivity;
import com.datalife.datalife.adapter.MeasureRecordAdapter;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.base.BaseRecordFragment;
import com.datalife.datalife.bean.BtRecordBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.custom.MyMarkerView;
import com.datalife.datalife.custom.YearXAxisFormatter;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.MeasureNorm;
import com.datalife.datalife.util.MyCalendar;
import com.datalife.datalife.widget.LX_LoadListView;
import com.datalife.datalife.widget.MyDateLinear;
import com.datalife.datalife.widget.SmallStateView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/2/5.
 */

public class HrRecordFragment extends BaseRecordFragment implements OnChartValueSelectedListener{


    @BindView(R.id.chart1)
    LineChart mChart;
    @BindView(R.id.mydate)
    MyDateLinear myDateLinear;
    @BindView(R.id.btn_date)
    TextView mDateTV;
    private XAxis xAxis;
    @BindView(R.id.tv_local_temp)
    TextView mLocalTemp;
    @BindView(R.id.tv_avg_value)
    TextView mAvgValue;
    @BindView(R.id.tv_history_last)
    TextView mHrLastValue;
    @BindView(R.id.tv_history_last_date)
    TextView mHrLastDate;
    @BindView(R.id.stateview)
    SmallStateView smallStateView;
    @BindView(R.id.mTvMonthDay)
    TextView mTvMonthDay;
    @BindView(R.id.tv_createtime)
    TextView tv_createtime;

    private String mDateNowWeekStr;
    public ArrayList<MeasureRecordBean> measureRecordBeans = null;
    MeasureRecordAdapter measureRecordAdapter = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case IDatalifeConstant.MESSAGEWHAT_CHART:
                    int pos = (int)(msg.getData().getFloat("x"));
                    if (measureRecordBeans.get(pos) != null) {
                        tv_createtime.setText(measureRecordBeans.get(pos).getCreateDate());
                    }
                    break;
            }
        }
    };

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_hr_record;
    }

    @Override
    protected void initEventAndData() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date date1 = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mDateNowWeekStr = simpleDateFormat.format(date1);

        mChart.setOnChartValueSelectedListener(this);// no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDrawBorders(false);
        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);
        mChart.setPinchZoom(true);
//        mChart.setDrawMarkers(false);

        // add data
//        setData(12, 50);
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view,handler);
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv);

        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(mTfLight);
        l.setTextColor(getResources().getColor(R.color.bg_line_chart));
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        xAxis = mChart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(160);
        leftAxis.setAxisMinimum(20);
        leftAxis.setLabelCount(4);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setEnabled(false);
        rightAxis.setAxisMaximum(160);
        rightAxis.setAxisMinimum(20);
        rightAxis.setLabelCount(4);
    }

    @Override
    public String getTitle() {
        return "心率";
    }

    public void onSuccess() {

    }
    @OnClick({R.id.date_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_layout:
                break;

        }
    }

    public void onfail(String str) {
//        toast("查不到数据");
        mChart.setData(null);
        mChart.invalidate();
        smallStateView.setValue(IDatalifeConstant.getWM(getActivity()),0, MeasureNorm.getHR(),"");
        mLocalTemp.setText("");
        mHrLastValue.setText("");
    }


    public void onTotalSuccess(ArrayList<BtRecordBean> btRecordBeans){

    }

    public void onSuccess(Object o,int index) {

        measureRecordBeans = (ArrayList<MeasureRecordBean>) o;

        tv_createtime.setText(measureRecordBeans.get(measureRecordBeans.size()-1).getCreateDate());
        setData(measureRecordBeans.size(), 50);
        mChart.invalidate();
    }

    private void setData(int count, float range) {

        if (count >= 2){
            double v1 = Double.parseDouble(measureRecordBeans.get(measureRecordBeans.size()-1).getCheckValue3());
            double v2 = Double.parseDouble(measureRecordBeans.get(measureRecordBeans.size()-2).getCheckValue3());
            String str = v1 - v2 + "";
            mHrLastValue.setText(str);
            mHrLastDate.setText(measureRecordBeans.get(measureRecordBeans.size()-2).getCreateDate().substring(0,10));
        }


        if (count>0){
            mLocalTemp.setText(measureRecordBeans.get(measureRecordBeans.size()-1).getCheckValue3());
            mTvMonthDay.setText(mDateNowWeekStr);
            smallStateView.setValue(IDatalifeConstant.getWM(getActivity()),Double.parseDouble(measureRecordBeans.get(measureRecordBeans.size()-1).getCheckValue3()), MeasureNorm.getHR(),"");
        }

        //设置一页最大显示个数为6，超出部分就滑动
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        float ratio = 1f;
        if (count > 6){
            ratio = (float) count/(float) 6;
        }
        mChart.zoom(ratio,1f,0,0);
        mChart.moveViewToX(count-1);

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < measureRecordBeans.size(); i++) {
            float mult = range;
            float val = (float) (Math.random() * mult* 0.01) + 37;
            yVals1.add(new Entry(i +0.3f, Float.parseFloat(measureRecordBeans.get(i).getCheckValue3()),getResources().getDrawable(R.mipmap.ic_line_spot)));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "血氧");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(getResources().getColor(R.color.bg_line_chart));
            set1.setDrawCircleHole(true);
            set1.setLineWidth(2f);
            set1.setCircleRadius(7f);
//            set1.setCircleSize(7f);
            set1.setFillAlpha(65);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
//            set1.setDrawCircleHole(false);
            set1.setDrawValues(false);
            set1.setFillDrawable(getResources().getDrawable(R.drawable.ic_blue_fill));
            set1.setDrawFilled(true);
            set1.setDrawIcons(true);


            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);
            // set data
            mChart.setData(data);

        }
    }

        @Override
        public void onValueSelected(Entry e, Highlight h) {
            Log.i("Entry selected", e.toString());

            mChart.centerViewToAnimated(e.getX(), e.getY(), mChart.getData().getDataSetByIndex(h.getDataSetIndex())
                    .getAxisDependency(), 500);
        }


        @Override
        public void onNothingSelected() {
            Log.i("Nothing selected", "Nothing selected.");
        }


    public void onAvg(ArrayList<BtRecordBean> btRecordBeans){
        String hrAvg = btRecordBeans.get(0).getCheckValue3Avg();
        mAvgValue.setText(hrAvg);
    }
}
