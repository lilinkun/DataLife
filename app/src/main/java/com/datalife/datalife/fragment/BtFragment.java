package com.datalife.datalife.fragment;

import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.FatActivity;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.activity.RecordActivity;
import com.datalife.datalife.bean.Bt;
import com.datalife.datalife.bean.LastMeasureDataBean;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.contract.BtContract;
import com.datalife.datalife.presenter.BtPresenter;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.MeasureNorm;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.widget.BigStateView;
import com.datalife.datalife.widget.CircleProgressBar;
import com.datalife.datalife.widget.DialProgress;
import com.datalife.datalife.widget.HealthDialProgress;
import com.datalife.datalife.widget.SmallStateView;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnBtResultListener;
import com.linktop.whealthService.MeasureType;
import com.linktop.whealthService.task.BtTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/1/18.
 * 体温
 */
public class BtFragment extends MeasureFragment
        implements OnBtResultListener ,BtContract.BtView{

    private final ObservableBoolean isUnitF = new ObservableBoolean(false);
    private Bt model;
    private BtTask mBtTask;

    private final int BTDATA = 0x111;

    @BindView(R.id.tv_temp_start)
    TextView mTempStartTv;
    @BindView(R.id.tv_temp)
    TextView mTempTv;
    @BindView(R.id.btn_starttest)
    Button mStartTestBtn;
    @BindView(R.id.bt_progress)
    CircleProgressBar mProgressBar;
    @BindView(R.id.tv_new_test)
    TextView mNewTestView;
    @BindView(R.id.tv_old_test)
    TextView mOldTestView;
    @BindView(R.id.dial_progress_bar)
    HealthDialProgress dialProgress;
    @BindView(R.id.iv_color_dial_bt)
    ImageView mIvBtDial;
    @BindView(R.id.tv_dial_bt)
    TextView mbtTextView;
    @BindView(R.id.pb_bt)
    ProgressBar mProgress;

    BtPresenter btPresenter = new BtPresenter(getActivity());

    private boolean isStartMeasure = false;

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case BTDATA:
                    DataLifeUtil.startAlarm(getActivity());
                    mProgress.setVisibility(View.GONE);
                    if(model.getTemp() > 43 || model.getTemp() < 34){
                        toast("请准确测量");
                        return;
                    }
                    dialProgress.setcolor(DataLifeUtil.getColor(getActivity()));
                    dialProgress.setValue((float)(model.getTemp()-31.5));
                    setValue(model.getTemp());
                    DataLifeUtil.saveBtData(getActivity(),String.valueOf(model.getTemp()));
                    isStartMeasure = false;
                    mNewTestView.setText(model.getTemp()+"");
                    mStartTestBtn.setText(getString(R.string.start));


//                    double temp = model.getTemp()- 35;
//                    mProgressBar.setProgress(temp);
                    btPresenter.putBtValue(HealthMonitorActivity.mMemberId + "",HealthMonitorActivity.mMachineBindId,String.valueOf(IDatalifeConstant.BTINT), MonitorInfoFragment.deviceId, DeviceData.getUniqueId(getActivity()),model.getTemp()+"");

                    break;
            }
        }
    };


    public BtFragment() {
    }

    @Override
    public boolean startMeasure() {
        if(mBtTask!= null) {
            mBtTask.start();
            isStartMeasure = true;
            mTempTv.setText("");
            mProgress.setVisibility(View.VISIBLE);
        } else {
            MonitorDataTransmissionManager.getInstance().startMeasure(MeasureType.BT);
            mStartTestBtn.setText(getString(R.string.stopmeasure));
        }

        if (!mNewTestView.getText().toString().equals("-")) {
            setOldTestView(mNewTestView.getText().toString());
            mNewTestView.setText("-");
            dialProgress.setValue(0);
        }

        return true;
    }

    public void setStart(){
        mStartTestBtn.setText(getString(R.string.start));
    }

    @Override
    public void stopMeasure() {
        if (mBtTask != null) {
            mBtTask.stop();
            mStartTestBtn.setText(getString(R.string.start));
            isStartMeasure = false;
            mProgress.setVisibility(View.GONE);
        }
    }

    @Override
    protected void getchange(String str) {
        mStartTestBtn.setText(str);
    }

    @Override
    public void clickUploadData(View v) {
    }

    @Override
    public String getTitle() {
        return getActivity().getResources().getString(R.string.bt);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mHcService != null) {
            mBtTask = mHcService.getBleDevManager().getBtTask();
            mBtTask.setOnBtResultListener(this);
        } else {
            MonitorDataTransmissionManager.getInstance().setOnBtResultListener(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        btPresenter.onStop();
    }

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_bt;
    }

    @Override
    protected void initEventAndData() {
        btPresenter.onCreate();
        btPresenter.attachView(this);
//        onBtListener = (OnBtListener) getActivity();
        model = new Bt();
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
                bundle.putInt(DataLifeUtil.PAGE,DataLifeUtil.HEALTH_HISTORY_PAGE_TEMP);
                UIHelper.launcherBundle(getActivity(), RecordActivity.class,bundle);
                break;
        }
    }

    @Override
    public void reset() {
        model.reset();
    }

    /*
    * 默认返回单位为摄氏度的温度值，若需要华氏度的温度值，根据公式转换
    * 本Demo使用Databinding的方式，详情请看该布局的温度显示控件TextView
    * */
    @Override
    public void onBtResult(double tempValue) {
        model.setTemp(tempValue);
        model.setTs(System.currentTimeMillis() / 1000L);
        myHandler.sendEmptyMessage(BTDATA);
        resetState();
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

    public void setOldTest(MeasureRecordBean btMeasureRecordBean){
        if (btMeasureRecordBean == null){
            mOldTestView.setText("-");
        }else {
            mOldTestView.setText(btMeasureRecordBean.getCheckValue1() + "");
        }
        mNewTestView.setText("-");
    }

    public void setOldTestView(String bttemp){
        if (bttemp != null && bttemp.trim().length() != 0){
            mOldTestView.setText(bttemp + "");
        }
    }

    //圆环下显示的字
    public void setValue(double bt){
        if (bt >= 37.5){
            mIvBtDial.setBackground(getResources().getDrawable(R.color.dial_red));
            mbtTextView.setText("高烧");
        }else if (bt <=  37.4 && bt >= 36){
            mIvBtDial.setBackground(getResources().getDrawable(R.color.dial_green));
            mbtTextView.setText("正常");
        }else if (bt <= 35.9){
            mIvBtDial.setBackground(getResources().getDrawable(R.color.dial_blue));
            mbtTextView.setText("低烧");
        }

    }
}

