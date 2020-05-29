package com.datalife.datalife.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.MemberItemAdapter;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.MachineBean;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.interf.OnCommonChangeListener;
import com.datalife.datalife.util.DefaultPicEnum;
import com.datalife.datalife.util.UToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/4/28.
 */
public class CommonChangeTitle extends LinearLayout implements View.OnClickListener {
    Context mContext;
    private LinearLayout mMemberLayout;
    private LinearLayout mLlFatHistory;
    private LinearLayout mDevLayout;
    private List<FamilyUserInfo> familyUserInfos = null;
    private PopupWindow popupWindow = null;
    private ArrayList<MachineBindMemberBean> machineBindMemberBean = null;
    private ArrayList<MachineBean> machineBeans = null;
    private OnCommonChangeListener mCommonChangeListener;
    private boolean isbinder = false;

    public CommonChangeTitle(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public CommonChangeTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public CommonChangeTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(){
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_change_title,null);
        mMemberLayout = (LinearLayout) v.findViewById(R.id.ll_member);
        mLlFatHistory = (LinearLayout) v.findViewById(R.id.ll_fat_history);
        mLlFatHistory.setOnClickListener(this);
        mDevLayout = (LinearLayout) v.findViewById(R.id.ll_dev);
        mDevLayout.setOnClickListener(this);
        mMemberLayout.setOnClickListener(this);
        this.addView(v ,new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
    }

    public void setData(List<FamilyUserInfo> familyUserInfos){
        this.familyUserInfos = familyUserInfos;
//        mAccountTv.setText(familyUserInfos.get(0).getMember_Name());
//        mAccountIv.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfos.get(0).getMember_Portrait()).getResPic());
    }

    public void visibleHistory(int visible){
//        mLlFatHistory.setVisibility(visible);
    }

    public void SetOnCommonChangeListener(OnCommonChangeListener commonChangeListener){
        this.mCommonChangeListener=commonChangeListener;
    }

    public void getisBinded(boolean isbind){
        this.isbinder = isbind;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_member:
                UToast.show(mContext,"1111111111");
                break;

            case R.id.ll_dev:
                UToast.show(mContext,"2222222222");
                break;
            case R.id.ll_fat_history:
                UToast.show(mContext,"asdadsads");
                break;
        }
    }

    public void setMemberData(ArrayList<MachineBindMemberBean> machineBindMemberBeans){
        this.machineBindMemberBean = machineBindMemberBeans;
    }

    public void setMachine(ArrayList<MachineBean> machineBeans){
        this.machineBeans = machineBeans;
    }
}
