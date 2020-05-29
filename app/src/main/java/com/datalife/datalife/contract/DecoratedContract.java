package com.datalife.datalife.contract;

import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.mvp.IView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by LG on 2018/5/25.
 */

public interface DecoratedContract {

    public interface DecoratedView extends IView{
        public void onSuccess(ArrayList<String> strs);
        public void onfail(String str);
        public void onDataSuccess(ArrayList<MeasureRecordBean> measureRecordBeans);
        public void onDataFail(String str);
    }


}
