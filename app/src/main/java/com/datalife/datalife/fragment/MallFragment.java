package com.datalife.datalife.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datalife.datalife.R;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.util.Eyes;
import com.datalife.datalife.util.UToast;

import butterknife.OnClick;

/**
 * Created by LG on 2018/1/15.
 */

public class MallFragment extends BaseFragment {

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_mall;
    }

    @Override
    protected void initEventAndData() {
//        Eyes.translucentStatusBar(getActivity());
    }


//    @OnClick()
//    public void onClick(View v) {
//
//    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }
}
