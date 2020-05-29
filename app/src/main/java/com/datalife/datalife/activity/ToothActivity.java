package com.datalife.datalife.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.ToothModeAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.dao.BrushManyBean;
import com.datalife.datalife.bean.BrushUseCount;
import com.datalife.datalife.bean.RedEnvelopeEntity;
import com.datalife.datalife.contract.BrushSaveContract;
import com.datalife.datalife.dao.BrushBean;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.dao.WxUserInfo;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.presenter.BrushPresenter;
import com.datalife.datalife.util.AllToothMode;
import com.datalife.datalife.util.AllToothTime;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.Eyes;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.SpUtils;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.util.UToast;
import com.datalife.datalife.widget.ToothDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import aicare.net.cn.toothbrushlibrary.bleprofile.BleProfileService;
import aicare.net.cn.toothbrushlibrary.bleprofile.BleProfileServiceReadyActivity;
import aicare.net.cn.toothbrushlibrary.entity.BatteryState;
import aicare.net.cn.toothbrushlibrary.entity.BrushDevice;
import aicare.net.cn.toothbrushlibrary.entity.GearBean;
import aicare.net.cn.toothbrushlibrary.entity.HistoryData;
import aicare.net.cn.toothbrushlibrary.entity.WorkData;
import aicare.net.cn.toothbrushlibrary.entity.WorkState;
import aicare.net.cn.toothbrushlibrary.toothbrush.ToothbrushService;
import aicare.net.cn.toothbrushlibrary.utils.L;
import aicare.net.cn.toothbrushlibrary.utils.ToothbrushBleConfig;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by LG on 2018/6/13.
 */

public class ToothActivity extends BleProfileServiceReadyActivity implements ToothModeAdapter.OnItemClickListener,BrushSaveContract.BrushView {

    @BindView(R.id.iv_head_tooth)
    ImageView mConnectIv;
    @BindView(R.id.iv_head_energer)
    ImageView mEnergeIv;
    @BindView(R.id.iv_heart_left)
    ImageView mIvHeartLeft;
    @BindView(R.id.iv_heart_center)
    ImageView mIvHeartCenter;
    @BindView(R.id.iv_heart_right)
    ImageView mIvHeartRight;
    @BindView(R.id.ll_tooth_mode)
    LinearLayout mLlToothMode;
    @BindView(R.id.ll_tooth)
    LinearLayout mLlTooth;
    @BindView(R.id.tv_mode)
    TextView mToothMode;
    @BindView(R.id.tv_tooth_time)
    TextView mToothTime;
    @BindView(R.id.tv_tooth_count)
    TextView mBrushCount;
    @BindView(R.id.ll_tooth_surplus)
    LinearLayout mBrushHeadLayout;
    @BindView(R.id.tv_day)
    TextView mTvDay;
    @BindView(R.id.tv_fraction)
    TextView mFractionTv;
    @BindView(R.id.tv_brush_tip)
    TextView mTipTv;
    @BindView(R.id.gif_iv)
    GifImageView gifImageView;
    @BindView(R.id.tv_brush_level)
    TextView tvBrushLevel;

    private RecyclerView mRvTooth;

    Unbinder unbinder;
    private final static String TAG = "ToothActivity";
    private ToothbrushService.ToothbrushBinder binder;
    private final static String PAIR_INFO = "pair_info";
    private long timeMills;
    private String mBlueToothAddress = "";
    private String[] authState,setStateArr,powerArr,durationArr,modeArr;
    private ToothDialog toothDialog;
    private boolean isAuth = false;
    private PopupWindow popupWindow;
    private WorkState workState;
    private AllToothMode[] allToothMode = AllToothMode.values();
    private AllToothTime[] allToothTimes = AllToothTime.values();
    private String mCurrentMode;
    private int chooseType = 0;
    private int position = 0;
    private List<MachineBindMemberBean> machineBindMemberBeans = null;
    private MachineBean machineBeans = null;
    private List<BrushBean> brushBeans = null;
    private BrushUseCount brushUseCount = null;
    private int intentcount = 0x118;
    private int intentRedEnvelope = 0x1221;
    private int handlerRedEnvelope = 12341;
    private List<BrushBean> brushBeans2 = new ArrayList<>();
    private RedEnvelopeEntity redEnvelopeEntity = null;

    private String addressStr = "01:B4:EC:B9:E4:8D";
    IWXAPI iwxapi = null;
    private WxUserInfo wxUserInfo = null;

    private BrushPresenter brushPresenter = new BrushPresenter(this);


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == handlerRedEnvelope){
                pingyi(gifImageView);
            }
        }
    };

    //平移
    public void pingyi(View v) {
        float translationX = v.getTranslationX();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, "translationX", translationX, 50f);
        objectAnimator.setDuration(1000);
        objectAnimator.start();
    }

    private Timer timer = new Timer();
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(handlerRedEnvelope);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.isDebug = true;
        setContentView(R.layout.activity_tooth);
        Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.bg_toolbar_title));
        unbinder = ButterKnife.bind(this);
        initEventAndData();

        iwxapi = WXAPIFactory.createWXAPI(this, IDatalifeConstant.APP_ID,true);
        iwxapi.registerApp(IDatalifeConstant.APP_ID);

        startScan();
    }


    protected void initEventAndData() {

        brushPresenter.attachView(this);
        brushPresenter.onCreate();

//        String appId = "wx7b154709878a1cbe";//开发者平台ID
//        IWXAPI api = WXAPIFactory.createWXAPI(this, appId, true);
//
//        if (api.isWXAppInstalled()) {
//            JumpToBizProfile.Req req = new JumpToBizProfile.Req();
//            req.toUserName = "gh_6b5f1a931f30"; // 公众号原始ID
//            req.extMsg = "";
//            req.profileType = JumpToBizProfile.JUMP_TO_NORMAL_BIZ_PROFILE; // 普通公众号
//            api.sendReq(req);
//        }else{
//            Toast.makeText(this, "微信未安装", Toast.LENGTH_SHORT).show();
//        }


        mConnectIv.setVisibility(View.VISIBLE);
        mConnectIv.setImageResource(R.mipmap.ic_bluetooth_brush_close);

        mEnergeIv.setVisibility(View.VISIBLE);

        authState = getResources().getStringArray(R.array.arr_auth_state);
        setStateArr = getResources().getStringArray(R.array.arr_set_state);
        powerArr = getResources().getStringArray(R.array.arr_power);
        durationArr = getResources().getStringArray(R.array.arr_duration);
        modeArr = getResources().getStringArray(R.array.arr_mode);

        setHeartCount(0);
        timeMills = (long) SpUtils.get(this, PAIR_INFO, 0L);

        workState = new WorkState((byte) 0x00, (byte) 0x00, (byte) 0x00);

        /*List<MachineBean> machineBeans1 = DBManager.getInstance(this).queryMachineBeanList();

        for (MachineBean machineBean : machineBeans1){
            if (machineBean.getMachineName().startsWith("ZSONIC")){
                machineBeans = machineBean;
            }
        }*/

        MachineBean machineBean = (MachineBean) getIntent().getBundleExtra(DataLifeUtil.BUNDLEMEMBER).getSerializable("machine");
        machineBeans = machineBean;

        upLoadData();

        addressStr = machineBeans.getMachineSn();

        machineBindMemberBeans = DBManager.getInstance(this).queryMachineBindMemberBeanList(machineBeans.getMachineBindId());

        brushBeans = DBManager.getInstance(this).queryBrushList(addressStr);

        brushPresenter.getBrushCount(machineBindMemberBeans.get(0).getMachineBindId(),ProApplication.SESSIONID,machineBindMemberBeans.get(0).getMember_Id());

        brushPresenter.isHaveRedEnvelope(ProApplication.SESSIONID,machineBindMemberBeans.get(0).getMM_Id());

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = simpleDateFormat.format(date);

        mTvDay.setText( 90 - brushBeans.size() + "次");

        if (brushBeans.size() >= 80){
            mTipTv.setVisibility(View.VISIBLE);
        }

        int a = 0;
        for (int i = 0;i<brushBeans.size();i++){
            if (brushBeans.get(i).getStartTime().contains(dateStr)){
                a++;
            }
        }

        if (a > 0){
            if (a > 3) {
                a = 3;
            }
            setHeartCount(a);
        }

//        batteryView.setPower(20f/100);
    }

    @OnClick({R.id.iv_head_left,R.id.iv_head_tooth,R.id.btn_history,R.id.ll_tooth_mode,R.id.ll_tooth_time,R.id.ll_tooth_surplus,R.id.gif_iv})
    public void onclick(View v){
        switch (v.getId()){
            case R.id.iv_head_left:
                finish();
                break;
            case R.id.iv_head_tooth:

                if (binder != null){
                    if (binder.isConnected()){
                        binder.disconnect();
                        mConnectIv.setImageResource(R.mipmap.ic_bluetooth_brush_close);
                    }else {
                        startScan();
                    }
                }

                break;
            case R.id.btn_history:

                Bundle bundle1 = new Bundle();
                bundle1.putString("memberid",machineBindMemberBeans.get(0).getMember_Id());
                bundle1.putString("machineid",machineBeans.getMachineBindId());

                UIHelper.launcherForResultBundle(this, ToothHistoryActivity.class,141, bundle1);
                break;
            case R.id.ll_tooth_mode:

                showPopupwindow(allToothMode,(int) (AllToothMode.getPageByValue(mToothMode.getText().toString()).getModeId())-1, DataLifeUtil.TOOTH_MODE);

                break;

            case R.id.ll_tooth_time:

                showPopupwindow(allToothTimes,AllToothTime.getPageByValue(mToothTime.getText().toString()).getId()-1,DataLifeUtil.TOOTH_TIME);

                break;

            case R.id.ll_tooth_surplus:

                Bundle bundle = new Bundle();
                if (brushUseCount != null){
                    bundle.putInt(DataLifeUtil.DAY, 90 - brushUseCount.getMachineUseTotal());
                }else {
                    if (brushBeans != null) {
                        bundle.putInt(DataLifeUtil.DAY, 90 - brushBeans.size());
                    } else {
                        bundle.putInt(DataLifeUtil.DAY, 85);
                    }
                }
                bundle.putString("address",addressStr);
                UIHelper.launcherForResultBundle(this,BrushHeadActivity.class,intentcount,bundle);

                break;

            case R.id.gif_iv:

                SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);

                if (sharedPreferences != null) {
                    String str = sharedPreferences.getString("logininfo", "");
                    try {
                        LoginBean loginBean = (LoginBean)DataLifeUtil.deSerialization(str);

                        if (loginBean.getOpenid() != null && loginBean.getUnionid() != null && loginBean.getOpenid().toString().trim().length() > 0  && loginBean.getUnionid().toString().trim().length() >0){
                            brushPresenter.isOpenRedEnvelope(ProApplication.SESSIONID,machineBindMemberBeans.get(0).getMM_Id());
                        }else if (loginBean.getOpenid().toString().trim().length() == 0 && loginBean.getOpenid().toString().trim().length() == 0){
//                            UToast.show(this,"请用微信登录");
                            WxUserInfo wxUserInfo = DBManager.getInstance(this).queryWxInfo();
                            if (wxUserInfo == null) {
                                if (!iwxapi.isWXAppInstalled()) {
                                    UToast.show(this,"您没有安装微信");
                                } else {
                                    DBManager.getInstance(this).deleteWxInfo();
                                    final SendAuth.Req req = new SendAuth.Req();
                                    req.scope = "snsapi_userinfo";
                                    req.state = "wechat_sdk_微信登录";
                                    iwxapi.sendReq(req);
                                }
                            }
                        }else if (loginBean.getOpenid().toString().trim().length() == 0){
                            UToast.show(this,"请去绑定微信服务号");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }


                break;
        }
    }


    //显示popupwindow
    public void showPopupwindow(Object[] str,int position,int type){
        if (popupWindow!=null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }else {
            View view = LayoutInflater.from(this).inflate(R.layout.pop_tooth_mode, null);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.tooth_popwindow);
            TextView titleTv = view.findViewById(R.id.tv_tooth_title);
            mRvTooth = (RecyclerView) view.findViewById(R.id.rv_tooth);
            if (type == DataLifeUtil.TOOTH_MODE) {
                titleTv.setText(R.string.tooth_mode);
            }else if (type == DataLifeUtil.TOOTH_TIME){
                titleTv.setText(R.string.brushing_time);
            }

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow!=null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        if(binder != null && binder.isConnected()) {
                            binder.disconnect();
                        }
                    }
                }
            });

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRvTooth.setLayoutManager(linearLayoutManager);
            ToothModeAdapter toothModeAdapter = new ToothModeAdapter(this,str,position, type);
            mRvTooth.setAdapter(toothModeAdapter);
            toothModeAdapter.setItemClickListener(this);


            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(1));
            popupWindow.showAtLocation(mLlTooth, Gravity.CENTER | Gravity.CENTER, 0, 0);
        }
    }

    private void setHeartCount(int count){
        switch (count){
            case 0:

                mIvHeartLeft.setImageResource(R.mipmap.ic_heart_empty);
                mIvHeartCenter.setImageResource(R.mipmap.ic_heart_empty);
                mIvHeartRight.setImageResource(R.mipmap.ic_heart_empty);

                break;

            case 1:

                mIvHeartLeft.setImageResource(R.mipmap.ic_heart_full);
                mIvHeartCenter.setImageResource(R.mipmap.ic_heart_empty);
                mIvHeartRight.setImageResource(R.mipmap.ic_heart_empty);

                break;

            case 2:

                mIvHeartLeft.setImageResource(R.mipmap.ic_heart_full);
                mIvHeartCenter.setImageResource(R.mipmap.ic_heart_full);
                mIvHeartRight.setImageResource(R.mipmap.ic_heart_empty);

                break;

            case 3:

                mIvHeartLeft.setImageResource(R.mipmap.ic_heart_full);
                mIvHeartCenter.setImageResource(R.mipmap.ic_heart_full);
                mIvHeartRight.setImageResource(R.mipmap.ic_heart_full);

                break;
        }
    }

    /*@Override
    protected void onServiceBinded(BleProfileService.LocalBinder localBinder) {
        this.binder = (ToothbrushService.ToothbrushBinder) localBinder;
    }*/

    @Override
    protected void onServiceUnbinded() {

    }

    @Override
    protected void onError(String s, int i) {
        if (i == 19){
            binder.disconnect();
        }

    }

    @Override
    protected void onResultChange(int i, String s) {

    }

    @Override
    protected void getResultState(int type, byte state) {
        if (state == ToothbrushBleConfig.UNKNOWN_COMMAND) {
            UToast.show(this,getResources().getString(R.string.unknown_command));
            return;
        }
        switch (type) {
            case ToothbrushService.STATE_SET_PARAM:
//                UToast.show(this,setStateArr[state - 1]);
                if (state == 3){
//                    UToast.show(this,setStateArr[state - 1]);
                }else if(state == 1){
                    if (chooseType == DataLifeUtil.TOOTH_MODE) {
                        mToothMode.setText(allToothMode[position].getModeName());
                    } else if (chooseType == DataLifeUtil.TOOTH_TIME) {
                        mToothTime.setText(allToothTimes[position].getTimeStr());
                    }
                }
                break;
            case ToothbrushService.STATE_SET_TIME:
                break;
            case ToothbrushService.STATE_SET_ID:
                break;
            case ToothbrushService.STATE_CANCEL_HISTORY:
                break;
            case ToothbrushService.STATE_DELETE_HISTORY:
                UToast.show(this,setStateArr[state - 1]);
                break;
        }
    }


    @Override
    protected void getWorkState(WorkState workState) {

        if (workState.getDuration() != 10 || workState.getDuration() != 13 || workState.getDuration() != 16){
            workState.setDuration((byte)13);
            workState.setMode((byte)0x02);
            if(binder != null){
                binder.setWorkParam(workState);
                return;
            }
        }

        String ass = durationArr[workState.getDuration() - 1];
        AllToothTime allToothTime = AllToothTime.getSecondByValue(ass);

        if (allToothTime == null){
            return;
        }

        mToothTime.setText(allToothTime.getTimeStr());
        if (workState.getMode() >= 5){
            mToothMode.setText(modeArr[0]);
        }else {
            mToothMode.setText(modeArr[workState.getMode() - 1]);
        }

    }

    @Override
    protected void getBatteryState(BatteryState batteryState) {
//        UToast.show(this,batteryState.getBattery() + "v" + ",power:" + powerArr[batteryState.getPowerState()]);
        if (batteryState.getBattery() >= 3.9){
            mEnergeIv.setImageResource(R.mipmap.ic_energy_high);
        }else if (batteryState.getBattery() < 3.9  && batteryState.getBattery() >= 3.6){
            mEnergeIv.setImageResource(R.mipmap.ic_energy_middle);
        }else if (batteryState.getBattery() < 3.6){
            mEnergeIv.setImageResource(R.mipmap.ic_energy_low);
        }
        /*if (batteryState.getPowerState() == 2) {
            mBrushBattery.setImageResource(R.mipmap.ic_energy_high);
        }else if (batteryState.getPowerState() == 1){
            mBrushBattery.setImageResource(R.mipmap.ic_energy_middle);
        }else if (batteryState.getPowerState() == 3){
            mBrushBattery.setImageResource(R.mipmap.ic_energy_low);
        }*/
    }

    private int historyCount = 0;

    @Override
    protected void getHistoryCount(int i) {
        historyCount = i;
    }

    List<BrushManyBean> brushManyBeans = new ArrayList<>();

    @Override
    protected void getHistoryData(HistoryData historyData) {
        WorkData workData = historyData.getWorkData();

        /*if (workData != null && workData.getLeftBrushTime() != 0) {
            getWorkData(workData);
        }*/

        BrushBean brushBean = new BrushBean();
        brushBean.setLeftBrushTime(workData.getLeftBrushTime());
        brushBean.setRightBrushTime(workData.getRightBrushTime());
        brushBean.setWorkTime(workData.getWorkTime());
        brushBean.setOverPowerCount(workData.getOverPowerCount());
        brushBean.setOverPowerTime(workData.getOverPowerTime());
        brushBean.setStartTime(workData.getStartTime());
        brushBean.setAddress(addressStr);
        brushBean.setSettingTime(Integer.valueOf(durationArr[workData.getDuration()-1]));
        brushBeans2.add(brushBean);


        BrushManyBean brushManyBean = new BrushManyBean();
        brushManyBean.setCheckValue1(workData.getLeftBrushTime());
        brushManyBean.setCheckValue2(workData.getRightBrushTime());
        brushManyBean.setCheckValue3(workData.getWorkTime());
        brushManyBean.setCheckValue4(workData.getOverPowerCount());
        brushManyBean.setCheckValue5(workData.getOverPowerTime());
        brushManyBean.setCheckValue6(Integer.valueOf(durationArr[workData.getDuration()-1]));
        brushManyBean.setProject_Id(IDatalifeConstant.BRUSHINT+"");
        brushManyBean.setMachine_Id(DataLifeUtil.MACHINE_BRUSH+"");
        brushManyBean.setMachine_Sn(machineBeans.getMachineSn());
        brushManyBean.setMachineBindId(machineBeans.getMachineBindId());
        brushManyBean.setMember_Id(machineBindMemberBeans.get(0).getMember_Id());
        brushManyBean.setCreateDate(workData.getStartTime());

        if (workData.getWorkTime() > 30) {
            brushManyBeans.add(brushManyBean);
        }else {
            return;
        }

        if (brushBeans2.size() == historyCount){
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            Type type=new TypeToken<List<BrushManyBean>>(){}.getType();
            String str = gson.toJson(brushManyBeans,type);
            brushPresenter.putBrushValues( ProApplication.SESSIONID,str,IDatalifeConstant.BRUSHMUL,brushManyBeans);
        }


    }

    @Override
    protected void getWorkData(WorkData workData) {
            if (workData != null){
                int leftBrush = workData.getLeftBrushTime();
                int rightBrush = workData.getRightBrushTime();

                int time = workData.getWorkTime();

                int duration = workData.getDuration();

                int ss = (int)(AllToothTime.getPageByValue(mToothTime.getText().toString()).getToothTime() * 60);

                if ((double)workData.getWorkTime() / workData.getDuration() < 0.5){
                    return;
                }

                if (workData.getWorkTime() <= 30){
                    return;
                }

                if (workData .getLeftBrushTime() == 0 || workData.getRightBrushTime() == 0){
                    return;
                }

                for (BrushBean dbBrushBean : brushBeans){
                    if (dbBrushBean.getStartTime().equals(workData.getStartTime())){
                        return;
                    }
                }

                BrushBean brushBean = new BrushBean();
                brushBean.setLeftBrushTime(leftBrush);
                brushBean.setRightBrushTime(rightBrush);
                brushBean.setWorkTime(time);
                brushBean.setOverPowerCount(workData.getOverPowerCount());
                brushBean.setOverPowerTime(workData.getOverPowerTime());
                brushBean.setStartTime(workData.getStartTime());
                brushBean.setAddress(addressStr);
                brushBean.setSettingTime(ss);

                DBManager.getInstance(this).insertBrushList(brushBean);

                List<BrushManyBean> brushManyBeans = new ArrayList<>();
                BrushManyBean brushManyBean = new BrushManyBean();
                brushManyBean.setCheckValue1(workData.getLeftBrushTime());
                brushManyBean.setCheckValue2(workData.getRightBrushTime());
                brushManyBean.setCheckValue3(workData.getWorkTime());
                brushManyBean.setCheckValue4(workData.getOverPowerCount());
                brushManyBean.setCheckValue5(workData.getOverPowerTime());
                brushManyBean.setCheckValue6(workData.getDuration());
                brushManyBean.setCheckValue7(IDatalifeConstant.getCalculation(this,brushBean));
//                brushManyBean.setCheckValue7(99);
                brushManyBean.setProject_Id(IDatalifeConstant.BRUSHINT+"");
                brushManyBean.setMachine_Id(DataLifeUtil.MACHINE_BRUSH+"");
                brushManyBean.setMachine_Sn(machineBeans.getMachineSn());
                brushManyBean.setMachineBindId(machineBeans.getMachineBindId());
                brushManyBean.setMember_Id(machineBindMemberBeans.get(0).getMember_Id());
                brushManyBean.setCreateDate(brushBean.getStartTime());
                brushManyBeans.add(brushManyBean);

                double sa = (double)time/ss * 40;

                int fraction = 0;

                int big = leftBrush>=rightBrush ? leftBrush:rightBrush;
                int small = leftBrush>=rightBrush ? rightBrush:leftBrush;

                int overPowerCount =workData.getOverPowerCount();

                if (overPowerCount >= 10){
                    overPowerCount = 10;
                }

                mFractionTv.setText(IDatalifeConstant.getCalculation(this,brushBean) + "分");

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Type type=new TypeToken<List<BrushManyBean>>(){}.getType();
                String str = gson.toJson(brushManyBeans,type);
                brushPresenter.putBrushValues( ProApplication.SESSIONID,str,IDatalifeConstant.BRUSHSINGLE,brushManyBeans);
//                UToast.show(this,"left:" + leftBrush + ",right:" + rightBrush + ",/ntime" + time + ",duration:" + duration);
            }
    }

    @Override
    protected void onOtaSuccess() {

    }

    @Override
    protected void onProgressChange(float v) {

    }

    @Override
    protected void getAuthState(byte b) {
        int s = (int)b;
        if (s == 1){
            if (toothDialog != null && toothDialog.isShowing()) {
                toothDialog.dismiss();
            }
            binder.queryBatteryState();
        }else if(s == 2){
            if (!isAuth){
                toothDialog = new ToothDialog(this);
                toothDialog.show();
                isAuth = true;
            }
        }else if(s == 3){

        }
    }


    @Override
    protected void getVersion(String s) {

    }

    @Override
    protected void getGears(GearBean gearBean) {
//        Byte[] gear = gearBean.getGears()[0];
//        mGears[0] = gear[0];
//        mGears[1] = gear[1];
//        mGears[2] = gear[2];
//        mGears[3] = gear[3];
//        mGears[4] = gear[4];
//        mGears[5] = gear[5];
//        Log.i("TAG", "getGears: " + Arrays.toString(mGears));
//        ((BaseAdapter) spinner_mode.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void getDevice(final BrushDevice brushDevice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("ToothActivity", "brushDevice = " + brushDevice.getAddress());


                List<MachineBean> machineBeans = DBManager.getInstance(ToothActivity.this).queryMachineBeanList();

                if (brushDevice.getAddress().equals(addressStr)) {
                    startConnectService(brushDevice.getAddress());
                }
            }
        });
    }

    @Override
    public void onStateChanged(String address, int state) {
        super.onStateChanged(address, state);
        switch (state) {
            case BleProfileService.STATE_CONNECTED:
                L.e(TAG, "STATE_CONNECTED");
//                btn_connect_state.setText(String.format(getResources().getString(R.string.connected_device), binder.getDeviceAddress()));
                break;
            case BleProfileService.STATE_DISCONNECTED:
                L.e(TAG, "STATE_DISCONNECTED");
//                btn_connect_state.setText(R.string.not_connect_device);
                if (mBlueToothAddress != null && !mBlueToothAddress.equals("")){
                    startConnectService(mBlueToothAddress);
                }
                break;
            case BleProfileService.STATE_SERVICES_DISCOVERED:
                L.e(TAG, "STATE_SERVICES_DISCOVERED");
                break;
            case BleProfileService.STATE_INDICATION_SUCCESS:
                L.e(TAG, "STATE_INDICATION_SUCCESS");
                if (isDeviceConnected()) {
//                    UToast.show(this,"mBlueToothAddress:" + mBlueToothAddress);
                    if (timeMills == 0) {
                        timeMills = System.currentTimeMillis();
                        SpUtils.put(this, PAIR_INFO, timeMills);
                    }
                    binder.requestPair(2000L);

                    binder.queryBatteryState();

                    binder.queryWorkState();

                    binder.operateHistory(ToothbrushBleConfig.QUERY_HISTORY);

                    mConnectIv.setImageResource(R.mipmap.ic_bluetooth_brush_open);

                    mBlueToothAddress = binder.getDeviceAddress();

                    Log.e(TAG,"mBlueToothAddress:" + mBlueToothAddress);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toothDialog != null){
            toothDialog.dismiss();
        }

        if (binder != null) {
            binder.disconnect();
        }

        brushPresenter.onStop();
    }

    @Override
    protected void onServiceBinded(ToothbrushService.ToothbrushBinder toothbrushBinder) {
        this.binder = toothbrushBinder;

    }

    @Override
    public void onItemClick(int position,int type) {
        popupWindow.dismiss();
        if (binder != null){

            if (!isDeviceConnected()){
                UToast.show(this,"未连接设备，设置无效");
                return;
            }

            this.chooseType = type;
            this.position = position;

            if (type == DataLifeUtil.TOOTH_MODE) {
                workState.setMode(allToothMode[position].getModeId());
//                workState.setMode((byte)0x03);
                String toothTime = mToothTime.getText().toString();
                workState.setDuration(AllToothTime.getPageByValue(toothTime).getToothId());
            }else if (type == DataLifeUtil.TOOTH_TIME){
                String toothMode = mToothMode.getText().toString();
                workState.setMode(AllToothMode.getPageByValue(toothMode).getModeId());
                workState.setDuration(allToothTimes[position].getToothId());
            }
            binder.setWorkParam(workState);

        }
    }

    /**
     * 上传数据
     */
    public void upLoadData(){
        List<BrushManyBean> brushManyBeans = DBManager.getInstance(this).queryBrushManyList();
        DBManager.getInstance(this).deleteBrushManyList();
        if (brushManyBeans != null && brushManyBeans.size() > 0) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Type type=new TypeToken<List<BrushManyBean>>(){}.getType();
                String str = gson.toJson(brushManyBeans,type);
                brushPresenter.putBrushValues( ProApplication.SESSIONID,str,IDatalifeConstant.BRUSHMUL,brushManyBeans);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == 2){
            }else if (requestCode == intentcount){
                int day = data.getIntExtra("day",0);
                mTvDay.setText(day + "次");
            }else if (requestCode == intentRedEnvelope){
                if(gifImageView != null) {
                    gifImageView.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void success() {
        brushPresenter.getBrushCount(machineBindMemberBeans.get(0).getMachineBindId(),ProApplication.SESSIONID,machineBindMemberBeans.get(0).getMember_Id());

        brushPresenter.isHaveRedEnvelope(ProApplication.SESSIONID,machineBindMemberBeans.get(0).getMM_Id());
//            UToast.show(this,"o my god");
    }

    @Override
    public void fail(String msg,List<BrushManyBean> brushManyBeans) {
        for (BrushManyBean brushManyBean : brushManyBeans) {
            DBManager.getInstance(this).insertBrushManyList(brushManyBean);
        }
    }

    @Override
    public void successMul() {
        if (isDeviceConnected()) {
            binder.operateHistory(ToothbrushBleConfig.DELETE_HISTORY);
            brushPresenter.getBrushCount(machineBindMemberBeans.get(0).getMachineBindId(),ProApplication.SESSIONID,machineBindMemberBeans.get(0).getMember_Id());
        }
    }

    @Override
    public void successCount(BrushUseCount brushUseCount) {
        this.brushUseCount = brushUseCount;
        mTvDay.setText(90 - brushUseCount.getMachineUseTotal() + "次");

        tvBrushLevel.setText(brushUseCount.getToothbrushName());

        if (brushUseCount.getMachineUseTotal()>= 80){
            mTipTv.setVisibility(View.VISIBLE);
        }

        if (brushUseCount.getDayUse() <= 3) {
            setHeartCount(brushUseCount.getDayUse());
        }else {
            setHeartCount(3);
        }
    }

    @Override
    public void failCount(String msg) {

    }

    @Override
    public void successRedEnvelope(RedEnvelopeEntity redEnvelopeEntity) {

        this.redEnvelopeEntity = redEnvelopeEntity;

        if(gifImageView != null) {
            gifImageView.setVisibility(View.VISIBLE);
        }
        timer.schedule(timerTask,3000);
    }

    @Override
    public void failRedEnvelope(String msg) {

    }

    @Override
    public void successOpenRedEnvelope() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("redenvelope",redEnvelopeEntity);
        UIHelper.launcherForResultBundle(this,RedEnvelopeResultActivity.class,intentRedEnvelope,bundle);
    }

    @Override
    public void failOpenRedEnvelope(String msg,String status) {
        UToast.show(this,status+"iii");
        if (status.contains("10000")){
            UToast.show(this,msg);
        }else {
            UToast.show(this, "领取红包失败");
//            wxUserInfo = DBManager.getInstance(this).queryWxInfo();
//            if (wxUserInfo == null) {
//                if (!iwxapi.isWXAppInstalled()) {
//                    UToast.show(this,"您没有安装微信");
//                } else {
//                    DBManager.getInstance(this).deleteWxInfo();
//                    final SendAuth.Req req = new SendAuth.Req();
//                    req.scope = "snsapi_userinfo";
//                    req.state = "wechat_sdk_微信登录";
//                    iwxapi.sendReq(req);
//                }
//            }
        }
    }
}
