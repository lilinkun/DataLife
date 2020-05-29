package com.datalife.datalife.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.FatActivity;
import com.datalife.datalife.activity.SimplebackActivity;
import com.datalife.datalife.adapter.MachineItemAdapter;
import com.datalife.datalife.adapter.MemberItemAdapter;
import com.datalife.datalife.bean.SimpleBackPage;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.util.UToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/7/19.
 */

public class CommonTitle extends LinearLayout implements View.OnClickListener{

    private RelativeLayout mRlMyDev;
    private RelativeLayout mRlDevMember;
    private LinearLayout commonTitleLayout;
    private TextView mTvMyDev;
    private TextView mTvDevMember;
    private ImageView mIcMachine;
    private ImageView mIcMember;

    private PopupWindow popupWindow;
    private Context mContext;
    private ArrayList<MachineBindMemberBean> machineBindMemberBeans = null;
    private ArrayList<MachineBean> machineBeans = null;
    private LinearLayout bottomLayout;
    private Handler myHandler;
    public List<FamilyUserInfo> familyUserInfos= null;

    public CommonTitle(Context context) {
        super(context);
        initView(context);
    }

    public CommonTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CommonTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        familyUserInfos = DBManager.getInstance(context).queryFamilyUserInfoList();
        this.mContext = context;

        View v = LayoutInflater.from(mContext).inflate(R.layout.commom_title,null);
        commonTitleLayout = (LinearLayout) v.findViewById(R.id.tv_common_title);
        mRlMyDev = (RelativeLayout) v.findViewById(R.id.rl_my_dev);
        mRlDevMember = (RelativeLayout) v.findViewById(R.id.rl_dev_member);
        mTvMyDev = (TextView) v.findViewById(R.id.tv_my_dev);
        mTvDevMember = (TextView) v.findViewById(R.id.tv_my_dev_member);
        mIcMember = (ImageView) v.findViewById(R.id.ic_member_click);
        mIcMachine = (ImageView) v.findViewById(R.id.ic_machine_click);
        mRlDevMember.setOnClickListener(this);
        commonTitleLayout.setOnClickListener(this);
        mRlMyDev.setOnClickListener(this);
        this.addView(v ,new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        machineBean();

        if (machineBeans != null  && machineBeans.size() > 0) {
            setDevMemberName(machineBeans.get(0));
        }
    }

    private void machineBean(){
        machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(mContext).queryMachineBeanList();
        ArrayList<MachineBean> machineBeans1 = new ArrayList<>();
        for (MachineBean machineBean : machineBeans){
            if (machineBean.getMachineName().startsWith("SWAN") || machineBean.getMachineName().startsWith("SWAN")){
                machineBeans1.add(machineBean);
            }
        }
        machineBeans = machineBeans1;
    }

    public void setDevMemberName(MachineBean devMemberName){
            mTvMyDev.setText(devMemberName.getMachineName());
            machineBindMemberBeans = (ArrayList<MachineBindMemberBean>) DBManager.getInstance(mContext).queryMachineBindMemberBeanList(devMemberName.getMachineBindId());
            if (machineBindMemberBeans != null && machineBindMemberBeans.size() != 0) {
                for (FamilyUserInfo familyUserInfo : familyUserInfos) {
                    if (String.valueOf(familyUserInfo.getMember_Id()).equals(machineBindMemberBeans.get(0).getMember_Id())) {
                        mTvDevMember.setText(familyUserInfo.getMember_Name());
                        changeMember(machineBindMemberBeans.get(0));

                    }
                }
            }else if (machineBindMemberBeans!=null && machineBindMemberBeans.size() == 0){

//                Bundle bundle = new Bundle();
//                bundle.putSerializable("machine", machineBeans.get(0));
//                UIHelper.showSimpleBackBundleForResult((FatActivity)mContext, SimplebackActivity.RESULT_BINDMEMBER, SimpleBackPage.BINDMEMBER,bundle);
            }

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.rl_my_dev:
                if (machineBindMemberBeans != null){
                    View contentView = LayoutInflater.from(mContext).inflate(R.layout.member_popup_listview, null);

                    machineBean();

                    mTvMyDev.setTextColor(getResources().getColor(R.color.bg_toolbar_title));
                    mIcMachine.setImageDrawable(getResources().getDrawable(R.mipmap.ic_click_tip));
                    ListView listView = (ListView) contentView.findViewById(R.id.lv_member);
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(new MachineItemAdapter(mContext,machineBeans,mTvMyDev.getText().toString()));

                    popupWindow = new PopupWindow(contentView,
                            LinearLayout.LayoutParams.MATCH_PARENT,  LayoutParams.WRAP_CONTENT, true);
                    popupWindow.setContentView(contentView);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.showAsDropDown(commonTitleLayout);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            bottomLayout.setBackgroundResource(R.color.transparent);
                            mTvMyDev.setTextColor(Color.WHITE);
                            mIcMachine.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unclick_tip));
                        }
                    });
                    bottomLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            mMachineId = machineBeans.get(position).getMachineId();
//                            mTvMyDev.setText(machineBeans.get(position).getMachineSn());
//                            popupWindow.dismiss();
//                            onDataListener.onMachine(machineBeans.get(position));
                            if (mTvMyDev.getText().toString().equals(machineBeans.get(position).getMachineName())){
                                popupWindow.dismiss();
                                return;
                            }

                            mTvMyDev.setText(machineBeans.get(position).getMachineName());

                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("machineid",machineBeans.get(position).getMachineBindId());
                            bundle.putSerializable("machine",machineBeans.get(position));
                            message.setData(bundle);
                            message.what = DataLifeUtil.COMMOMHANDLERMACHINE;
                            myHandler.sendMessage(message);
                            popupWindow.dismiss();
                        }
                    });
                }else {
                    UToast.show(mContext,"暂未连接设备");
                }
                break;

            case R.id.rl_dev_member:
                if (machineBindMemberBeans != null){
                    View contentView = LayoutInflater.from(mContext).inflate(R.layout.member_popup_listview, null);

                    mTvDevMember.setTextColor(getResources().getColor(R.color.bg_toolbar_title));
                    mIcMember.setImageDrawable(getResources().getDrawable(R.mipmap.ic_click_tip));
                    ListView listView = (ListView) contentView.findViewById(R.id.lv_member);
                    listView.setVisibility(View.VISIBLE);
                    MemberItemAdapter memberItemAdapter = new MemberItemAdapter(mContext,machineBindMemberBeans,mTvDevMember.getText().toString());
                    listView.setAdapter(memberItemAdapter);

                    popupWindow = new PopupWindow(contentView,
                            LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    popupWindow.setContentView(contentView);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.showAsDropDown(commonTitleLayout);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            bottomLayout.setBackgroundResource(R.color.transparent);
                            mTvDevMember.setTextColor(Color.WHITE);
                            mIcMember.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unclick_tip));
                        }
                    });
                    bottomLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            mMemberId = String.valueOf(machineBindMemberBean.get(position).getMember_Id());
//                            getMemberId(machineBindMemberBean.get(position));
                            MachineBindMemberBean machineBindMember = machineBindMemberBeans.get(position);
                            for(int i = 0;i<familyUserInfos.size();i++){
                                if(familyUserInfos.get(i).getMember_Id() == Integer.valueOf(machineBindMember.getMember_Id())){
                                    mTvDevMember.setText(familyUserInfos.get(i).getMember_Name());
                                }
                            }
                            changeMember(machineBindMember);

                            popupWindow.dismiss();

                        }
                    });
                }else {
                    UToast.show(mContext,"暂未连接设备");
                }
                break;
        }
    }

//    public void setData(ArrayList<MachineBindMemberBean> machineBindMemberBeans,ArrayList<MachineBean> machineBeans){
//        this.machineBindMemberBeans = machineBindMemberBeans;
//        this.machineBeans = machineBeans;
//    }

    public void setLayout(View bottomLayout){
        this.bottomLayout = (LinearLayout) bottomLayout;
    }

    public void setHandler(Handler handler){
        this.myHandler = handler;
    }

    public void setDevName(MachineBean devName){
        mTvDevMember.setText(R.string.my_member);
        mTvMyDev.setText(devName.getMachineName());
        setDevMemberName(devName);
    }

    //设备绑定成员变化
    public void changeMember(MachineBindMemberBean machineBindMemberBean){
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("memberid",machineBindMemberBean.getMember_Id());
        bundle.putSerializable("machinemember",machineBindMemberBean);
        message.setData(bundle);
        message.what = DataLifeUtil.COMMOMHANDLERMEMBER;
        if (myHandler != null) {
            myHandler.sendMessage(message);
        }
    }
}
