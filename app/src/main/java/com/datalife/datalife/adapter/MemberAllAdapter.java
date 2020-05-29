package com.datalife.datalife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.dao.MachineBindMemberBean;
import com.datalife.datalife.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/8/20.
 */

public class MemberAllAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<FamilyUserInfo> familyUserInfos = null;
    String mName;

    public MemberAllAdapter(Context context,String name,ArrayList<FamilyUserInfo> familyUserInfos){
        this.mContext = context;
        this.mName = name;
        this.familyUserInfos = familyUserInfos;
    }

    @Override
    public int getCount() {
        return familyUserInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return familyUserInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_member,null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_member_item);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.iv_member_item);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        for(int i = 0;i<familyUserInfos.size();i++){
            if (familyUserInfos.get(i).getMember_Id() == Integer.valueOf(familyUserInfos.get(position).getMember_Id())) {
                if (familyUserInfos.get(i).getMember_Name().equals(mName)){
                    viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.ecg_bg));
                }else {
                    viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.black_text_bg));
                }
                viewHolder.textView.setText(familyUserInfos.get(i).getMember_Name());
//                viewHolder.img.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfos.get(i).getMember_Portrait()).getResPic());
            }
        }

        return convertView;
    }

    class ViewHolder{
        TextView textView;
        ImageView img;
    }

}
