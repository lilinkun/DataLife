package com.datalife.datalife.contract;

import android.content.Context;

import com.datalife.datalife.bean.DownloadBean;
import com.datalife.datalife.bean.GeneBean;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/3/27.
 */

public interface ReportContract {

    public interface ReportView extends IView {
        public void BackValueSuccess(ArrayList<GeneBean> geneBeans);
        public void BackValueFail(String msg);
    }

}
