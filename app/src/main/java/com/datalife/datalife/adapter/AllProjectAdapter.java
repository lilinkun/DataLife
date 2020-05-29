package com.datalife.datalife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.util.AllProjectEnum;

import java.util.ArrayList;

/**
 * Created by LG on 2018/8/21.
 */

public class AllProjectAdapter extends BaseAdapter {
    private Context mContext;
    private String projectName;

    public AllProjectAdapter(Context context,String project){
        this.mContext = context;
        this.projectName = project;
    }

    @Override
    public int getCount() {
        return AllProjectEnum.values().length;
    }

    @Override
    public Object getItem(int position) {
        return AllProjectEnum.values()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       ViewHolder viewHolder = null;

        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_allproject_item,null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_all_project_item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(AllProjectEnum.values()[position].getType());

        if (AllProjectEnum.values()[position].getType().equals(projectName)){
            viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.bg_toolbar_title));
        }else {
            viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.black_text_bg));
        }


        return convertView;
    }

    class ViewHolder{
        TextView textView;
    }

   /* public enum AllProjectEnum {
        TYPE_BP("血压",211),
        TYPE_SPO2H("血氧",217),
        TYPE_BFR("体脂率",214),
        TYPE_WEIGHT("体重",213);

        private String type;
        private int typeid;

        private AllProjectEnum(String type,int typeid){
            this.type = type;
            this.typeid =typeid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getTypeid() {
            return typeid;
        }

        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }
    }*/
}
