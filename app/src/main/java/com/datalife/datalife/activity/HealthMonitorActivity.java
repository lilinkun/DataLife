package com.datalife.datalife.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.FragmentsHealthAdapter;
import com.datalife.datalife.adapter.MemberAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseHealthActivity;
import com.datalife.datalife.base.BaseHealthFragment;
import com.datalife.datalife.bean.LastMeasureDataBean;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.bean.SimpleBackPage;
import com.datalife.datalife.contract.HealthMonitorContract;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.fragment.BpFragment;
import com.datalife.datalife.fragment.BtFragment;
import com.datalife.datalife.fragment.ECGFragment;
import com.datalife.datalife.fragment.EquipmanagerFragment;
import com.datalife.datalife.fragment.MonitorInfoFragment;
import com.datalife.datalife.fragment.SPO2HFragment;
import com.datalife.datalife.interf.OnDataListener;
import com.datalife.datalife.interf.OnPageListener;
import com.datalife.datalife.interf.RecyclerItemClickListener;
import com.datalife.datalife.presenter.HealthMonitorPresenter;
import com.datalife.datalife.service.HcService;
import com.datalife.datalife.util.AlertDialogBuilder;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.PermissionManager;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.util.UToast;
import com.datalife.datalife.widget.CustomViewPager;
import com.datalife.datalife.widget.NoDevDialog;
import com.linktop.DeviceType;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.constant.BluetoothState;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.datalife.datalife.util.DensityUtil.dip2px;

/**
 * Created by LG on 2018/1/18.
 * 健康检测仪演示界面
 */

public class HealthMonitorActivity extends BaseHealthActivity implements MonitorDataTransmissionManager.OnServiceBindListener,OnDataListener,
        ViewPager.OnPageChangeListener,OnPageListener,ServiceConnection,HealthMonitorContract.HealthMonitorView {

    @BindView(R.id.view_pager)
    public CustomViewPager viewPager;
    @BindView(R.id.btn_measure)
    Button btnMeasure;
    @BindView(R.id.btn_upload_data)
    Button btnUploadData;
    @BindView(R.id.tv_headtop)
    TextView mHeadTopTv;
    @BindView(R.id.iv_head_right)
    ImageView mRightImage;
    @BindView(R.id.iv_bp_operation)
    ImageView mIvOperation;
    @BindView(R.id.include_battery)
    RelativeLayout relativeLayout;
    @BindView(R.id.rv_member)
    RecyclerView recyclerView;
    @BindView(R.id.iv_bluetooth)
    ImageView mIvBluetooth;
    @BindView(R.id.tv_battery)
    TextView mBattery;
    @BindView(R.id.ll_top)
    LinearLayout mTopLayout;
    @BindView(R.id.pb_loadding)
    ProgressBar mLoadingPb;

    public List<FamilyUserInfo> familyUserInfos= null;
    private FragmentsHealthAdapter adapter = null;
    private final SparseArray<BaseHealthFragment> sparseArray = new SparseArray<>();
    private int mPosition;
    public boolean isShowUploadButton;
    public HcService mHcService;
    private MonitorInfoFragment mMonitorInfoFragment;
    private EquipmanagerFragment mEquipmanagerFragment;
    private ECGFragment mEcgFragment;
    private BpFragment mBpFragment;
    private BtFragment mBtFragment;
    private SPO2HFragment mSpo2HFragment;
    private ArrayList<MachineBean> machineBeans = null;
    private MachineBean machineBean = null;
    public static String mMemberId = "";
    public static String mMachineBindId = "";
    private MemberAdapter memberAdapter = null;
    private ArrayList<MachineBindMemberBean> machineBindMemberBeans = null;
    private boolean isBlueToothOpen = false;
    private int batteryValue;
    private ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private boolean isJumpEquit = false;
    private final int mRequestCode = 0x0121;

    private NoDevDialog noDevDialog = null;

    HealthMonitorPresenter healthMonitorPresenter = new HealthMonitorPresenter(this);

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HcService.BLE_STATE:
                    final int state = (int) msg.obj;
                    if (state == BluetoothState.BLE_NOTIFICATION_ENABLED) {
                        mHcService.dataQuery(HcService.DATA_QUERY_SOFTWARE_VER);
                    } else {
                        mMonitorInfoFragment.onBleState(state);

                        if (state == BluetoothState.BLE_CONNECTED_DEVICE){

                            if (machineBean == null){
                                if (machineBeans != null && machineBeans.size() > 0) {
                                    machineBean = machineBeans.get(0);
                                }
                            }

                            if (mIvBluetooth != null) {
                                mIvBluetooth.setImageResource(R.mipmap.ic_bluetooth_open);
                            }
                            isBlueToothOpen = true;

                            if (viewPager != null) {
                                if (viewPager.getCurrentItem() == DataLifeUtil.PAGE_EQUIT) {
                                    mEquipmanagerFragment.changeState(mHcService.returnDeviceName());
                                }
                            }
                        }else {
                            if (mIvBluetooth != null) {
                                mIvBluetooth.setImageResource(R.mipmap.ic_bluetooth_close);
                            }
                            isBlueToothOpen = false;
                        }
                    }
                    break;

                case IDatalifeConstant.MESSAGESERVICE:
                    if (msg.getData() != null) {
                        String name = msg.getData().getString("name");
                        BluetoothDevice bluetoothDevice = (BluetoothDevice)msg.getData().getParcelable("device");
                        mMonitorInfoFragment.setName(name,bluetoothDevice);

                        for (int i = 0;i < machineBeans.size();i++){
                            String names = machineBeans.get(i).getMachineName();
                            if (name.equals(names) && name.startsWith("HC0")) {
                                mMonitorInfoFragment.getMember(machineBeans.get(i));
//                                if (viewPager.getCurrentItem() != DataLifeUtil.PAGE_EQUIT) {
                                    mHcService.stopSearchAndConnect(name);
//                                }
                            }
                        }
                    }
                    break;

                case IDatalifeConstant.VISIBLEHEALTH:
//                    Bundle bundle = msg.getData();
//                    int batteryValue = bundle.getInt("battery");
                    if (batteryValue != 0) {
                        mBattery.setText(batteryValue + "%");
                    }
                    break;
            }
        }
    };

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mHcService = ((HcService.LocalBinder) service).getService();
        mHcService.setHandler(mHandler);
        //UI
        adapter = new FragmentsHealthAdapter
                (getSupportFragmentManager());
        getMenusFragments();
        adapter.setFragments(sparseArray);
        viewPager.setAdapter(adapter);

        if (getIntent().getBundleExtra(DataLifeUtil.BUNDLEMEMBER) != null){
            mLoadingPb.setVisibility(View.GONE);
            viewPager.setCurrentItem(DataLifeUtil.PAGE_EQUIT);

            if (noDevDialog != null && noDevDialog.isShowing()){
                noDevDialog.dismiss();
            }
        }else{

            boolean isHasBindDev = false;
            for (MachineBean machineBean : machineBeans){
                if (machineBean.getMachineName().startsWith("HC0") || machineBean.getMachineName().startsWith("hc0")){
                    isHasBindDev = true;
                }
            }
            if (!isHasBindDev){
            }else {
                    mHcService.initBluetooth();
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mHcService = null;
    }

    public void onOpenBLE() {
        startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), MonitorInfoFragment.REQUEST_OPEN_BT);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_monitor);

        BluetoothAdapter blueadapter= BluetoothAdapter.getDefaultAdapter();
        if(!blueadapter.isEnabled()){
            onOpenBLE();
        }

        if (PermissionManager.canScanBluetoothDevice(this)){
        }

        healthMonitorPresenter.onCreate();
        healthMonitorPresenter.attachView(this);

        viewPager.setCanScroll(false);//设置viewpager不能滑动
        viewPager.setOffscreenPageLimit(1);
        viewPager.setPageMargin(10);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0, false);
        familyUserInfos = DBManager.getInstance(this).queryFamilyUserInfoList();
        //绑定服务，
        // 类型是 HealthMonitor（HealthMonitor健康检测仪），
        if (ProApplication.isUseCustomBleDevService) {
            Intent serviceIntent = new Intent(this, HcService.class);
            bindService(serviceIntent, this, BIND_AUTO_CREATE);
        } else {
            //绑定服务，
            // 类型是 HealthMonitor（HealthMonitor健康检测仪），
            MonitorDataTransmissionManager.getInstance().bind(DeviceType.HealthMonitor, this);
        }


        machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(this).queryMachineBeanList();

        noDevDialog = new NoDevDialog(this,R.style.MyDialog);
        noDevDialog.setYesOnclickListener(new NoDevDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                noDevDialog.dismiss();
                isJumpEquit = true;
//                viewPager.setCurrentItem(DataLifeUtil.PAGE_EQUIT);
                Intent intent = new Intent();
                intent.setClass(HealthMonitorActivity.this,BlueToothDevActivity.class);
                startActivityForResult(intent,mRequestCode);
            }
        });
        noDevDialog.setNoOnclickListener(new NoDevDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                noDevDialog.dismiss();
            }
        });
        healthMonitorPresenter.getMachineInfo("1","15",ProApplication.SESSIONID);

        mIvBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBlueToothOpen){
                    mMonitorInfoFragment.clickConnect();
                }
            }
        });
        if (viewPager.getCurrentItem() == DataLifeUtil.PAGE_HOME || viewPager.getCurrentItem() == DataLifeUtil.PAGE_EQUIT){
            mTopLayout.setVisibility(View.GONE);
        }else {
            mTopLayout.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void onDestroy() {
        if (ProApplication.isUseCustomBleDevService) {
            unbindService(this);
            if (viewPager.getCurrentItem() != DataLifeUtil.PAGE_EQUIT){
                mHcService.stopSearch();
            }
        } else {
            //解绑服务
            MonitorDataTransmissionManager.getInstance().unBind();
        }
        ProApplication.isShowUploadButton.set(false);
        super.onDestroy();
        healthMonitorPresenter.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onServiceBind() {
        /*
        * 为避免某些不是自己所要的设备出现在蓝牙设备扫描列表中，需要调用该API去设置蓝牙设备名称前缀白名单。
        * 蓝牙设备扫描时以白名单内的字段作为设备名前缀的设备将被添加到蓝牙设备扫描列表中，其余的过滤。
        * PS：不使用该API，设备列表将不过滤。
        *     若使用该API，请代入有效的资源ID。
        * */
        MonitorDataTransmissionManager.getInstance().setScanDevNamePrefixWhiteList(this,R.array.health_monitor_dev_name_prefixes);
        //服务绑定成功后加载各个测量界面
        adapter = new FragmentsHealthAdapter(getSupportFragmentManager());
        getMenusFragments();
        adapter.setFragments(sparseArray);

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onServiceUnbind() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        Log.e("onPageSelected", "position:" + mPosition + ", isShowUploadButton ? " + isShowUploadButton);
        btnUploadData.setVisibility((mPosition > 0 && isShowUploadButton) ? View.VISIBLE : View.GONE);

        mHeadTopTv.setText(adapter.getPageTitle(position));

        if (position == DataLifeUtil.PAGE_HOME|| position == DataLifeUtil.PAGE_EQUIT){
            mRightImage.setVisibility(View.GONE);
            mTopLayout.setVisibility(View.GONE);
        }else if (position == DataLifeUtil.PAGE_BP || position == DataLifeUtil.PAGE_ECG || position == DataLifeUtil.PAGE_TEMP || position == DataLifeUtil.PAGE_SPO2H){
            mRightImage.setImageResource(R.mipmap.ic_operation);
            mRightImage.setVisibility(View.VISIBLE);
            mTopLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public void clickUploadData(View v) {
        if (mPosition > 0) {
            sparseArray.get(mPosition).uploadData();
        }
    }

    public void layoutVisible(int visible){
            relativeLayout.setVisibility(visible);
    }

    private void getMenusFragments() {
        mMonitorInfoFragment = new MonitorInfoFragment();
        mEquipmanagerFragment = new EquipmanagerFragment();
        mEcgFragment = new ECGFragment();
        mBpFragment =  new BpFragment();
        mBtFragment = new BtFragment();
        mSpo2HFragment = new SPO2HFragment();
        sparseArray.put(DataLifeUtil.PAGE_HOME, mMonitorInfoFragment);
        sparseArray.put(DataLifeUtil.PAGE_BP,mBpFragment);
        sparseArray.put(DataLifeUtil.PAGE_TEMP, mBtFragment);
        sparseArray.put(DataLifeUtil.PAGE_SPO2H, mSpo2HFragment);
        sparseArray.put(DataLifeUtil.PAGE_ECG, mEcgFragment);
        sparseArray.put(DataLifeUtil.PAGE_EQUIT, mEquipmanagerFragment);
    }

    public void reset() {
        viewPager.setCanScroll(false);
        btnMeasure.setText("点击开始测量");
    }

    @Override
    public void onBack() {
        if (viewPager.getCurrentItem() == DataLifeUtil.PAGE_EQUIT){
            setResult(Activity.RESULT_OK);
            finish();
        }else {
            onBackMeasuring();
            viewPager.setCurrentItem(0);
            mMonitorInfoFragment.setDevMemberId(mMemberId);
        }
    }

    @Override
    public void onPage(int page) {

//        if (!isBlueToothOpen){
//            toast("您还没有连接设备");
//            return;
//        }

        if (page == DataLifeUtil.PAGE_ECG){
            mEcgFragment.onVisible();
        }
        mPosition = page;

        if (machineBindMemberBeans != null && machineBindMemberBeans.size() > 0) {
            for (int i = 0; i < machineBindMemberBeans.size(); i++) {
                if (machineBindMemberBeans.get(i).getMember_Id().equals(mMemberId)) {
                    if (memberAdapter == null){
                        onMember(machineBindMemberBeans,machineBean.getMachineBindId());
                    }
                    memberAdapter.setClick(i);
                    memberAdapter.notifyDataSetChanged();
                }
            }
        }

        viewPager.setCurrentItem(page,false);
    }

    //测试页面上选择成员列表
    @Override
    public void onMember(final ArrayList<MachineBindMemberBean> machineBindMemberBean,String machinebindid) {

        this.mMachineBindId = machinebindid;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (recyclerView == null){
            return;
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        this.machineBindMemberBeans = machineBindMemberBean;
        memberAdapter = new MemberAdapter(this,machineBindMemberBeans);
        recyclerView.setAdapter(memberAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < 0){
                    return;
                }
                memberAdapter.setClick(position);
                memberAdapter.notifyDataSetChanged();
                healthMonitorPresenter.getNewMeasureInfo(machineBindMemberBeans.get(position).getMember_Id(),machineBindMemberBeans.get(position).getMachine_Id(),ProApplication.SESSIONID);
                mMemberId = machineBindMemberBeans.get(position).getMember_Id();
            }

            @Override
            public void onLongClick(View view, int posotion) {

            }
        }));
    }

    @Override
    public void onMachine(MachineBean machineBean) {
        this.machineBean = machineBean;
    }

    @Override
    public void onChageMember(MachineBindMemberBean machineBindMemberBean) {
        machineBindMemberBeans = (ArrayList<MachineBindMemberBean>) DBManager.getInstance(this).queryMachineBindMemberBeanList(machineBindMemberBean.getMachineBindId());
        healthMonitorPresenter.getNewMeasureInfo(machineBindMemberBean.getMember_Id(),machineBindMemberBean.getMachine_Id(),ProApplication.SESSIONID);
        if(machineBindMemberBeans != null){
            for (int i = 0; i < machineBindMemberBeans.size();i++){
                if (machineBindMemberBeans.get(i).getMember_Id().equals(machineBindMemberBean.getMember_Id())){
                    recyclerView.scrollToPosition(i);
                }
            }
        }
    }

    @Override
    public void onBattery(int batteryValue) {
        this.batteryValue = batteryValue;
        mHandler.sendEmptyMessage(IDatalifeConstant.VISIBLEHEALTH);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            back();
            return true;
        }
        return  super.onKeyDown(keyCode, event);
    }

    private void onBackMeasuring(){
        if (ProApplication.isUseCustomBleDevService){
            if(mHcService.getBleDevManager().isMeasuring()){
                new AlertDialogBuilder
                        (this)
                        .setTitle("提示")
                        .setMessage("测量中，若要退出界面请先停止测量。")
                        .setPositiveButton("好的", null)
                        .create().show();
                return;
            }
        }else{
            if (MonitorDataTransmissionManager.getInstance().isMeasuring()) {
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("测量中，若要退出界面请先停止测量。")
                        .setPositiveButton("好的", null)
                        .create().show();
                return;
            }
        }
    }

    @OnClick({R.id.iv_head_left,R.id.iv_head_right,R.id.btn_connect,R.id.iv_bluetooth})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_head_left:
                back();
                break;

            case R.id.iv_head_right:
                switch (viewPager.getCurrentItem()){
                    case DataLifeUtil.PAGE_BP:
                        onIvOperation(R.mipmap.ic_bp_operation);
                        break;

                    case DataLifeUtil.PAGE_ECG:
                        onIvOperation(R.mipmap.ic_ecg_operation);
                        break;

                    case DataLifeUtil.PAGE_TEMP:
                        onIvOperation(R.mipmap.ic_temp_operation);
                        break;

                    case DataLifeUtil.PAGE_SPO2H:
                        onIvOperation(R.mipmap.ic_spo2h_operation);
                        break;
                }
                break;

            case R.id.btn_connect:
                mMonitorInfoFragment.clickConnect();
                break;

            case R.id.iv_bluetooth:
                if (!mHcService.isConnected) {
                    mMonitorInfoFragment.clickConnect();
                }
                break;

        }
    }

    //点击提示键的时候
    public void onIvOperation(int idres){

        if (mIvOperation != null && mIvOperation.isShown()){
            mIvOperation.setVisibility(View.GONE);
        }else {
            mIvOperation.setImageResource(idres);
            mIvOperation.setVisibility(View.VISIBLE);
        }
    }

    //点击回退键的时候
    public void back(){
        onBackMeasuring();

        if (mIvOperation != null && mIvOperation.isShown()){
            mIvOperation.setVisibility(View.GONE);
            return;
        }

        if (viewPager.getCurrentItem() == DataLifeUtil.PAGE_HOME) {
            finish();
        }else if (viewPager.getCurrentItem() == DataLifeUtil.PAGE_EQUIT){
            if (isJumpEquit){
                mLoadingPb.setVisibility(View.GONE);
                machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(this).queryMachineBeanList();
                boolean isHaveHealthDev = false;
                if (machineBeans != null && machineBeans.size() > 0) {
                    for (MachineBean machineBean : machineBeans) {
                        if (machineBean.getMachineName().startsWith("HC0") || machineBean.getMachineName().startsWith("hc0")){
                            isHaveHealthDev = true;
                            mHcService.initBluetooth();
                            mHcService.quicklyConnect();
                            mEquipmanagerFragment.onDestroy();
                        }
                    }
                    if (!isHaveHealthDev){
                        if (noDevDialog != null) {
                            mLoadingPb.setVisibility(View.GONE);
                            noDevDialog.ShowDialog();
                        }
                    }
                }else {
                    if (noDevDialog != null) {
                        mLoadingPb.setVisibility(View.GONE);
                        noDevDialog.ShowDialog();
                    }
                }

                viewPager.setCurrentItem(DataLifeUtil.PAGE_HOME);
            }else {
            setResult(Activity.RESULT_OK);
            finish();
            }
        }else{
            onBack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case MonitorInfoFragment.REQUEST_OPEN_BT:
                    //蓝牙启动结果
                    Toast.makeText(this, resultCode == Activity.RESULT_OK ? "蓝牙已打开" : "蓝牙打开失败", Toast.LENGTH_SHORT).show();

//                    mMonitorInfoFragment.clickConnect();
                    if (viewPager.getCurrentItem() == DataLifeUtil.PAGE_HOME){
                        mMonitorInfoFragment.onBleState(BluetoothState.BLE_CONNECTING_DEVICE);
                        mHcService.quicklyConnect();
                    }

                    if (viewPager.getCurrentItem() == DataLifeUtil.PAGE_EQUIT){
                        mEquipmanagerFragment.quicklyConnect();
                    }
                    break;

                case mRequestCode:
                    mLoadingPb.setVisibility(View.GONE);
                    machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(this).queryMachineBeanList();
                    boolean isHaveHealthDev = false;
                    if (machineBeans != null && machineBeans.size() > 0) {
                        for (MachineBean machineBean : machineBeans) {
                            if (machineBean.getMachineName().startsWith("HC0") || machineBean.getMachineName().startsWith("hc0")){
                                isHaveHealthDev = true;
                                mHcService.initBluetooth();
                                mHcService.quicklyConnect();
                            }
                        }
                        if (!isHaveHealthDev){
                            if (noDevDialog != null) {
                                mLoadingPb.setVisibility(View.GONE);
                                noDevDialog.ShowDialog();
                            }
                        }
                    }else {
                        if (noDevDialog != null) {
                            mLoadingPb.setVisibility(View.GONE);
                            noDevDialog.ShowDialog();
                        }
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void sendSuccess() {

    }

    @Override
    public void sendFail(String str) {

    }

    @Override
    public void getBindListSuccess(List<MachineBindMemberBean> machineBindMemberBeans) {

    }

    @Override
    public void getBindListFail(String str) {

    }

    @Override
    public void getSuccess(LastMeasureDataBean<MeasureRecordBean> bean) {
        MeasureRecordBean btMeasureRecordBean = bean.getProject2();
        MeasureRecordBean bpMeasureRecordBean = bean.getProject1();
        MeasureRecordBean spo2hMeasureRecordBean = bean.getProject3();
        MeasureRecordBean ecgMeasureRecordBean = bean.getProject5();
        mEcgFragment.setOldTest(ecgMeasureRecordBean);
        mBpFragment.setOldTest(bpMeasureRecordBean);
        mBtFragment.setOldTest(btMeasureRecordBean);
        mSpo2HFragment.setOldTest(spo2hMeasureRecordBean);
    }

    @Override
    public void getFail(String str) {
        toast(str);
    }

    @Override
    public void onSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews) {
        mLoadingPb.setVisibility(View.GONE);
        ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> machineBeans = resultNews;
        DBManager.getInstance(this).deleteAllMachineBean();
        DBManager.getInstance(this).deleteAllMachineBindBean();
        ArrayList<MachineBindMemberBean> machineBindMemberBeans = null;
        boolean isHaveHealth = false;
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
                if (machineBean.getMachineName().startsWith("HC0") || machineBean.getMachineName().startsWith("hc0")){
                    isHaveHealth = true;
                }
            }catch (SQLiteConstraintException e){
                Log.e("error:" , e.getMessage());
            }
        }
        if (!isHaveHealth){
            if (noDevDialog != null) {
                if (viewPager.getCurrentItem() != DataLifeUtil.PAGE_EQUIT) {
                    noDevDialog.ShowDialog();
                }
            }
        }
    }

    @Override
    public void onfail(String str) {
        mLoadingPb.setVisibility(View.GONE);
//        DBManager.getInstance(this).deleteAllMachineBean();
        if (str.contains("查无数据")){
            DBManager.getInstance(this).deleteAllMachineBean();
            if (noDevDialog != null) {
                if (viewPager.getCurrentItem() != DataLifeUtil.PAGE_EQUIT) {
                    noDevDialog.ShowDialog();
                }
            }
        }

    }

}
