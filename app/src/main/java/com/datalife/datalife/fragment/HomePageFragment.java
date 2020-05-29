package com.datalife.datalife.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.DataTestActivity;
import com.datalife.datalife.activity.FatActivity;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.activity.LoginActivity;
import com.datalife.datalife.activity.MainChangeActivity;
import com.datalife.datalife.activity.SimplebackActivity;
import com.datalife.datalife.activity.ToothActivity;
import com.datalife.datalife.activity.WebViewActivity;
import com.datalife.datalife.adapter.HealthNewsAdapter;
import com.datalife.datalife.adapter.InstrumentTestAdapter;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.bean.BannerBean;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.dao.FlashListBean;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.dao.NewsInfo;
import com.datalife.datalife.bean.SimpleBackPage;
import com.datalife.datalife.contract.HomePageContract;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.presenter.HomePagePresenter;
import com.datalife.datalife.bean.TestEnum;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.widget.BannerM;
import com.datalife.datalife.widget.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by LG on 2018/1/18.
 */

public class HomePageFragment extends BaseFragment implements HomePageContract.HomePageView, AdapterView.OnItemClickListener{


    @BindView(R.id.ic_health_news)
    ListViewForScrollView mNewsLv;
    @BindView(R.id.ll_more_layout)
    LinearLayout mMoreLayout;
    @BindView(R.id.banner)
    BannerM banner;
    @BindView(R.id.recycler_test)
    RecyclerView mRecyclerViewTest;
    @BindView(R.id.tv_test_details)
    TextView mTvDetails;

    int mIndex = 1;
    ArrayList<NewsInfo> newsInfos = new ArrayList<>();
    HealthNewsAdapter healthNewsAdapter = null;
    HomePagePresenter homePagePresenter = new HomePagePresenter(getActivity());
    public List<FamilyUserInfo> familyUserInfos= null;
    public static final int TESTHANDLER = 0x112233;

    DBManager dbManager = null;
    Activity activity = null;

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_homepage;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TESTHANDLER:

                    int position = msg.getData().getInt("position");

                    switch (position){
                        case 0:
                            fatTest();
                            break;

                        case 1:
                            healthTest();
                            break;

                        case 2:

                            List<MachineBean> machineBeans = dbManager.queryMachineBeanList();

                            for (MachineBean machineBean : machineBeans){
                                if (machineBean.getMachineName().startsWith("ZSONIC")) {
                                    toothTest();
                                }
                            }

                            break;
                    }

                    break;
            }
        }
    };

    @Override
    protected void initEventAndData() {

        activity = getActivity();
        homePagePresenter.onCreate();
        homePagePresenter.attachView(this);
        dbManager = DBManager.getInstance(getActivity());
        familyUserInfos = dbManager.queryFamilyUserInfoList();
        if ( healthNewsAdapter != null){
            healthNewsAdapter = new HealthNewsAdapter(getActivity(), newsInfos);
            mNewsLv.setAdapter(healthNewsAdapter);
        }else{
            newsInfos = (ArrayList<NewsInfo>)dbManager.queryNewsInfoList();
            if(newsInfos != null && newsInfos.size() != 0) {
                healthNewsAdapter = new HealthNewsAdapter(getActivity(), newsInfos);
                mNewsLv.setAdapter(healthNewsAdapter);
            }
            homePagePresenter.getNewsInfo(mIndex+"","15");
        }
        mNewsLv.setOnItemClickListener(this);
        homePagePresenter.onFlashPage();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewTest.setLayoutManager(linearLayoutManager);
        InstrumentTestAdapter instrumentTestAdapter = new InstrumentTestAdapter(getActivity(), TestEnum.values(),handler);
        mRecyclerViewTest.setAdapter(instrumentTestAdapter);
    }

    @OnClick({R.id.ll_more_layout,R.id.tv_test_details})
    public void onClick(View v){
        switch (v.getId()){

            case R.id.ll_more_layout:
                mIndex++;
                homePagePresenter.getNewsInfo(mIndex+"","6");
                break;

            case R.id.tv_test_details:

                UIHelper.launcherForResult(getActivity(), DataTestActivity.class, 1231);

                break;
        }
    }

    //点击健康监测仪
    private void healthTest(){
        onJumpTest(HealthMonitorActivity.class, 111);
    }


    //点击体脂称
    private void fatTest(){
        onJumpTest( FatActivity.class, 121);
    }


    private void onJumpTest(Class<?> targetActivity,int requestCode){
        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(DataLifeUtil.LOGIN, MODE_PRIVATE);
        if (mySharedPreferences.getBoolean(DataLifeUtil.LOGIN,false) == false){
            Intent intent = new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            return;
        }

        if (familyUserInfos == null || familyUserInfos.size() == 0) {
            // 创建构建器
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.no_add_user)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            UIHelper.showSimpleBackForResult(getActivity(), SimplebackActivity.RESULT_ADDUSER, SimpleBackPage.ADDUSER);
                        }
                    }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,
                                    int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }else {
            UIHelper.launcherForResult(getActivity(), targetActivity, requestCode);
        }
    }

    private void toothTest(){
        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(DataLifeUtil.LOGIN, MODE_PRIVATE);
        if (mySharedPreferences.getBoolean(DataLifeUtil.LOGIN,false) == false){
            Intent intent = new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            return;
        }

        if (familyUserInfos == null || familyUserInfos.size() == 0) {
            // 创建构建器
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.no_add_user)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            UIHelper.showSimpleBackForResult(getActivity(), SimplebackActivity.RESULT_ADDUSER, SimpleBackPage.ADDUSER);
                        }
                    }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,
                                    int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }else {
            UIHelper.launcherForResult(getActivity(), ToothActivity.class, 131);
        }
    }

    /*
     * 获取圆角图片
     */
    private void rectRoundBitmap(ImageView imageView,int res){
        //得到资源文件的BitMap
        Bitmap image= BitmapFactory.decodeResource(getResources(),res);
        //创建RoundedBitmapDrawable对象
        RoundedBitmapDrawable roundImg = RoundedBitmapDrawableFactory.create(getResources(),image);
        //抗锯齿
        roundImg.setAntiAlias(true);
        //设置圆角半径
        roundImg.setCornerRadius(15);
        //设置显示图片
        imageView.setImageDrawable(roundImg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 111){
            }
        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void onSuccess(ArrayList<NewsInfo> resultNews) {

        if (mIndex != 1){

            if(newsInfos.get(0).getAdd_time().equals(resultNews.get(0).getAdd_time())){
                return;
            }

            for(int i = 0;i < resultNews.size();i++){
                newsInfos.add(resultNews.get(i));
            }
            healthNewsAdapter.notifyDataSetChanged();
        }else {

            if(newsInfos == null || newsInfos.size() == 0){
                newsInfos = resultNews;
                healthNewsAdapter = new HealthNewsAdapter(getActivity(), newsInfos);
                mNewsLv.setAdapter(healthNewsAdapter);
            }else{
                newsInfos = resultNews;
                healthNewsAdapter.notifyDataSetChanged();
            }

        }
        dbManager.deleteAllNewsInfo();
        dbManager.insertNewsInfoList(newsInfos);

    }

    public void getMember(){
        familyUserInfos = dbManager.queryFamilyUserInfoList();
    }

    @Override
    public void onfail(String str) {
//        toast(str + "");
    }

    @Override
    public void onFlashSuccess(ArrayList<FlashListBean> flashListBeans) {

        //放图片地址的集合
        ArrayList<BannerBean> list_path = new ArrayList<>();
        for(int i = 0;i<flashListBeans.size();i++){
            BannerBean banner = new BannerBean(flashListBeans.get(i).getF_FlashName(), flashListBeans.get(i).getF_FlashPic(), "");
            list_path.add(banner);
//            list_title.add(flashListBeans.get(i).getF_FlashName());
        }
        if (banner != null) {
            banner.setBannerBeanList(list_path)
                    .setDefaultImageResId(R.mipmap.ic_health_man)
                    .setIndexPosition(BannerM.INDEX_POSITION_BOTTOM)
                    .setIndexColor(activity.getResources().getColor(R.color.colorPrimary))
                    .setIntervalTime(3)
                    .setOnItemClickListener(new BannerM.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Log.e("LG", "position = " + position);

                            UIHelper.launcher(getActivity(), MainChangeActivity.class);


                        }
                    }).show();
        }
    }

    @Override
    public void onFlashFail(String str) {
        List<FlashListBean> flashListBeans = dbManager.queryFlash();
        if (flashListBeans.size()>0){
            ArrayList<FlashListBean> flashList = new ArrayList<>();
            for(int i = 0; i<flashListBeans.size();i++){
                flashList.add(flashListBeans.get(i));
            }
            onFlashSuccess(flashList);
        }
    }

    @Override
    public void onEquipSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews) {

    }

    @Override
    public void onEquipFail(String msg) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       String str =  newsInfos.get(position).getLink();

        Intent intent = new Intent();
        intent.setClass(getActivity(),WebViewActivity.class);
        intent.putExtra("url",str);
        intent.putExtra("type","news");
        startActivity(intent);
    }


}
