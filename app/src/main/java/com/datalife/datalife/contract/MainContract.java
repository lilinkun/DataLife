package com.datalife.datalife.contract;

import com.datalife.datalife.bean.DownloadBean;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/1/15.
 */

public interface MainContract {
    public interface MainView extends IView{
        public void onBackFamilyListDataSuccess(ArrayList<FamilyUserInfo> listResultBean);
        public void onBackFamilyListDataFail(String str);
        public void onSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews);
        public void onfail(String str);
        public void updateSuccess(DownloadBean downloadBean);
        public void updateFail(String str);
    }

    public interface MainPresenter{
    }
}
