package com.datalife.datalife.contract;

import com.datalife.datalife.bean.BtRecordBean;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/3/14.
 */

public interface RecordContract {

    public interface View extends IView{
            public void onSuccess(ArrayList<BtRecordBean> newsInfos);
            public void onfail(String str);
            public void onSuccess(Object o);

            public void onEcgFail(String str);
            public void onBtFail(String str);
            public void onBpFail(String str);
            public void onHrFail(String str);
            public void onSpo2hFail(String str);
    }

}
