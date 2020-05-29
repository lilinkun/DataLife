package com.datalife.datalife.interf;

import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.dao.MachineBindMemberBean;

import java.util.ArrayList;

/**
 * Created by LG on 2018/2/1.
 */

public interface OnDataListener {

    public void onBack();

    public void onPage(int page);

    public void onMember(ArrayList<MachineBindMemberBean> machineBindMemberBean,String machinebindid);

    public void onMachine(MachineBean machineBean);

    public void onChageMember(MachineBindMemberBean machineBindMemberBean);

    public void onBattery(int batteryValue);
}
