package com.datalife.datalife.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.datalife.datalife.R;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.contract.BrushHeadContract;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.presenter.BrushHeadPresenter;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.UToast;
import com.datalife.datalife.widget.CustomTitleBar;
import com.datalife.datalife.widget.PercentCircle;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/9/28.
 */

public class BrushHeadActivity extends BaseActivity implements BrushHeadContract.BrushHeadView{

    @BindView(R.id.brush_circle)
    PercentCircle mBrushCircle;
    @BindView(R.id.ll_brush)
    LinearLayout mBrushLayout;

    private BrushHeadPresenter brushHeadPresenter = new BrushHeadPresenter(this);
    private List<MachineBindMemberBean> machineBindMemberBeans = null;
    private MachineBean machineBeans = null;
    private int day = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_brushhead;
    }

    @Override
    protected void initEventAndData() {

        brushHeadPresenter.attachView(this);
        brushHeadPresenter.onCreate();

        Bundle bundle = getIntent().getBundleExtra(DataLifeUtil.BUNDLEMEMBER);
        day = bundle.getInt(DataLifeUtil.DAY);
        String address = bundle.getString("address");
        mBrushCircle.setTargetPercent(day);

        machineBeans = DBManager.getInstance(this).queryMachineBean(address);

        machineBindMemberBeans = DBManager.getInstance(this).queryMachineBindMemberBeanList(machineBeans.getMachineBindId());
    }

    @OnClick({R.id.iv_head_left,R.id.btn_buy_brush_head,R.id.btn_rechange_brush_head})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_head_left:
                Intent intent = new Intent();
                intent.putExtra("day",day);
                setResult(Activity.RESULT_OK,intent);
                finish();

                break;

            case R.id.btn_rechange_brush_head:

                View contentView = LayoutInflater.from(this).inflate(R.layout.pop_brushhead,null);
                LinearLayout linearLayout = (LinearLayout) contentView.findViewById(R.id.ll_popbrush);
                Button sureBtn = (Button) contentView.findViewById(R.id.btn_bg_left);
                Button exitBtn = (Button) contentView.findViewById(R.id.btn_bg_right);
                final PopupWindow popupWindow = new PopupWindow(contentView,
                    LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT, true);
                popupWindow.setContentView(contentView);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAtLocation(mBrushLayout,Gravity.CENTER | Gravity.CENTER, 0, 0);

                sureBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()){
                            brushHeadPresenter.getChangeBrushHead(machineBindMemberBeans.get(0).getMachineBindId(), ProApplication.SESSIONID,machineBindMemberBeans.get(0).getMember_Id());
                            popupWindow.dismiss();
                        }
                    }
                });


                exitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()){
                            popupWindow.dismiss();
                        }
                    }
                });

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()){
                            popupWindow.dismiss();
                        }
                    }
                });
                break;

            case R.id.btn_buy_brush_head:

                break;

        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.putExtra("day",day);
            setResult(Activity.RESULT_OK,intent);
            finish();
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSuccess() {
        UToast.show(this,"更换清空数据成功");

        day = 90;
        mBrushCircle.setTargetPercent(day);
    }

    @Override
    public void onFail(String msg) {

    }
}
