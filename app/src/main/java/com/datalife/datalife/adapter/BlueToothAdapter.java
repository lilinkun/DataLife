package com.datalife.datalife.adapter;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.activity.SimplebackActivity;
import com.datalife.datalife.bean.BlueType;
import com.datalife.datalife.bean.Bluetoothbean;
import com.datalife.datalife.bean.SimpleBackPage;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.util.UToast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/1/28.
 */
public class BlueToothAdapter extends BaseAdapter {

    private Context context;
    private List<BluetoothDevice> bluetoothbeans;
    private Handler handler;
    private ArrayList<TextView> textViews = new ArrayList<>();
    private int currentPosition = 0;
    private ArrayList<MachineBean> machineBeans = null;
    private ArrayList<String> machineList = null;
    private ArrayList<BlueType> blueTypes = null;

    public BlueToothAdapter(Context context , ArrayList<BluetoothDevice> bluetoothbeans, Handler myHandler, ArrayList<BlueType> blueTypes){
        this.context = context;
        this.bluetoothbeans = bluetoothbeans;
        this.handler = myHandler;
        this.blueTypes = blueTypes;
        init();
    }

    public void setMachineList(ArrayList<BluetoothDevice> bluetoothbeans, ArrayList<BlueType> blueTypes){
        this.bluetoothbeans = bluetoothbeans;
        this.blueTypes = blueTypes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bluetoothbeans.size();
    }

    @Override
    public Object getItem(int position) {
        return bluetoothbeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item,parent, false);

            viewHolder = new ViewHolder();

            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_bluename);
            viewHolder.tv_bind = (TextView) convertView.findViewById(R.id.tv_bind);
            viewHolder.tv_bind_device = (TextView) convertView.findViewById(R.id.tv_dev_typename);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /*if (textViews != null) {
            if (!textViews.contains(viewHolder.tv_bind)) {
                textViews.add(viewHolder.tv_bind);
                UToast.show(context,"size:" + textViews.size());
            }
        }*/

        String toothblueStr = "";
        if (blueTypes.get(position).getBlueType() == DataLifeUtil.TYPE_HEALTH) {
            viewHolder.tv_name.setText(bluetoothbeans.get(position).getName());
//            viewHolder.tv_name.setText(context.getResources().getString(R.string.health_test) +" - " +bluetoothbeans.get(position).getName());
            viewHolder.tv_bind_device.setText(R.string.datalife_health_test);
            toothblueStr = bluetoothbeans.get(position).getName();
        }else if (blueTypes.get(position).getBlueType() == DataLifeUtil.TYPE_FAT){
            viewHolder.tv_name.setText(DataLifeUtil.SWAN + "-" + bluetoothbeans.get(position).getAddress());
//            viewHolder.tv_name.setText(context.getResources().getString(R.string.body_fat) +" - " + bluetoothbeans.get(position).getAddress());
            viewHolder.tv_bind_device.setText(R.string.datalife_body_fat);
            toothblueStr = DataLifeUtil.SWAN + "-" + bluetoothbeans.get(position).getAddress();
//            viewHolder.textView.setText(mContext.getResources().getString(R.string.body_fat) + machineBeans.get(position).getMachineSn());
        }else if (blueTypes.get(position).getBlueType() == DataLifeUtil.TYPE_TOOTH){
            viewHolder.tv_name.setText(DataLifeUtil.ZSONIC + "-" + bluetoothbeans.get(position).getAddress());
//            viewHolder.tv_name.setText(context.getResources().getString(R.string.body_fat) +" - " + bluetoothbeans.get(position).getAddress());
            viewHolder.tv_bind_device.setText(R.string.datalife_blue_tooth);
            toothblueStr = DataLifeUtil.ZSONIC + "-" + bluetoothbeans.get(position).getAddress();
        }

        if(machineList.contains(toothblueStr)){
            viewHolder.tv_bind.setTextColor(context.getResources().getColor(R.color.bg_toolbar_title));
            viewHolder.tv_bind.setText(R.string.add_member);
            viewHolder.tv_bind.setVisibility(View.GONE);
        }else{
            viewHolder.tv_bind.setTextColor(context.getResources().getColor(R.color.bg_toolbar_title));
            viewHolder.tv_bind.setText(R.string.bind_equit);
            viewHolder.tv_bind.setVisibility(View.VISIBLE);
        }

        viewHolder.tv_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (textViews.get(position).getText().toString().equals(context.getString(R.string.add_member))){

                if (((TextView)v).getText().toString().equals(context.getString(R.string.add_member))){
                    machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(context).queryMachineBeanList();
                    init();
                    Bundle bundle = new Bundle();
                    for (int i = 0;i<machineBeans.size();i++) {
                        if (machineBeans.get(i).getMachineSn().equals(bluetoothbeans.get(position).getAddress())){
                            bundle.putSerializable("machine", machineBeans.get(i));
                        }
                    }
                    Activity activity = (Activity) context;
                    UIHelper.showSimpleBackBundleForResult(activity, SimplebackActivity.RESULT_BINDMEMBER, SimpleBackPage.BINDMEMBER,bundle);
                }else if (((TextView)v).getText().toString().equals(context.getString(R.string.bind_equit))){
                    List<FamilyUserInfo> familyUserInfo = DBManager.getInstance(context).queryFamilyUserInfoList();
                    if (familyUserInfo.size() > 0) {
                        Message message = new Message();
                        message.what = IDatalifeConstant.MESSAGEMACHINE;
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        message.setData(bundle);
                        handler.sendMessage(message);
                        currentPosition = position;
                    }else {
                        UToast.show(context,"请先添加成员");
                    }
                }
            }
        });

        return convertView;
    }

    public void onChangeText(String str){
//        textViews.get(currentPosition).setText(R.string.add_member);
        machineList.add(str);
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tv_name;
        TextView tv_bind;
        TextView tv_bind_device;
    }

    public void init(){
        machineList= new ArrayList<>();
        machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(context).queryMachineBeanList();
        for (int i = 0;i<machineBeans.size();i++){
            machineList.add(machineBeans.get(i).getMachineName());
        }
    }
}
