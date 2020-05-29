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
import com.datalife.datalife.adapter.AllProjectAdapter;
import com.datalife.datalife.adapter.MemberAllAdapter;
import com.datalife.datalife.adapter.MemberItemAdapter;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.util.AllProjectEnum;
import com.datalife.datalife.util.UToast;

import java.util.ArrayList;

/**
 * Created by LG on 2018/8/20.
 */

public class DataTestTitle extends LinearLayout implements View.OnClickListener {

    private LinearLayout commonTitleLayout;
    private LinearLayout mLlMember,mLlTypeName;
    private TextView mMemberName,mTvTypeName;
    private ImageView mIcMember,mIcType;
    private Context mContext;
    private PopupWindow popupWindow;
    private View bottomLayout;
    private Handler handler;
    private FamilyUserInfo familyUserInfo;

    public DataTestTitle(Context context) {
        super(context);
        initView(context);
    }

    public DataTestTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DataTestTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    private void initView(Context context){
        this.mContext = context;
        View  v = LayoutInflater.from(context).inflate(R.layout.datatitle,null);
        commonTitleLayout = (LinearLayout) v.findViewById(R.id.tv_common_title);
        mLlMember = (LinearLayout) v.findViewById(R.id.ll_member_name);
        mLlTypeName = (LinearLayout) v.findViewById(R.id.ll_type_name);
        mMemberName = (TextView) v.findViewById(R.id.tv_member_name);
        mTvTypeName = (TextView) v.findViewById(R.id.tv_type_name);
        mIcMember = (ImageView) v.findViewById(R.id.ic_member_click);
        mIcType = (ImageView) v.findViewById(R.id.ic_type_click);

        mLlMember.setOnClickListener(this);
        mLlTypeName.setOnClickListener(this);

        this.addView(v ,new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ll_type_name:
                View typeView = LayoutInflater.from(mContext).inflate(R.layout.member_popup_listview, null);

                mTvTypeName.setTextColor(getResources().getColor(R.color.bg_toolbar_title));
                mIcType.setImageDrawable(getResources().getDrawable(R.mipmap.ic_click_tip));
                ListView listView1 = (ListView) typeView.findViewById(R.id.lv_member);
                listView1.setVisibility(View.VISIBLE);

                listView1.setAdapter(new AllProjectAdapter(mContext,mTvTypeName.getText().toString()));

                popupWindow = new PopupWindow(typeView,
                        LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setContentView(typeView);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(commonTitleLayout);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        bottomLayout.setBackgroundResource(R.color.transparent);
                        mTvTypeName.setTextColor(mContext.getResources().getColor(R.color.black_text_bg));
                        mIcType.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unclick_tip));
                    }
                });
                bottomLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        mTvTypeName.setText(AllProjectEnum.values()[position].getType());
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("typeid",AllProjectEnum.values()[position].getTypeid());
                        if (mMemberName.getText().toString().equals("全部成员")){
                            bundle.putSerializable("Member",null);
                        }else {
                            bundle.putSerializable("Member",familyUserInfo);
                        }
                        message.setData(bundle);
                        message.what = 0x323;
                        handler.sendMessage(message);

                        popupWindow.dismiss();

                    }
                });
                break;

            case R.id.ll_member_name:
                    View contentView = LayoutInflater.from(mContext).inflate(R.layout.member_popup_listview, null);

                    mMemberName.setTextColor(getResources().getColor(R.color.bg_toolbar_title));
                    mIcMember.setImageDrawable(getResources().getDrawable(R.mipmap.ic_click_tip));
                    ListView listView = (ListView) contentView.findViewById(R.id.lv_member);
                    listView.setVisibility(View.VISIBLE);
                    final ArrayList<FamilyUserInfo> familyUserInfos =  (ArrayList<FamilyUserInfo>) DBManager.getInstance(mContext).queryFamilyUserInfoList();
                    listView.setAdapter(new MemberAllAdapter(mContext,mMemberName.getText().toString(),familyUserInfos));

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
                            mMemberName.setTextColor(mContext.getResources().getColor(R.color.black_text_bg));
                            mIcMember.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unclick_tip));
                        }
                    });
                    bottomLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            familyUserInfo = familyUserInfos.get(position);
                            mMemberName.setText(familyUserInfos.get(position).getMember_Name());
                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Member",familyUserInfos.get(position));
                            if (mTvTypeName.getText().toString().equals("全部项目")){
                                bundle.putInt("typeid",0);
                            }else {
                                bundle.putInt("typeid", AllProjectEnum.getPageByValue(mTvTypeName.getText().toString()).getTypeid());
                            }
                            message.setData(bundle);
                            message.what = 0x322;
                            handler.sendMessage(message);

                            popupWindow.dismiss();

                        }
                    });

                break;
        }

    }


    public void setLayout(View v){
        this.bottomLayout = v;
    }
}
