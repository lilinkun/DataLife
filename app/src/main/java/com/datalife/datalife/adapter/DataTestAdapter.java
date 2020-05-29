package com.datalife.datalife.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.DatatestDetailActivity;
import com.datalife.datalife.bean.DataTestBean;
import com.datalife.datalife.bean.TestHistoryBean;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.db.DBManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by LG on 2018/8/20.
 */

public class DataTestAdapter extends RecyclerView.Adapter<DataTestAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<DataTestBean> testHistoryBeans = null;
    private Context mContext;
    private ArrayList<FamilyUserInfo> familyUserInfo ;
    private OnItemClickListener mItemClickListener;


    public DataTestAdapter(Context context,ArrayList<DataTestBean> testHistoryBeans){
        this.mContext = context;
        this.testHistoryBeans = testHistoryBeans;
        this.familyUserInfo = (ArrayList<FamilyUserInfo>)DBManager.getInstance(mContext).queryFamilyUserInfoList();
    }

    public void setData(ArrayList<DataTestBean> testHistoryBeans){
        this.testHistoryBeans = testHistoryBeans;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_datatest,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        String dateStr = testHistoryBeans.get(position).getCreateDate();
        String time =  dateStr.substring(11);

        getProject(testHistoryBeans.get(position).getServer_Id(),holder.mTvType);

        holder.mTvDate.setText(testHistoryBeans.get(position).getCreateDate());
        holder.mTvMessage.setText(testHistoryBeans.get(position).getMessage());

        holder.itemView.setTag(position);

        for (int i = 0 ; i < familyUserInfo.size(); i++){
            if (familyUserInfo.get(i).getMember_Id() == testHistoryBeans.get(position).getMember_Id()){
                holder.mTvName.setText(familyUserInfo.get(i).getMember_Name());
            };
        }

    }

    @Override
    public int getItemCount() {
        return testHistoryBeans.size();
    }


    private void getProject(int serviceId,TextView mType){
        if(serviceId == 211){
            mType.setText(mContext.getString(R.string.bp));
        }else if(serviceId == 214){
            mType.setText(mContext.getString(R.string.bfr_value));
        }else if(serviceId == 213){
            mType.setText(mContext.getString(R.string.my_weight));
        }else if(serviceId == 217){
            mType.setText(mContext.getString(R.string.spo2h_value));
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvName;
        private TextView mTvType;
        private TextView mTvDate;
        private TextView mTvMessage;
        private LinearLayout mDataLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            mTvMessage = itemView.findViewById(R.id.tv_test_message);
            mTvDate = itemView.findViewById(R.id.tv_test_date);
            mTvName = itemView.findViewById(R.id.tv_test_name);
            mTvType = itemView.findViewById(R.id.tv_test_type);
            mDataLayout = itemView.findViewById(R.id.ll_test_datalist);

        }
    }
}
