package com.datalife.datalife.contract;

import com.datalife.datalife.bean.DataTestBean;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/8/20.
 */

public interface DataTestContract {

    public interface DataTestView extends IView {
        public void success(ArrayList<DataTestBean> dataTestBeans);
        public void fail(String msg);
    }
}
