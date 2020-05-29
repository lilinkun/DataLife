package com.datalife.datalife.activity;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.EcgRecordAdapter;
import com.datalife.datalife.adapter.FatMemberAdapter;
import com.datalife.datalife.adapter.FragmentAdapter;
import com.datalife.datalife.adapter.FragmentsAdapter;
import com.datalife.datalife.adapter.MemberItemAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.base.BaseRecordFragment;
import com.datalife.datalife.bean.BtRecordBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.contract.RecordContract;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.fragment.BpFragment;
import com.datalife.datalife.fragment.BpRecordFragment;
import com.datalife.datalife.fragment.BtFragment;
import com.datalife.datalife.fragment.BtRecordFragment;
import com.datalife.datalife.fragment.ECGFragment;
import com.datalife.datalife.fragment.EcgRecordFragment;
import com.datalife.datalife.fragment.HrRecordFragment;
import com.datalife.datalife.fragment.SPO2HFragment;
import com.datalife.datalife.fragment.Spo2hRecordFragment;
import com.datalife.datalife.interf.OnBtRecordLisener;
import com.datalife.datalife.presenter.RecordPresenter;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DefaultPicEnum;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.widget.CustomTitleBar;
import com.datalife.datalife.widget.CustomViewPager;
import com.datalife.datalife.widget.RoundImageView;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/2/5.
 */

public class RecordActivity extends BaseActivity implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener, RecordContract.View,OnBtRecordLisener {


    private final SparseArray<BaseRecordFragment> sparseArray = new SparseArray<>();

    @BindView(R.id.view_page)
    CustomViewPager customViewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.custom_title)
    CustomTitleBar customTitleBar;
    @BindView(R.id.ll_content)
    LinearLayout linearLayout;
    @BindView(R.id.tv_membername)
    TextView mMemberName;
    @BindView(R.id.roundiv_member)
    ImageView mRoundMember;
    @BindView(R.id.rl_record)
    RelativeLayout mRlMember;

    private int state;
    public static int MYDAY = 1;
    public static int MYWEEK = 2;
    public static int MYMONTH = 3;
    public static int MYYEAR = 4;

    public static int SPO2HRECORD = 0;
    public static int HRRECORD = 1;
    public static int BPRECORD = 2;
    public static int BTRECORD = 3;
    public static int ECGRECORD = 4;

    private ArrayList<MachineBindMemberBean> machineBindMemberBeans = null;
    private PopupWindow popupWindow = null;

    String memberID = "";
    private String mDateNowStr;
    private String mDateNowWeekStr;

    private BtRecordFragment btRecordFragment;
    private EcgRecordFragment ecgRecordFragment;
    private Spo2hRecordFragment spo2hRecordFragment;
    private HrRecordFragment hrRecordFragment;
    private BpRecordFragment bpRecordFragment;
    private List<FamilyUserInfo> familyUserInfos = null;
    private FamilyUserInfo userInfos = null;
    private int page = 0;
    private boolean isAll = false;

    protected RecordPresenter recordPresenter = new RecordPresenter(this);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    protected void initEventAndData() {

        familyUserInfos = DBManager.getInstance(this).queryFamilyUserInfoList();
        ArrayList<MachineBean> machineBeans = (ArrayList<MachineBean>)DBManager.getInstance(this).queryMachineBeanList();

        if (getIntent()!= null){
            Bundle bundle = getIntent().getBundleExtra(DataLifeUtil.BUNDLEMEMBER);
            if (bundle != null) {
                memberID = bundle.getString(DataLifeUtil.BUNDLEMEMBERID);
                if (String.valueOf(bundle.getInt(DataLifeUtil.PAGE)) != null){
                    page = bundle.getInt(DataLifeUtil.PAGE);
                }
                for (FamilyUserInfo familyUserInfo : familyUserInfos) {
                    if (String.valueOf(familyUserInfo.getMember_Id()).equals(memberID)) {
                        mMemberName.setText(familyUserInfo.getMember_Name());
                        mRoundMember.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfo.getMember_Portrait()).getResPic());
                    }
                }
            }else {
                memberID = String.valueOf(familyUserInfos.get(0).getMember_Id());
                mMemberName.setText(familyUserInfos.get(0).getMember_Name());
                mRoundMember.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfos.get(0).getMember_Portrait()).getResPic());
            }
        }
        recordPresenter.onCreate();
        recordPresenter.attachView(this);

        btRecordFragment = new BtRecordFragment();
        ecgRecordFragment = new EcgRecordFragment();
        spo2hRecordFragment = new Spo2hRecordFragment();
        hrRecordFragment = new HrRecordFragment();
        bpRecordFragment = new BpRecordFragment();

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        getMenusFragments();
        adapter.setFragments(sparseArray);
        Bundle bundle1 = new Bundle();
        bundle1.putString(DataLifeUtil.BUNDLEMEMBERID,memberID);
        adapter.setBundle(bundle1);
        customViewPager.setOffscreenPageLimit(4);
        customViewPager.setPageMargin(10);
        customViewPager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(this);
        customViewPager.addOnPageChangeListener(this);
        customViewPager.setCurrentItem(0, false);
        customViewPager.setCanScroll(false);
        tabLayout.setupWithViewPager(customViewPager);

        customViewPager.setCurrentItem(page);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mDateNowStr = simpleDateFormat.format(date);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.add(Calendar.DAY_OF_MONTH, -7);
        Date date1 = calendar1.getTime();
        mDateNowWeekStr = simpleDateFormat.format(date1);

        recordPresenter.onGetListValue("1",IDatalifeConstant.ShowCount,memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.SPO2HINT),"","", ProApplication.SESSIONID);
        recordPresenter.onGetBtRecordValue(memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.SPO2HINT),ProApplication.SESSIONID);

        for (FamilyUserInfo userInfos : familyUserInfos){
            if (userInfos.getMember_Id() == Integer.valueOf(memberID)){
                this.userInfos = userInfos;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recordPresenter.onStop();
    }

    @OnClick({R.id.iv_head_left,R.id.rl_record})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_head_left:
                finish();
                break;

            case R.id.rl_record:

                View contentView = LayoutInflater.from(this).inflate(R.layout.member_popup_listview, null);

                RelativeLayout relativeLayout = (RelativeLayout) contentView.findViewById(R.id.rl_add_member);
                GridView gridView = (GridView) contentView.findViewById(R.id.lv_add_member);
                relativeLayout.setVisibility(View.VISIBLE);
                gridView.setAdapter(new FatMemberAdapter(this,null,isAll));

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
                        FamilyUserInfo familyUserInfo = familyUserInfos.get(position);
                                mRoundMember.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfo.getMember_Portrait()).getResPic());
                                mMemberName.setText(familyUserInfo.getMember_Name());
                                memberID = String.valueOf(familyUserInfo.getMember_Id());
                                onMemberClick(customViewPager.getCurrentItem());
                        popupWindow.dismiss();
                    }
                });
                break;
        }
    }

    private void getMenusFragments() {
        sparseArray.put(SPO2HRECORD, spo2hRecordFragment);
        sparseArray.put(HRRECORD, hrRecordFragment);
        sparseArray.put(BPRECORD, bpRecordFragment);
        sparseArray.put(BTRECORD, btRecordFragment);
        sparseArray.put(ECGRECORD, ecgRecordFragment);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == SPO2HRECORD){
//            if(spo2hRecordFragment.measureRecordBeans == null) {
//            spo2hRecordFragment.measureRecordBeans.clear();
//            if(spo2hRecordFragment.measureRecordBeans == null) {
             recordPresenter.onGetListValue(String.valueOf(1), IDatalifeConstant.ShowCount, memberID, DataLifeUtil.MACHINE_HEALTH, String.valueOf(IDatalifeConstant.SPO2HINT), "", "", ProApplication.SESSIONID);
             recordPresenter.onGetBtRecordValue(memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.SPO2HINT),ProApplication.SESSIONID);
//            }
        }else if(position == HRRECORD){
//            if(hrRecordFragment.measureRecordBeans == null){
                recordPresenter.onGetListValue(String.valueOf(1),IDatalifeConstant.ShowCount,memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.ECGINT),"","", ProApplication.SESSIONID);
                recordPresenter.onGetBtRecordValue(memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.ECGINT),ProApplication.SESSIONID);
//            }
        }else if(position == BPRECORD){
//            if (bpRecordFragment.measureRecordBeans == null) {
                recordPresenter.onGetListValue(String.valueOf(1), IDatalifeConstant.ShowCount, memberID, DataLifeUtil.MACHINE_HEALTH, String.valueOf(IDatalifeConstant.BPINT), "", "", ProApplication.SESSIONID);
                recordPresenter.onGetBtRecordValue(memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.BPINT),ProApplication.SESSIONID);
//            }
        }else if(position == BTRECORD){
//            if (btRecordFragment.measureRecordBeans == null) {
                recordPresenter.onGetListValue(String.valueOf(1),IDatalifeConstant.ShowCount,memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.BTINT),"","", ProApplication.SESSIONID);
                recordPresenter.onGetBtRecordValue(memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.BTINT),ProApplication.SESSIONID);
//            }
        }else if(position == ECGRECORD){
            recordPresenter.onGetListValue(String.valueOf(1),IDatalifeConstant.ShowCount,memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.ECGINT),"","", ProApplication.SESSIONID);
        }
    }

    public void onMemberClick(int position){
        if (position == SPO2HRECORD){
            recordPresenter.onGetListValue(String.valueOf(1), IDatalifeConstant.ShowCount, memberID, DataLifeUtil.MACHINE_HEALTH, String.valueOf(IDatalifeConstant.SPO2HINT), "", "", ProApplication.SESSIONID);
            recordPresenter.onGetBtRecordValue(memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.SPO2HINT),ProApplication.SESSIONID);
        }else if(position == HRRECORD){
            recordPresenter.onGetListValue(String.valueOf(1),IDatalifeConstant.ShowCount,memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.ECGINT),"","", ProApplication.SESSIONID);
            recordPresenter.onGetBtRecordValue(memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.ECGINT),ProApplication.SESSIONID);
        }else if(position == BPRECORD){
            recordPresenter.onGetListValue(String.valueOf(1), IDatalifeConstant.ShowCount, memberID, DataLifeUtil.MACHINE_HEALTH, String.valueOf(IDatalifeConstant.BPINT), "", "", ProApplication.SESSIONID);
            recordPresenter.onGetBtRecordValue(memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.BPINT),ProApplication.SESSIONID);
        }else if(position == BTRECORD){
            recordPresenter.onGetListValue(String.valueOf(1),IDatalifeConstant.ShowCount,memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.BTINT),"","", ProApplication.SESSIONID);
            recordPresenter.onGetBtRecordValue(memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.BTINT),ProApplication.SESSIONID);
        }else if(position == ECGRECORD){
            recordPresenter.onGetListValue(String.valueOf(1),IDatalifeConstant.ShowCount,memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.ECGINT),"","", ProApplication.SESSIONID);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void onSuccess(ArrayList<BtRecordBean> newsInfos) {
        if (newsInfos != null && newsInfos.size()>0){
            if(newsInfos.get(0).getProject_Id().equals(String.valueOf(IDatalifeConstant.BTINT))){
                btRecordFragment.onAvg(newsInfos);
            }else if(newsInfos.get(0).getProject_Id().equals(String.valueOf(IDatalifeConstant.SPO2HINT))){
                spo2hRecordFragment.onAvg(newsInfos);
            }else if(newsInfos.get(0).getProject_Id().equals(String.valueOf(IDatalifeConstant.BPINT))){
                bpRecordFragment.onAvg(newsInfos);
            }else if(newsInfos.get(0).getProject_Id().equals(String.valueOf(IDatalifeConstant.ECGINT))){
                hrRecordFragment.onAvg(newsInfos);
            }
        }
    }

    @Override
    public void onfail(String str) {
//        btRecordFragment.onfail(str);
//        spo2hRecordFragment.onfail(str);
//        hrRecordFragment.onfail(str);
//        ecgRecordFragment.onFail();
    }

    @Override
    public void onSuccess(Object o) {
        ArrayList<MeasureRecordBean> measureRecordBeans = (ArrayList<MeasureRecordBean>) o;

        if (measureRecordBeans.size() > 0) {
            if (measureRecordBeans.get(0).getProjectId().equals(String.valueOf(IDatalifeConstant.BTINT))) {
                btRecordFragment.onSuccess(measureRecordBeans);
            } else if (measureRecordBeans.get(0).getProjectId().equals(String.valueOf(IDatalifeConstant.ECGINT))){
                if (customViewPager.getCurrentItem() == 4) {
                    ecgRecordFragment.onSuccess(measureRecordBeans, 1);
                }else if (customViewPager.getCurrentItem() == 1){
                    hrRecordFragment.onSuccess(measureRecordBeans,1);
                }
            } else if (measureRecordBeans.get(0).getProjectId().equals(String.valueOf(IDatalifeConstant.SPO2HINT))){
                spo2hRecordFragment.onSuccess(measureRecordBeans,1);
            }else if (measureRecordBeans.get(0).getProjectId().equals(String.valueOf(IDatalifeConstant.BPINT))){
                bpRecordFragment.onSuccess(measureRecordBeans);
            }
        }

    }

    @Override
    public void onEcgFail(String str) {
        ecgRecordFragment.onFail(str);
    }

    @Override
    public void onBtFail(String str) {
        btRecordFragment.onfail(str);
    }

    @Override
    public void onBpFail(String str) {
        bpRecordFragment.onfail(str);
    }

    @Override
    public void onHrFail(String str) {
        hrRecordFragment.onfail(str);
    }

    @Override
    public void onSpo2hFail(String str) {
        spo2hRecordFragment.onfail(str);
    }

    @Override
    public void onBtIntent(int Date, int pageIndex, String startDate, String finishDate) {
        recordPresenter.onGetListValue(String.valueOf(pageIndex),IDatalifeConstant.ShowCount,memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.BTINT),startDate,finishDate, ProApplication.SESSIONID);
    }

    @Override
    public void onSpo2hIntent(int Date, int pageIndex, String startDate, String finishDate) {
        recordPresenter.onGetListValue(String.valueOf(pageIndex),IDatalifeConstant.ShowCount,memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.SPO2HINT),startDate,finishDate, ProApplication.SESSIONID);
    }

    @Override
    public void onHrIntent(int Date, int pageIndex, String startDate, String finishDate) {
        recordPresenter.onGetListValue(String.valueOf(pageIndex),IDatalifeConstant.ShowCount,memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.ECGINT),startDate,finishDate, ProApplication.SESSIONID);
    }

    @Override
    public void onEcgIntent(int date ,int pageIndex) {
        recordPresenter.onGetListValue(String.valueOf(pageIndex),IDatalifeConstant.ShowCount,memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.ECGINT),"","", ProApplication.SESSIONID);
    }

    @Override
    public void onBpIntent(int Date, int pageIndex, String startDate, String finishDate) {
        recordPresenter.onGetListValue(String.valueOf(pageIndex),IDatalifeConstant.ShowCount,memberID,DataLifeUtil.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.BPINT),startDate,finishDate, ProApplication.SESSIONID);
    }
}
