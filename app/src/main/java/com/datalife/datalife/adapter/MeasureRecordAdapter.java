package com.datalife.datalife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.RecordActivity;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.dao.Spo2hDao;
import com.datalife.datalife.util.IDatalifeConstant;

import java.util.ArrayList;

/**
 * Created by LG on 2018/2/9.
 */

public class MeasureRecordAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MeasureRecordBean> measureRecordBeans ;
    private int measureType = 0;

    public MeasureRecordAdapter(Context context, ArrayList<MeasureRecordBean> measureRecordBeans,int measuretype){
            this.mContext = context;
            this.measureRecordBeans = measureRecordBeans;
            this.measureType = measuretype;
    }

    public void setData(ArrayList<MeasureRecordBean> measureRecordBeans){
        this.measureRecordBeans = measureRecordBeans;
    }

    @Override
    public int getCount() {
        return measureRecordBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return measureRecordBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.spo2h_record_adapter,null);

            viewHolder = new ViewHolder();

            viewHolder.mFaceImg = (ImageView) convertView.findViewById(R.id.ic_spo2h);

            viewHolder.mTimeTesting = (TextView) convertView.findViewById(R.id.tv_testing_time);

            viewHolder.mWeightTv = (TextView) convertView.findViewById(R.id.tv_weight_value);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTimeTesting.setText(measureRecordBeans.get(position).getCreateDate());
        if (measureType == RecordActivity.BPRECORD){
            viewHolder.mWeightTv.setText(measureRecordBeans.get(position).getCheckValue1()+"/"+measureRecordBeans.get(position).getCheckValue2());
        }else if (measureType == RecordActivity.HRRECORD){
            viewHolder.mWeightTv.setText(measureRecordBeans.get(position).getCheckValue3());
        }else{
            viewHolder.mWeightTv.setText(measureRecordBeans.get(position).getCheckValue1());
        }

        return convertView;
    }

    class ViewHolder{
        ImageView mFaceImg;
        TextView mTimeTesting;
        TextView mWeightTv;

    }
}
