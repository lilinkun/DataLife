package com.datalife.datalife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.bean.GeneBean;
import com.datalife.datalife.widget.RoundImageView;

import java.util.ArrayList;

/**
 * Created by LG on 2018/3/27.
 */

public class ReportAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<GeneBean> geneBeans = null;

    public ReportAdapter(Context context,ArrayList<GeneBean> geneBeans){
        this.mContext = context;
        this.geneBeans = geneBeans;
    }


    @Override
    public int getCount() {
        return geneBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return geneBeans.get(position);
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

            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_report,null);

            viewHolder.nameTv = convertView.findViewById(R.id.tv_name);
            viewHolder.dateTv = convertView.findViewById(R.id.tv_date);
            viewHolder.reportIdTv = convertView.findViewById(R.id.tv_report_id);
            viewHolder.reportMobileTv = convertView.findViewById(R.id.tv_report_mobile);
            viewHolder.mealnameTv = convertView.findViewById(R.id.tv_report_mealname);
            viewHolder.reportAddressTv = convertView.findViewById(R.id.tv_report_address);
            viewHolder.reportStatusTv = convertView.findViewById(R.id.tv_report_status);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameTv.setText(geneBeans.get(position).getName());
        viewHolder.dateTv.setText(geneBeans.get(position).getCreateDate() + "");
        viewHolder.reportIdTv.setText(geneBeans.get(position).getGCId() + "");
        viewHolder.reportMobileTv.setText(geneBeans.get(position).getMobile() + "");
        viewHolder.mealnameTv.setText(geneBeans.get(position).getSetMealName() + "");
        viewHolder.reportAddressTv.setText(geneBeans.get(position).getAddress() + "");
        viewHolder.reportStatusTv.setText(geneBeans.get(position).getGCStatusName() + "");

        return convertView;
    }

    class ViewHolder{
        TextView nameTv;
        TextView dateTv;
        TextView reportIdTv;
        TextView mealnameTv;
        TextView reportMobileTv;
        TextView reportAddressTv;
        TextView reportStatusTv;
    }
}
