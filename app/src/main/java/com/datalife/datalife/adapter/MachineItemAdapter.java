package com.datalife.datalife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.dao.MachineBean;

import java.util.ArrayList;

/**
 * Created by LG on 2018/7/12.
 */

public class MachineItemAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MachineBean> machineBeans = null;
    private String mName;

    public MachineItemAdapter(Context context, ArrayList<MachineBean> machineBeans,String name){
        this.mContext = context;
        this.machineBeans = machineBeans;
        this.mName = name;
    }

    @Override
    public int getCount() {
        return machineBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return machineBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_machine_choose,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_machine_name);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (machineBeans.get(position).getMachineName().equals(mName)){
            viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.ecg_bg));
        }else {
            viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.black_text_bg));
        }

        viewHolder.textView.setText(machineBeans.get(position).getMachineName());

        return convertView;
    }

    class ViewHolder{
        TextView textView;
    }
}
