package com.datalife.datalife.contract;

import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.dao.FlashListBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.dao.NewsInfo;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/3/2.
 */

public interface HomePageContract {

    public interface HomePageView extends IView {
        public void onSuccess(ArrayList<NewsInfo> resultNews);
        public void onfail(String str);
        public void onFlashSuccess(ArrayList<FlashListBean> flashListBeans);
        public void onFlashFail(String str);
        public void onEquipSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews);
        public void onEquipFail(String msg);
    }
}
