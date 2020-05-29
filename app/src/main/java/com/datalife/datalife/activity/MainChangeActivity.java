package com.datalife.datalife.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.MainMachineAdapter;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.widget.GridSpacingItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by LG on 2018/10/15.
 */

public class MainChangeActivity extends BaseActivity {

    @BindView(R.id.rv_equit)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_rechange;
    }

    @Override
    protected void initEventAndData() {


        List<MachineBean> machineBeans = DBManager.getInstance(this).queryMachineBeanList();

        GridLayoutManager  gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        int spanCount = 2; // 2 columns
        int spacing = 20; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        recyclerView.setLayoutManager(gridLayoutManager);

        if (machineBeans.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            MainMachineAdapter mainMachineAdapter = new MainMachineAdapter(this, machineBeans);
            recyclerView.setAdapter(mainMachineAdapter);
        }

    }

}
