package com.datalife.datalife.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.datalife.datalife.dao.BrushBean;
import com.datalife.datalife.dao.BrushBeanDao;
import com.datalife.datalife.dao.BrushManyBean;
import com.datalife.datalife.dao.BrushManyBeanDao;
import com.datalife.datalife.dao.DaoMaster;
import com.datalife.datalife.dao.DaoSession;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.FamilyUserInfoDao;
import com.datalife.datalife.dao.FlashListBean;
import com.datalife.datalife.dao.FlashListBeanDao;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.dao.MachineBeanDao;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.dao.MachineBindMemberBeanDao;
import com.datalife.datalife.dao.NewsInfo;
import com.datalife.datalife.dao.NewsInfoDao;
import com.datalife.datalife.dao.Spo2hDao;
import com.datalife.datalife.dao.Spo2hDaoDao;
import com.datalife.datalife.dao.WxUserInfo;
import com.datalife.datalife.dao.WxUserInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by LG on 2018/2/10.
 */

public class DBManager {
    private final static String dbName = "health_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 插入一条记录
     *
     * @param spo2hDao
     */
    public void insertUser(Spo2hDao spo2hDao) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Spo2hDaoDao userDao = daoSession.getSpo2hDaoDao();
        userDao.insert(spo2hDao);
    }

    /**
     * 插入用户集合
     *
     * @param spo2hDao
     */
    public void insertUserList(List<Spo2hDao> spo2hDao) {
        if (spo2hDao == null || spo2hDao.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Spo2hDaoDao userDao = daoSession.getSpo2hDaoDao();
        userDao.insertInTx(spo2hDao);
    }

    /**
     * 删除一条记录
     */
    public void deleteUser(Spo2hDao spo2hDao) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Spo2hDaoDao userDao = daoSession.getSpo2hDaoDao();
        userDao.delete(spo2hDao);
    }

    /**
     * 更新一条记录
     *
     * @param spo2hDao
     */
    public void updateUser(Spo2hDao spo2hDao) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Spo2hDaoDao userDao = daoSession.getSpo2hDaoDao();
        userDao.update(spo2hDao);
    }


    /**
     * 查询用户列表
     */
    public List<Spo2hDao> queryUserList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Spo2hDaoDao userDao = daoSession.getSpo2hDaoDao();
        QueryBuilder<Spo2hDao> qb = userDao.queryBuilder();
        List<Spo2hDao> list = qb.list();
        return list;
    }

    /**
     * 查询用户列表
     */
    public List<Spo2hDao> queryUserList(int age) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Spo2hDaoDao userDao = daoSession.getSpo2hDaoDao();
        QueryBuilder<Spo2hDao> qb = userDao.queryBuilder();
        qb.where(Spo2hDaoDao.Properties.Name.gt(age)).orderAsc(Spo2hDaoDao.Properties.Name);
        List<Spo2hDao> list = qb.list();
        return list;
    }

    /**
     * 插入一条记录
     *
     * @param newsInfo
     */
    public void insertNews(NewsInfo newsInfo) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        NewsInfoDao newsInfoDao = daoSession.getNewsInfoDao();
        newsInfoDao.insert(newsInfo);
    }

    /**
     * 插入用户集合
     *
     * @param newsInfo
     */
    public void insertNewsInfoList(List<NewsInfo> newsInfo) {
        if (newsInfo == null || newsInfo.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        NewsInfoDao newsInfoDao = daoSession.getNewsInfoDao();
        newsInfoDao.insertInTx(newsInfo);
    }

    /**
     * 删除一条记录
     */
    public void deleteNewsInfo(NewsInfo newsInfo) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        NewsInfoDao newsInfoDao = daoSession.getNewsInfoDao();
        newsInfoDao.delete(newsInfo);
    }

    /**
     * 删除所有记录
     */
    public void deleteAllNewsInfo() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        NewsInfoDao newsInfoDao = daoSession.getNewsInfoDao();
        newsInfoDao.deleteAll();
    }

    /**
     * 更新一条记录
     *
     * @param newsInfo
     */
    public void updateNewsInfo(NewsInfo newsInfo)  {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        NewsInfoDao newsInfoDao = daoSession.getNewsInfoDao();
        newsInfoDao.update(newsInfo);
    }

    /**
     * 查询用户列表
     */
    public List<NewsInfo> queryNewsInfoList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        NewsInfoDao userDao = daoSession.getNewsInfoDao();
        QueryBuilder<NewsInfo> qb = userDao.queryBuilder();
        List<NewsInfo> list = qb.list();
        return list;
    }

    /**
     * 查询用户列表
     */
    /*public List<NewsInfo> queryNewsInfoList(int age) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        NewsInfoDao userDao = daoSession.getNewsInfoDao();
        QueryBuilder<NewsInfo> qb = userDao.queryBuilder();
        qb.where(NewsInfoDao.Properties.Name.gt(age)).orderAsc(NewsInfoDao.Properties.Name);
        List<NewsInfo> list = qb.list();
        return list;
    }*/

    /**
     * 插入一条记录
     *
     * @param machineBean
     */
    public void insertMachine(MachineBean machineBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBeanDao userDao = daoSession.getMachineBeanDao();
        userDao.insert(machineBean);
    }

    /**
     * 查询机器列表
     */
    public List<MachineBean> queryMachineBeanList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBeanDao machineBeanDao = daoSession.getMachineBeanDao();
        QueryBuilder<MachineBean> qb = machineBeanDao.queryBuilder();
        List<MachineBean> list = qb.list();
        return list;
    }

    /**
     * 查询机器
     */
    public MachineBean queryMachineBean(String machineSn) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBeanDao machineBeanDao = daoSession.getMachineBeanDao();
        QueryBuilder<MachineBean> qb = machineBeanDao.queryBuilder();
        qb.where(MachineBeanDao.Properties.MachineSn.eq(machineSn)).orderAsc(MachineBeanDao.Properties.MachineSn);
        MachineBean list = qb.unique();
        return list;
    }

    /**
     * 插入一条记录
     *
     * @param machineBean
     */
    public void insertMachineBindMember(MachineBindMemberBean machineBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBindMemberBeanDao userDao = daoSession.getMachineBindMemberBeanDao();
        userDao.insert(machineBean);
    }

    /**
     * 查询用户列表
     */
    public List<MachineBindMemberBean> queryMachineBindMemberBeanList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBindMemberBeanDao machineBindMemberBeanDao = daoSession.getMachineBindMemberBeanDao();
        QueryBuilder<MachineBindMemberBean> qb = machineBindMemberBeanDao.queryBuilder();
        List<MachineBindMemberBean> list = qb.list();
        return list;
    }

    /**
     * 查询用户列表
     */
    public List<MachineBindMemberBean> queryMachineBindMemberBeanList(String bindId) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBindMemberBeanDao machineBindMemberBeanDao = daoSession.getMachineBindMemberBeanDao();
        QueryBuilder<MachineBindMemberBean> qb = machineBindMemberBeanDao.queryBuilder();
        qb.where(MachineBindMemberBeanDao.Properties.MachineBindId.eq(bindId)).orderAsc(MachineBindMemberBeanDao.Properties.MachineBindId);
        List<MachineBindMemberBean> list = qb.list();
        return list;
    }

    /**
     * 删除所有记录
     */
    public void deleteAllMachineBindBean() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBindMemberBeanDao machineBindMemberBeanDao = daoSession.getMachineBindMemberBeanDao();
        machineBindMemberBeanDao.deleteAll();
    }

    /**
     * 查询用户列表
     */
    public List<MachineBean> queryMachineBeanList(String name) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBeanDao machineBeanDao = daoSession.getMachineBeanDao();
        QueryBuilder<MachineBean> qb = machineBeanDao.queryBuilder();
        qb.where(MachineBeanDao.Properties.User_name.eq(name)).orderAsc(MachineBeanDao.Properties.User_name);
        List<MachineBean> list = qb.list();
        return list;
    }

    /**
     * 删除所有记录
     */
    public void deleteAllMachineBean() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBeanDao machineBeanDao = daoSession.getMachineBeanDao();
        machineBeanDao.deleteAll();
    }
    /**
     * 插入一条记录
     *
     * @param familyUserInfo
     */
    public void insertMember(FamilyUserInfo familyUserInfo) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FamilyUserInfoDao familyUserInfoDao = daoSession.getFamilyUserInfoDao();
        familyUserInfoDao.insert(familyUserInfo);
    }

    /**
     * 查询用户列表
     */
    public List<FamilyUserInfo> queryFamilyUserInfoList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FamilyUserInfoDao familyUserInfoDao = daoSession.getFamilyUserInfoDao();
        QueryBuilder<FamilyUserInfo> qb = familyUserInfoDao.queryBuilder();
        List<FamilyUserInfo> list = qb.list();
        return list;
    }


    /**
     * 删除所有记录
     */
    public void deleteAllFamilyUserInfoBean() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FamilyUserInfoDao familyUserInfoDao = daoSession.getFamilyUserInfoDao();
        familyUserInfoDao.deleteAll();
    }

    /**
     * 插入广告
     * @param flashListBean
     */
    public void insertFlash(FlashListBean flashListBean){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FlashListBeanDao flashListBeanDao = daoSession.getFlashListBeanDao();
        flashListBeanDao.insert(flashListBean);
    }

    /**
     * 删除记录
     */
    public void deleteFlash(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FlashListBeanDao flashListBeanDao = daoSession.getFlashListBeanDao();
        flashListBeanDao.deleteAll();
    }

    public List<FlashListBean> queryFlash(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FlashListBeanDao flashListBeanDao = daoSession.getFlashListBeanDao();
        QueryBuilder<FlashListBean> qb = flashListBeanDao.queryBuilder();
        List<FlashListBean> list = qb.list();
        return list;
    }

    /**
     * 插入微信用户
     * @param wxUserInfo
     */
    public void insertWxInfo(WxUserInfo wxUserInfo){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WxUserInfoDao wxUserInfoDao = daoSession.getWxUserInfoDao();
        wxUserInfoDao.insert(wxUserInfo);
    }

    /**
     * 删除记录
     */
    public void deleteWxInfo(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WxUserInfoDao wxUserInfoDao = daoSession.getWxUserInfoDao();
        wxUserInfoDao.deleteAll();
    }

    public WxUserInfo queryWxInfo(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WxUserInfoDao wxUserInfoDao = daoSession.getWxUserInfoDao();
        QueryBuilder<WxUserInfo> qb = wxUserInfoDao.queryBuilder();
        WxUserInfo list = qb.unique();
        return list;
    }

    /**
     * 插入刷牙记录
     */
    public void insertBrushList(BrushBean brushBean){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BrushBeanDao brushBeanDao = daoSession.getBrushBeanDao();
        brushBeanDao.insert(brushBean);
    }

    /**
     * 删除记录
     */
    public void deleteBrushList(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BrushBeanDao brushBeanDao = daoSession.getBrushBeanDao();
        brushBeanDao.deleteAll();
    }

    public List<BrushBean> queryBrushList(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BrushBeanDao brushBeanDao = daoSession.getBrushBeanDao();
        QueryBuilder<BrushBean> qb = brushBeanDao.queryBuilder();
        List<BrushBean> list = qb.list();
        return list;
    }

    public List<BrushBean> queryBrushList(String address){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BrushBeanDao brushBeanDao = daoSession.getBrushBeanDao();
        QueryBuilder<BrushBean> qb = brushBeanDao.queryBuilder();
        qb.where(BrushBeanDao.Properties.Address.eq(address)).orderAsc(BrushBeanDao.Properties.Address);
        List<BrushBean> list = qb.list();
        return list;
    }

    /**
     * 插入刷牙记录
     */
    public void insertBrushManyList(BrushManyBean brushBean){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BrushManyBeanDao brushBeanDao = daoSession.getBrushManyBeanDao();
        brushBeanDao.insert(brushBean);
    }

    /**
     * 删除记录
     */
    public void deleteBrushManyList(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BrushManyBeanDao brushBeanDao = daoSession.getBrushManyBeanDao();
        brushBeanDao.deleteAll();
    }

    public List<BrushManyBean> queryBrushManyList(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BrushManyBeanDao brushBeanDao = daoSession.getBrushManyBeanDao();
        QueryBuilder<BrushManyBean> qb = brushBeanDao.queryBuilder();
        List<BrushManyBean> list = qb.list();
        return list;
    }

   /* public List<BrushManyBean> queryBrushManyList(String address){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BrushManyBeanDao brushBeanDao = daoSession.getBrushManyBeanDao();
        QueryBuilder<BrushManyBean> qb = brushBeanDao.queryBuilder();
        qb.where(BrushManyBeanDao.Properties.Address.eq(address)).orderAsc(BrushBeanDao.Properties.Address);
        List<BrushBean> list = qb.list();
        return list;
    }*/

}