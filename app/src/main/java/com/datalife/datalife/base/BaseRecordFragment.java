package com.datalife.datalife.base;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import com.datalife.datalife.activity.RecordActivity;
import com.datalife.datalife.contract.RecordContract;
import com.datalife.datalife.interf.OnBtRecordLisener;
import com.datalife.datalife.presenter.RecordPresenter;

/**
 * Created by LG on 2018/2/5.
 */

public abstract class BaseRecordFragment extends BaseFragment{


    protected RecordActivity mActivity;
    public abstract String getTitle();
    protected Typeface mTfRegular;
    protected Typeface mTfLight;

//    protected RecordPresenter recordPresenter = new RecordPresenter(getActivity());
    protected OnBtRecordLisener onBtRecordLisener = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (RecordActivity) getActivity();
        onBtRecordLisener = (OnBtRecordLisener)getActivity();
        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
