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
import com.datalife.datalife.util.DefaultPicEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/2/7.
 */

public class MemberItemAdapter extends BaseAdapter {

    Context mContext;
    List<MachineBindMemberBean> arrayList = new ArrayList<>();
    ArrayList<FamilyUserInfo> familyUserInfos = null;
    String mName;

    public MemberItemAdapter(Context context,List<MachineBindMemberBean> list,String name){
        this.mContext = context;
        this.arrayList = list;
        this.mName = name;
        familyUserInfos =  (ArrayList<FamilyUserInfo>)DBManager.getInstance(context).queryFamilyUserInfoList();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(R.layout.listview_member,null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_member_item);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.iv_member_item);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        for(int i = 0;i<familyUserInfos.size();i++){
            if (familyUserInfos.get(i).getMember_Id() == Integer.valueOf(arrayList.get(position).getMember_Id())) {
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
