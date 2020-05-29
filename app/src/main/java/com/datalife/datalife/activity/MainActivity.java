package com.datalife.datalife.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.FragmentsAdapter;
import com.datalife.datalife.adapter.NavAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.bean.DownloadBean;
import com.datalife.datalife.bean.MachineBindBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.bean.SimpleBackPage;
import com.datalife.datalife.contract.MainContract;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.fragment.HealthHomeFragment;
import com.datalife.datalife.fragment.HomeEquitFragment;
import com.datalife.datalife.fragment.HomePageFragment;
import com.datalife.datalife.fragment.MallFragment;
import com.datalife.datalife.fragment.MeFragment;
import com.datalife.datalife.interf.OnBackListener;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.presenter.LoginPresenter;
import com.datalife.datalife.presenter.MainPresenter;
import com.datalife.datalife.receiver.NetworkBroadcast;
import com.datalife.datalife.util.AlertDialogBuilder;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.Eyes;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.PermissionManager;
import com.datalife.datalife.util.StatusBarUtil;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.util.UToast;
import com.datalife.datalife.util.UpdateManager;
import com.datalife.datalife.widget.CustomTitleBar;
import com.datalife.datalife.widget.DownloadingDialog;
import com.datalife.datalife.widget.RoundImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.update.BGADownloadProgressEvent;
import cn.bingoogolapple.update.BGAUpgradeUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener,MainContract.MainView,OnBackListener, EasyPermissions.PermissionCallbacks{

    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.tv_me) TextView mMeText;
    @BindView(R.id.tv_homepage) TextView mHomePageText;
    @BindView(R.id.tv_health_home) TextView mHealthHomeText;
    @BindView(R.id.tv_mall) TextView mMallText;
    @BindView(R.id.ic_homepage) ImageView mHomePageImage;
    @BindView(R.id.ic_health_home) ImageView mHealthImage;
    @BindView(R.id.ic_mall) ImageView mMallImage;
    @BindView(R.id.ic_me) ImageView mMeImage;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.gv_nav) GridView mNavGridView;
    @BindView(R.id.ic_nav_subtract) ImageView mNavSubtractIv;
    @BindView(R.id.ic_nav_plus) ImageView mNavPlusIv;
    @BindView(R.id.tv_nav_name)  TextView mNavNameTv;
    @BindView(R.id.iv_account) RoundImageView mHeadAccountIv;

    private final SparseArray<BaseFragment> sparseArray = new SparseArray<>();
    private List<FamilyUserInfo> familyUserInfos = new ArrayList<>();
    public static LoginBean loginBean;
    private NavAdapter navAdapter;

    private DownloadingDialog mDownloadingDialog;
    private String mNewVersion = "2";
    private String mApkUrl = "http://192.168.0.168:81/update/datalife.apk";

    private HomePageFragment mHomePageFragment =  new HomePageFragment();
    private HomeEquitFragment mHomeEquitFragment = new HomeEquitFragment();

    //登陆错误次数
    public static int loginNum = 0;

    private double currentCode = 0;
    private double serviceCode = 0;

    /**
     * 下载文件权限请求码
     */
    private static final int RC_PERMISSION_DOWNLOAD = 1;
    /**
     * 删除文件权限请求码
     */
    private static final int RC_PERMISSION_DELETE = 2;

    private AlertDialog alertDialog = null;

    private MainPresenter mainPresenter = new MainPresenter(this);

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 111:
                    break;

                case 222:
                    break;

                case 8878:

//                    UToast.show(MainActivity.this,"asdasdadas");

                    mHomeEquitFragment.notnet();

                    break;
            }
        }
    };

    @Override
    protected void initEventAndData() {
        mainPresenter.onCreate();
        mainPresenter.attachView(this);
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mToolbar.setNavigationIcon(R.mipmap.ic_homepage_userinfo);
        navigationView.setNavigationItemSelectedListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        NetworkBroadcast localReceiver = new NetworkBroadcast();
        localReceiver.setHandler(handler);
        registerReceiver(localReceiver, intentFilter);

        onPageBind();

        changeTab(0);

        mainPresenter.update(ProApplication.SESSIONID);
        // 监听下载进度
        BGAUpgradeUtil.getDownloadProgressEventObservable()
                .compose(this.<BGADownloadProgressEvent>bindToLifecycle())
                .subscribe(new Action1<BGADownloadProgressEvent>() {
                    @Override
                    public void call(BGADownloadProgressEvent downloadProgressEvent) {
                        if (mDownloadingDialog != null && mDownloadingDialog.isShowing() && downloadProgressEvent.isNotDownloadFinished()) {
                            mDownloadingDialog.setProgress(downloadProgressEvent.getProgress(), downloadProgressEvent.getTotal());
                        }
                    }
                });

        loginBean = DataLifeUtil.getLoginData(this);
        if (loginBean != null){
            mNavNameTv.setText(loginBean.getUser_name());

            if (loginBean.getHeadPic() != null && loginBean.getHeadPic().trim().length() != 0){
                Picasso.with(mHeadAccountIv.getContext()).load(loginBean.getHeadPic()).into(mHeadAccountIv);
            }
        }

        String sessionid = DeviceData.getUniqueId(this);
        ProApplication.SESSIONID = sessionid;

        mainPresenter.getFamilyDataList(sessionid,"50","1");
        mainPresenter.getMachineInfo("1","20",sessionid);

        mNavGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == familyUserInfos.size()){
                    UIHelper.showSimpleBackForResult(MainActivity.this, SimplebackActivity.RESULT_ADDUSER,SimpleBackPage.ADDUSER);
                }
            }
        });
        test();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_nav;
    }

    int i = 0;
    @OnClick({R.id.ll_health_home,R.id.ll_homepage,R.id.ll_mall,R.id.ll_me,R.id.ic_nav_plus,R.id.ic_nav_subtract})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_health_home:
//                Eyes.translucentStatusBar(this);
                changeTab(DataLifeUtil.PAGE_HEALTHHOME);
                mViewPager.setCurrentItem(DataLifeUtil.PAGE_HEALTHHOME,false);
                break;
            case R.id.ll_homepage:
//                Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.bg_toolbar_title));
                changeTab(DataLifeUtil.PAGE_HOMEPAGE);
                mViewPager.setCurrentItem(DataLifeUtil.PAGE_HOMEPAGE,false);
                break;
            case R.id.ll_mall:
//                Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.bg_toolbar_title));
                changeTab(DataLifeUtil.PAGE_MALL);
                mViewPager.setCurrentItem(DataLifeUtil.PAGE_MALL,false);
                break;
            case R.id.ll_me:
//                Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.bg_toolbar_title));
                changeTab(DataLifeUtil.PAGE_ME);
                mViewPager.setCurrentItem(DataLifeUtil.PAGE_ME,false);
                break;
            case R.id.ic_nav_plus:

                UIHelper.showSimpleBackForResult(this, SimplebackActivity.RESULT_ADDUSER,SimpleBackPage.ADDUSER);

                break;

            case R.id.ic_nav_subtract:
                i++;
                if (navAdapter != null){
                    if (i%2 == 1){
                        navAdapter.onDelete();
                        mNavSubtractIv.setImageResource(R.mipmap.ic_change_delete);
                    }else{
                        navAdapter.onDeleteGone();
                        mNavSubtractIv.setImageResource(R.mipmap.ic_nav_subtract);
                    }
                }

                break;
        }
    }


    public void onPageBind() {
        FragmentsAdapter adapter = new FragmentsAdapter(getSupportFragmentManager());
        getMenusFragments();
        adapter.setFragments(sparseArray);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
    }

    private void getMenusFragments() {
        sparseArray.put(DataLifeUtil.PAGE_HOMEPAGE, mHomeEquitFragment);
        sparseArray.put(DataLifeUtil.PAGE_MALL, new MallFragment());
        sparseArray.put(DataLifeUtil.PAGE_HEALTHHOME, new HealthHomeFragment());
        sparseArray.put(DataLifeUtil.PAGE_ME, new MeFragment());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        changeTab(position);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    //改变tab
    private void changeTab(int position){
        switch (position){
            case DataLifeUtil.PAGE_HOMEPAGE:
                mToolbar.setVisibility(View.GONE);
                mHomePageText.setTextColor(getResources().getColor(R.color.blue));
                mHomePageImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_equip_select));
                mMallText.setTextColor(getResources().getColor(R.color.gray));
                mMallImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mall_normal));
                mHealthHomeText.setTextColor(getResources().getColor(R.color.gray));
                mHealthImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_health_normal));
                mMeText.setTextColor(getResources().getColor(R.color.gray));
                mMeImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_me_normal));
                break;

            case DataLifeUtil.PAGE_MALL:
                mToolbar.setVisibility(View.GONE);
                mHomePageText.setTextColor(getResources().getColor(R.color.gray));
                mHomePageImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_equip_unselect));
                mMallText.setTextColor(getResources().getColor(R.color.blue));
                mMallImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mall_actived));
                mHealthHomeText.setTextColor(getResources().getColor(R.color.gray));
                mHealthImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_health_normal));
                mMeText.setTextColor(getResources().getColor(R.color.gray));
                mMeImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_me_normal));
                break;

            case DataLifeUtil.PAGE_HEALTHHOME:
                mToolbar.setVisibility(View.GONE);
                mHomePageText.setTextColor(getResources().getColor(R.color.gray));
                mHomePageImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_equip_unselect));
                mMallText.setTextColor(getResources().getColor(R.color.gray));
                mMallImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mall_normal));
                mHealthHomeText.setTextColor(getResources().getColor(R.color.blue));
                mHealthImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_health_actived));
                mMeText.setTextColor(getResources().getColor(R.color.gray));
                mMeImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_me_normal));
                break;


            case DataLifeUtil.PAGE_ME:
                mToolbar.setVisibility(View.GONE);
                mHomePageText.setTextColor(getResources().getColor(R.color.gray));
                mHomePageImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_equip_unselect));
                mMallText.setTextColor(getResources().getColor(R.color.gray));
                mMallImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mall_normal));
                mHealthHomeText.setTextColor(getResources().getColor(R.color.gray));
                mHealthImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_health_normal));
                mMeText.setTextColor(getResources().getColor(R.color.blue));
                mMeImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_me_actived));

                SharedPreferences sharedPreferences = getSharedPreferences(DataLifeUtil.LOGIN, Context.MODE_PRIVATE);
                if (sharedPreferences.getBoolean(DataLifeUtil.LOGIN,false) == false){

                    if(alertDialog == null) {
                        alertDialog = new AlertDialog.Builder(this).setMessage("您还没有登录，请先登录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, LoginActivity.class);
                                startActivityForResult(intent, IDatalifeConstant.INTENT_LOGIN);
                                finish();
                                alertDialog = null;
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mViewPager.setCurrentItem(0);
                                alertDialog = null;
                            }
                        }).create();

                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }

                    return;
                }

                break;
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        mainPresenter.getMachineInfo("1","20",ProApplication.SESSIONID);
        SharedPreferences sharedPreferences = getSharedPreferences(DataLifeUtil.LOGIN, Context.MODE_PRIVATE);
        String sre = sharedPreferences.getString("account","");
        if (sre == null || sre.equals("")){
            mViewPager.setCurrentItem(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

            if (requestCode == SimplebackActivity.RESULT_ADDUSER){
                FamilyUserInfo navUserInfo = (FamilyUserInfo) data.getSerializableExtra("familyUserInfo");

                DBManager.getInstance(this).insertMember(navUserInfo);
//                mHomePageFragment.getMember();
                mHomeEquitFragment.getMember();

                if (navUserInfo != null){
                    if(navAdapter != null){
                        familyUserInfos.add(navUserInfo);
                        if (navAdapter == null){
                            navAdapter = new NavAdapter(this,familyUserInfos);
                            mNavGridView.setAdapter(navAdapter);
                        }
                        navAdapter.notifyDataSetChanged();
                    }
                }
            }

            if (requestCode == IDatalifeConstant.INTENT_LOGIN){
                mViewPager.setCurrentItem(0);
            }

            if (requestCode == IDatalifeConstant.SETTINGREQUESTCODE){

//                recyclerView.setVisibility(View.INVISIBLE);
                mHomeEquitFragment.setRestartApp();

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
    public void onBackFamilyListDataSuccess(ArrayList<FamilyUserInfo> listResultBean) {
        familyUserInfos = listResultBean;
        DBManager.getInstance(this).deleteAllFamilyUserInfoBean();
        for (int i = 0;i < familyUserInfos.size();i++){
            DBManager.getInstance(this).insertMember(familyUserInfos.get(i));
        }
        mHomeEquitFragment.getMember();

        navAdapter = new NavAdapter(this,familyUserInfos);
        mNavGridView.setAdapter(navAdapter);

    }

    @Override
    public void onBackFamilyListDataFail(String str) {
        Log.e("MainActivity:" , str);
        if (str.equals("登录已失效")){
            SharedPreferences sharedPreferences = getSharedPreferences(DataLifeUtil.LOGIN, Context.MODE_PRIVATE);
            String sre = sharedPreferences.getString("account","");
            if (sharedPreferences != null && sre.trim().length()>0) {
                Intent intent = new Intent();
                intent.putExtra("restart", true);
                intent.setClass(this, LoginActivity.class);
                startActivityForResult(intent, IDatalifeConstant.INTENT_LOGIN);
                finish();
            }
        }
    }

    @Override
    public void onSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews) {
        ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> machineBeans = resultNews;
        DBManager.getInstance(this).deleteAllMachineBean();
        DBManager.getInstance(this).deleteAllMachineBindBean();
        ArrayList<MachineBindMemberBean> machineBindMemberBeans = null;
        for (int i = 0;i<machineBeans.size();i++) {
            try{
                MachineBean machineBean = new MachineBean();
                machineBean.setCreateDate(machineBeans.get(i).getCreateDate());
                machineBean.setMachineBindId(machineBeans.get(i).getMachineBindId());
                machineBean.setMachineId(machineBeans.get(i).getMachineId());
                machineBean.setMachineName(machineBeans.get(i).getMachineName());
                machineBean.setMachineSn(machineBeans.get(i).getMachineSn());
                machineBean.setMachineStatus(machineBeans.get(i).getMachineStatus());
                machineBean.setUser_id(machineBeans.get(i).getUser_id());
                machineBean.setUser_name(machineBeans.get(i).getUser_name());
                DBManager.getInstance(this).insertMachine(machineBean);
                machineBindMemberBeans = machineBeans.get(i).getMachineMemberBind();
                for (int j = 0;j<machineBindMemberBeans.size();j++){
                    DBManager.getInstance(this).insertMachineBindMember(machineBindMemberBeans.get(j));
                }
            }catch (SQLiteConstraintException e){
                toast(e.getMessage());
                Log.e("error:" , e.getMessage());
            }
        }
        mHomeEquitFragment.onGetEquitSuccess();
    }

    @Override
    public void onfail(String str) {
        if (str.contains("登录已失效")){
            SharedPreferences mySharedPreferences = getSharedPreferences(DataLifeUtil.LOGIN, MODE_PRIVATE);
            mySharedPreferences.edit().putString("account","").putBoolean(DataLifeUtil.LOGIN,false).commit();
        }
//        DBManager.getInstance(this).deleteAllMachineBean();
//        DBManager.getInstance(this).deleteAllMachineBindBean();
    }

    @Override
    public void updateSuccess(DownloadBean downloadBean) {
        currentCode = UpdateManager.getInstance().getVersionName(this);
        serviceCode = Double.parseDouble(downloadBean.getVer());
        if (serviceCode > currentCode) {

            mApkUrl = downloadBean.getUrl();

            deleteApkFile();
            downloadApkFile();
        }
    }

    @Override
    public void updateFail(String str) {
        toast(str);
    }

    /**
     * 删除之前升级时下载的老的 apk 文件
     */
    @AfterPermissionGranted(RC_PERMISSION_DELETE)
    public void deleteApkFile() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 删除之前升级时下载的老的 apk 文件
            BGAUpgradeUtil.deleteOldApk();
        } else {
            EasyPermissions.requestPermissions(this, "使用 BGAUpdateDemo 需要授权读写外部存储权限!", RC_PERMISSION_DELETE, perms);
        }
    }

    /**
     * 下载新版 apk 文件
     */
    @AfterPermissionGranted(RC_PERMISSION_DOWNLOAD)
    public void downloadApkFile() {
        if (serviceCode <= currentCode) {
            return;
        }

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 如果新版 apk 文件已经下载过了，直接 return，此时不需要开发者调用安装 apk 文件的方法，在 isApkFileDownloaded 里已经调用了安装」
            if (BGAUpgradeUtil.isApkFileDownloaded(mNewVersion)) {
                return;
            }

            // 下载新版 apk 文件
            BGAUpgradeUtil.downloadApkFile(mApkUrl, mNewVersion)
                    .subscribe(new Subscriber<File>() {
                        @Override
                        public void onStart() {
                            showDownloadingDialog();
                        }

                        @Override
                        public void onCompleted() {
                            dismissDownloadingDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissDownloadingDialog();
                        }

                        @Override
                        public void onNext(File apkFile) {
                            if (apkFile != null) {
                                BGAUpgradeUtil.installApk(apkFile);
                            }
                        }
                    });
        } else {
            EasyPermissions.requestPermissions(this, "使用 BGAUpdateDemo 需要授权读写外部存储权限!", RC_PERMISSION_DOWNLOAD, perms);
        }
    }

    /**
     * 显示下载对话框
     */
    private void showDownloadingDialog() {
        if (mDownloadingDialog == null) {
            mDownloadingDialog = new DownloadingDialog(this);
        }
        mDownloadingDialog.show();
    }

    /**
     * 隐藏下载对话框
     */
    private void dismissDownloadingDialog() {
        if (mDownloadingDialog != null) {
            mDownloadingDialog.dismiss();
        }
    }

    @Override
    public void onBack() {
        mViewPager.setCurrentItem(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            exitByDoubleClick();
        }

        return false;
    }

    boolean isExit = false;
    private void exitByDoubleClick() {
        Timer tExit=null;
        if(!isExit){
            isExit=true;
            toast("再按一次退出程序");
            tExit=new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit=false;//取消退出
                }
            },2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        }else{
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
    }


    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    //动态申请权限的测试方法
    public void test() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                    showToast("自Android 6.0开始需要打开位置权限才可以搜索到Ble设备");
                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            }
        }

        if (ProApplication.isUseCustomBleDevService) {
            final boolean isObtain = PermissionManager.isObtain(this, PermissionManager.PERMISSION_LOCATION, PermissionManager.requestCode_location);
            if (!isObtain) {
                return;
            } else {
                if (!PermissionManager.canScanBluetoothDevice(this)) {
                    new AlertDialogBuilder(this)
                            .setTitle("提示")
                            .setMessage("Android 6.0及以上系统需要打开GPS才能扫描蓝牙设备。")
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton("打开GPS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PermissionManager.openGPS(MainActivity.this);
                                }
                            }).create().show();
                    return;
                }
            }
        }
    }

}
