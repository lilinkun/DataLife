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
import com.datalife.datalife.interf.OnNavDeleteListener;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/1/29.
 */

public class NavAdapter extends BaseAdapter implements OnNavDeleteListener{

    private Context context;
    private List<FamilyUserInfo> familyUserInfos;
    ViewHolder viewHolder = null;
    ArrayList<ImageView> imageViews = new ArrayList<>();

    public NavAdapter(Context context, List<FamilyUserInfo> familyUserInfos){

        this.context = context;
        this.familyUserInfos = familyUserInfos;

    }

    @Override
    public int getCount() {
        return familyUserInfos.size()+1;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.nav_user_item ,null);

            viewHolder.navUserNameTv = convertView.findViewById(R.id.tv_nav_username);
            viewHolder.navUserNameIv = convertView.findViewById(R.id.iv_nav_user);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(position < familyUserInfos.size()){
            viewHolder.navUserNameTv.setText(familyUserInfos.get(position).getMember_Name());

            DataLifeUtil.GetPIC(context,familyUserInfos.get(position).getMember_Portrait(),viewHolder.navUserNameIv);

            viewHolder.navUserIv = convertView.findViewById(R.id.ic_nav_delete);
            imageViews.add(viewHolder.navUserIv);
            viewHolder.navUserIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    familyUserInfos.remove(position);
                    notifyDataSetChanged();
                }
            });
        }else if (position == familyUserInfos.size()){
            viewHolder.navUserNameIv.setImageResource(R.mipmap.ic_nav_plus);
            viewHolder.navUserNameTv.setVisibility(View.GONE);
        }


        return convertView;
    }

    @Override
    public void onDelete() {
        for (int i = 0;i < imageViews.size() ; i++){
            imageViews.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void onDeleteGone(){
        for (int i = 0;i < imageViews.size() ; i++){
            imageViews.get(i).setVisibility(View.GONE);
        }
    }


    class ViewHolder{

        TextView navUserNameTv;
        RoundImageView navUserNameIv;
        ImageView navUserIv;

    }

}
