package com.datalife.datalife.activity;

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
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.BlueToothAdapter;
import com.datalife.datalife.adapter.MachineAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.bean.BlueType;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.bean.SimpleBackPage;
import com.datalife.datalife.contract.EquipmentContract;
import com.datalife.datalife.dao.MachineBean;
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
import com.datalife.datalife.widget.CustomTitleBar;
import com.linktop.constant.BluetoothState;
import com.linktop.constant.DeviceInfo;
import com.linktop.constant.UUIDConfig;
import com.linktop.constant.WareType;
import com.linktop.infs.OnSendCodeToDevCallback;
import com.linktop.whealthService.BleDevManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import aicare.net.cn.iweightlibrary.utils.AicareBleConfig;
import aicare.net.cn.iweightlibrary.utils.L;
import aicare.net.cn.iweightlibrary.utils.ParseData;
import aicare.net.cn.toothbrushlibrary.bleprofile.*;
import aicare.net.cn.toothbrushlibrary.entity.BrushDevice;
import aicare.net.cn.toothbrushlibrary.scandecoder.ScanRecord;
import butterknife.BindView;
import butterknife.OnClick;

import static com.datalife.datalife.util.IDatalifeConstant.REQUEST_OPEN_BT;

/**
 * Created by LG on 2018/8/14.
 */

public class BlueToothDevActivity extends BaseActivity implements EquipmentContract.EquipmentView, AdapterView.OnItemClickListener {

    @BindView(R.id.lv_bluetooth)
    ListView mBluetoothLv;
    @BindView(R.id.lv_searched_bluetooth)
    ListView mSearchLv;
    @BindView(R.id.tv_connect_equip)
    TextView mEquipNameTv;
    @BindView(R.id.rl_binding_equit)
    RelativeLayout mBindingEquitLayout;
    @BindView(R.id.custom_title)
    CustomTitleBar customTitleBar;


    ArrayList<MachineBean> machineBeans = new ArrayList<>();
    EquipmentPresenter equipmentPresenter = new EquipmentPresenter(this);
    private boolean isSupportBLE;
    private BluetoothAdapter mAdapter;
    public HashMap<String,BluetoothDevice> bls = new HashMap<>();
    public HashMap<String,Integer> bint = new HashMap<>();

    private String TAG = "EquipmanagerFragment";
    private String sn = "";

    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<>();
    private ArrayList<String> arraynames = new ArrayList<>();
    private ArrayList<BlueType> blueTypes = new ArrayList<>();
    private boolean aBoolean = false;
    private BlueToothAdapter blueToothAdapter = null;
    private String devicename;
    private String mCurrentName;

    ScanCallback scanCallback = null;
    BluetoothAdapter.LeScanCallback leScanCallback = null;
    private MachineAdapter machineAdapter = null;
    private BluetoothDevice bluetoothDevice = null;
    private int mRequestCode = 11212;

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 111:
//                mSearchLv.setAdapter();
                    break;
                case IDatalifeConstant.MESSAGESERVICE:
                    if (!aBoolean){
                        int typeId = msg.getData().getInt("type");
                        blueToothAdapter = new BlueToothAdapter(BlueToothDevActivity.this,mDeviceList,myHandler,blueTypes);
                        mSearchLv.setAdapter(blueToothAdapter);
                        mSearchLv.setVisibility(View.VISIBLE);
                        aBoolean = !aBoolean;
                    }else{
                        blueToothAdapter.setMachineList(mDeviceList,blueTypes);
//                        blueToothAdapter.notifyDataSetChanged();
//                        aBoolean = !aBoolean;
                    }
                    break;
                case IDatalifeConstant.MESSAGEMACHINE:
                    final int position = msg.getData().getInt("position");

                    devicename = arraynames.get(position);
                    mCurrentName = arraynames.get(position);
                    int type = blueTypes.get(position).getBlueType();
                    if (type == DataLifeUtil.TYPE_HEALTH){
                        sn = "C4" + arraynames.get(position).substring(5);
                    }else if(type == DataLifeUtil.TYPE_FAT){
                        sn = mDeviceList.get(position).getAddress();
                        mCurrentName = DataLifeUtil.SWAN +"-"+ sn;
                    }else if(type == DataLifeUtil.TYPE_TOOTH){
                        sn = mDeviceList.get(position).getAddress();
                        mCurrentName = DataLifeUtil.ZSONIC +"-"+ sn;
                    }


                    if (type == DataLifeUtil.TYPE_TOOTH){
                        Bundle bundle = new Bundle();
                        bundle.putString("sn",sn);
                        bundle.putString("MachineName",mCurrentName);
                        UIHelper.launcherForResultBundle(BlueToothDevActivity.this,ToothBindActivity.class,mRequestCode,bundle);
                        if (mAdapter != null) {
                            mAdapter.stopLeScan(leScanCallback);
                        }
                        return;
                    }

                    new AlertDialog.Builder(BlueToothDevActivity.this).setMessage("确定是否绑定机器?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            equipmentPresenter.sendDeviceInfo(mCurrentName,sn, ProApplication.SESSIONID);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId()  {
        return R.layout.activity_bluetooth;
    }

    public void changeState(String str){
//        mEquipNameTv.setText(str);
//        mBindingEquitLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initEventAndData() {

        equipmentPresenter.onCreate();
        equipmentPresenter.attachView(this);

        machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(this).queryMachineBeanList();
        if (machineBeans != null && machineBeans.size() > 0){
            machineAdapter = new MachineAdapter(this,machineBeans);
            mBluetoothLv.setAdapter(machineAdapter);
            mBluetoothLv.setVisibility(View.VISIBLE);
            int totalHeight = 0;
            if (machineBeans.size() >= 3){
                for(int i=0;i<3;i++) {
                    View viewItem = machineAdapter.getView(i, null, mBluetoothLv);
                    viewItem.measure(0, 0);
                    totalHeight += viewItem.getMeasuredHeight();
                }
                    ViewGroup.LayoutParams layoutParams = mBluetoothLv.getLayoutParams();
                    layoutParams.height = totalHeight + 60;
                    mBluetoothLv.setLayoutParams(layoutParams);
            }
        }

        mDeviceList.clear();

        mBluetoothLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("machine", machineBeans.get(position));
                UIHelper.showSimpleBackBundleForResult(BlueToothDevActivity.this, SimplebackActivity.RESULT_BINDMEMBER, SimpleBackPage.BINDMEMBER,bundle);
            }
        });
        mBluetoothLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                toast("断开连接");
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
        isSupportBLE = getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        if (!isSupportBLE) {
            UToast.show(this.getBaseContext(), "Mobile phone not support BLE!");
        } else {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mAdapter = bluetoothManager.getAdapter();

            if(mAdapter.isEnabled()){
                quicklyConnect();
            }else {
                onOpenBLE();
            }

        }
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
                                handmessage(name,device,DataLifeUtil.TYPE_HEALTH);
                            }else {
                                int flag = AicareBleConfig.isAicareDevice(scanRecord);
                                if (flag != Integer.MIN_VALUE) {
                                    L.e(TAG, "name: " + device.getAddress() + "; scanRecord: " + ParseData.byteArr2Str(scanRecord));
                                    handmessage(device.getAddress(),device,DataLifeUtil.TYPE_FAT);
                                }

                                if(device != null && !TextUtils.isEmpty(device.getAddress())) {
                                    ScanRecord scanResult = ScanRecord.parseFromBytes(scanRecord);
                                    if(scanResult != null) {
                                        aicare.net.cn.toothbrushlibrary.utils.L.e("------------------------1----------------------");
                                        byte[] manufacturerData = scanResult.getManufacturerSpecificData();
                                        if(!isArrEmpty(manufacturerData)) {
                                            aicare.net.cn.toothbrushlibrary.utils.L.e("------------------------2----------------------");
                                            List<ParcelUuid> uuidList = scanResult.getServiceUuids();
                                            if(!isListEmpty(uuidList)) {
                                                aicare.net.cn.toothbrushlibrary.utils.L.e("------------------------3----------------------");
                                                if(uuidList.contains(ParcelUuid.fromString("0000f9b0-0000-1000-8000-00805f9b34fb")) && uuidList.contains(ParcelUuid.fromString("0000fef5-0000-1000-8000-00805f9b34fb")) && manufacturerData[0] == -84 && manufacturerData[1] == 14) {
                                                    aicare.net.cn.toothbrushlibrary.utils.L.e("manufacturerData = " + aicare.net.cn.toothbrushlibrary.utils.ParseData.arr2Str(manufacturerData));
                                                    BrushDevice brushDevice = new BrushDevice();
                                                    brushDevice.setAddress(device.getAddress());
                                                    brushDevice.setName(device.getName());
                                                    brushDevice.setRssi(rssi);

                                                    handmessage(device.getAddress(),device,DataLifeUtil.TYPE_TOOTH);

                                                }
                                            }
                                        }
                                    }
                                }
                            }



                        }
                    };
                    mAdapter.startLeScan(leScanCallback);
                }
            } else {
                UToast.show(this.getBaseContext(), "Please enable the mobile phone's bluetooth!");
            }
    }

    public void handmessage(String name,BluetoothDevice device,int type){
        if (!bls.containsKey(name)){
            bls.put(name,device);
            bint.put(name,type);
            arraynames.add(name);
            mDeviceList.add(device);
            BlueType blueType = new BlueType(name,type,device);
            blueTypes.add(blueType);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("name" , name);
            bundle.putInt("type",type);
            bundle.putParcelable("device",device);
            message.setData(bundle);
            message.what = IDatalifeConstant.MESSAGESERVICE;
            myHandler.sendMessage(message);
        }
    }

    public void onOpenBLE() {
        startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), REQUEST_OPEN_BT);
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

    @OnClick({R.id.btn_bluetooth_search,R.id.tv_bind_member,R.id.iv_head_left})
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
                UIHelper.showSimpleBackBundleForResult(this, SimplebackActivity.RESULT_BINDMEMBER, SimpleBackPage.BINDMEMBER,bundle);
                break;

            case R.id.iv_head_left:

                setResult(Activity.RESULT_OK);
                finish();

                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            setResult(Activity.RESULT_OK);
            finish();
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        for (int i = 0;i<machineBeans.size();i++){
        }
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
        DBManager.getInstance(this).deleteAllMachineBean();
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
                DBManager.getInstance(this).insertMachine(machineBean);
            }catch (SQLiteConstraintException e){
                toast(e.getMessage());
                Log.e("error:" , e.getMessage());
            }
        }

        machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(this).queryMachineBeanList();
        if (machineAdapter == null){
            machineAdapter = new MachineAdapter(this,machineBeans);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == mRequestCode){
                equipmentPresenter.getDevMachineInfo("1","30",ProApplication.SESSIONID);
            }
        }
    }

    @Override
    public void onEquipFail(String str) {
        Log.e("LG","str:" + str);
        if (str.contains("查无数据")){
            DBManager.getInstance(this).deleteAllMachineBean();
        }
    }

    @Override
    public void sendSuccess() {
        equipmentPresenter.getDevMachineInfo("1","15",ProApplication.SESSIONID);

        blueToothAdapter.onChangeText(mCurrentName);

//        blueToothAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendFail(String str) {
        toast(str + "");
    }


    private boolean isArrEmpty(byte[] b) {
        return b == null || b.length == 0;
    }

    private boolean isListEmpty(List<ParcelUuid> list) {
        return list == null || list.size() == 0;
    }

}
