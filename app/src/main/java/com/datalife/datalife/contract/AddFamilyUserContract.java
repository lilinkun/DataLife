package com.datalife.datalife.contract;

import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/2/4.
 */

public interface AddFamilyUserContract {

    public interface AddFamilyView extends IView{
        public void onsuccess();
        public void onfail(String failMsg);
        public void onBackFamilyListDataSuccess(ArrayList<FamilyUserInfo> familyUserInfos);
        public void onBackFamilyListDataFail(String str);
        public void onSuccess(List<MachineBindMemberBean> machineBindMemberBeans);
        public void onFail(String failMsg);
    }

    public interface Presenter{

    }
}
