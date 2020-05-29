package com.datalife.datalife.fragment;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.ECGLargeChartActivity;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.activity.RecordActivity;
import com.datalife.datalife.adapter.HealthTestAdapter;
import com.datalife.datalife.adapter.MemberAdapter;
import com.datalife.datalife.bean.ECG;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.binding.ObservableString;
import com.datalife.datalife.contract.BtContract;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.interf.RecyclerItemClickListener;
import com.datalife.datalife.presenter.BtPresenter;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.widget.DoubleWaveView;
import com.datalife.datalife.widget.EcgWaveView;
import com.datalife.datalife.widget.WaveView;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnEcgResultListener;
import com.linktop.whealthService.MeasureType;
import com.linktop.whealthService.task.EcgTask;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import lib.linktop.obj.DataFile;
import lib.linktop.obj.LoadECGBean;
import lib.linktop.sev.HmLoadDataTool;

/**
 * Created by LG on 2018/1/18.
 */

public class ECGFragment extends MeasureFragment
        implements OnEcgResultListener,BtContract.BtView{

    @BindView(R.id.fl_openECGLarge)
    FrameLayout mOpenECGLargeFl;
    @BindView(R.id.ecg_draw_chart)
    EcgWaveView mEcgWaveView;
    @BindView(R.id.tv_ecg_hr)
    TextView mEcgHrTv;
    @BindView(R.id.tv_ecg_hrv)
    TextView mEcgHrvTv;
    @BindView(R.id.tv_ecg_mood)
    TextView mEcgMoodTv;
    @BindView(R.id.tv_ecg_br)
    TextView mEcgBrTv;
    @BindView(R.id.tv_rrMin)
    TextView mEcgRrMinTv;
    @BindView(R.id.tv_rrMax)
    TextView mEcgRrMaxTv;
    @BindView(R.id.btn_starttest)
    Button mStartTestBtn;
    @BindView(R.id.ll_table)
    RelativeLayout mTableLayout;
    @BindView(R.id.rv_test)
    RecyclerView rvTest;
    @BindView(R.id.rv_old_test)
    RecyclerView rvOldTest;

    private ECG model;
    private EcgTask mEcgTask;
    private final ObservableInt pagerSpeed = new ObservableInt(1);
    private final ObservableFloat calibration = new ObservableFloat(1.0f);
    private final ObservableString pagerSpeedStr = new ObservableString("25mm/s");
    private final ObservableString calibrationStr = new ObservableString("10mm/mV");
    private final StringBuilder ecgWaveBuilder = new StringBuilder();

    public final int ECGHANDDATA = 0x3313;
    private boolean isOver = false;

    private final int ECGHANDLERHR = 0X2120;
    private final int ECGHANDLERBR = 0X2121;
    private final int ECGHANDLERRRMAX = 0X2122;
    private final int ECGHANDLERRRMIN = 0X2123;
    private final int ECGHANDLERMOOD = 0X2124;
    private final int ECGHANDLERHRV = 0X2125;

    private ArrayList<String> oldList = new ArrayList<>();
    private ArrayList<String> newList = new ArrayList<>();

    private HealthTestAdapter oldHealthTestAdapter = null;
    private HealthTestAdapter newHealthTestAdapter = null;


    BtPresenter btPresenter = new BtPresenter(getActivity());

    private Handler myEcgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ECGHANDDATA:
                    DataLifeUtil.startAlarm(getActivity());
                    mEcgBrTv.setText(model.getBr() + "");
                    mEcgRrMaxTv.setText(model.getRrMax() + "");
                    mEcgRrMinTv.setText(model.getRrMin() + "");
                    mEcgHrTv.setText(model.getHr() + "");
                    mEcgMoodTv.setText(model.getMood() + "");
                    mEcgHrvTv.setText(model.getHrv() + "");

                    newList.clear();
                    newList.add(model.getRrMax() + "");
                    newList.add(model.getRrMin() + "");
                    newList.add(model.getHrv() + "");
                    newList.add(model.getHr() + "");
                    newList.add(model.getMood() + "");
                    newList.add(model.getBr() + "");
                    newHealthTestAdapter.setArrayList(newList);
                    newHealthTestAdapter.notifyDataSetChanged();
                    btPresenter.putEcgValue(HealthMonitorActivity.mMemberId+"",HealthMonitorActivity.mMachineBindId,String.valueOf(IDatalifeConstant.ECGINT), MonitorInfoFragment.deviceId,DeviceData.getUniqueId(getActivity()),model.getRrMax()+"",model.getRrMin() + "",model.getHr() + "",model.getHrv() + "",model.getMood() + "",model.getBr() + "");

                    try{
                        DataLifeUtil.saveEcgData(getActivity(),DataLifeUtil.serialize(model));
                    }catch (Exception e){
                        toast("心电图数据未保存");
                    }
                    break;
                case ECGHANDLERHR:
                    mEcgHrTv.setText(model.getHr() + "");
                    break;
                case ECGHANDLERRRMAX:
                    mEcgRrMaxTv.setText(model.getRrMax() + "");
                    break;
                case ECGHANDLERRRMIN:
                    mEcgRrMinTv.setText(model.getRrMin() + "");
                    break;
                case ECGHANDLERHRV:
                    mEcgHrvTv.setText(model.getHrv() + "");
                    break;
                case ECGHANDLERMOOD:
                    mEcgMoodTv.setText(model.getMood() + "");
                    break;
                case ECGHANDLERBR:
                    mEcgBrTv.setText(model.getBr() + "");
                    break;
            }
        }
    };


    public ECGFragment() {
    }

    @Override
    public boolean startMeasure() {
        if (mEcgTask != null) {

            if (mHcService.getBleDevManager().getBatteryTask().isCharging()) {
                toast(R.string.health_charging);
                return true;
            }

            if (newList!=null && oldHealthTestAdapter != null && newList.size()>0){
                oldHealthTestAdapter.setArrayList(newList);
                oldHealthTestAdapter.notifyDataSetChanged();
                newHealthTestAdapter.setArrayList(null);
                newHealthTestAdapter.notifyDataSetChanged();
            }

            isOver = false;

            if (mEcgTask.isModuleExist()) {
                mEcgRrMaxTv.setText("");
                mEcgRrMinTv.setText("");
                mEcgHrTv.setText("");
                mEcgBrTv.setText("");
                mEcgHrvTv.setText("");
                mEcgMoodTv.setText("");

                mEcgTask.initEcgTg();
                mEcgTask.start();
                if (mTableLayout != null && mTableLayout.isShown()) {
                    mTableLayout.setVisibility(View.GONE);
                }
                return true;
            } else {
//                toast("This Device's ECG module is not exist.");
                return false;
            }
        } else {
            if (MonitorDataTransmissionManager.getInstance().isEcgModuleExist()) {
                MonitorDataTransmissionManager.getInstance().startMeasure(MeasureType.ECG);
                mStartTestBtn.setText(getString(R.string.stopmeasure));
                isOver = false;
                return true;
            } else {
//                toast("This Device's ECG module is not exist.");
                return false;
            }
        }
    }

    @Override
    public void stopMeasure() {
        if (mEcgTask != null) {
            mEcgTask.stop();
            reset();
        } else {
            MonitorDataTransmissionManager.getInstance().stopMeasure();
            mStartTestBtn.setText("开始");
        }
    }

    @Override
    protected void getchange(String str) {
        mStartTestBtn.setText(str);
    }

    @Override
    public void clickUploadData(View v) {
        if (model == null || model.isEmptyData()) {
            toast("不能上传空数据");
            return;
        }
//        HmLoadDataTool.getInstance().uploadData(DataFile.DATA_ECG, model);
    }

    @Override
    public String getTitle() {
        return getActivity().getResources().getString(R.string.electrocardiogram);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);if (mHcService != null) {
            mEcgTask = mHcService.getBleDevManager().getEcgTask();
            mEcgTask.setOnEcgResultListener(this);
            //It is better to set user information who is measuring ECG.
            //if it is not,SDK will use a default user information.
            // Of course,it may reduce the accuracy of the measurement results.
            mEcgTask.setUserInfo("liguo", 27, 166, 65, false);
        } else {
            MonitorDataTransmissionManager.getInstance().setECGUerInfo("liguo", 27, 166, 65, false);
            MonitorDataTransmissionManager.getInstance().setOnEcgResultListener(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        btPresenter.onStop();
    }

    public void onVisible(){
        if (mTableLayout != null && mTableLayout.isShown()) {
            mTableLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_ecg;
    }

    @Override
    protected void initEventAndData() {
        btPresenter.onCreate();
        btPresenter.attachView(this);
        model = new ECG();
        mEcgWaveView.setCalibration(calibration.get());
        mEcgWaveView.setPagerSpeed(pagerSpeed.get());
        if (mTableLayout != null && mTableLayout.isShown()) {
            mTableLayout.setVisibility(View.GONE);
        }

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTest.setLayoutManager(linearLayoutManager1);
        newHealthTestAdapter = new HealthTestAdapter(getActivity(),R.color.bg_toolbar_title,null);
        rvTest.setAdapter(newHealthTestAdapter);
        rvTest.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    rvOldTest.scrollBy(dx,dy);
                }
            }
        });

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvOldTest.setLayoutManager(linearLayoutManager2);
        oldHealthTestAdapter = new HealthTestAdapter(getActivity(),R.color.yellow_test,oldList);
        rvOldTest.setAdapter(oldHealthTestAdapter);
        rvOldTest.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    rvTest.scrollBy(dx,dy);
                }
            }
        });
    }

    @Override
    public void reset() {
        model.reset();
        ecgWaveBuilder.setLength(0);
        mEcgWaveView.clear();
    }

    long startTs = 0L;
    int i = 0;

    /*
    * 心电图数据点
    * */
    @Override
    public synchronized void onDrawWave(int wave) {
//        i++;
//        if (startTs == 0L) startTs = System.currentTimeMillis();
        //将数据点在心电图控件里描绘出来
        mEcgWaveView.preparePoint(wave);
        //将数据点存入容器，查看大图使用
        ecgWaveBuilder.append(wave).append(",");
    }

    @Override
    public void onSignalQuality(int i) {

    }

    @Override
    public void onAvgHr(int avgHr) {
        model.setHr(avgHr);
        myEcgHandler.sendEmptyMessage(ECGHANDLERHR);
    }

    @Override
    public void onRRMax(int rr) {
        model.setRrMax(rr);
        myEcgHandler.sendEmptyMessage(ECGHANDLERRRMAX);
    }

    @Override
    public void onRRMin(int rr) {
        model.setRrMin(rr);
        myEcgHandler.sendEmptyMessage(ECGHANDLERRRMIN);
    }

    @Override
    public void onHrv(int hrv) {
        model.setHrv(hrv);
        myEcgHandler.sendEmptyMessage(ECGHANDLERHRV);
    }

    @Override
    public void onMood(int mood) {
        model.setMood(mood);
        myEcgHandler.sendEmptyMessage(ECGHANDLERMOOD);
    }

    @Override
    public void onBr(int br) {
        model.setBr(br);
        myEcgHandler.sendEmptyMessage(ECGHANDLERBR);
    }

    /*
    * 心电图测量持续时间,该回调一旦触发说明一次心电图测量结束
    * */
    @Override
    public void onEcgDuration(long duration) {
        final long l = (System.currentTimeMillis() - startTs) / 1000L;
        final long l1 = i / l;
        Log.e("onEcgDuration", "" + l1);
        startTs = 0L;
        i = 0;
        model.setDuration(duration);
        model.setTs(System.currentTimeMillis() / 1000L);
        String ecgWave = ecgWaveBuilder.toString();
        ecgWave = ecgWave.substring(0, ecgWave.length() - 1);
        model.setWave(ecgWave);

        myEcgHandler.sendEmptyMessage(ECGHANDDATA);

//        Log.e("ECG:" , model.toString());
//        btPresenter.putEcgValue(HealthMonitorActivity.mMemberId + "",HealthMonitorActivity.mMachineBindId,String.valueOf(IDatalifeConstant.ECGINT), MonitorInfoFragment.deviceId, DeviceData.getUniqueId(getActivity()),model.getRrMax() + "",model.getRrMin()+"",model.getHr()+"",model.getHrv()+"",model.getMood()+"",model.getBr()+"");

        isOver = true;

        resetState();
    }

    public void setOldTest(MeasureRecordBean ecgMeasureRecordBean){
        oldList.clear();
        if (ecgMeasureRecordBean != null){
            oldList.add(ecgMeasureRecordBean.getCheckValue1());
            oldList.add(ecgMeasureRecordBean.getCheckValue2());
            oldList.add(ecgMeasureRecordBean.getCheckValue4());
            oldList.add(ecgMeasureRecordBean.getCheckValue3());
            oldList.add(ecgMeasureRecordBean.getCheckValue5());
            oldList.add(ecgMeasureRecordBean.getCheckValue6());
        }
        oldHealthTestAdapter.setArrayList(oldList);
        oldHealthTestAdapter.notifyDataSetChanged();
        newList.clear();
        newHealthTestAdapter.setArrayList(newList);
        newHealthTestAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick({R.id.fl_openECGLarge, R.id.btn_starttest,R.id.tv_reference_value,R.id.tv_back,R.id.ll_more_historyrecord})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fl_openECGLarge:
                if (isOver){
                    openECGLarge();
                }
                break;
            case R.id.btn_starttest:
                clickMeasure(view);
                break;
            case R.id.tv_reference_value:
                mTableLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_back:
                mTableLayout.setVisibility(View.GONE);
                break;
            case R.id.ll_more_historyrecord:
                Bundle bundle = new Bundle();
                bundle.putString(DataLifeUtil.BUNDLEMEMBERID,HealthMonitorActivity.mMemberId);
                bundle.putInt(DataLifeUtil.PAGE,DataLifeUtil.HEALTH_HISTORY_PAGE_ECG);
                UIHelper.launcherBundle(getActivity(), RecordActivity.class,bundle);
                break;
        }

    }

    @Override
    public void uploadData() {
        if (model == null || model.isEmptyData()) {
            toast("不能上传空数据");
            return;
        }

    }

    public void openECGLarge() {
        Intent intent = new Intent(mActivity, ECGLargeChartActivity.class);
        intent.putExtra("pagerSpeed", pagerSpeed.get());
        intent.putExtra("calibration", calibration.get());
        intent.putExtra("model", model);
        startActivity(intent);
    }

    /*
    * 点击设置时间基准(走纸速度)
    * 该值反应心电图x轴的幅度，设置的值这里没做保存，请自行保存，以便下次启动该页面时自动设置已保存的值
    * */
    public void clickSetPagerSpeed() {
        int checkedItem = pagerSpeed.get() - 1;
        onShowSingleChoiceDialog(true, "时间基准", R.array.ecg_pager_speed, checkedItem);
    }

    /*
    * 点击设置增益
    * 该值反应心电图y轴的幅度，设置的值这里没做保存，请自行保存，以便下次启动该页面时自动设置已保存的值
    * */
    public void clickSetCalibration() {
        int checkedItem = calibration.get() == 0.5f ? 0 : (int) calibration.get();
        onShowSingleChoiceDialog(false, "增益", R.array.ecg_calibration, checkedItem);
    }

    private void onShowSingleChoiceDialog(final boolean isTimeRef, String titleResId, int itemsId, int checkedItem) {
        final String[] items = getResources().getStringArray(itemsId);
        new AlertDialog.Builder(mActivity)
                .setTitle(titleResId)
                .setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isTimeRef) {
                            pagerSpeed.set(which + 1);
                            pagerSpeedStr.set(items[which]);
                        } else {
                            calibration.set(which == 0 ? 0.5f : which);
                            calibrationStr.set(items[which]);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.close, null)
                .create()
                .show();
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
}
