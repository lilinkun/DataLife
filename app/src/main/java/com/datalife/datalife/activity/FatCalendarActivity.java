package com.datalife.datalife.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.FatMemberAdapter;
import com.datalife.datalife.adapter.TestAdapter;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.bean.DateBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.bean.TestBean;
import com.datalife.datalife.calendar.OnCalendarClickListener;
import com.datalife.datalife.calendar.schedule.ScheduleLayout;
import com.datalife.datalife.calendar.schedule.ScheduleRecyclerView;
import com.datalife.datalife.contract.DecoratedContract;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.interf.RecyclerItemClickListener;
import com.datalife.datalife.presenter.DecoratedPresenter;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DefaultPicEnum;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.RecycleViewScrollHelper;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.widget.CustomCalendar;
import com.datalife.datalife.widget.CustomTitleBar;
import com.datalife.datalife.widget.RoundImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/8/17.
 */

public class FatCalendarActivity extends BaseActivity implements DecoratedContract.DecoratedView, OnCalendarClickListener {

    @BindView(R.id.slSchedule)
    ScheduleLayout slSchedule;
    @BindView(R.id.rvScheduleList)
    ScheduleRecyclerView rvScheduleList;
    @BindView(R.id.custom_title)
    CustomTitleBar customTitleBar;
    @BindView(R.id.ll_content)
    LinearLayout linearLayout;
    @BindView(R.id.tv_membername)
    TextView mMemberNameTv;
    @BindView(R.id.roundiv_member)
    RoundImageView mMemberFace;
    @BindView(R.id.rl_record)
    RelativeLayout relativeLayout;
    @BindView(R.id.tv_current_date)
    TextView dateView;


    public static int lineweek = 1;
    public static int selectDay = 0;
    public static int MONTHMODE = 1;
    public static int WEEKMODE = 2;
    public static int MODE = 0;
    private List<DateBean> list1;
    RecycleViewScrollHelper mScrollHelper = null;
    private ArrayList<String> arrayList = new ArrayList();
    private String memberId = "";
    private String dateStr = "";
    private TestAdapter testAdapter = null;
    private List<List<TestBean>> lists = null;
    private ArrayList<MeasureRecordBean> measureRecordBeans = null;
    private String mDateNow = "";
    private FamilyUserInfo familyUserInfo = null;
    private ArrayList<MachineBindMemberBean> machineBindMemberBeans = null;
    private List<FamilyUserInfo> familyUserInfos = null;
    private String machineBindId;
    private boolean isAll = false;
    private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;
    private List<Integer> integers ;

    private PopupWindow popupWindow = null;

    private String[] array = new String[]{"体重","BMI","体脂率","肌肉率","水分","去脂体重","骨量","内脏体脂指数","基础代谢率"};

    DecoratedPresenter decoratedPresenter = new DecoratedPresenter(this);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initEventAndData() {

        decoratedPresenter.onCreate();
        decoratedPresenter.attachView(this);
        Bundle bundle = getIntent().getBundleExtra(DataLifeUtil.BUNDLEMEMBER);
        familyUserInfos = DBManager.getInstance(this).queryFamilyUserInfoList();
        if (bundle != null) {
            memberId = bundle.getString(DataLifeUtil.BUNDLEMEMBERID);
            for (FamilyUserInfo familyUserInfo : familyUserInfos){
                if(familyUserInfo.getMember_Id() == Integer.valueOf(memberId)){
                    this.familyUserInfo = familyUserInfo;
                }
            }
            machineBindId = bundle.getString("MachineId");
        }else {
            familyUserInfo = familyUserInfos.get(0);
            memberId = String.valueOf(familyUserInfo.getMember_Id());
        }

        if (machineBindId == null || machineBindId.equals("")){
            machineBindMemberBeans = (ArrayList<MachineBindMemberBean>) DBManager.getInstance(this).queryMachineBindMemberBeanList();
            isAll = true;
            mMemberNameTv.setText(familyUserInfo.getMember_Name());
            mMemberFace.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfo.getMember_Portrait()).getResPic());
        }else {
            machineBindMemberBeans = (ArrayList<MachineBindMemberBean>) DBManager.getInstance(this).queryMachineBindMemberBeanList(machineBindId);
            isAll = true;
            mMemberNameTv.setText(familyUserInfo.getMember_Name());
            mMemberFace.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfo.getMember_Portrait()).getResPic());
        }

        initDate();
        slSchedule.setOnCalendarClickListener(this);
        initScheduleList();

        selectDay = 0;
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        dateStr = simpleDateFormat.format(date);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        mDateNow = simpleDateFormat1.format(date);
        setClickDay();
        list1 = new ArrayList<>();

        for (int i = 0;i<list1.size();i++){
            arrayList.add(list1.get(i).getCal());
        }
        MODE = WEEKMODE;

        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;


        String ds = simpleDateFormat1.format(date);
        decoratedPresenter.onGetListValue("1","50",memberId,DataLifeUtil.MACHINE_FAT,"6",ds,ds,DeviceData.getUniqueId(this));

    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        setCurrentSelectDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy" + "年" + "MM" + "月");
        String dateStr = simpleDateFormat.format(date);
        dateView.setText(dateStr);
    }


    private void initScheduleList() {
        rvScheduleList = slSchedule.getSchedulerRecyclerView();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvScheduleList.setLayoutManager(manager);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        rvScheduleList.setItemAnimator(itemAnimator);
        rvScheduleList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(DataLifeUtil.BUNDLEMEASURE,measureRecordBeans.get(position));
                bundle.putSerializable(DataLifeUtil.BUNDLEMACHINEMEMBER,familyUserInfo);
                UIHelper.launcherForResultBundle(FatCalendarActivity.this, FatTestHistoryActivity.class, 123,bundle);
            }

            @Override
            public void onLongClick(View view, int posotion) {
            }
        }));

    }

    private void setCurrentSelectDate(int year, int month, int day) {
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        decoratedPresenter.onStop();
    }

    @OnClick({R.id.iv_head_left,R.id.rl_record,R.id.ic_left,R.id.ic_right})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_head_left:
                finish();
                break;

            case R.id.ic_left:
                slSchedule.clickIv("left");
                break;

            case R.id.ic_right:
                slSchedule.clickIv("right");
                break;

            case R.id.rl_record:
                View contentView = LayoutInflater.from(this).inflate(R.layout.member_popup_listview, null);

                RelativeLayout relativeLayout = (RelativeLayout) contentView.findViewById(R.id.rl_add_member);
                GridView gridView = (GridView) contentView.findViewById(R.id.lv_add_member);
                relativeLayout.setVisibility(View.VISIBLE);
                gridView.setAdapter(new FatMemberAdapter(this,machineBindMemberBeans,isAll));

                LinearLayout llx = (LinearLayout) contentView.findViewById(R.id.ll_x);
                llx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow = new PopupWindow(contentView,
                        LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT, true);
                popupWindow.setContentView(contentView);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
//                popupWindow.showAsDropDown(relativeLayout);
                popupWindow.showAtLocation(findViewById(R.id.ll_total), Gravity.FILL,0,0);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        linearLayout.setBackgroundResource(R.color.transparent);
                    }
                });
                linearLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (isAll){
                            familyUserInfo = familyUserInfos.get(position);
                            mMemberFace.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfo.getMember_Portrait()).getResPic());
                            mMemberNameTv.setText(familyUserInfo.getMember_Name());
                            memberId = String.valueOf(familyUserInfo.getMember_Id());
                        }else {
                            for (FamilyUserInfo familyUserInfo : familyUserInfos){
                                if (familyUserInfo.getMember_Id() == Integer.valueOf(machineBindMemberBeans.get(position).getMember_Id())){
                                    mMemberFace.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfo.getMember_Portrait()).getResPic());
                                    mMemberNameTv.setText(familyUserInfo.getMember_Name());
                                    memberId = String.valueOf(familyUserInfo.getMember_Id());
                                }
                            }
                        }
                        setClickDay();

                        popupWindow.dismiss();

                    }
                });
                break;
        }
    }

    //获取日期下有点的日期
    private void setClickDay(){
        decoratedPresenter.onTestDay(memberId,"6",dateStr, DeviceData.getUniqueId(this));
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void onSuccess(ArrayList<String> strs) {
        if (strs.size()>0){
            list1.clear();
            String str = "";
            integers = new ArrayList<>();
            for (int i = 0;i<strs.size();i++){
                str = strs.get(i);
                integers.add(Integer.valueOf(str));

            }
            slSchedule.addTaskHints(integers);
        }else{
            rvScheduleList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onfail(String str) {

    }

    @Override
    public void onDataSuccess(ArrayList<MeasureRecordBean> measureRecordBean) {
        this.measureRecordBeans = measureRecordBean;

        if (testAdapter == null){
            testAdapter = new TestAdapter(this,measureRecordBeans,familyUserInfo);
            rvScheduleList.setAdapter(testAdapter);
        }else{
            testAdapter.setMeasureRecordBeans(measureRecordBean,familyUserInfo);
            testAdapter.notifyDataSetChanged();
        }
        rvScheduleList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDataFail(String str) {

    }

    @Override
    public void onClickDate(int year, int month, int day) {
        setCurrentSelectDate(year, month, day);
        int mm = month + 1;
        String MM = "";
        if (mm < 10){
            MM = "0" + mm;
        }else {
            MM = "" + mm;
        }
        dateView.setText(year + "年" + MM + "月");

        String dayStr = year + "-" + MM + "-" + day;

        if(integers != null && integers.size() > 0) {
            if (integers.contains(day)) {
                decoratedPresenter.onGetListValue("1","50",memberId,DataLifeUtil.MACHINE_FAT,"6",dayStr,dayStr,DeviceData.getUniqueId(this));
            }else {
                if (rvScheduleList != null) {
                    rvScheduleList.setVisibility(View.GONE);
                }
            }
        }else {
            if (rvScheduleList != null) {
                rvScheduleList.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPageChange(int year, int month, int day) {

    }


}

