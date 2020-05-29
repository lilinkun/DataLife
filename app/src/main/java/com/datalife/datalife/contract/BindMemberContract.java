package com.datalife.datalife.contract;

import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.mvp.IView;

import java.util.List;

/**
 * Created by LG on 2018/3/6.
 */

public interface BindMemberContract {

    public interface BindMemberView extends IView{
        public void onSuccess(List<MachineBindMemberBean> machineBindMemberBean);
        public void onFail(String str);
        public void onFail();
        public void onSuccess();
    }

    public interface BindMemberPresenter {

    }

}
