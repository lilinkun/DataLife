package com.datalife.datalife.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.NumericWheelAdapter;
import com.datalife.datalife.interf.OnWheelScrollListener;
import com.datalife.datalife.util.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LG on 2018/1/28.
 */

public class PopMyinfoDateSelect  implements View.OnClickListener {

    private RelativeLayout exit;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private TextView cancel, ok;
    private View v;
    private View contentView;
    private LinearLayout content;

    private Animation bottom_in;
    private Animation bottom_out;
    private Animation close_alpha;
    private Animation show_alpha;

    private Context context;

    private PopupWindow window;

    private int mYear, mMonth, mDay;
    private int nowYear, nowMonth, nowDay;
    private int endYear, endMonth, endDay;

    private FrameLayout dateSelecetPopLayout;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2014-12-22 19:33:25 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        bottom_in = AnimationUtils.loadAnimation(context, R.anim.bottom_in);
        bottom_out = AnimationUtils.loadAnimation(context, R.anim.bottom_out);
        close_alpha = AnimationUtils.loadAnimation(context, R.anim.close_alpha);
        show_alpha = AnimationUtils.loadAnimation(context, R.anim.show_alpha);

        // 获取今天的日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate2 = new Date(System.currentTimeMillis() );
        String dateOk = sdf.format(newDate2);

        String[] a1 = dateOk.split(" ");
        String[] ymd1 = a1[0].split("-");// 年月日数组

        endYear = Integer.parseInt(ymd1[0]);
        endMonth = Integer.parseInt(ymd1[1]);
        endDay = Integer.parseInt(ymd1[2]);
        exit = (RelativeLayout) contentView.findViewById(R.id.exitPop);
        exit.setOnClickListener(this);
        dateSelecetPopLayout= (FrameLayout) contentView.findViewById(R.id.dateSelecetPopLayout);
        dateSelecetPopLayout.setOnClickListener(this);
        year = (WheelView) contentView.findViewById(R.id.year);
        month = (WheelView) contentView.findViewById(R.id.month);
        day = (WheelView) contentView.findViewById(R.id.day);
        year.setAdapter(new NumericWheelAdapter(nowYear, endYear));
        year.setLabel("年");
        year.setVisibleItems(7);
        if (endYear == nowYear) {
            year.setCanScroll(false);
        } else {

            year.setCanScroll(true);
        }
        year.setCurrentItem(mYear - 1970);

        month.setAdapter(new NumericWheelAdapter(nowMonth, endMonth));
        month.setLabel("月");
        month.setVisibleItems(7);
        month.setCyclic(true);
        month.setCurrentItem(mMonth);

        day.setAdapter(new NumericWheelAdapter(nowDay, endDay));
        day.setLabel("日");
        day.setVisibleItems(7);
        day.setCyclic(true);
        day.setCurrentItem(mDay - 1);

        year.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                getDate();
                setMINMonth();
                setMINDay();
            }
        });

        month.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                getDate();
                setMINDay();

            }
        });

        day.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                getDate();
            }
        });

        content = (LinearLayout) contentView.findViewById(R.id.content);
        cancel = (TextView) contentView.findViewById(R.id.cancel);
        ok = (TextView) contentView.findViewById(R.id.ok);
        content.setOnClickListener(this);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);

        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        contentView.setOnClickListener(this);
        SystemBarTintManager manager=new SystemBarTintManager((Activity)context);
        manager.setNavigationBarTintEnabled(true);
        int navigationBarHeight=manager.getConfig().getNavigationBarHeight();
        contentView.setPadding(0,0,0,navigationBarHeight);
        window = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

    }

    public PopMyinfoDateSelect(Context context, View v) {
        this.v = v;
        this.context = context;
        contentView = (View) ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.popwindow_myinfo_date, null);
        if(Build.VERSION.SDK_INT>=14) {
            contentView.setFitsSystemWindows(true);
        }
        Calendar mDate = Calendar.getInstance();
        mYear = mDate.get(Calendar.YEAR);
        nowYear = mYear;
        int month = mDate.get(Calendar.MONTH);
        mMonth = month;
        nowMonth = mMonth;
        mDay = mDate.get(Calendar.DAY_OF_MONTH);
        nowDay = mDay;

        findViews();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                if (dataListener != null) {
                    dataListener.onDateSelect(mYear, mMonth, mDay);
                }
                closeWindow();
                break;
            case R.id.cancel:
            case R.id.exitPop:
            case R.id.dateSelecetPopLayout:
                closeWindow();
                break;

        }
    }

    public void show() {
        window.showAtLocation(v, Gravity.CENTER | Gravity.CENTER, 0, 0);
        content.startAnimation(bottom_in);
        exit.startAnimation(show_alpha);
        exit.setVisibility(View.VISIBLE);
    }

    private void closeWindow() {
        window.dismiss();
        if(dataListener!=null){
            dataListener.onWindowClose();
        }
    }

    private Date1SelectListener dataListener = null;

    public interface Date1SelectListener {
        public void onDateSelect(int year, int month, int day);
        public void onWindowClose();
    }

    public void setDate1SelectListener(Date1SelectListener dataListener) {
        this.dataListener = dataListener;
    }

    /**
     * 设置默认时间
     */
    public void setCurrentTime(int year, int month, int day) {
        this.year.setCurrentItem(year - nowYear);
        if (nowYear == year) {
            this.month.setAdapter(new NumericWheelAdapter(nowMonth, 12));
            this.month.setCurrentItem(month - nowMonth - 1);
        } else {
            this.month.setAdapter(new NumericWheelAdapter(1, 12));
            this.month.setCurrentItem(month - 1);
        }
        setMINDay();
        if (nowYear == year && month - 1 == nowMonth) {
            this.day.setCurrentItem(day - nowDay);
        } else {
            this.day.setCurrentItem(day - 1);
        }
        mYear = year;
        mMonth = month;
        mDay = day;

        setMINMonth();
        setMINDay();
    }

    private void setMINDay() {
        int maxday;
        switch (mMonth) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                maxday = 31;
                break;
            case 2:
                if (mYear % 4 == 0 && mYear % 100 != 0 || mYear % 400 == 0) {
                    maxday = 29;
                } else {
                    maxday = 28;
                }
                break;
            default:
                maxday = 30;
                break;
        }
        if (mYear == nowYear && (mMonth - 1) == nowMonth) {
            if (mMonth == endMonth)
                day.setAdapter(new NumericWheelAdapter(nowDay, endDay));
            else {
                day.setAdapter(new NumericWheelAdapter(nowDay, maxday));
            }
        } else {
            if (mMonth == endMonth)
                day.setAdapter(new NumericWheelAdapter(1, endDay));
            else {
                day.setAdapter(new NumericWheelAdapter(1, maxday));
            }
        }
    }

    private void setMINMonth() {
        if (mYear == nowYear) {
            if(endMonth>=nowMonth + 1) {
                month.setAdapter(new NumericWheelAdapter(nowMonth + 1, endMonth));
            }else{
                month.setAdapter(new NumericWheelAdapter(nowMonth + 1, 12));
            }
        } else {
            month.setAdapter(new NumericWheelAdapter(1, endMonth));
        }
    }

    private void getDate() {
        mYear = year.getCurrentItem() + nowYear;// 得到当前选中年
        if (mYear == nowYear) {
            mMonth = month.getCurrentItem() + nowMonth + 1;// 是本年的当前选中月
        } else {
            mMonth = month.getCurrentItem() + 1;// 非本年的当前选中月
        }
        if (mYear == nowYear && (mMonth - 1) == nowMonth) {
            mDay = day.getCurrentItem() + nowDay;// 是本年本月的当前选中月
            // mDay = day.getCurrentItem();
        } else {
            mDay = day.getCurrentItem() + 1;// 非本年本月的当前选中月
        }
    }
}
