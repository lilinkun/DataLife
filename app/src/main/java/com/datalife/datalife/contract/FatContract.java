package com.datalife.datalife.contract;

import com.datalife.datalife.bean.LastFatMeasureDataBean;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/5/25.
 */

public interface FatContract {


    public interface FatView extends IView{
        public void onSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews);
        public void onfail(String str);
        public void onSuccess();
        public void onFail();
        public void onSuccess(LastFatMeasureDataBean<MeasureRecordBean> measureRecordBean);
    }

    public interface FatPresenter{

    }


}
