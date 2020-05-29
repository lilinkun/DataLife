package com.datalife.datalife.contract;

import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.dao.BrushBean;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/10/11.
 */

public interface BrushHistoryContract {

    public interface BrushHistoryView extends IView{
        public void onSuccess(ArrayList<BrushBean> brushBeans);
        public void onFail(String msg);
    }


}
