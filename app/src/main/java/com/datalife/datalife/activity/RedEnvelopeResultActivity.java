package com.datalife.datalife.activity;

import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.bean.RedEnvelopeEntity;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.Eyes;
import com.datalife.datalife.util.UIHelper;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/10/17.
 */

public class RedEnvelopeResultActivity extends BaseActivity {

    @BindView(R.id.tv_red_envelope_value)
    TextView mTvRedEnvelopeValue;
    @BindView(R.id.tv_red_envelope_tip)
    TextView mTvRedEnvelopeTip;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_redenvelope_result;
    }

    @Override
    protected void initEventAndData() {
        Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.red));

        RedEnvelopeEntity redEnvelopeEntity = (RedEnvelopeEntity) getIntent().getBundleExtra(DataLifeUtil.BUNDLEMEMBER).getSerializable("redenvelope");
        if (redEnvelopeEntity != null){
            mTvRedEnvelopeValue.setText(redEnvelopeEntity.getAmount());
        }

    }

    @OnClick({R.id.iv_head_left,R.id.tv_myenvelope})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_head_left:
                setResult(RESULT_OK);
                finish();

                break;

            case R.id.tv_myenvelope:

                UIHelper.launcher(this,MyRedEnvelopeListActivity.class);

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){

            setResult(RESULT_OK);

        }


        return super.onKeyDown(keyCode, event);
    }
}
