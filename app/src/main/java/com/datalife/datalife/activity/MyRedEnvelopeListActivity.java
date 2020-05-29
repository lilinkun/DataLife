package com.datalife.datalife.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.RedEnvelopeAdapter;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.bean.RedEnvelopeBean;
import com.datalife.datalife.util.Eyes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/10/18.
 */

public class MyRedEnvelopeListActivity extends BaseActivity {

    @BindView(R.id.rv_red_envelope_list)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_red_envelope_list;
    }

    @Override
    protected void initEventAndData() {
        Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.white));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<RedEnvelopeBean> redEnvelopeBeans = new ArrayList<>();

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        Random random = new Random();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-SS");
        for (int i = 0; i < 10;i++){
            Date date = new Date();
            RedEnvelopeBean redEnvelopeBean = new RedEnvelopeBean();
            redEnvelopeBean.setRedEnvelopeSum(random.nextInt(10));
            redEnvelopeBean.setRedEnvelopeTime(simpleDateFormat.format(date));
            redEnvelopeBean.setRedEnvelopeType("百年中堂红包");
            redEnvelopeBeans.add(redEnvelopeBean);
        }

        RedEnvelopeAdapter redEnvelopeAdapter = new RedEnvelopeAdapter(this,redEnvelopeBeans);

        mRecyclerView.setAdapter(redEnvelopeAdapter);


    }

    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:

                finish();

                break;
        }
    }

}
