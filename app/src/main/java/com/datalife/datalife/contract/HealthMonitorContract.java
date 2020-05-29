package com.datalife.datalife.contract;

import com.datalife.datalife.bean.LastMeasureDataBean;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/2/8.
 */

public interface HealthMonitorContract {

    public interface HealthMonitorView extends IView{
        public void sendSuccess();
        public void sendFail(String str);
        public void getBindListSuccess(List<MachineBindMemberBean> machineBindMemberBeans);
        public void getBindListFail(String str);
        public void getSuccess(LastMeasureDataBean<MeasureRecordBean> bean);
        public void getFail(String str);
        public void onSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews);
        public void onfail(String str);
    }

    public interface HealthMonitorPresenter{

    }

}
