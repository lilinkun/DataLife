package com.datalife.datalife.contract;

import android.content.Context;

import com.datalife.datalife.bean.DownloadBean;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.mvp.IView;

/**
 * Created by LG on 2018/1/31.
 */

public interface SettingContract {

    public interface SettingView extends IView {
        void loginoutSuccess();
        void loginoutFail(String failMsg);
        void updateSuccess(DownloadBean downloadBean);
        void updateFail(String failMsg);
        void bindSuccess();
        void bindFail(String failMsg);
    }

    public interface SettingPresenter{
        void loginout(Context context);
    }

}
