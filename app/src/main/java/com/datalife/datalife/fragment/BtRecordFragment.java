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
import com.datalife.datalife.base.BaseRecordFragment;
import com.datalife.datalife.bean.BtRecordBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.custom.DayXAxisFormatter;
import com.datalife.datalife.custom.MonthXAxisFormatter;
import com.datalife.datalife.custom.MyMarkerView;
import com.datalife.datalife.custom.WeekXAxisValueFormatter;
import com.datalife.datalife.custom.YearXAxisFormatter;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.MeasureNorm;
import com.datalife.datalife.util.MyCalendar;
import com.datalife.datalife.widget.LX_LoadListView;
import com.datalife.datalife.widget.MyDateLinear;
import com.datalife.datalife.widget.SmallStateView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.DecimalFormat;
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

public class BtRecordFragment extends BaseRecordFragment implements OnChartValueSelectedListener, MyDateLinear.MyDateLinearListener {

    private MyCalendar calendar;
    private int wm_width, wm_height;

    @BindView(R.id.chart1)
    LineChart mChart;
    @BindView(R.id.mydate)
    MyDateLinear myDateLinear;
    @BindView(R.id.btn_date)
    TextView mDateTV;
    @BindView(R.id.tv_local_temp)
    TextView mLocalTemp;
    @BindView(R.id.tv_avg_value)
    TextView mAvgValue;
    @BindView(R.id.stateview)
    SmallStateView smallStateView;
    @BindView(R.id.tv_history_last)
    TextView mBtLastValue;
    @BindView(R.id.tv_history_last_date)
    TextView mHrLastDate;
    @BindView(R.id.mTvMonthDay)
    TextView mTvMonthDay;
    @BindView(R.id.tv_createtime)
    TextView tv_createtime;

    public ArrayList<MeasureRecordBean> measureRecordBeans = null;
    ArrayList<BtRecordBean> btRecordBeans = null;
    private int state;
    private String mDateNowStr;
    private String mDateNowWeekStr;
    private int pageIndex = 1;
    private XAxis xAxis;

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_btrecord;
    }

    @Override
    public String getTitle() {
        return "体温";
    }

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
    protected void initEventAndData() {
        Date date = new Date();
        state = RecordActivity.MYDAY;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.add(Calendar.DAY_OF_MONTH, -30);
        Date date1 = calendar1.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mDateNowStr = simpleDateFormat.format(date);
        mDateNowWeekStr = simpleDateFormat.format(date1);

        WindowManager wm = (WindowManager) getActivity().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        wm_width = wm.getDefaultDisplay().getWidth();
        wm_height = wm.getDefaultDisplay().getHeight();

        calendar = calendar.getInstance();
        myDateLinear.setMyDateLinearListener(this);
        myDateLinear.setMaxYear(calendar.get(Calendar.YEAR));
//        myDateLinear.setMinYear(1950);
        myDateLinear.init();
        myDateLinear.init(1990, 1, 1);

        mDateTV.setText(mDateNowStr);

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
        leftAxis.setAxisMaximum(39);
        leftAxis.setAxisMinimum(35);
        leftAxis.setLabelCount(4);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setEnabled(false);
        rightAxis.setAxisMaximum(39);
        rightAxis.setAxisMinimum(35);
        rightAxis.setLabelCount(4);
    }

    @OnClick({R.id.date_layout})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.date_layout:
                if (calendar != null) {
                    myDateLinear.init(calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH));
                    setMyVisible(myDateLinear);
                }
                break;

        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    private void setData(int count, float range) {

        if (count >= 2){
            DecimalFormat df   = new DecimalFormat("######0.0");
            double v1 = Double.parseDouble(measureRecordBeans.get(measureRecordBeans.size()-1).getCheckValue1());
            double v2 = Double.parseDouble(measureRecordBeans.get(measureRecordBeans.size()-2).getCheckValue1());
            double s = v1 -v2;
            String str = df.format(s) + "";
            mBtLastValue.setText(str);
            mHrLastDate.setText(measureRecordBeans.get(measureRecordBeans.size()-2).getCreateDate().substring(0,10));
        }

        if (count > 0){
            mLocalTemp.setText(measureRecordBeans.get(measureRecordBeans.size()-1).getCheckValue1());
            smallStateView.setValue(IDatalifeConstant.getWM(getActivity()),Double.parseDouble(measureRecordBeans.get(measureRecordBeans.size()-1).getCheckValue1()), MeasureNorm.getBT(),"");
            mTvMonthDay.setText(mDateNowWeekStr);
        }


        //设置一页最大显示个数为6，超出部分就滑动
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        float ratio = 1f;
        if (count >= 6){
            ratio = (float) count/(float) 6;
            mChart.zoom(ratio,1f,0,0);
            mChart.moveViewToX(count-1);
        }

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < measureRecordBeans.size(); i++) {
            float mult = range;
            float val = (float) (Math.random() * mult* 0.01) + 37;
            yVals1.add(new Entry(i +0.3f, Float.parseFloat(measureRecordBeans.get(i).getCheckValue1()),getResources().getDrawable(R.mipmap.ic_line_spot)));
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
            set1 = new LineDataSet(yVals1, "体温");

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

    public void onfail(String str) {
//        toast("查不到数据");
        mChart.setData(null);
        mChart.invalidate();
    }

    public void onSuccess(Object o) {
        /*if (measureRecordBeans!=null && measureRecordBeans.size() >0) {

            if(((ArrayList<MeasureRecordBean>)o).size() > 0){
                for(int i = 0; i < ((ArrayList<MeasureRecordBean>)o).size();i++){
                    measureRecordBeans.add(((ArrayList<MeasureRecordBean>)o).get(i));
                };
            }
        }else {*/
            measureRecordBeans = (ArrayList<MeasureRecordBean>) o;
//        }

        tv_createtime.setText(measureRecordBeans.get(measureRecordBeans.size()-1).getCreateDate());

        setData(measureRecordBeans.size(), 50);
        mChart.invalidate();
    }

    public void setMyVisible(View view) {
        if (myDateLinear.getVisibility() == View.VISIBLE) {
            return;
        }
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fenxiang_weiyi_input);
        view.setAnimation(animation);
        view.setVisibility(View.VISIBLE);
    }

    public void setMyInvisible(View view) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fenxiang_weiyi_output);
        view.setAnimation(animation);
        view.setVisibility(View.GONE);
    }

    @Override
    public int getscreenWidth() {
        return wm_width;
    }

    @Override
    public int getscreenHeight() {
        return wm_height;
    }

    @Override
    public void cancle() {
        setMyInvisible(myDateLinear);
    }

    @Override
    public void sure(String dateStr) {
        setMyInvisible(myDateLinear);
        String after_date = myDateLinear.getSelcet_my_year() + "-"
                + myDateLinear.getSelcet_my_month() + "-"
                + myDateLinear.getSelcet_my_day();
        String str_date = after_date;
        calendar.set(Calendar.YEAR, myDateLinear.getSelcet_my_year());
        calendar.set(Calendar.MONTH, myDateLinear.getSelcet_my_month() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, myDateLinear.getSelcet_my_day());

        Date born = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            born = df.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String str = df.format(born);
        String date1 = "";
        mDateTV.setText(dateStr);
        if (state == RecordActivity.MYWEEK) {
            date1 = dateStr.substring(0, 10);
        }else if (state == RecordActivity.MYDAY){
            date1 = str;
        }else if (state == RecordActivity.MYMONTH){
            date1 = str.substring(0,str.lastIndexOf("-")+1) + "01";
            str = IDatalifeConstant.getLastDayOfMonth(str);
        }else if (state == RecordActivity.MYYEAR){
            String year = str.substring(0,str.indexOf("-"));
            date1 = year + "-01-01";
            str = year + "-12-31";
        }
        pageIndex = 1;
        measureRecordBeans.clear();
        onBtRecordLisener.onBtIntent(state, pageIndex, date1, str);
    }

    @Override
    public void realTime(String data) {
    }

    public void onAvg(ArrayList<BtRecordBean> btRecordBeans){
        String avg = btRecordBeans.get(0).getCheckValue1Avg();
        mAvgValue.setText(avg);
    }

}
