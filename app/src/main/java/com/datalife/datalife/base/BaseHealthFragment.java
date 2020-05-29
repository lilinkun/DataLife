package com.datalife.datalife.base;

import android.content.Context;
import android.support.annotation.NonNull;

import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.interf.OnDataListener;
import com.datalife.datalife.service.HcService;
import com.datalife.datalife.util.UToast;

/**
 * Created by LG on 2018/1/18.
 */

public abstract class BaseHealthFragment extends BaseFragment{

//    protected HealthHomeActivity mActivity;
    protected HealthMonitorActivity mActivity;
    public abstract String getTitle();
    protected OnDataListener onDataListener;
    protected HcService mHcService;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mActivity = (HealthHomeActivity) getActivity();
        mActivity = (HealthMonitorActivity) getActivity();
        if (ProApplication.isUseCustomBleDevService){
            mHcService = mActivity.mHcService;
        }
        onDataListener = (OnDataListener) mActivity;
    }

    public abstract void reset();


    protected void resetState() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.reset();
            }
        });
    }

    protected void toast(@NonNull String text) {
        UToast.show(getActivity(), text);
    }

    protected void toast(@NonNull int intres) {
        UToast.show(getActivity(), getString(intres));
    }

    public void uploadData() {

    }
}

