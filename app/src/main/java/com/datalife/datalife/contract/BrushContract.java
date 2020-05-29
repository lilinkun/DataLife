package com.datalife.datalife.contract;

import com.datalife.datalife.bean.DataTestBean;
import com.datalife.datalife.mvp.IView;

/**
 * Created by LG on 2018/10/9.
 */

public interface BrushContract {

    public interface BrushBindView extends IView {
        public void success();
        public void fail(String msg);
    }
}
