package com.datalife.datalife.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.util.DefaultPicEnum;

import java.util.List;

/**
 * Created by LG on 2018/4/11.
 */

public class FamilyRecyclerAdapter extends RecyclerView.Adapter<FamilyRecyclerAdapter.ViewHolder> {

    private List<FamilyUserInfo> familyUserInfos;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_member,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FamilyUserInfo familyUserInfo = familyUserInfos.get(position);

        DefaultPicEnum defaultPicEnum = DefaultPicEnum.getPageByValue(familyUserInfos.get(position).getMember_Portrait());
        String date = familyUserInfos.get(position).getMember_DateOfBirth();
        if (date.length() >= 11 ){
            date = date.substring(0,11);
        }

        holder.imMemberFace.setImageResource(defaultPicEnum.getResPic());
        holder.tvMemberName.setText(familyUserInfos.get(position).getMember_Name());
        holder.tvMemberPhoneNum.setText(date);

    }

    @Override
    public int getItemCount() {
        return familyUserInfos.size();
    }

    public FamilyRecyclerAdapter(List<FamilyUserInfo> familyUserInfos){
        this.familyUserInfos = familyUserInfos;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imMemberFace;
        TextView tvMemberName;
        TextView tvMemberPhoneNum;

        public ViewHolder(View itemView) {
            super(itemView);
            imMemberFace = (ImageView) itemView.findViewById(R.id.iv_member_face);
            tvMemberName = (TextView) itemView.findViewById(R.id.tv_member_name);
            tvMemberPhoneNum = (TextView) itemView.findViewById(R.id.tv_member_phonenum);
        }

    }

}
