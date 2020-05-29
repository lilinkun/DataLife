package com.datalife.datalife.fragment;

import android.view.View;

import com.datalife.datalife.R;
import com.datalife.datalife.base.BaseFragment;

import butterknife.OnClick;

/**
 * Created by LG on 2018/1/16.
 */

public class MyOrderFragment extends BaseFragment {

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initEventAndData() {


    }

    @OnClick({})
    public void onClick(View v) {

    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }
}
