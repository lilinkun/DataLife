package com.datalife.datalife.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.activity.RecordActivity;
import com.datalife.datalife.adapter.MemberAdapter;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.bean.SPO2H;
import com.datalife.datalife.contract.BtContract;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.dao.Spo2hDao;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.interf.RecyclerItemClickListener;
import com.datalife.datalife.presenter.BtPresenter;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.MeasureNorm;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.widget.HealthDialProgress;
import com.datalife.datalife.widget.OxWaveView;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnSPO2HResultListener;
import com.linktop.whealthService.MeasureType;
import com.linktop.whealthService.task.OxTask;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/1/18.
 */

public class SPO2HFragment extends MeasureFragment
        implements OnSPO2HResultListener,BtContract.BtView {

    private SPO2H model;
    private OxTask mOxTask;

    @BindView(R.id.tv_spo2h)
    TextView mSpo2hTv;
    @BindView(R.id.oxWave)
    OxWaveView mOxWaveTv;
    @BindView(R.id.iv_spo2h)
    ImageView ivspo2h;
    @BindView(R.id.btn_starttest)
    Button mStartTestBtn;
    @BindView(R.id.ic_heartrate)
    ImageView mIvHeartrate;
    @BindView(R.id.tv_old_test)
    TextView mOldTest;
    @BindView(R.id.tv_new_test)
    TextView mNewTest;
    @BindView(R.id.dial_progress_bar)
    HealthDialProgress healthDialProgress;
    @BindView(R.id.iv_color_dial_spo2h)
    ImageView mIvSpo2hDial;
    @BindView(R.id.tv_dial_spo2h)
    TextView mTvSpo2hDial;
    @BindView(R.id.pb_spo2h)
    ProgressBar mSpo2hProgressBar;

    BtPresenter btPresenter = new BtPresenter(getActivity());

    private final int SPO2HDATA = 0x222;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case SPO2HDATA:

                    DataLifeUtil.startAlarm(getActivity());
//                    mSpo2hHrTv.setText(model.getHr() + "");
                    mSpo2hTv.setText(model.getValue() + "%");
                    mNewTest.setText(model.getValue() + "");
                    mSpo2hProgressBar.setVisibility(View.GONE);
                    if (model.getValue() < 50){
                        toast("您测量不准确");
                        return;
                    }
                    healthDialProgress.setcolor(DataLifeUtil.getColor(getActivity()));
                    healthDialProgress.setValue((float)(model.getValue()-60));

                    DataLifeUtil.saveSpo2hData(getActivity(),model.getValue()+"");

                    DBManager dbManager = DBManager.getInstance(getActivity());
                    Spo2hDao spo2hDao = new Spo2hDao();
                    spo2hDao.setCreateTime(DataLifeUtil.getCurrentTime());
                    spo2hDao.setName("liguo");
                    spo2hDao.setSpo2hValue(model.getValue()+"");
                    dbManager.insertUser(spo2hDao);

                    if (model.getValue() != 0) {
                        btPresenter.putSpo2hValue(HealthMonitorActivity.mMemberId + "",HealthMonitorActivity.mMachineBindId, String.valueOf(IDatalifeConstant.SPO2HINT), MonitorInfoFragment.deviceId, DeviceData.getUniqueId(getActivity()), model.getValue() + "", model.getHr() + "");
                    }
                    break;
            }

        }
    };

    public SPO2HFragment() {
    }

    @Override
    public boolean startMeasure() {
        if (mOxTask != null) {
            mOxTask.start();
            mSpo2hProgressBar.setVisibility(View.VISIBLE);
            mSpo2hTv.setText("");
            if (mNewTest !=null && !mNewTest.getText().toString().equals("-")){
                mOldTest.setText(mNewTest.getText().toString());
                mNewTest.setText("-");
                healthDialProgress.setValue(0);
            }
        } else {
            MonitorDataTransmissionManager.getInstance().startMeasure(MeasureType.SPO2H);

            mStartTestBtn.setText(getString(R.string.stopmeasure));
            if (mIvHeartrate != null && mIvHeartrate.isShown()) {
                mIvHeartrate.setVisibility(View.GONE);
            }

        }
        return true;
    }

    @Override
    public void stopMeasure() {
        if (mOxTask != null) {
            mOxTask.stop();
            mSpo2hProgressBar.setVisibility(View.GONE);
        } else {
            MonitorDataTransmissionManager.getInstance().stopMeasure();
        }
    }

    @Override
    protected void getchange(String str) {
        mStartTestBtn.setText(str);
        if (str != null  && str.equals(getString(R.string.stopmeasure))){
            if (mIvHeartrate != null && mIvHeartrate.isShown()) {
                mIvHeartrate.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void clickUploadData(View v) {
    }

    @Override
    public String getTitle() {
        return getActivity().getResources().getString(R.string.spo2h_value);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mHcService != null) {
            mOxTask = mHcService.getBleDevManager().getOxTask();
            mOxTask.setOnSPO2HResultListener(this);
        } else {
            MonitorDataTransmissionManager.getInstance().setOnSPO2HResultListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        btPresenter.onStop();
    }

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_spo2h;
    }

    @Override
    protected void initEventAndData() {
        btPresenter.onCreate();
        btPresenter.attachView(this);
        model = new SPO2H();

    }

    @Override
    public void reset() {
        model.reset();
        mOxWaveTv.clear();
    }

    @Override
    public void onSPO2HResult(int spo2h, int heartRate) {
        model.setTs(System.currentTimeMillis() / 1000L);
        model.setValue(spo2h);
        model.setHr(heartRate);

        handler.sendEmptyMessage(SPO2HDATA);
        resetState();
    }

    @OnClick({R.id.btn_starttest,R.id.ll_more_historyrecord})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_starttest:
                clickMeasure(view);
                break;
            case R.id.ll_more_historyrecord:
                Bundle bundle = new Bundle();
                bundle.putString(DataLifeUtil.BUNDLEMEMBERID,HealthMonitorActivity.mMemberId);
                bundle.putInt(DataLifeUtil.PAGE,DataLifeUtil.HEALTH_HISTORY_PAGE_SPO2H);
                UIHelper.launcherBundle(getActivity(), RecordActivity.class,bundle);
                break;
        }
    }


    @Override
    public void onSPO2HWave(int value) {
        mOxWaveTv.preparePoints(value);
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFail() {

    }

    public void setOldTest(MeasureRecordBean spo2hMeasureRecordBean){
        if (spo2hMeasureRecordBean == null){
            mOldTest.setText("-");
        }else{
            mOldTest.setText(spo2hMeasureRecordBean.getCheckValue1()+"");
        }
        mNewTest.setText("-");
    }

    //圆环下显示的字
    public void setValue(double spo2h){
        if (spo2h >= 110){
            mIvSpo2hDial.setBackground(getResources().getDrawable(R.color.dial_red));
            mTvSpo2hDial.setText("含氧过高");
        }else if (spo2h <= 110 && spo2h >= 90){
            mIvSpo2hDial.setBackground(getResources().getDrawable(R.color.dial_green));
            mTvSpo2hDial.setText("正常");
        }else if (spo2h <= 89){
            mIvSpo2hDial.setBackground(getResources().getDrawable(R.color.dial_blue));
            mTvSpo2hDial.setText("含氧过低");
        }

    }
}