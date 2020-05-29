package com.datalife.datalife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.datalife.datalife.R;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DefaultPicEnum;

import java.util.ArrayList;

/**
 * Created by LG on 2018/2/7.
 */

public class AddMemberAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<String> mArrayList;

    public AddMemberAdapter(Context context,ArrayList<String> list){
        this.mContext = context;
        this.mArrayList = list;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.add_grid_adpter,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_grid);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DefaultPicEnum defaultPicEnum = DefaultPicEnum.getPageByValue(mArrayList.get(position));
        if (defaultPicEnum != null){
            viewHolder.imageView.setImageResource(defaultPicEnum.getResPic());
        }


        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
    }
}
