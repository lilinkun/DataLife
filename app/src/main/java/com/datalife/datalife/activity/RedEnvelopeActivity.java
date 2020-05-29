package com.datalife.datalife.activity;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.datalife.datalife.R;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.util.Eyes;
import com.datalife.datalife.util.UIHelper;

import butterknife.OnClick;

/**
 * Created by LG on 2018/10/17.
 */

public class RedEnvelopeActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_redenvelope;
    }

    @Override
    protected void initEventAndData() {
        Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.red));
    }

    @OnClick({R.id.btn_red_envelope,R.id.iv_head_left})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_red_envelope:

                UIHelper.launcher(this,RedEnvelopeResultActivity.class);
                finish();

                break;

            case R.id.iv_head_left:

                finish();

                break;
        }
    }

}
