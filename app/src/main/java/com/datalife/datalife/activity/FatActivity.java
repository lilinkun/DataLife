package com.datalife.datalife.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.bean.LastFatMeasureDataBean;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.contract.BtContract;
import com.datalife.datalife.contract.FatContract;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.dao.Spo2hDao;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.fragment.HomePageFragment;
import com.datalife.datalife.fragment.MonitorInfoFragment;
import com.datalife.datalife.interf.OnCommonChangeListener;
import com.datalife.datalife.presenter.BtPresenter;
import com.datalife.datalife.presenter.FatPresenter;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DefaultPicEnum;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.Eyes;
import com.datalife.datalife.util.MeasureNorm;
import com.datalife.datalife.util.StatusBarUtil;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.util.UToast;
import com.datalife.datalife.widget.CommonChangeTitle;
import com.datalife.datalife.widget.CommonLayout;
import com.datalife.datalife.widget.CommonTitle;
import com.datalife.datalife.widget.DialProgress;
import com.datalife.datalife.widget.GradationScrollView;
import com.datalife.datalife.widget.NoDevDialog;
import com.datalife.datalife.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import aicare.net.cn.iweightlibrary.bleprofile.BleProfileService;
import aicare.net.cn.iweightlibrary.entity.BodyFatData;
import aicare.net.cn.iweightlibrary.entity.User;
import aicare.net.cn.iweightlibrary.entity.WeightData;
import aicare.net.cn.iweightlibrary.utils.AicareBleConfig;
import aicare.net.cn.iweightlibrary.utils.L;
import aicare.net.cn.iweightlibrary.wby.WBYService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LG on 2018/4/23.
 */

public class FatActivity extends BleProfileServiceReadyActivity implements OnCommonChangeListener,FatContract.FatView, View.OnClickListener {

    private WBYService.WBYBinder binder;
    private final static String TAG = "FatActivity";
    private Unbinder unbinder = null;
    private User user = null;

    @BindView(R.id.commonlayout)
    CommonLayout mCommonLayout;
    @BindView(R.id.mcctitle)
    CommonTitle mCommonChangeTitle;
    @BindView(R.id.btn_connect)
    TextView mConnectBtn;
    @BindView(R.id.dial_progress_bar)
    DialProgress mDialProgress;
    @BindView(R.id.pg_weight_value)
    TextView mWeightValue;
    @BindView(R.id.ll_fat_test)
    LinearLayout mTestFatLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tv_connected_equit)
    TextView mConnectedEquit;
    @BindView(R.id.iv_head_right)
    ImageView mRoundImageView;
    @BindView(R.id.pb_loadding)
    ProgressBar mProgressBar;

    private List<FamilyUserInfo> familyUserInfos;
    private String mMemberId = "";
    private String mMachineBindId = "";
    private boolean isBinded = false;
    public final int fathandler = 0x1112;
    private ArrayList<MachineBean> machineBeans;
    private MachineBean machineBean;
    private BluetoothDevice device;
    private ArrayList<MachineBindMemberBean> machineBindMemberBeans;
    private MachineBindMemberBean machineBindMemberBean = null;
    private FamilyUserInfo familyUserInfo = null;
    private boolean isHasBindDev = false;
    private int mRequestCode = 0x0012;

    FatPresenter fatPresenter = new FatPresenter(this);

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case fathandler:
                    break;
                case DataLifeUtil.COMMOMHANDLERMEMBER:
                    machineBindMemberBean = (MachineBindMemberBean) msg.getData().getSerializable("machinemember");
                    mMemberId = msg.getData().getString("memberid");
                    fatPresenter.getNewMeasureInfo(mMemberId,DataLifeUtil.MACHINE_FAT,ProApplication.SESSIONID);
                    break;
                case DataLifeUtil.COMMOMHANDLERMACHINE:
                    machineBean = (MachineBean)msg.getData().getSerializable("machine");
                    mMachineBindId = msg.getData().getString("machineid");
//                    startConnect(machineBean.getMachineSn());
                    mCommonChangeTitle.setDevName(machineBean);

                    if (binder != null) {
                        if (isDeviceConnected()) {
                            binder.disconnect();
                        }else {
                            stopScan();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                startScan();
                            }
                        }).start();

                    }

                    break;
                case HomePageFragment.TESTHANDLER:
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    break;
            }
        }
    };

    Timer timer = new Timer();
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = fathandler;
            myHandler.sendMessage(message);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fat);
//        StatusBarUtil.setStatusBarColor(this, R.color.bg_toolbar_title);

        fatPresenter.onCreate();
        fatPresenter.attachView(this);
        timer.schedule(timerTask,5000);

        Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.bg_toolbar_title));
        unbinder = ButterKnife.bind(this);

        mCommonLayout.setHandler(myHandler);
        startScan();

        machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(this).queryMachineBeanList();

        boolean isFirst = true;
        for (MachineBean machineBean : machineBeans){
            if (machineBean.getMachineName().startsWith("SWAN") || machineBean.getMachineName().startsWith("swan")){
                isHasBindDev = true;
//                this.machineBean = machineBean;
                if (isFirst) {
                    mCommonChangeTitle.setDevName(machineBean);
                    mMachineBindId = machineBean.getMachineBindId();
                    machineBindMemberBeans = (ArrayList<MachineBindMemberBean>) DBManager.getInstance(this).queryMachineBindMemberBeanList(mMachineBindId);
                    isFirst = false;
                }
            }
        }

        familyUserInfos = DBManager.getInstance(this).queryFamilyUserInfoList();
        fatPresenter.getMachineInfo("1","35",ProApplication.SESSIONID);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        if (familyUserInfos != null && familyUserInfos.size() != 0){
//            mCommonChangeTitle.SetOnCommonChangeListener(this);
//            mCommonChangeTitle.setData(familyUserInfos,machineBeans);
            mCommonChangeTitle.setHandler(myHandler);
            mCommonChangeTitle.setLayout(mTestFatLayout);
            mMemberId = String.valueOf(familyUserInfos.get(0).getMember_Id());
            familyUserInfo = (FamilyUserInfo) familyUserInfos.get(0);
            mRoundImageView.setVisibility(View.VISIBLE);
            mRoundImageView.setImageResource(R.mipmap.ic_fat_history);
            }

        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams)mDialProgress.getLayoutParams();
        layoutParams.width = width*3/5;
        mDialProgress.setLayoutParams(layoutParams);
        mConnectBtn.setVisibility(View.GONE);
        mConnectBtn.setText(getString(R.string.connecting_equit));
        mConnectedEquit.setText(getString(R.string.connecting_equit));

        mConnectedEquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDeviceConnected() && mConnectedEquit.getText().toString().equals(getString(R.string.click_connect))){
                    stopScan();
                    startScan();
                }
            }
        });


        if (machineBindMemberBeans != null  && machineBindMemberBeans.size() > 0) {
            mMemberId = machineBindMemberBeans.get(0).getMember_Id();
            fatPresenter.getNewMeasureInfo(machineBindMemberBeans.get(0).getMember_Id(), DataLifeUtil.MACHINE_FAT, ProApplication.SESSIONID);
        }


        if (!ensureBLESupported()) {
           Toast.makeText(this, R.string.not_support_ble,Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!isBLEEnabled()) {
            showBLEDialog();
        }
//        mCommonChangeTitle.visibleHistory(View.VISIBLE);


    }

    @OnClick({R.id.iv_head_left,R.id.btn_connect,R.id.iv_head_right})
    public void onClick(View v){
        switch (v.getId()){

            case R.id.iv_head_left:
                finish();
                break;

            case R.id.btn_connect:

                if (mConnectBtn.getText().toString().equals(getString(R.string.click_connect))) {
                    startScan();
                }else if (mConnectBtn.getText().toString().equals(getString(R.string.connected))){
                    binder.disconnect();
                }

                break;

            case R.id.iv_head_right:

                if (isHasBindDev) {
                    /*if (mMachineBindId!=null && mMachineBindId.equals("")){
                        for (int i = 0;i<machineBindMemberBeans.size();i++){
                            if (machineBindMemberBeans.get(i).getMember_Id().equals(mMemberId)){
                                mMachineBindId = machineBindMemberBeans.get(i).getMachineBindId()
                            }
                        }
                    }*/
                    Bundle bundle = new Bundle();
                    bundle.putString(DataLifeUtil.BUNDLEMEMBERID, mMemberId);
                    bundle.putSerializable(DataLifeUtil.FAMILYUSERINFO, familyUserInfo);
                    bundle.putString("MachineId", mMachineBindId);
                    UIHelper.launcherForResultBundle(this, FatCalendarActivity.class, 122, bundle);
                }else {
                    UToast.show(this,"您还没有绑定过设备");
                }

                break;
        }
    }


    @Override
    protected void onError(String errMsg, int errCode) {
    }

    @Override
    protected void onGetWeightData(WeightData weightData) {
        /*if (user == null && binder != null) {
            syncDate();
        }*/
        mCommonLayout.onWeight(weightData);
        mDialProgress.setValue((float) (weightData.getWeight()));
        mWeightValue.setText(String.valueOf(weightData.getWeight()));
    }

    private void showInfo(String str){
//        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
        Log.i(TAG,"SettingStatus = " + str);
    }

    @Override
    protected void onGetSettingStatus(int status) {
        L.e(TAG, "SettingStatus = " + status);
        switch (status) {
            case AicareBleConfig.SettingStatus.NORMAL:
                showInfo(getString(R.string.settings_status, getString(R.string.normal)));
                break;
            case AicareBleConfig.SettingStatus.LOW_POWER:
                showInfo(getString(R.string.settings_status, getString(R.string.low_power)));
                break;
            case AicareBleConfig.SettingStatus.LOW_VOLTAGE:
                showInfo(getString(R.string.settings_status, getString(R.string.low_voltage)));
                break;
            case AicareBleConfig.SettingStatus.ERROR:
                showInfo(getString(R.string.settings_status, getString(R.string.error)));
                break;
            case AicareBleConfig.SettingStatus.TIME_OUT:
                showInfo(getString(R.string.settings_status, getString(R.string.time_out)));
                break;
            case AicareBleConfig.SettingStatus.UNSTABLE:
                showInfo(getString(R.string.settings_status, getString(R.string.unstable)));
                break;
            case AicareBleConfig.SettingStatus.SET_UNIT_SUCCESS:
                showInfo(getString(R.string.settings_status, getString(R.string.set_unit_success)));
                break;
            case AicareBleConfig.SettingStatus.SET_UNIT_FAILED:
                showInfo(getString(R.string.settings_status, getString(R.string.set_unit_failed)));
                break;
            case AicareBleConfig.SettingStatus.SET_TIME_SUCCESS:
                showInfo(getString(R.string.settings_status, getString(R.string.set_time_success)));
                break;
            case AicareBleConfig.SettingStatus.SET_TIME_FAILED:
                showInfo(getString(R.string.settings_status, getString(R.string.set_time_failed)));
                break;
            case AicareBleConfig.SettingStatus.SET_USER_SUCCESS:
                showInfo(getString(R.string.settings_status, getString(R.string.set_user_success)));
                break;
            case AicareBleConfig.SettingStatus.SET_USER_FAILED:
                showInfo(getString(R.string.settings_status, getString(R.string.set_user_failed)));
                break;
            case AicareBleConfig.SettingStatus.UPDATE_USER_LIST_SUCCESS:
                showInfo(getString(R.string.settings_status, getString(R.string.update_user_list_success)));
                break;
            case AicareBleConfig.SettingStatus.UPDATE_USER_LIST_FAILED:
                showInfo(getString(R.string.settings_status, getString(R.string.update_user_list_failed)));
                break;
            case AicareBleConfig.SettingStatus.UPDATE_USER_SUCCESS:
                showInfo(getString(R.string.settings_status, getString(R.string.update_user_success)));
                break;
            case AicareBleConfig.SettingStatus.UPDATE_USER_FAILED:
                showInfo(getString(R.string.settings_status, getString(R.string.update_user_failed)));
                break;
            case AicareBleConfig.SettingStatus.NO_HISTORY:
                showInfo(getString(R.string.settings_status, getString(R.string.no_history)));
                break;
            case AicareBleConfig.SettingStatus.HISTORY_START_SEND:
                showInfo(getString(R.string.settings_status, getString(R.string.history_start_send)));
                break;
            case AicareBleConfig.SettingStatus.HISTORY_SEND_OVER:
                showInfo(getString(R.string.settings_status, getString(R.string.history_send_over)));
                break;
            case AicareBleConfig.SettingStatus.NO_MATCH_USER:
                showInfo(getString(R.string.settings_status, getString(R.string.no_match_user)));
                break;
            case AicareBleConfig.SettingStatus.ADC_MEASURED_ING:
                showInfo(getString(R.string.settings_status, getString(R.string.adc_measured_ind)));
                syncDate();
                break;
            case AicareBleConfig.SettingStatus.ADC_ERROR:
                showInfo(getString(R.string.settings_status, getString(R.string.adc_error)));
                break;
            case AicareBleConfig.SettingStatus.UNKNOWN:
                showInfo(getString(R.string.settings_status, getString(R.string.unknown)));
                break;
            case AicareBleConfig.SettingStatus.REQUEST_DISCONNECT:
                showInfo(getString(R.string.settings_status, getString(R.string.request_disconnect)));
                break;
        }
    }

    @Override
    protected void onGetResult(int index, String result) {

    }

    @Override
    protected void onGetFatData(boolean isHistory, BodyFatData bodyFatData) {
        Log.e("LG","bodyFatData:" + bodyFatData);
        if(bodyFatData!= null){
        boolean isHeight = false;

            if (bodyFatData.getHeight() == 0){
                for (int i = 0;i < familyUserInfos.size();i++){
                    if (familyUserInfos.get(i).getMember_Id() == Integer.valueOf(mMemberId)){
                        familyUserInfo = familyUserInfos.get(i);
                        bodyFatData.setSex(Integer.valueOf(familyUserInfo.getMember_Sex()));
                        bodyFatData.setAge(DataLifeUtil.getAgeByBirthDay(familyUserInfo.getMember_DateOfBirth()));
//                        bodyFatData.setWeight(familyUserInfo.getMember_Stature());
                        bodyFatData.setHeight((int)familyUserInfo.getMember_Stature());
                    }
                }
            }

           mCommonLayout.onGetFatData(bodyFatData);
            if (mMemberId.equals("")){
                if (machineBindMemberBeans != null && machineBindMemberBeans.size() == 0){
                    UToast.show(this,"请先绑定用户再测量");
                    return;
                }else {
                    mMemberId = machineBindMemberBeans.get(0).getMember_Id();
                }
            }

            if (machineBindMemberBeans != null && machineBindMemberBeans.size() == 0){
                UToast.show(this,"请先绑定用户再测量");
                return;
            }

            if (bodyFatData.getBmi() == 0){
                UToast.show(this,"未得出数据,请再次测量");
                return;
            }
            DataLifeUtil.startAlarm(this);

            double value = 0;

            if (bodyFatData.getDbw() == 0){
                value = bodyFatData.getWeight() *(1-bodyFatData.getBfr()/100);
            }else {
                value = bodyFatData.getDbw();
            }

                fatPresenter.putfattest(mMemberId + "", machineBean.getMachineBindId(), device.getAddress(), DeviceData.getUniqueId(this), bodyFatData.getHeight() + "", bodyFatData.getAge() + "",
                        bodyFatData.getWeight() + "", bodyFatData.getBmi() + "", bodyFatData.getBfr() + "", bodyFatData.getRom() + "",
                        bodyFatData.getVwc() + "", value + "", bodyFatData.getBm() + "", bodyFatData.getUvi() + "", bodyFatData.getBmr() + "",
                        bodyFatData.getSex() + "");
//            Toast.makeText(this,s + " ",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,bodyFatData.toString() + " ",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onServiceBinded(WBYService.WBYBinder binder) {
        this.binder = binder;
    }

    @Override
    protected void onServiceUnbinded() {
        this.binder = null;
    }

    @Override
    protected void getAicareDevice(BluetoothDevice device, int rssi, int flag) {
        this.device = device;
        for (int i = 0;i<machineBeans.size();i++) {
            if (machineBean != null) {
                if (device.getAddress().equals(machineBean.getMachineSn())) {
                    startConnect(device.getAddress());
                }
            }else {
                if (device.getAddress().equals(machineBeans.get(i).getMachineSn())) {
                    machineBean = machineBeans.get(i);
                    startConnect(device.getAddress());
                }
            }
        }
    }

    @Override
    protected void onStateChanged(String deviceAddress, int state) {
        super.onStateChanged(deviceAddress, state);
        switch (state) {
            case BleProfileService.STATE_CONNECTED:
//                Toast.makeText(this,getString(R.string.state_connected, deviceAddress),Toast.LENGTH_LONG).show();
                showInfo(getString(R.string.state_connected, deviceAddress));
                syncDate();

                mConnectBtn.setText(getString(R.string.connected));
                mConnectedEquit.setText(getString(R.string.connected));
                setIsBinded(true);

                for (int i = 0;i<machineBeans.size();i++){
                    if(machineBeans.get(i).getMachineSn().equals(deviceAddress)){
                        mCommonChangeTitle.setDevName(machineBeans.get(i));
                        machineBean = machineBeans.get(i);
                        mMachineBindId = machineBeans.get(i).getMachineBindId();
                        machineBindMemberBeans = (ArrayList<MachineBindMemberBean>)DBManager.getInstance(this).queryMachineBindMemberBeanList(mMachineBindId);

                        if (machineBindMemberBeans != null  && machineBindMemberBeans.size() > 0) {
                            mMemberId = machineBindMemberBeans.get(0).getMember_Id();
                            fatPresenter.getNewMeasureInfo(machineBindMemberBeans.get(0).getMember_Id(), DataLifeUtil.MACHINE_FAT, ProApplication.SESSIONID);
                        }
//                        mCommonChangeTitle.setMemberData(machineBindMemberBeans);
                    }
                }

                break;
            case BleProfileService.STATE_DISCONNECTED:
//                Toast.makeText(this,getString(R.string.state_disconnected),Toast.LENGTH_LONG).show();
                showInfo(getString(R.string.state_disconnected));
                mConnectBtn.setText(R.string.click_connect);
                mConnectedEquit.setText(getString(R.string.connecting));
                setIsBinded(false);
                startScan();
                break;
            case BleProfileService.STATE_SERVICES_DISCOVERED:
//                Toast.makeText(this,getString(R.string.state_service_discovered),Toast.LENGTH_LONG).show();
                showInfo(getString(R.string.state_service_discovered));
                break;
            case BleProfileService.STATE_INDICATION_SUCCESS:
//                Toast.makeText(this,getString(R.string.state_indication_success),Toast.LENGTH_LONG).show();
                showInfo(getString(R.string.state_indication_success));
                syncDate();
                if (this.binder != null) {
                    this.binder.queryBleVersion();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        stopScan();
        unbinder.unbind();
        if (isDeviceConnected()) {
            this.binder.disconnect();
        }
        super.onDestroy();
    }

    @Override
    public void getMemberId(String memberId) {
        this.mMemberId = memberId;
        syncDate();
    }

    private void syncDate(){
        for (int i = 0;i<familyUserInfos.size();i++){
            if(familyUserInfos.get(i).getMember_Id() == Integer.parseInt(mMemberId)){
                familyUserInfo= familyUserInfos.get(i);
            }
        }
        int age = 0;
        try{
            String str = familyUserInfo.getMember_DateOfBirth();
            age = DataLifeUtil.getAge(DataLifeUtil.parse(familyUserInfo.getMember_DateOfBirth()));
        }catch  (Exception e) {
            e.printStackTrace();
        }
        int sex = Integer.parseInt(familyUserInfo.getMember_Sex()) + 1;

        User user = new User(age, sex, age, (int)(familyUserInfo.getMember_Stature()), (int)familyUserInfo.getMember_Weight(), 551);
        binder.syncUser(user);
    }

    @Override
    public void onChangeWindow(int res) {
        mTestFatLayout.setBackgroundResource(res);
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    public void setIsBinded(boolean isbind){
        this.isBinded = isbind;
//        mCommonChangeTitle.getisBinded(isbind);
    }

    @Override
    public void onSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews) {
        mProgressBar.setVisibility(View.GONE);
        ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> machineBeans = resultNews;
        DBManager.getInstance(this).deleteAllMachineBean();
        DBManager.getInstance(this).deleteAllMachineBindBean();
        ArrayList<MachineBindMemberBean> machineBindMemberBeans = null;
        boolean isHaveFat = false;
        for (int i = 0;i<machineBeans.size();i++) {
            try{
                MachineBean machineBean = new MachineBean();
                machineBean.setCreateDate(machineBeans.get(i).getCreateDate());
                machineBean.setMachineBindId(machineBeans.get(i).getMachineBindId());
                machineBean.setMachineId(machineBeans.get(i).getMachineId());
                machineBean.setMachineName(machineBeans.get(i).getMachineName());
                machineBean.setMachineSn(machineBeans.get(i).getMachineSn());
                machineBean.setMachineStatus(machineBeans.get(i).getMachineStatus());
                machineBean.setUser_id(machineBeans.get(i).getUser_id());
                machineBean.setUser_name(machineBeans.get(i).getUser_name());
                DBManager.getInstance(this).insertMachine(machineBean);
                machineBindMemberBeans = machineBeans.get(i).getMachineMemberBind();
                for (int j = 0;j<machineBindMemberBeans.size();j++){
                    DBManager.getInstance(this).insertMachineBindMember(machineBindMemberBeans.get(j));
                }

                if (machineBean.getMachineName().startsWith("SWAN") || machineBean.getMachineName().startsWith("swan")){
                    isHaveFat = true;
                }

            }catch (SQLiteConstraintException e){
                Log.e("error:" , e.getMessage());
            }
        }
        if (!isHaveFat){
            JumpFind();
        }
    }

    @Override
    public void onfail(String str) {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
//        DBManager.getInstance(this).deleteAllMachineBean();
        if (str.contains("查无数据")){
            DBManager.getInstance(this).deleteAllMachineBean();
            JumpFind();
        }
    }

    //跳到查找页面
    private void JumpFind(){
        final NoDevDialog noDevDialog = new NoDevDialog(this,R.style.MyDialog);
        noDevDialog.setYesOnclickListener(new NoDevDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                noDevDialog.dismiss();
                if (!isHasBindDev){
                    UToast.show(FatActivity.this,"请先绑定体脂称再测量");
                    stopScan();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("page","page");
//                    UIHelper.launcherForResultBundle(FatActivity.this, HealthMonitorActivity.class, mRequestCode,bundle);
                    Intent intent = new Intent();
                    intent.setClass(FatActivity.this,BlueToothDevActivity.class);
                    startActivityForResult(intent,mRequestCode);
                }
            }
        });
        noDevDialog.setNoOnclickListener(new NoDevDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                noDevDialog.dismiss();
            }
        });
        mProgressBar.setVisibility(View.GONE);
        noDevDialog.ShowDialog();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFail() {

    }

    @Override
    public void onSuccess(LastFatMeasureDataBean<MeasureRecordBean> measureRecordBeans) {
        if (measureRecordBeans != null) {
            MeasureRecordBean measureRecordBean = measureRecordBeans.getProject6();
            if (measureRecordBean != null) {
                BodyFatData bodyFatData = new BodyFatData();
                bodyFatData.setWeight(Double.parseDouble(measureRecordBean.getCheckValue3()));
                bodyFatData.setBmi(Double.parseDouble(measureRecordBean.getCheckValue4()));
                bodyFatData.setBfr(Double.parseDouble(measureRecordBean.getCheckValue5()));
                bodyFatData.setRom(Double.parseDouble(measureRecordBean.getCheckValue6()));
                bodyFatData.setVwc(Double.parseDouble(measureRecordBean.getCheckValue7()));
                bodyFatData.setDbw(Double.parseDouble(measureRecordBean.getCheckValue8()));
                bodyFatData.setBm(Double.parseDouble(measureRecordBean.getCheckValue9()));
                if (measureRecordBean.getCheckValue10().equals("0.0")) {
                    bodyFatData.setUvi(0);
                } else {
                    double v = Double.parseDouble(measureRecordBean.getCheckValue10());
                    bodyFatData.setUvi((int) v);
                }
                bodyFatData.setBmr(Double.parseDouble(measureRecordBean.getCheckValue11()));
                if (mCommonLayout != null) {
                    mCommonLayout.onGetFatData(bodyFatData);
                }
            }else {
                mCommonLayout.onGetFatData(null);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == mRequestCode){
                machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(this).queryMachineBeanList();
                ArrayList<MachineBean> arrayList = new ArrayList<>();
                for (MachineBean machineBean : machineBeans){
                    if (machineBean.getMachineName().startsWith("SWAN") || machineBean.getMachineName().startsWith("swan")){
                        isHasBindDev = true;
                        arrayList.add(machineBean);
                    }
                }

                if (!isHasBindDev){
                    JumpFind();
                }

                if (familyUserInfos != null && familyUserInfos.size() != 0){
//            mCommonChangeTitle.SetOnCommonChangeListener(this);
//            mCommonChangeTitle.setData(familyUserInfos,machineBeans);
                    mCommonChangeTitle.setHandler(myHandler);
                    mCommonChangeTitle.setLayout(mTestFatLayout);
                    mMemberId = String.valueOf(familyUserInfos.get(0).getMember_Id());
                    familyUserInfo = (FamilyUserInfo) familyUserInfos.get(0);
                    mRoundImageView.setVisibility(View.VISIBLE);
                    mRoundImageView.setImageResource(R.mipmap.ic_fat_history);

                    if (arrayList != null && arrayList.size() > 0){
                        startScan();
                        mCommonChangeTitle.setDevMemberName(arrayList.get(0));
                    }
                }
            }
        }
    }
}
