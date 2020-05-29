package com.datalife.datalife.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.util.DefaultPicEnum;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/7/11.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder>{

    private Context context;
    private ArrayList<MachineBindMemberBean> machineBindMemberBeans = null;
    private ArrayList<FamilyUserInfo> familyUserInfos = null;
    private int mNowPosition = 0;
    private OnItemClickListener mOnItemClickListener;//声明接口

    public MemberAdapter(Context context,ArrayList<MachineBindMemberBean> machineBindMemberBeans){
        this.context = context;
        this.machineBindMemberBeans = machineBindMemberBeans;
        familyUserInfos =  (ArrayList<FamilyUserInfo>) DBManager.getInstance(context).queryFamilyUserInfoList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_member_test,parent,false);
        int width = IDatalifeConstant.display(context).getWidth();

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_adapter_member);
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.width = width/5;
        linearLayout.setLayoutParams(layoutParams);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        for(int i = 0;i<familyUserInfos.size();i++){
            if (familyUserInfos.get(i).getMember_Id() == Integer.valueOf(machineBindMemberBeans.get(position).getMember_Id())) {
                holder.mTextView.setText(familyUserInfos.get(i).getMember_Name());
                holder.mRoundImageView.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfos.get(i).getMember_Portrait()).getResPic());
            }
        }

        if (position == mNowPosition){
            holder.mTextView.setTextColor(context.getResources().getColor(R.color.black_text_bg));
            holder.mBgRoundImageView.setVisibility(View.GONE);
//            holder.mRoundImageView.getBackground().setAlpha(100);
        }else {
            holder.mTextView.setTextColor(Color.GRAY);
            holder.mBgRoundImageView.setVisibility(View.VISIBLE);
//            holder.mRoundImageView.getBackground().setAlpha(60);
        }

    }

    @Override
    public int getItemCount() {
        return machineBindMemberBeans.size();
    }

    public void setClick(int position){
            this.mNowPosition = position;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onLongClick(View view, int posotion);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        RoundImageView mRoundImageView;
        TextView mTextView;
        RoundImageView mBgRoundImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mRoundImageView = itemView.findViewById(R.id.iv_member_face);
            mTextView = itemView.findViewById(R.id.tv_name);
            mBgRoundImageView = itemView.findViewById(R.id.iv_member_bg);
        }
    }
}
