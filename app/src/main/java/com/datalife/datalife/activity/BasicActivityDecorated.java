package com.datalife.datalife.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.TestAdapter;
import com.datalife.datalife.bean.BtRecordBean;
import com.datalife.datalife.bean.TestBean;
import com.datalife.datalife.contract.RecordContract;
import com.datalife.datalife.decorators.EventDecorator;
import com.datalife.datalife.decorators.HighlightWeekendsDecorator;
import com.datalife.datalife.decorators.MySelectorDecorator;
import com.datalife.datalife.decorators.OneDayDecorator;
import com.datalife.datalife.interf.RecyclerItemClickListener;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.widget.RecycleViewDivider;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Shows off the most basic usage
 */
public class BasicActivityDecorated extends AppCompatActivity implements OnDateSelectedListener {

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    @BindView(R.id.calendarView)
    MaterialCalendarView widget;
    @BindView(R.id.rv_test)
    RecyclerView recyclerView;
    @BindView(R.id.tv_calendar_title)
    TextView mCalendarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        ButterKnife.bind(this);

        widget.setOnDateChangedListener(this);
//        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        widget.setLeftArrowMask(getResources().getDrawable(R.mipmap.ic_navigation_arrow_back));
        widget.setRightArrowMask(getResources().getDrawable(R.mipmap.ic_navigation_arrow_forward));

        Calendar instance = Calendar.getInstance();
        widget.setSelectedDate(instance.getTime());

//        widget.setTileHeight(70);
//        widget.setTileWidth(90);
        Calendar instance1 = Calendar.getInstance();
        instance1.set(2010, Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(2050, Calendar.DECEMBER, 31);

        CharSequence[] array = new CharSequence[]{Html.fromHtml("<font color=\"#32b2fb\">"+"日"+"</font>"),"一", "二", "三", "四", "五", Html.fromHtml("<font color=\"#32b2fb\">"+"六"+"</font>")};
        widget.setWeekDayFormatter(new ArrayWeekDayFormatter(array));
        widget.setTopbarVisible(false);
        widget.setTitleFormatter(new DateFormatTitleFormatter(new SimpleDateFormat("yyyy年MM月")));
        // 设置你选中日期的背景底色
        widget.setSelectionColor(getResources().getColor(R.color.blue_bg));

        widget.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Date date1 = date.getDate();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月");
                mCalendarTitle.setText(simpleDateFormat.format(date1) + "");
            }
        });

        widget.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();

        widget.addDecorators(
                new MySelectorDecorator(this),
                new HighlightWeekendsDecorator(),
                oneDayDecorator
        );

        List<List<TestBean>> lists = new ArrayList<>();

        for (int j = 0;j <2;j++) {
            List<TestBean> testBeans = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                TestBean testBean = new TestBean();
                testBean.setTestTitle("体重");
                testBean.setTestValue("75KG");
                testBean.setTextArraw(R.mipmap.ic_arraw_top);
                testBeans.add(testBean);
            }
            lists.add(testBeans);
        }

        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

//        TestAdapter testAdapter = new TestAdapter(this,lists);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(testAdapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 40, getResources().getColor(R.color.grey_bg)));
//        recyclerView.addItemDecoration(null);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UIHelper.launcherForResult(BasicActivityDecorated.this, FatTestHistoryActivity.class, 123);
            }

            @Override
            public void onLongClick(View view, int posotion) {

            }
        }));

        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String str = simpleDateFormat.format(date.getDate());
        widget.invalidateDecorators();
    }

    @OnClick({R.id.ic_left_arraw,R.id.ic_right_arraw})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.ic_left_arraw:
                widget.goToPrevious();
                break;

            case R.id.ic_right_arraw:
                widget.goToNext();
                break;
        }
    }

    /**
     * Simulate an API call to show how to add decorators
     */
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {

            Calendar calendar = Calendar.getInstance();

            ArrayList<CalendarDay> dates = new ArrayList<>();

            String date = "2018-04-27";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date str = simpleDateFormat.parse(date);
                calendar.setTime(str);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar.add(Calendar.MONTH, -2);

            for (int i = 0; i < 30; i++) {
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
                calendar.add(Calendar.DATE, 7);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            widget.addDecorator(new EventDecorator(getResources().getColor(R.color.bg_toolbar_title), calendarDays));
        }
    }
}
