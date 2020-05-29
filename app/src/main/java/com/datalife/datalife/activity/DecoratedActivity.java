package com.datalife.datalife.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.FatMemberAdapter;
import com.datalife.datalife.adapter.MemberItemAdapter;
import com.datalife.datalife.adapter.TestAdapter;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.bean.DateBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.bean.TestBean;
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
import com.datalife.datalife.widget.RecycleViewDivider;
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
 * Created by LG on 2018/5/4.
 */

public class DecoratedActivity extends BaseActivity implements View.OnTouchListener, RecycleViewScrollHelper.OnScrollPositionChangedListener,DecoratedContract.DecoratedView {

    @BindView(R.id.cal)
    CustomCalendar cal;
    @BindView(R.id.rv_test)
    RecyclerView recyclerView;
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
//    @BindView(R.id.refresh)
//    RecyclerView refreshLayout;


    public static int lineweek = 1;
    public static int selectDay = 0;
    public static int MONTHMODE = 1;
    public static int WEEKMODE = 2;
    public static int MODE = 0;
    private static final int FLING_MIN_DISTANCE = 50;   //最小距离
    private static final int FLING_MIN_VELOCITY = 0;    //最小速度
    private static final int FLING_TOP_MIN_DISTANCE = 100;   //上下最小距离
    private static final int FLING_TOP_MIN_VELOCITY = 0;    //上下最小速度
    private GestureDetector mGestureDetector;
    private GestureDetector mSecondGestureDetector;
    private GestureDetector mLayoutGestureDetector;
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

    private PopupWindow popupWindow = null;

    private String[] array = new String[]{"体重","BMI","体脂率","肌肉率","水分","去脂体重","骨量","内脏体脂指数","基础代谢率"};

    DecoratedPresenter decoratedPresenter = new DecoratedPresenter(this);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_decorated;
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
//            for (int i =0;i<familyUserInfos.size();i++){
//                if (familyUserInfos.get(i).getMember_Id() == Integer.valueOf(machineBindMemberBeans.get(0).getMember_Id())){
                    mMemberNameTv.setText(familyUserInfo.getMember_Name());
                    mMemberFace.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfo.getMember_Portrait()).getResPic());
//                    familyUserInfo = familyUserInfos.get(i);
//                }
//            }
        }

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

        mGestureDetector = new GestureDetector(this, myGestureListener);
        mSecondGestureDetector = new GestureDetector(this,mySecondGestureListener);
        mLayoutGestureDetector = new GestureDetector(this,myLayoutGestureListener);
        linearLayout.setOnTouchListener(this);
        MODE = WEEKMODE;
        cal.setRenwu(mDateNow, list1,WEEKMODE);
        cal.setOnTouchListener(this);
        cal.setLongClickable(true);
        cal.setOnClickListener(new CustomCalendar.onClickListener() {
            @Override
            public void onLeftRowClick() {
                onRightFling();
            }
            @Override
            public void onRightRowClick() {
                onLeftFling();
            }
            @Override
            public void onTitleClick(String monthStr, Date month) {
//                Toast.makeText(MainActivity.this, "点击了标题："+monthStr, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onWeekClick(int weekIndex, String weekStr) {
//                Toast.makeText(MainActivity.this, "点击了星期："+weekStr, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDayClick(int day, String dayStr, DateBean finish) {
                Log.w("", "点击了日期:"+dayStr);
                    if(arrayList.contains(dayStr)){
//                        recyclerView.setVisibility(View.VISIBLE);
                        decoratedPresenter.onGetListValue("1","50",memberId,DataLifeUtil.MACHINE_FAT,"6",dayStr,dayStr,DeviceData.getUniqueId(DecoratedActivity.this));
                    }else{
                        recyclerView.setVisibility(View.GONE);
                    }
            }
        });

        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        mScrollHelper = new RecycleViewScrollHelper(this);
//        mScrollHelper.setCheckScrollToTopBottomTogether(false);
        mScrollHelper.setCheckScrollToTopFirstBottomAfter(false);
        mScrollHelper.setCheckIfItemViewFullRecycleViewForBottom(true);
        mScrollHelper.setCheckIfItemViewFullRecycleViewForTop(true);
//        mScrollHelper.setTopOffsetFaultTolerance(100);
//        mScrollHelper.setBottomFaultTolerance(100);
//        mScrollHelper.attachToRecycleView(recyclerView);

//        testAdapter = new TestAdapter(this,lists);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setOnTouchListener(this);
//        recyclerView.setAdapter(testAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.computeVerticalScrollOffset();
                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE){
                }

                if (MODE != MONTHMODE) {
                    if (dy > 0) {
                        MODE = MONTHMODE;
                        cal.setRenwu(mDateNow, list1, MONTHMODE);
                    } else if (dy < 0) {
                        MODE = WEEKMODE;
                        cal.setRenwu(mDateNow, list1, WEEKMODE);
                    }
                }
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(DataLifeUtil.BUNDLEMEASURE,measureRecordBeans.get(position));
                bundle.putSerializable(DataLifeUtil.BUNDLEMACHINEMEMBER,familyUserInfo);
                UIHelper.launcherForResultBundle(DecoratedActivity.this, FatTestHistoryActivity.class, 123,bundle);
            }

            @Override
            public void onLongClick(View view, int posotion) {
            }
        }));

//        refreshLayout.setEnabled(false);
        /*refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                MODE = WEEKMODE;
                cal.setRenwu(mDateNow, list1, WEEKMODE);
            }
        });*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        decoratedPresenter.onStop();
    }

    @OnClick({R.id.iv_head_left,R.id.rl_record})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_head_left:
                finish();
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
//                            mElectrocarDiogramLayout.setBackgroundResource(R.color.transparent);
                        linearLayout.setBackgroundResource(R.color.transparent);
                    }
                });
                linearLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
//                    mElectrocarDiogramLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            mMemberId = String.valueOf(machineBindMemberBean.get(position).getMember_Id());
                        /*mCommonChangeListener.getMemberId(String.valueOf(machineBindMemberBeans.get(position).getMember_Id()));
                        MachineBindMemberBean machineBindMember = machineBindMemberBeans.get(position);
                        for(int i = 0;i<familyUserInfos.size();i++){
                            if(familyUserInfos.get(i).getMember_Id() == Integer.valueOf(machineBindMember.getMember_Id())){
                                mAccountTv.setText(familyUserInfos.get(i).getMember_Name());
                                mAccountIv.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfos.get(position).getMember_Portrait()).getResPic());
                            }
                        }*/
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == recyclerView){
            return mSecondGestureDetector.onTouchEvent(event);
        }else if(v == linearLayout){
            return mLayoutGestureDetector.onTouchEvent(event);
        }else {
                return mGestureDetector.onTouchEvent(event);
            }
    }

    GestureDetector.SimpleOnGestureListener myGestureListener = new GestureDetector.SimpleOnGestureListener(){
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e("<--滑动测试-->", "开始滑动");
            float x = e1.getX()-e2.getX();
            float x2 = e2.getX()-e1.getX();
            if(x>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY){
                onLeftFling();
                return true;
            }else if(x2>FLING_MIN_DISTANCE&&Math.abs(velocityX)>FLING_MIN_VELOCITY){
                onRightFling();
                return true;
            }
            float y = e1.getY()-e2.getY();
            float y2 = e2.getY()-e1.getY();
            if(y>FLING_TOP_MIN_DISTANCE&&Math.abs(velocityY)>FLING_TOP_MIN_VELOCITY){
                MODE = MONTHMODE;
                cal.setRenwu(mDateNow, list1, MONTHMODE);
                return true;
            }else if(y2>FLING_TOP_MIN_DISTANCE&&Math.abs(velocityY)>FLING_TOP_MIN_VELOCITY){
                MODE = WEEKMODE;
                cal.setRenwu(mDateNow, list1, WEEKMODE);
                return true;
            }
            return false;
        };
    };

    GestureDetector.SimpleOnGestureListener mySecondGestureListener = new GestureDetector.SimpleOnGestureListener(){
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float y = e1.getY()-e2.getY();
            float y2 = e2.getY()-e1.getY();
            if(y>FLING_TOP_MIN_DISTANCE&&Math.abs(velocityY)>FLING_TOP_MIN_VELOCITY){
                MODE = MONTHMODE;
                cal.setRenwu(mDateNow, list1, MONTHMODE);
                return true;
            }else if(y2>FLING_TOP_MIN_DISTANCE&&Math.abs(velocityY)>FLING_TOP_MIN_VELOCITY){
                MODE = WEEKMODE;
                cal.setRenwu(mDateNow, list1, WEEKMODE);
                return true;
            }
            return false;
        };
    };

    GestureDetector.SimpleOnGestureListener myLayoutGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float y = e1.getY()-e2.getY();
            float y2 = e2.getY()-e1.getY();
            if(y>FLING_TOP_MIN_DISTANCE&&Math.abs(velocityY)>FLING_TOP_MIN_VELOCITY){
                MODE = MONTHMODE;
                cal.setRenwu(mDateNow, list1, MONTHMODE);
                return true;
            }else if(y2>FLING_TOP_MIN_DISTANCE&&Math.abs(velocityY)>FLING_TOP_MIN_VELOCITY){
                MODE = WEEKMODE;
                cal.setRenwu(mDateNow, list1, WEEKMODE);
                return true;
            }
            return false;
        }
    };

    private void onLeftFling(){
        cal.monthChange(1);
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(100);
                    DecoratedActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                            try {
                                Date date = simpleDateFormat.parse(dateStr);
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(date);
                                calendar.add(calendar.MONTH, 1);
                                date=calendar.getTime();
                                dateStr = simpleDateFormat.format(date);
                                setClickDay();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            cal.setRenwu(list1);
                        }
                    });
                }catch (Exception e){
                }
            }
        }.start();
    }

    private void onRightFling(){
        cal.monthChange(-1);
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(100);
                    DecoratedActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                            try {
                                Date date = simpleDateFormat.parse(dateStr);
                                Calendar calendar   =   new GregorianCalendar();
                                calendar.setTime(date);
                                calendar.add(calendar.MONTH, -1);
                                date=calendar.getTime();
                                dateStr = simpleDateFormat.format(date);
                                setClickDay();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            cal.setRenwu(list1);
                        }
                    });
                }catch (Exception e){
                }
            }
        }.start();
    }

    @Override
    public void onScrollToTop() {
        MODE = WEEKMODE;
        cal.setRenwu(mDateNow, list1, WEEKMODE);
    }

    @Override
    public void onScrollToBottom() {

    }

    @Override
    public void onScrollToUnknown(boolean isTopViewVisible, boolean isBottomViewVisible) {
        MODE = MONTHMODE;
        cal.setRenwu(mDateNow, list1, MONTHMODE);
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
            for (int i = 0;i<strs.size();i++){
                str = strs.get(i);
                if(str.length() == 1){
                    str = "0" + str;
                }

                list1.add(new DateBean(dateStr+ "-" + str));
                arrayList.add(list1.get(i).getCal());
            }
            cal.setRenwu(list1);
        }else{
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onfail(String str) {

    }

    @Override
    public void onDataSuccess(ArrayList<MeasureRecordBean> measureRecordBean) {
        this.measureRecordBeans = measureRecordBean;

        testAdapter = null;
        if (testAdapter == null){
            testAdapter = new TestAdapter(this,measureRecordBeans,familyUserInfo);
            recyclerView.setAdapter(testAdapter);
        }else{
            testAdapter.notifyDataSetChanged();
        }
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDataFail(String str) {

    }
}
