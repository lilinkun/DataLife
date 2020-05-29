package com.datalife.datalife.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.SimplebackActivity;
import com.datalife.datalife.adapter.BlueToothAdapter;
import com.datalife.datalife.adapter.MachineAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseHealthFragment;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.bean.SimpleBackPage;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.contract.EquipmentContract;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.presenter.EquipmentPresenter;
import com.datalife.datalife.service.HcService;
import com.datalife.datalife.util.AlertDialogBuilder;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.PermissionManager;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.util.UToast;
import com.linktop.constant.BluetoothState;
import com.linktop.constant.DeviceInfo;
import com.linktop.constant.UUIDConfig;
import com.linktop.constant.WareType;
import com.linktop.infs.OnBleConnectListener;
import com.linktop.infs.OnDeviceInfoListener;
import com.linktop.infs.OnSendCodeToDevCallback;
import com.linktop.whealthService.BleDevManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import aicare.net.cn.iweightlibrary.utils.AicareBleConfig;
import aicare.net.cn.iweightlibrary.utils.L;
import aicare.net.cn.iweightlibrary.utils.ParseData;
import butterknife.BindView;
import butterknife.OnClick;

import static com.datalife.datalife.service.HcService.DATA_QUERY_BATTERY_INFO;
import static com.datalife.datalife.service.HcService.DATA_QUERY_CONFIRM_BG_MODULE_EXIST;
import static com.datalife.datalife.service.HcService.DATA_QUERY_CONFIRM_ECG_MODULE_EXIST;
import static com.datalife.datalife.service.HcService.DATA_QUERY_FIRMWARE_VER;
import static com.datalife.datalife.service.HcService.DATA_QUERY_HARDWARE_VER;
import static com.datalife.datalife.service.HcService.DATA_QUERY_ID_AND_KEY;
import static com.datalife.datalife.service.HcService.DATA_QUERY_SOFTWARE_VER;
import static com.datalife.datalife.util.IDatalifeConstant.REQUEST_OPEN_BT;

/**
 * Created by LG on 2018/1/17.
 */

public class EquipmanagerFragment extends BaseHealthFragment implements OnBleConnectListener,OnDeviceInfoListener,EquipmentContract.EquipmentView, AdapterView.OnItemClickListener{


    @BindView(R.id.lv_bluetooth)
    ListView mBluetoothLv;
    @BindView(R.id.lv_searched_bluetooth)
    ListView mSearchLv;
    @BindView(R.id.tv_connect_equip)
    TextView mEquipNameTv;
    @BindView(R.id.rl_binding_equit)
    RelativeLayout mBindingEquitLayout;

    ArrayList<MachineBean> machineBeans = new ArrayList<>();
    EquipmentPresenter equipmentPresenter = new EquipmentPresenter(getActivity());
    private boolean isSupportBLE;
    private BluetoothAdapter mAdapter;
    private BluetoothGatt mGatt;
    private BluetoothGattCharacteristic mHRMEnabledChara;
    private BluetoothGattCharacteristic mHRMChara;
    private Timer mBatteryQueryTimer;
    private BleDevManager mBleDevManager;
    private int mState;
    public HashMap<String,BluetoothDevice> bls = new HashMap<>();

    private String TAG = "EquipmanagerFragment";

    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<>();
    private ArrayList<String> arraynames = new ArrayList<>();
    private boolean aBoolean = false;
    private BlueToothAdapter blueToothAdapter = null;
    private String devicename;
    private String mCurrentName;

    ScanCallback scanCallback = null;
    BluetoothAdapter.LeScanCallback leScanCallback = null;
    private MachineAdapter machineAdapter = null;
    private BluetoothDevice bluetoothDevice = null;

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        switch (msg.what){
            case 111:
//                mSearchLv.setAdapter();
                break;
            /*case IDatalifeConstant.MESSAGESERVICE:
                if (!aBoolean){

//                    blueToothAdapter = new BlueToothAdapter(getActivity(),mDeviceList,myHandler);
                    mSearchLv.setAdapter(blueToothAdapter);
                    mSearchLv.setVisibility(View.VISIBLE);
                    aBoolean = !aBoolean;
                }else{
                    blueToothAdapter.notifyDataSetChanged();
                    aBoolean = !aBoolean;
                }
                break;*/
            case IDatalifeConstant.MESSAGEMACHINE:
                final int position = msg.getData().getInt("position");
                new AlertDialog.Builder(getActivity()).setMessage("确定是否绑定机器?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        devicename = arraynames.get(position);
                        String sn = "";
                        mCurrentName = arraynames.get(position);
                        if (devicename.startsWith("HC02")){
                            sn = "C4" + arraynames.get(position).substring(5);
                        }else{
                            sn = mDeviceList.get(position).getAddress();
                            mCurrentName = DataLifeUtil.SWAN +"-"+ sn;
                        }
                        equipmentPresenter.sendDeviceInfo(mCurrentName,sn, ProApplication.SESSIONID);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
            case HcService.BLE_STATE:

                clickConnect();

                break;
        }
        }
    };

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
                    new AlertDialogBuilder(getActivity())
                            .setTitle("提示")
                            .setMessage("Android 6.0及以上系统需要打开GPS才能扫描蓝牙设备。")
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton("打开GPS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PermissionManager.openGPS(getActivity());
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
        }
    }

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_equipment;
    }

    public void changeState(String str){
//        mEquipNameTv.setText(str);
//        mBindingEquitLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initEventAndData() {

        equipmentPresenter.onCreate();
        equipmentPresenter.attachView(this);

        machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(getActivity()).queryMachineBeanList();
        if (machineBeans != null && machineBeans.size() > 0){
            machineAdapter = new MachineAdapter(getActivity(),machineBeans);
            mBluetoothLv.setAdapter(machineAdapter);
            mBluetoothLv.setVisibility(View.VISIBLE);
        }

        mDeviceList.clear();

        mBluetoothLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("machine", machineBeans.get(position));
                UIHelper.showSimpleBackBundleForResult(getActivity(), SimplebackActivity.RESULT_BINDMEMBER, SimpleBackPage.BINDMEMBER,bundle);
            }
        });
        mBluetoothLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                toast("断开连接");
                mHcService.disConnect();
                return true;
            }
        });

        mSearchLv.setOnItemClickListener(this);
        initBluetooth();

        equipmentPresenter.getDevMachineInfo("1","15",ProApplication.SESSIONID);
    }

    public void initBluetooth() {
        arraynames.clear();

        if (mAdapter != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                mAdapter.getBluetoothLeScanner().stopScan(scanCallback);
//            } else {
//                mAdapter.stopLeScan(leScanCallback);
//            }
            mAdapter.stopLeScan(leScanCallback);
        }

        if (blueToothAdapter != null){
            blueToothAdapter.notifyDataSetChanged();
            bls.clear();
        }
        isSupportBLE = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        if (!isSupportBLE) {
            UToast.show(getActivity().getBaseContext(), "Mobile phone not support BLE!");
        } else {
            BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
            mAdapter = bluetoothManager.getAdapter();

            if (mHcService.isConnected){
                return;
            }

            if (mAdapter.isEnabled()) {
                setBleState(BluetoothState.BLE_OPENED_AND_DISCONNECT);
            } else {
                setBleState(BluetoothState.BLE_CLOSED);
            }
        }
    }

    private void getDevWareVersion(final WareType wareType) {
        if (mGatt == null)
            return;
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BluetoothGattService deviceInfoService = mGatt.getService(UUID.fromString(UUIDConfig.DEV_INFO_SER_UUID));
        if (deviceInfoService == null) {
            Log.e(TAG, "deviceInfoService service not found!");
            return;
        }
        String uuidStr = "";
        switch (wareType) {
            case VER_FIRMWARE:
                uuidStr = UUIDConfig.DEV_INFO_FIRMWARE_REV_UUID;
                break;
            case VER_HARDWARE:
                uuidStr = UUIDConfig.DEV_INFO_HARDWARE_PCB_UUID;
                break;
            case VER_SOFTWARE:
                uuidStr = UUIDConfig.DEV_INFO_SOFTWARE_REV_UUID;
                break;
        }
        final UUID uuid = UUID.fromString(uuidStr);
        BluetoothGattCharacteristic wareCharacteristic = deviceInfoService.getCharacteristic(uuid);
        if (wareCharacteristic == null) {

            return;
        }
        mGatt.readCharacteristic(wareCharacteristic);
    }

    private void setBleState(int state) {
//        if (mState != state) {
            mState = state;
            if (state == BluetoothState.BLE_NOTIFICATION_ENABLED) {
                dataQuery(DATA_QUERY_SOFTWARE_VER);
            }else if(state == BluetoothState.BLE_OPENED_AND_DISCONNECT){
                final int bluetoothEnable = isBluetoothEnable();
                if (bluetoothEnable == -1) {
                    onBLENoSupported();
                } else if (bluetoothEnable == 0) {
                    onOpenBLE();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            quicklyConnect();
                        }
                    }).start();

                }
            }
//        }
    }

    public void quicklyConnect() {
        if (mAdapter != null) {
            if (mAdapter.isEnabled()) {
                //Normally,the bluetooth name of Health monitor is start with "HC02" or other custom prefix.
                //You can use the prefix name to filter all the scanned bluetooth devices to reduce the size of the scanning list.
                    leScanCallback = new BluetoothAdapter.LeScanCallback() {
                        @Override
                        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                            final String name = device.getName();
                            Log.e("onLeScan", "dev name:" + name);
                            if (!TextUtils.isEmpty(name) && name.startsWith("HC02")) {
                                Log.e("onLeScan", "dev name:" + name);
                                //  mAdapter.stopLeScan(this);
                                //  connect(device,name);
                                handmessage(name,device);
                            }else {
                                int flag = AicareBleConfig.isAicareDevice(scanRecord);
                                if (flag != Integer.MIN_VALUE) {
                                    L.e(TAG, "name: " + device.getAddress() + "; scanRecord: " + ParseData.byteArr2Str(scanRecord));
                                    handmessage(device.getAddress(),device);
                                }
                            }
                        }
                    };
                    mAdapter.startLeScan(leScanCallback);
            } else {
                UToast.show(getActivity().getBaseContext(), "Please enable the mobile phone's bluetooth!");
            }
        }
    }

    public void handmessage(String name,BluetoothDevice device){
        if (!bls.containsKey(name)){
            bls.put(name,device);
            arraynames.add(name);
            mDeviceList.add(device);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("name" , name);
            bundle.putParcelable("device",device);
            message.setData(bundle);
            message.what = IDatalifeConstant.MESSAGESERVICE;
            myHandler.sendMessage(message);
        }
    }

    @Override
    public void onBLENoSupported() {
//        Toast.makeText(getContext(), "蓝牙不支持", Toast.LENGTH_SHORT).show();
        Log.e("DATALIFE","蓝牙不支持");
    }


    @Override
    public void onOpenBLE() {
        startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), REQUEST_OPEN_BT);
    }

    @Override
    public void onBleState(int i) {

    }

    @Override
    public void onUpdateDialogBleList() {

    }

    public int isBluetoothEnable() {
        if (mAdapter == null || !isSupportBLE) {
            return -1;
        } else {
            return mAdapter.isEnabled() ? 1 : 0;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disConnect();
        equipmentPresenter.onStop();

        if (mAdapter != null) {
            if (1 < 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAdapter.getBluetoothLeScanner().stopScan(scanCallback);
            }
            }else {
                mAdapter.stopLeScan(leScanCallback);
            }
        }
    }

    public void disConnect() {
        if (mGatt != null) {
            mGatt.disconnect();
        }
    }

    @Override
    protected void otherevent() {
    }

    @OnClick({R.id.btn_bluetooth_search,R.id.tv_bind_member})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_bluetooth_search:

                initBluetooth();
                break;

            case R.id.tv_bind_member:
                Bundle bundle = new Bundle();
                for (int i = 0;i<machineBeans.size();i++) {
                    String str = mEquipNameTv.getText().toString();
                    if (machineBeans.get(i).getMachineName().equals(str)){
                        bundle.putSerializable("machine", machineBeans.get(i));
                    }
                }
                UIHelper.showSimpleBackBundleForResult(getActivity(), SimplebackActivity.RESULT_BINDMEMBER, SimpleBackPage.BINDMEMBER,bundle);
                break;

        }
    }

    public void dataQuery(int step) {
        switch (step) {
            case DATA_QUERY_SOFTWARE_VER:
                getDevWareVersion(WareType.VER_SOFTWARE);
                break;
            case DATA_QUERY_HARDWARE_VER:
                getDevWareVersion(WareType.VER_HARDWARE);
                break;
            case DATA_QUERY_FIRMWARE_VER:
                getDevWareVersion(WareType.VER_FIRMWARE);
                break;
            case DATA_QUERY_CONFIRM_ECG_MODULE_EXIST:
                //TODO To confirm the ECG module of the device is exist.
                mBleDevManager.getEcgTask().checkModuleExist(new OnSendCodeToDevCallback() {
                    @Override
                    public void onReceived() {
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dataQuery(DATA_QUERY_CONFIRM_BG_MODULE_EXIST);
                    }
                });
                break;
            case DATA_QUERY_CONFIRM_BG_MODULE_EXIST:
                //TODO To confirm the BG module of the device is exist.
                mBleDevManager.getBsTask().checkModuleExist(new OnSendCodeToDevCallback() {
                    @Override
                    public void onReceived() {
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dataQuery(DATA_QUERY_ID_AND_KEY);
                    }
                });
                break;
            case DATA_QUERY_ID_AND_KEY:
                //TODO To read device id and key.
                mBleDevManager.getDeviceTask().getDeviceInfo();
                break;
            case DATA_QUERY_BATTERY_INFO:
                readDevBatteryInfo();
                break;
        }
    }

    private void readDevBatteryInfo() {
        mBatteryQueryTimer = new Timer();
        mBatteryQueryTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mBleDevManager.getBatteryTask().batteryQuery();
            }
        }, 100L, 300 * 1000L);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            if (1<0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mAdapter.getBluetoothLeScanner().stopScan(scanCallback);
                }
            }else {
                mAdapter.stopLeScan(leScanCallback);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        for (int i = 0;i<machineBeans.size();i++){
            if(machineBeans.get(i).getMachineName().equals(arraynames.get(position))){
                if (mHcService != null && !bls.isEmpty()){
                    mHcService.connect(bls.get(machineBeans.get(i).getMachineName()),machineBeans.get(i).getMachineName());
                }
            }
        }


    }

    @Override
    public void onDeviceInfo(DeviceInfo deviceInfo) {

    }

    @Override
    public void onReadDeviceInfoFailed() {

    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void onEquipSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultMachines) {
        ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> machineBindBean = resultMachines;
        DBManager.getInstance(getActivity()).deleteAllMachineBean();
        for (int i = 0;i<machineBindBean.size();i++) {
            try{
                MachineBean machineBean = new MachineBean();
                machineBean.setCreateDate(machineBindBean.get(i).getCreateDate());
                machineBean.setMachineBindId(machineBindBean.get(i).getMachineBindId());
                machineBean.setMachineId(machineBindBean.get(i).getMachineId());
                machineBean.setMachineName(machineBindBean.get(i).getMachineName());
                machineBean.setMachineSn(machineBindBean.get(i).getMachineSn());
                machineBean.setMachineStatus(machineBindBean.get(i).getMachineStatus());
                machineBean.setUser_id(machineBindBean.get(i).getUser_id());
                machineBean.setUser_name(machineBindBean.get(i).getUser_name());
                DBManager.getInstance(getActivity()).insertMachine(machineBean);
            }catch (SQLiteConstraintException e){
                toast(e.getMessage());
                Log.e("error:" , e.getMessage());
            }
        }

        machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(getActivity()).queryMachineBeanList();
        if (machineAdapter == null){
            machineAdapter = new MachineAdapter(getActivity(),machineBeans);
            mBluetoothLv.setAdapter(machineAdapter);
            mBluetoothLv.setVisibility(View.VISIBLE);
        }else {
            machineAdapter.setMachineBean(machineBeans);
            machineAdapter.notifyDataSetChanged();
        }
        for(int i = 0;i<machineBeans.size();i++) {
            if (machineBeans.get(i).getMachineName().equals(mCurrentName)) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("machine", machineBeans.get(i));
                UIHelper.showSimpleBackBundleForResult(this, SimplebackActivity.RESULT_BINDMEMBER, SimpleBackPage.BINDMEMBER, bundle);
            }
                }
//        for (int i = 0;i<machineBeans.size();i++) {
//            try{
//                DBManager.getInstance(getActivity()).insertMachine(machineBeans.get(i));
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("machine",machineBeans.get(i));
//                UIHelper.showSimpleBackBundleForResult(this, SimplebackActivity.RESULT_BINDMEMBER, SimpleBackPage.BINDMEMBER,bundle);
//            }catch (SQLiteConstraintException e){
//                toast(e.getMessage());
//                Log.e("error:" , e.getMessage());
//            }
//
//        }
    }

    @Override
    public void onEquipFail(String str) {
        Log.e("LG","str:" + str);
        if (str.contains("查无数据")){
            DBManager.getInstance(getActivity()).deleteAllMachineBean();
        }
    }

    @Override
    public void sendSuccess() {
        equipmentPresenter.getDevMachineInfo("1","15",ProApplication.SESSIONID);
//        mHcService.connect(bls.get(devicename),devicename);

        blueToothAdapter.onChangeText(mCurrentName);

//        blueToothAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendFail(String str) {
        toast(str + "");
    }


    @Override
    public String getTitle() {
        return "设备管理";
    }

    @Override
    public void reset() {
    }

}