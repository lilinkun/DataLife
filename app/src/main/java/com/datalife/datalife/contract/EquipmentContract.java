package com.datalife.datalife.contract;

import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.ResultFamily;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/3/5.
 */

public interface EquipmentContract {

    public interface EquipmentView extends IView {
        public void onEquipSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews);
        public void onEquipFail(String str);
        public void sendSuccess();
        public void sendFail(String str);
    }

    public interface EquipmentPresenter{

    }
}