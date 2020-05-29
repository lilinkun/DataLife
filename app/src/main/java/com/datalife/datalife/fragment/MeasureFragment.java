package com.datalife.datalife.fragment;

import android.view.View;
import android.widget.Button;

import com.datalife.datalife.R;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseHealthFragment;
import com.linktop.MonitorDataTransmissionManager;

/**
 * Created by LG on 2018/2/8.
 */

public abstract class MeasureFragment extends BaseHealthFragment {

    public MeasureFragment() {
    }

    public abstract boolean startMeasure();

    public abstract void stopMeasure();

    protected abstract void getchange(String str);


    public void clickMeasure(View v) {
        if (ProApplication.isUseCustomBleDevService) {
            if (!mHcService.isConnected) {
                toast(R.string.health_uncontact);
                return;
            }
            //判断设备是否在充电，充电时不可测量
            if (mHcService.getBleDevManager().getBatteryTask().isCharging()) {
                toast(R.string.health_charging);
                return;
            }
            if (mHcService.getBleDevManager().isMeasuring()) {
                stopMeasure();
                //设置ViewPager可滑动
                mActivity.viewPager.setCanScroll(true);
                getchange(getString(R.string.start));
            } else {
                reset();
                if (startMeasure()) {
                /*
                 * 请注意了：为了代码逻辑不会混乱，每一单项在测量过程中请确保用户无法通过任何途径
                 * (当然，如果用户强制关闭页面就不管了)切换至其他测量单项的界面，直到本项一次测量结束。
                 */
                //设置ViewPager不可滑动
                mActivity.viewPager.setCanScroll(false);
                getchange(getString(R.string.stopmeasure));
                }
            }
        } else {
            final MonitorDataTransmissionManager manager = MonitorDataTransmissionManager.getInstance();

            //判断手机是否和设备实现连接
            if (!manager.isConnected()) {
                toast(R.string.health_uncontact);
                return;
            }
            //判断设备是否在充电，充电时不可测量
            if (manager.isCharging()) {
                toast(R.string.health_charging);
                return;
            }
            //判断是否测量中...
            if (manager.isMeasuring()) {
//            if (mPosition != 2) {//体温没有停止方法，当点击停止的是非体温时才执行停止
                //停止测量
                stopMeasure();
                //设置ViewPager可滑动
                mActivity.viewPager.setCanScroll(true);
                getchange(getString(R.string.start));
//            }
            } else {
                reset();
                //开始测量
                if (startMeasure()) {
                      /*
                       * 请注意了：为了代码逻辑不会混乱，每一单项在测量过程中请确保用户无法通过任何途径
                       * (当然，如果用户强制关闭页面就不管了)切换至其他测量单项的界面，直到本项一次测量结束。
                       */
                    //设置ViewPager不可滑动
                    mActivity.viewPager.setCanScroll(false);
                    getchange(getString(R.string.stopmeasure));
                }
            }

        }
    }

    public abstract void clickUploadData(View v);

    protected void resetState() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.reset();
                getchange(getString(R.string.start));
            }
        });
    }
}
