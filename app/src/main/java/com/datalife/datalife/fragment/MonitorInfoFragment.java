package com.datalife.datalife.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.activity.RecordActivity;
import com.datalife.datalife.activity.SimplebackActivity;
import com.datalife.datalife.adapter.BindDevListAdapter;
import com.datalife.datalife.adapter.MachineItemAdapter;
import com.datalife.datalife.adapter.MemberItemAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseHealthFragment;
import com.datalife.datalife.bean.Bp;
import com.datalife.datalife.bean.ECG;
import com.datalife.datalife.bean.LastMeasureDataBean;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.binding.ObservableString;
import com.datalife.datalife.contract.HealthMonitorContract;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.interf.OnCommonChangeListener;
import com.datalife.datalife.interf.OnPageListener;
import com.datalife.datalife.presenter.HealthMonitorPresenter;
import com.datalife.datalife.service.HcService;
import com.datalife.datalife.util.AlertDialogBuilder;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DefaultPicEnum;
import com.datalife.datalife.util.PermissionManager;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.widget.CommonChangeTitle;
import com.datalife.datalife.widget.CommonTitle;
import com.datalife.datalife.widget.RoundImageView;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.constant.BluetoothState;
import com.linktop.constant.DeviceInfo;
import com.linktop.constant.WareType;
import com.linktop.infs.OnBatteryListener;
import com.linktop.infs.OnBleConnectListener;
import com.linktop.infs.OnDeviceInfoListener;
import com.linktop.infs.OnDeviceVersionListener;
import com.linktop.whealthService.BleDevManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import lib.linktop.common.CssSubscriber;
import lib.linktop.common.LogU;
import lib.linktop.common.ResultPair;
import lib.linktop.intf.OnCssSocketRunningListener;
import lib.linktop.obj.Device;
import lib.linktop.obj.LoadBean;
import lib.linktop.sev.CssServerApi;
import lib.linktop.sev.HmLoadDataTool;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by LG on 2018/1/18.
 */
public class MonitorInfoFragment  extends BaseHealthFragment
        implements OnDeviceVersionListener, OnBleConnectListener, OnBatteryListener, OnDeviceInfoListener,HealthMonitorContract.HealthMonitorView {

    public static final int REQUEST_OPEN_BT = 0x23;
    private final int MESSAGE_BLE_CONNECTING_DEVICE = 0x234;

    private final ObservableString btnText = new ObservableString("打开蓝牙");
    private final ObservableString power = new ObservableString("");
    private final ObservableString id = new ObservableString("");//当前选定的设备id
    private final ObservableString key = new ObservableString("");//当前选定的设备key
    private final ObservableString softVer = new ObservableString("");
    private final ObservableString hardVer = new ObservableString("");
    private final ObservableString firmVer = new ObservableString("");
    private final ObservableBoolean isLogin = ProApplication.isLogin;
    private final ObservableInt isDevBind = new ObservableInt(0);
    private boolean showScanList = false;
    private BleDeviceListDialogFragment mBleDeviceListDialogFragment;
    private OnPageListener onPageListener;

    private BindDevListAdapter mAdapter;
    private String MCONNECTSTR = "connectStr";
    private final int CONNECTHANDLER = 0x1231;

    private boolean isCssSocketOpen;
    private Subscription subscription;

    public  List<FamilyUserInfo> familyUserInfos= null;
    public static FamilyUserInfo familyUserInfo = null;

    @BindView(R.id.ll_electrocardiogram)
    LinearLayout mElectrocarDiogramLayout;
    @BindView(R.id.ll_search_layout)
    LinearLayout mSearchLayout;
    @BindView(R.id.tv_binddev)
    TextView tv_search;
    @BindView(R.id.ll_dev_top)
    LinearLayout linearLayout;
    @BindView(R.id.ll_dev_bottom)
    LinearLayout bottomLayout;
    @BindView(R.id.tv_my_dev)
    TextView mDevNameTv;
    @BindView(R.id.tv_my_dev_member)
    TextView mDevMemberName;
    @BindView(R.id.tv_connect)
    TextView mTvDevConnect;
    @BindView(R.id.iv_dev_icon)
    ImageView mIcDev;
    @BindView(R.id.iv_member_icon)
    ImageView mIcMember;
//    @BindView(R.id.commomtitle)
//    CommonTitle commonTitle;

    public static String deviceId = "";
    private PopupWindow popupWindow;
    public static ArrayList<String> mNames = new ArrayList<>();
    public static ArrayList<Parcelable> parcelables = new ArrayList<>();
    private boolean isFirstLogin = false;
    private String mMemberId = "";
    private String mMachineId = "";
    private boolean isBinded = false;
    private ArrayList<MachineBean> machineBeans = null;
    private ArrayList<MachineBindMemberBean> machineBindMemberBean = null;

    HealthMonitorPresenter healthMonitorPresenter = new HealthMonitorPresenter(getActivity());

    private Handler myHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case CONNECTHANDLER:
                    if (msg.getData() == null)
                        return;
                    String resStr = msg.getData().getString(MCONNECTSTR);
                    if (resStr == null){
                        return;
                    }
                    break;

                case MESSAGE_BLE_CONNECTING_DEVICE:
                    Toast.makeText(mActivity, "蓝牙连接中...", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    tv_search.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

       Timer timer = new Timer();
       TimerTask timerTask = new TimerTask() {
         @Override
        public void run() {
                 Message message = new Message();
                 message.what = 1;
                 myHandler.sendMessage(message);
             }
     };

    public MonitorInfoFragment() {
    }

    @Override
    public String getTitle() {
        return getActivity().getResources().getString(R.string.health_testing);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ProApplication.isUseCustomBleDevService) {
            BleDevManager bleDevManager = mHcService.getBleDevManager();
            mHcService.setOnDeviceVersionListener(this);
            bleDevManager.getBatteryTask().setBatteryStateListener(this);
            bleDevManager.getDeviceTask().setOnDeviceInfoListener(this);
//            timer.schedule(timerTask,5000);
        } else {
            MonitorDataTransmissionManager.getInstance().setOnBleConnectListener(this);
            MonitorDataTransmissionManager.getInstance().setOnBatteryListener(this);
            MonitorDataTransmissionManager.getInstance().setOnDevIdAndKeyListener(this);
            MonitorDataTransmissionManager.getInstance().setOnDeviceVersionListener(this);
        }

    }

    @Override
    public void onDestroy() {
        //demo中把该界面当成主界面（相对于健康检测仪而言，当然也可以认为是上层的Activity），当销毁主界面前，应该把蓝牙连接断掉
        if (ProApplication.isUseCustomBleDevService) {
            if (mHcService.isConnected) {
                mHcService.disConnect();
            }
        } else if (MonitorDataTransmissionManager.getInstance().isConnected())
            MonitorDataTransmissionManager.getInstance().disConnectBle();
        if (isLogin.get()) {
            //该长连接，设备连接蓝牙时启动，设备断开蓝牙停止。
            HmLoadDataTool.getInstance().destroyCssSocket();
            isDevBind.set(0);
        }
        super.onDestroy();
    }

    protected int getlayoutId() {
        return R.layout.fragment_health_info;
    }

    @Override
    protected void initEventAndData() {

        familyUserInfos = DBManager.getInstance(getActivity()).queryFamilyUserInfoList();

        machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(getActivity()).queryMachineBeanList();

        healthMonitorPresenter.onCreate();
        healthMonitorPresenter.attachView(this);

        initData();

        if (familyUserInfos != null && familyUserInfos.size() != 0){
//            mAccountTv.setText(familyUserInfos.get(0).getMember_Name());
//            roundImageView.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfos.get(0).getMember_Portrait()).getResPic());
            if (mMemberId == null || mMemberId.equals("")){
                mMemberId = String.valueOf(familyUserInfos.get(familyUserInfos.size() - 1).getMember_Id());
            }
            HealthMonitorActivity.mMemberId = mMemberId;
        }

        if (familyUserInfos != null && familyUserInfos.size() != 0) {
            familyUserInfo = familyUserInfos.get(0);
        }

        onPageListener = (OnPageListener) getActivity();

        onBleState(MonitorDataTransmissionManager.getInstance().getBleState());
        if (isLogin.get()) {
            mAdapter = new BindDevListAdapter(getContext(), id);
//            customRecyclerView.setAdapter(mAdapter);
            getDevList(false);
        }
    }


    //判断设备是否绑定了成员
    public boolean isHaveMember(){
        if (mDevMemberName.getText().toString().equals(getActivity().getResources().getString(R.string.my_member))){
            toast("请先绑定健康监测仪成员再测量");
            return false;
        }
        return true;
    }

    @OnClick({R.id.ll_temp,R.id.ll_oxygen,R.id.ll_ecg,R.id.ll_heartrate,R.id.tv_history,R.id.tv_binddev,R.id.rl_dev_member,R.id.rl_my_dev})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.ll_temp:
                if (isHaveMember()) {
                    onPageListener.onPage(DataLifeUtil.PAGE_TEMP);
                }
                break;

            case R.id.ll_oxygen:
                if (isHaveMember()) {
                    onPageListener.onPage(DataLifeUtil.PAGE_SPO2H);
                }
                break;

            case R.id.ll_ecg:
                if (isHaveMember()) {
                    onPageListener.onPage(DataLifeUtil.PAGE_ECG);
                }
                break;

            case R.id.ll_heartrate:
                if (isHaveMember()) {
                    onPageListener.onPage(DataLifeUtil.PAGE_BP);
                }
                break;

            case R.id.tv_history:
                UIHelper.launcherForResult(getActivity(),RecordActivity.class,22331);
                break;

            case R.id.ll_health:

                popupWindow.dismiss();
                break;

            case R.id.tv_binddev:
                onPageListener.onPage(DataLifeUtil.PAGE_EQUIT);
                break;

            case R.id.rl_my_dev:

                    View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.member_popup_listview, null);

                    ArrayList<MachineBean> machineBeans1 = new ArrayList<>();
                    for (MachineBean machineBean : machineBeans){
                        if (machineBean.getMachineName().startsWith("HC0") || machineBean.getMachineName().startsWith("hc0")){
                            machineBeans1.add(machineBean);
                        }
                    }
                    machineBeans = machineBeans1;

                    ListView listView = (ListView) contentView.findViewById(R.id.lv_member);
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(new MachineItemAdapter(getActivity(),machineBeans,mDevNameTv.getText().toString()));

                    popupWindow = new PopupWindow(contentView,
                            LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    popupWindow.setContentView(contentView);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.showAsDropDown(linearLayout);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            bottomLayout.setBackgroundResource(R.color.transparent);
                            mIcDev.setImageResource(R.mipmap.ic_unclick_tip);
                            mDevNameTv.setTextColor(getResources().getColor(R.color.black_text_bg));
                        }
                    });
                    bottomLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            machineBindMemberBean = null;
                            machineBindMemberBean = (ArrayList<MachineBindMemberBean>)DBManager.getInstance(getActivity()).queryMachineBindMemberBeanList(machineBeans.get(position).getMachineBindId());

                            if (machineBindMemberBean != null && machineBindMemberBean.size() > 0) {
                                for (int i = 0; i < familyUserInfos.size(); i++) {
                                    if (familyUserInfos.get(i).getMember_Id() == Integer.valueOf(machineBindMemberBean.get(0).getMember_Id())) {
                                        mDevMemberName.setText(familyUserInfos.get(i).getMember_Name());
                                    }
                                }
                            }

                            if (mDevNameTv.getText().toString().equals(machineBeans.get(position).getMachineName())){
                                popupWindow.dismiss();
                                return;
                            }

                            mMachineId = machineBeans.get(position).getMachineId();
                            mDevNameTv.setText(machineBeans.get(position).getMachineName());
                            popupWindow.dismiss();

                            onDataListener.onMachine(machineBeans.get(position));

                            if (mHcService.isConnected) {
                                mHcService.disConnect();
                            }

                                mHcService.stopSearch();
                                mHcService.setDeviceName(machineBeans.get(position).getMachineName());
                                mHcService.quicklyConnect();

                        }
                    });
                mIcDev.setImageResource(R.mipmap.ic_click_tip);
                mDevNameTv.setTextColor(getResources().getColor(R.color.ecg_bg));

                break;

            case R.id.rl_dev_member:

                    if (mDevMemberName.getText().toString().equals(getActivity().getResources().getString(R.string.my_member))){

                        return;

                    }

                    View contentView1 = LayoutInflater.from(getActivity()).inflate(R.layout.member_popup_listview, null);

                    ListView listView1 = (ListView) contentView1.findViewById(R.id.lv_member);
                    listView1.setVisibility(View.VISIBLE);
                    listView1.setAdapter(new MemberItemAdapter(getActivity(),machineBindMemberBean,mDevMemberName.getText().toString()));

                    popupWindow = new PopupWindow(contentView1,
                            LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    popupWindow.setContentView(contentView1);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.showAsDropDown(linearLayout);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            bottomLayout.setBackgroundResource(R.color.transparent);
                            mIcMember.setImageResource(R.mipmap.ic_unclick_tip);
                            mDevMemberName.setTextColor(getResources().getColor(R.color.black_text_bg));
                        }
                    });
                    bottomLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
                    listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            getMemberId(machineBindMemberBean.get(position));
                            MachineBindMemberBean machineBindMember = machineBindMemberBean.get(position);
                            for(int i = 0;i<familyUserInfos.size();i++){
                                if(familyUserInfos.get(i).getMember_Id() == Integer.valueOf(machineBindMember.getMember_Id())){
                                    mDevMemberName.setText(familyUserInfos.get(i).getMember_Name());
                                }
                            }
                            popupWindow.dismiss();

                        }
                    });
                mIcMember.setImageResource(R.mipmap.ic_click_tip);
                mDevMemberName.setTextColor(getResources().getColor(R.color.ecg_bg));

                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permissionGranted = PermissionManager.isPermissionGranted(grantResults);
        switch (requestCode) {
            case PermissionManager.requestCode_location:
                if (permissionGranted) {
                    try {
                        Thread.sleep(1000L);
                        clickConnect();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "没有定位权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_OPEN_BT:
                    //蓝牙启动结果
                    Toast.makeText(getContext(), resultCode == Activity.RESULT_OK ? "蓝牙已打开" : "蓝牙打开失败", Toast.LENGTH_SHORT).show();
                    break;
                case SimplebackActivity.RESULT_EQUIPMANAGER:
                    if (data != null) {
                        String name = data.getStringExtra("name");
                        toast(name);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void reset() {
        power.set("");
        id.set("");
        key.set("");
        softVer.set("");
        hardVer.set("");
        firmVer.set("");
    }

    /******
     * 以上两个回调值，可以根据设备ID保存在SP里，
     * 这样可以在某些未连接设备但已知设备ID的情况下，
     * 直接获取并显示设备的软硬件版本号
     * 但是切记，设备升级软硬件，会更新版本号，所以每次连接蓝牙设备都应该读取软硬件版本号，
     * 若有做本地保存，及时更新本地保存，才能保证任何情况下显示版本号都是最新的。
     **************************************************************/
    @Override
    public void onBLENoSupported() {
        if (getActivity() != null){
//            Toast.makeText(getActivity(), "蓝牙不支持", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOpenBLE() {
        startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), REQUEST_OPEN_BT);
    }

    public void setName(String name,Parcelable parcelable){
        mNames.add(name);
        parcelables.add(parcelable);
    }

    //设置绑定机器的成员
    public void setDevMemberId(String memberid){
        for (int i = 0;i < familyUserInfos.size();i++){
            if (String.valueOf(familyUserInfos.get(i).getMember_Id()).equals(memberid)) {
                mDevMemberName.setText(familyUserInfos.get(i).getMember_Name());
            }
        }
    }

    @Override
    public void onBleState(int bleState) {
        Message message = new Message();
        message.what = CONNECTHANDLER;
        Bundle bundle = new Bundle();
        switch (bleState) {
            case BluetoothState.BLE_CLOSED:
//                mConnectBtn.setText("打开蓝牙");
                bundle.putString(MCONNECTSTR,"打开蓝牙");
                reset();
                setIsBinded(false);
                isDevBind.set(0);
                mTvDevConnect.setText("打开蓝牙");
                break;
            case BluetoothState.BLE_OPENED_AND_DISCONNECT:
                reset();
                isDevBind.set(0);
                setIsBinded(false);
                bundle.putString(MCONNECTSTR,"点击连接");
                mTvDevConnect.setText("连接中");

                Log.e("LG","888888888888800000000");
//                mHcService.stopSearch();
//                clickConnect();
                /**
                 * 自动连接
                 */
                if (!isFirstLogin){
                    clickConnect();
                    isFirstLogin = !isFirstLogin;
                }
                break;
            case BluetoothState.BLE_CONNECTING_DEVICE:
                bundle.putString(MCONNECTSTR,"连接中");
                mTvDevConnect.setText("连接中");
                setIsBinded(false);
                break;
            case BluetoothState.BLE_CONNECTED_DEVICE:
                bundle.putString(MCONNECTSTR,"已连接");
                mTvDevConnect.setText("已连接");
                mSearchLayout.setVisibility(View.GONE);
                setIsBinded(true);
                healthMonitorPresenter.getBindMemberList("1","16","",ProApplication.SESSIONID);

                break;
        }

        message.setData(bundle);
        myHandler.sendMessage(message);
    }

    @Override
    public void onUpdateDialogBleList() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mBleDeviceListDialogFragment != null && mBleDeviceListDialogFragment.isShowing()) {
                    mBleDeviceListDialogFragment.refresh();
                }
            }
        });
    }

    /*
    * 设备插着USB充电线，未充满电的状态
    *
    *
    * */
    @Override
    public void onBatteryCharging() {
        power.set("充电中...");
//        mPowerTv.setText("充电中...");
    }

    /*
    * 设备拔掉USB充电线，正常使用
    * */
    @Override
    public void onBatteryQuery(int batteryValue) {
        power.set(batteryValue + "%");
        onDataListener.onBattery(batteryValue);
    }

    /*
    * 设备插着USB充电线，已充满电的状态
    * */
    @Override
    public void onBatteryFull() {
        power.set("已充满");
    }

    private void getDevList(final boolean isToast) {
        CssServerApi.getDevList()
                .subscribe(new CssSubscriber<List<Device>>() {
                    @Override
                    public void onNextRequestSuccess(List<Device> devices) {
                        mAdapter.clearItems();
                        mAdapter.addItems(devices);
                        checkDevIsBind(devices, isToast);
                    }

                    @Override
                    public void onNextRequestFailed(int status) {
                        switch (status) {
                            case -1:
//                                toast("网络断开了，检查网络");
                                break;
                            default:
//                                toast("请求失败");
                                break;
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("getDevList - onError", e.getMessage());
                    }
                });
    }

    private void checkDevIsBind(List<Device> list, final boolean isToast) {
        // 登录时，页面刚创建 id 为空，确定 所获取的绑定设备列表是否有设备
        if (TextUtils.isEmpty(id.get())) {
            //若有设备，拣选列表第一个设备作为当前选定设备。
            if (list.size() > 0) {
                final Device currDev = list.get(0);
                id.set(currDev.getDevId());
//                mDeviceId.setText(currDev.getDevId());
                isDevBind.set(currDev.isPrimaryBind() ? 1 : 2);
            }
        } else
            Observable.from(list)
                    .filter(new Func1<Device, Boolean>() {
                        @Override
                        public Boolean call(Device device) {
                            Log.e("checkDevIsBind - call", "mDevId:" + id.get() + ", deviceId:" + device.getDevId());
                            return device.getDevId().equals(id.get());
                        }
                    })
                    .subscribe(new Subscriber<Device>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(Device device) {
                            isDevBind.set(device.isPrimaryBind() ? 1 : 2);
                            if (isToast)
                                toast(device.isPrimaryBind() ? "绑定成功" : "关注成功");
                        }
                    });
    }

    /**
     * 点击切换蓝牙连接状态
     */
    public void clickConnect() {
        if (ProApplication.isUseCustomBleDevService) {
            final boolean isObtain = PermissionManager.isObtain(this, PermissionManager.PERMISSION_LOCATION, PermissionManager.requestCode_location);
            if (!isObtain) {
                return;
            } else {
                if (!PermissionManager.canScanBluetoothDevice(getContext())) {
                    new AlertDialogBuilder(mActivity)
                            .setTitle("提示")
                            .setMessage("Android 6.0及以上系统需要打开GPS才能扫描蓝牙设备。")
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton("打开GPS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PermissionManager.openGPS(mActivity);
                                }
                            }).create().show();
                    return;
                }
            }
            if (mHcService.isConnected) {
                mHcService.disConnect();
            } else {
                final int bluetoothEnable = mHcService.isBluetoothEnable();
                if (bluetoothEnable == -1) {
                    onBLENoSupported();
                } else if (bluetoothEnable == 0) {
                    onOpenBLE();
                } else {
                    mHcService.quicklyConnect();
                }
            }
        } else {
            final int bleState = MonitorDataTransmissionManager.getInstance().getBleState();
            Log.e("clickConnect", "bleState:" + bleState);
            switch (bleState) {
                case BluetoothState.BLE_CLOSED:
                    MonitorDataTransmissionManager.getInstance().bleCheckOpen();
                    break;
                case BluetoothState.BLE_OPENED_AND_DISCONNECT:
                    if (MonitorDataTransmissionManager.getInstance().isScanning()) {
                        new AlertDialog.Builder(mActivity)
                                .setTitle("提示")
                                .setMessage("正在扫描设备，请稍后...")
                                .setNegativeButton(android.R.string.cancel, null)
                                .setPositiveButton("停止扫描", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MonitorDataTransmissionManager.getInstance().scan(false);
                                    }
                                }).create().show();
                    } else {
                        final boolean isObtain = PermissionManager.isObtain(this, PermissionManager.PERMISSION_LOCATION, PermissionManager.requestCode_location);
                        if (isObtain) {
                            if (PermissionManager.canScanBluetoothDevice(getContext())) {
                                if (showScanList) {
                                    connectByDeviceList();
                                } else {
                                    MonitorDataTransmissionManager.getInstance().scan(true);
                                }
                            } else {
                                new AlertDialog.Builder(mActivity)
                                        .setTitle("提示")
                                        .setMessage("Android 6.0及以上系统需要打开GPS才能扫描蓝牙设备。")
                                        .setNegativeButton(android.R.string.cancel, null)
                                        .setPositiveButton("打开GPS", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                PermissionManager.openGPS(mActivity);
                                            }
                                        }).create().show();
                            }
                        }
                    }
                    break;
                case BluetoothState.BLE_CONNECTING_DEVICE:
                    Message message = new Message();
                    message.what = MESSAGE_BLE_CONNECTING_DEVICE;
                    myHandler.sendMessage(message);
                    break;
                case BluetoothState.BLE_CONNECTED_DEVICE:
                case BluetoothState.BLE_NOTIFICATION_DISABLED:
                    toast("没有连接设备");
                    break;
                case BluetoothState.BLE_NOTIFICATION_ENABLED:
                    MonitorDataTransmissionManager.getInstance().disConnectBle();
                    HmLoadDataTool.getInstance().destroyCssSocket();
                    break;
            }
        }
    }

    public void initData(){

        for (MachineBean machineBean : machineBeans){
            if (machineBean.getMachineName().startsWith("HC0")){
                mDevNameTv.setText(machineBean.getMachineName());
                machineBindMemberBean = (ArrayList<MachineBindMemberBean>)DBManager.getInstance(getActivity()).queryMachineBindMemberBeanList(machineBean.getMachineBindId());
                if (machineBindMemberBean != null && machineBindMemberBean.size() != 0){
                    for (int i = 0;i<familyUserInfos.size();i++){
                        if (String.valueOf(familyUserInfos.get(i).getMember_Id()).equals(machineBindMemberBean.get(0).getMember_Id())){
                            mDevMemberName.setText(familyUserInfos.get(i).getMember_Name());
                            getMemberId(machineBindMemberBean.get(0));
                            getMember(machineBean);
                            mMemberId = String.valueOf(familyUserInfos.get(i).getMember_Id());
                            HealthMonitorActivity.mMemberId = mMemberId;
                            HealthMonitorActivity.mMachineBindId = machineBean.getMachineBindId();
                        }
                    }
                }
            }
        }

        mTvDevConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTvDevConnect.getText().toString().equals("点击连接")){
                    clickConnect();
                }

            }
        });

    }

    /*
    * 从设备列表中选择设备连接（用于周围环境有多台相同型号蓝牙设备的情况，避免连错）
    * */
    private void connectByDeviceList() {
        mBleDeviceListDialogFragment = new BleDeviceListDialogFragment();
        mBleDeviceListDialogFragment.show(mActivity.getSupportFragmentManager(), "");
    }


    @Override
    public void onDeviceInfo(DeviceInfo device) {
        Log.e("onDeviceInfo", device.toString());
        deviceId = device.getDeviceId();
        String deviceKey = device.getDeviceKey();
//        如果需要id 和 key 中的字母参数小写，可以如下转换
        deviceId = deviceId.toLowerCase();
        deviceKey = deviceKey.toLowerCase();
        id.set(deviceId);
//        mDeviceId.setText(deviceId);
        key.set(deviceKey);
//        mDeviceKey.setText(deviceKey);
        if (mHcService != null) {
            mHcService.dataQuery(HcService.DATA_QUERY_BATTERY_INFO);
        }
        if (isLogin.get()) {
            //从服务器确认是否绑定
            getDevList(false);
//            startUpCssDev();
        }

        String deviceName = mHcService.returnDeviceName();
//      healthMonitorPresenter.sendDeviceInfo(deviceName,deviceId, DeviceData.getUniqueId(getActivity()));
    }

    @Override
    public void onReadDeviceInfoFailed() {
        id.set("无法获取设备ID");
        key.set("无法获取设备key");
//        mDeviceId.setText("无法获取设备ID");
//        mDeviceKey.setText("无法获取设备key");
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
        toast(str);
    }

    @Override
    public void getBindListSuccess(List<MachineBindMemberBean> machineBindMemberBeans) {
    }

    public void getMember(MachineBean machineBean){
        mDevNameTv.setText(machineBean.getMachineName());

        machineBindMemberBean = (ArrayList<MachineBindMemberBean>)DBManager.getInstance(getActivity()).queryMachineBindMemberBeanList(machineBean.getMachineBindId());
        /*for (int j = 0; j < machineBindMemberBean.size();j++){
            if (machineBindMemberBean.get(j).getMachineBindId().equals(machineBean.getMachineBindId())) {
                machineBindMemberBean.add(machineBindMemberBean.get(j));
            }
        }*/

        if (machineBindMemberBean != null && machineBindMemberBean.size() != 0){
            for(int i = 0;i<familyUserInfos.size();i++){
                if(familyUserInfos.get(i).getMember_Id() == Integer.valueOf(machineBindMemberBean.get(0).getMember_Id())){
                    mDevMemberName.setText(familyUserInfos.get(i).getMember_Name());
                    getMemberId(machineBindMemberBean.get(0));
                }
            }
            }
            onDataListener.onMember(machineBindMemberBean,machineBean.getMachineBindId());

//        commonTitle.setData(machineBindMemberBean,machineBeans);
//        commonTitle.setLayout(bottomLayout);
//        commonTitle.setHandler(myHandler);
    }

    @Override
    public void getBindListFail(String str) {

    }

    @Override
    public void getSuccess(LastMeasureDataBean<MeasureRecordBean> bean) {

    }

    @Override
    public void getFail(String str) {

    }

    @Override
    public void onSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews) {

    }

    @Override
    public void onfail(String str) {

    }

    public void setMemberName(String name){
        mDevMemberName.setText(name);
    }

    @Override
    public void onDeviceVersion(WareType wareType, String version) {
        switch (wareType) {
            case VER_SOFTWARE:
                softVer.set(version);
                if (mHcService != null) {
                    mHcService.dataQuery(HcService.DATA_QUERY_HARDWARE_VER);
                }
                break;
            case VER_HARDWARE:
                hardVer.set(version);
                if (mHcService != null) {
                    mHcService.dataQuery(HcService.DATA_QUERY_FIRMWARE_VER);
                }
                break;
            case VER_FIRMWARE:
                firmVer.set(version);
                if (mHcService != null) {
                    mHcService.dataQuery(HcService.DATA_QUERY_CONFIRM_ECG_MODULE_EXIST);
                }
                break;
        }
    }

    public void getMemberId(MachineBindMemberBean machineBindMemberBean) {
        this.mMemberId = machineBindMemberBean.getMember_Id();
        onDataListener.onChageMember(machineBindMemberBean);
        HealthMonitorActivity.mMemberId = mMemberId;
    }

    public void setIsBinded(boolean isbind){
        this.isBinded = isbind;
    }

}