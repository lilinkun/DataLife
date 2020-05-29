package com.datalife.datalife.contract;

import com.datalife.datalife.mvp.IView;

/**
 * Created by LG on 2018/2/28.
 */

public interface BtContract {

    public interface BtView extends IView{
        public void onSuccess();
        public void onFail();
    }

    public interface BtPresenter{

    }
}
