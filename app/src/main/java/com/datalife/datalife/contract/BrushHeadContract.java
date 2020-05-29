package com.datalife.datalife.contract;

import com.datalife.datalife.mvp.IView;

/**
 * Created by LG on 2018/10/12.
 */

public interface BrushHeadContract {

    public interface BrushHeadView extends IView{
        public void onSuccess();
        public void onFail(String msg);
    }
}
