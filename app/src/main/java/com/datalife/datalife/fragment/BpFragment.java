package com.datalife.datalife.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.activity.RecordActivity;
import com.datalife.datalife.base.BaseHealthFragment;
import com.datalife.datalife.bean.Bp;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.contract.BtContract;
import com.datalife.datalife.interf.OnResponseListener;
import com.datalife.datalife.presenter.BtPresenter;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.MeasureNorm;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.widget.BigStateView;
import com.datalife.datalife.widget.CustomColorBar;
import com.datalife.datalife.widget.CustomSeekbar;
import com.datalife.datalife.widget.HealthDialProgress;
import com.datalife.datalife.widget.SmallStateView;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnBpResultListener;
import com.linktop.whealthService.MeasureType;
import com.linktop.whealthService.task.BpTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.linktop.obj.DataFile;
import lib.linktop.obj.LoadBpBean;
import lib.linktop.sev.HmLoadDataTool;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by LG on 2018/1/18.
 * 血压
 */
public class BpFragment extends MeasureFragment
        implements OnBpResultListener,OnResponseListener ,BtContract.BtView{

    private Bp model;
    private BpTask mBpTask;

    @BindView(R.id.tv_hr)
    TextView mHrTv;//舒张压/收缩压
    @BindView(R.id.btn_starttest)
    Button mClickTestBtn;
    @BindView(R.id.tv_new_dbp_test)
    TextView mNewDbpTest;
//    @BindView(R.id.tv_new_sbp_test)
//    TextView mNewSbpTest;
    @BindView(R.id.tv_old_dbp_test)
    TextView mOldDbpTest;
    @BindView(R.id.tv_old_test)
    TextView mOldText;
    @BindView(R.id.tv_new_test)
    TextView mNewTest;
//    @BindView(R.id.tv_old_sbp_test)
//    TextView mOldSbpTest;
    @BindView(R.id.dial_progress_bar)
    HealthDialProgress mHealthSbpDialProgress;
    @BindView(R.id.tv_dial_sbp)
    TextView mSbpTv;
    @BindView(R.id.pb_sbp)
    ProgressBar mSbpProgressBar;
    @BindView(R.id.tv_bp_chart)
    TextView mBpChart;
    @BindView(R.id.ic_bp_chart)
    ImageView mIvBpChart;

    private final int BPDATA = 0x123;
    private final int HANDLERBP = 0x2018;

    private Context context;

    private ArrayList<String> volume_sections = new ArrayList<String>();
    BtPresenter btPresenter = new BtPresenter(getActivity());
    private boolean isStartMeasure = false;

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BPDATA:
                    DataLifeUtil.startAlarm(getActivity());

                    int sbpValue = model.getSbp();
                    int dbpValue = model.getDbp();

//                    mHrTv.setText(sbpValue + "/" + dbpValue + "(mmHg)");
                    mClickTestBtn.setText(R.string.start);
                    mSbpProgressBar.setVisibility(View.GONE);

                    /*if (sbpValue <= 80 || dbpValue <= 50 || sbpValue < dbpValue){
                        toast("数据不正常,请检查设备是否戴紧");
                        return;
                    }*/


                    mHealthSbpDialProgress.setcolor(DataLifeUtil.getColor(getActivity()));
                    mHealthSbpDialProgress.setValue(getBpResult(sbpValue,dbpValue));
                    mSbpTv.setVisibility(View.VISIBLE);

                    mNewDbpTest.setText(sbpValue+"/"+ dbpValue+ "(mmHg)");

//                    mNewDbpTest.setText(model.getDbp()+"");

                    if (dbpValue < 10 || sbpValue<10){
                        toast("您测量不准确,请准确测量");
                        return;
                    }

//                    mSbpPb.setProgress(model.getSbp());
                    isStartMeasure = false;

                    try{
                        DataLifeUtil.saveBpData(getActivity(),DataLifeUtil.serialize(model));
                    }catch (Exception e){
                        toast("血压没保存");
                    }
                    btPresenter.putBpValue(HealthMonitorActivity.mMemberId + "",HealthMonitorActivity.mMachineBindId,String.valueOf(IDatalifeConstant.BPINT),MonitorInfoFragment.deviceId, DeviceData.getUniqueId(getActivity()),model.getDbp() + "",model.getSbp() + "",model.getHr()+"");

                    break;

            }
        }
    };


    private float getBpResult(int sbpValue,int dbpValue){

        if(90 > sbpValue){
            if (dbpValue < 60){
                mSbpTv.setText(R.string.bp_low);
                return 5f;
            }else if (60<= dbpValue && dbpValue<= 85) {
                mSbpTv.setText(R.string.bp_normal);
                return 15f;
            }else if (dbpValue > 85  && dbpValue<=90){
                mSbpTv.setText(R.string.bp_scarce);
                return 25f;
            }else if (dbpValue > 90  && dbpValue<=100){
                mSbpTv.setText(R.string.bp_little);
                return 35f;
            }else if (dbpValue > 100  && dbpValue<=110){
                mSbpTv.setText(R.string.bp_middle);
                return 45f;
            }else if (dbpValue > 110 ){
                mSbpTv.setText(R.string.bp_very_high);
                return 55f;
            }
        }else if (90 <= sbpValue && sbpValue <= 130){
            if (dbpValue<= 85) {
                mSbpTv.setText(R.string.bp_normal);
                return 15f;
            }else if (dbpValue > 85  && dbpValue<=90){
                mSbpTv.setText(R.string.bp_scarce);
                return 25f;
            }else if (dbpValue > 90  && dbpValue<=100){
                mSbpTv.setText(R.string.bp_little);
                return 35f;
            }else if (dbpValue > 100  && dbpValue<=110){
                mSbpTv.setText(R.string.bp_middle);
                return 45f;
            }else if (dbpValue > 110 ){
                mSbpTv.setText(R.string.bp_very_high);
                return 55f;
            }
        }else if ( 130 < sbpValue && sbpValue <= 140){
            if (dbpValue <= 90){
                mSbpTv.setText(R.string.bp_scarce);
                return 25f;
            }else if (dbpValue > 90  && dbpValue<=100){
                mSbpTv.setText(R.string.bp_little);
                return 35f;
            }else if (dbpValue > 100  && dbpValue<=110){
                mSbpTv.setText(R.string.bp_middle);
                return 45f;
            }else if (dbpValue > 110 ){
                mSbpTv.setText(R.string.bp_very_high);
                return 55f;
            }
        }else if (140 < sbpValue && sbpValue <= 160){
            if (dbpValue <= 100){
                mSbpTv.setText(R.string.bp_little);
                return 35f;
            }else if (dbpValue > 100  && dbpValue<=110){
                mSbpTv.setText(R.string.bp_middle);
                return 45f;
            }else if (dbpValue > 110 ){
                mSbpTv.setText(R.string.bp_very_high);
                return 55f;
            }
        }else if (160 < sbpValue && sbpValue <= 180){
            if(dbpValue <= 110){
                mSbpTv.setText(R.string.bp_middle);
                return 45f;
            } else if (dbpValue > 110 ){
                mSbpTv.setText(R.string.bp_very_high);
                return 55f;
            }
        }else if (180 < sbpValue){
            mSbpTv.setText(R.string.bp_very_high);
            return 55f;
        }

        return 15f;
    }

    private int testColor(float fColor){
            if (fColor == 5f){
                return R.color.color_bp_low;
            }else if(fColor == 15f){
                return R.color.color_bp_normal;
            }else if (fColor == 25f){
                return R.color.color_bp_scarce_high;
            }else if(fColor == 35f){
                return R.color.color_bp_little_high;
            }else if(fColor == 45f){
                return R.color.color_bp_middle_high;
            }else if(fColor == 55f){
                return R.color.color_bp_very_high;
            }
            return R.color.color_bp_normal;
    }

    public BpFragment(){

    }

    @Override
    public boolean startMeasure() {
        if (mBpTask != null) {
            if (mHcService.getBleDevManager().getBatteryTask().getPower() < 20) {
                toast("设备电量过低，请充电\nLow power.Please charge.");
                setStart();
                return false;
            }
            isStartMeasure = true;
            if (mNewDbpTest != null && !mNewDbpTest.getText().toString().equals("-")){
                setOldNumber(mNewDbpTest.getText().toString());
//                mNewSbpTest.setText("-");
                mNewDbpTest.setText("-");
                mHealthSbpDialProgress.setValue(0);
            }
            mBpTask.start();
            mHrTv.setText("");
            mSbpProgressBar.setVisibility(View.VISIBLE);
        } else {
            if (MonitorDataTransmissionManager.getInstance().getBatteryValue() < 20) {
                toast("设备电量过低，请充电\nLow power.Please charge.");
                setStart();
                return false;
            }
            MonitorDataTransmissionManager.getInstance().startMeasure(MeasureType.BP);

            mClickTestBtn.setText("停止测量");

            Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_anim);
                if (rotate != null) {
//                    MIcRoundIv.startAnimation(rotate);
                }  else {
//                    MIcRoundIv.setAnimation(rotate);
//                    MIcRoundIv.startAnimation(rotate);
                }

        }
        return true;
    }

    @Override
    public void stopMeasure() {
        if (mBpTask != null) {
            mBpTask.stop();
            isStartMeasure = false;
            mSbpProgressBar.setVisibility(View.GONE);
        } else {
            MonitorDataTransmissionManager.getInstance().stopMeasure();
            setStart();
        }
    }

    public void setStart(){
        mClickTestBtn.setText("开始测量");
    }

    @Override
    protected void getchange(String str) {
        mClickTestBtn.setText(str);

    }

    @Override
    public void clickUploadData(View v) {
        if (model == null || model.isEmptyData()) {
            toast("不能上传空数据");
            return;
        }
//        HmLoadDataTool.getInstance().uploadData(DataFile.DATA_BP, model);
    }

    @Override
    public String getTitle() {
        return getActivity().getResources().getString(R.string.bp);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mHcService != null) {
            mBpTask = mHcService.getBleDevManager().getBpTask();
            mBpTask.setOnBpResultListener(this);
        } else {
            //设置血压测量回调接口
            MonitorDataTransmissionManager.getInstance().setOnBpResultListener(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        btPresenter.onStop();
    }

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_bp;
    }

    @Override
    protected void initEventAndData() {
        btPresenter.onCreate();
        btPresenter.attachView(this);
        model = new Bp();

        mHealthSbpDialProgress.setcolor(DataLifeUtil.getColor(getActivity()));
    }

    @Override
    public void reset() {
        model.reset();
    }


    @OnClick({R.id.btn_starttest,R.id.ll_more_historyrecord,R.id.tv_bp_chart})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_starttest:
                if (mClickTestBtn.getText().toString().equals(getResources().getString(R.string.back))){
                    mIvBpChart.setVisibility(View.GONE);
                    if (isStartMeasure){
                        mClickTestBtn.setText(R.string.stopmeasure);
                    }else {
                        mClickTestBtn.setText(R.string.start);
                    }
                }else {
                    clickMeasure(view);
                }
                break;
            case R.id.ll_more_historyrecord:
                Bundle bundle = new Bundle();
                bundle.putString(DataLifeUtil.BUNDLEMEMBERID,HealthMonitorActivity.mMemberId);
                bundle.putInt(DataLifeUtil.PAGE,DataLifeUtil.HEALTH_HISTORY_PAGE_BP);
                UIHelper.launcherBundle(getActivity(), RecordActivity.class,bundle);
                break;
            case R.id.tv_bp_chart:

                mIvBpChart.setVisibility(View.VISIBLE);

                mClickTestBtn.setText(R.string.back);

                break;
        }
    }

    @Override
    public void onBpResult(final int systolicPressure, final int diastolicPressure, final int heartRate) {
        Log.e("LG","00000000000000fs" + systolicPressure);
        //测量时间（包括本demo其他测量项目的测量时间），既可以以点击按钮开始测试的那个时间为准，
        // 也可以以测量结果出来时为准，看需求怎么定义
        //这里demo演示，为了方便，采用后者。
        model.setTs(System.currentTimeMillis() / 1000L);
        model.setSbp(systolicPressure);
        model.setDbp(diastolicPressure);
        model.setHr(heartRate);
        myHandler.sendEmptyMessage(BPDATA);
        resetState();
    }

    @Override
    public void onLeakError(int errorType) {
        resetState();
        Observable.just(errorType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer error) {
                        int textId = 0;
                        switch (error) {
                            case 0:
                                mSbpProgressBar.setVisibility(View.GONE);
                                textId = R.string.leak_and_check;
                                setStart();
                                break;
                            case 1:
                                mSbpProgressBar.setVisibility(View.GONE);
                                textId = R.string.measurement_void;
                                setStart();
                                break;
                            default:
                                break;
                        }
                        if (textId != 0)
                            Toast.makeText(getContext(), getString(textId), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void onTouchResponse(int volume) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFail() {

    }

    public void setOldTest(MeasureRecordBean bpMeasureRecordBean){
        if (bpMeasureRecordBean == null){
            mOldDbpTest.setText("-");
//            mOldSbpTest.setText("-");
        }else {
            mOldDbpTest.setText(bpMeasureRecordBean.getCheckValue2() + "/" + bpMeasureRecordBean.getCheckValue1()+"(mmHg)");
//            int intRes = testColor(getBpResult(Integer.valueOf(bpMeasureRecordBean.getCheckValue2()),Integer.valueOf(bpMeasureRecordBean.getCheckValue1())));
//            mOldDbpTest.setTextColor(getResources().getColor(intRes));
//            mOldText.setTextColor(getResources().getColor(intRes));
        }

        mNewDbpTest.setText("-");
//        mNewSbpTest.setText("-");
    }

    public void setOldNumber(String mOldDbp){
        if (mOldDbp.contains("mmHg")){
            mOldDbpTest.setText(mOldDbp);
        }else {
            mOldDbpTest.setText(mOldDbp+"(mmHg)");
        }
//        mOldSbpTest.setText(mOldSbp + "");
    }

}
