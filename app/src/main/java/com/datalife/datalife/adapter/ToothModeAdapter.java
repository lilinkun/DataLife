package com.datalife.datalife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.util.AllToothMode;
import com.datalife.datalife.util.AllToothTime;
import com.datalife.datalife.util.DataLifeUtil;

import java.util.ArrayList;

/**
 * Created by LG on 2018/9/12.
 */

public class ToothModeAdapter extends RecyclerView.Adapter<ToothModeAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private Object[] arrayList;
    private int positionId = 3;
    private OnItemClickListener itemClickListener;
    private int type;

    public ToothModeAdapter(Context context, Object[] str, int positionId,int type){
        this.context = context;
        this.arrayList = str;
        this.positionId = positionId;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_tooth_mode,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.itemView.setTag(position);

        if (type == DataLifeUtil.TOOTH_MODE) {
            holder.modeTv.setText(((AllToothMode)arrayList[position]).getModeName());
        }else if (type == DataLifeUtil.TOOTH_TIME){
            holder.modeTv.setText(((AllToothTime)arrayList[position]).getToothTime()+"分钟");
        }
        if (position == positionId){
            holder.arrawTv.setVisibility(View.VISIBLE);
            holder.modeTv.setTextColor(context.getResources().getColor(R.color.bg_toolbar_title));
        }else {
            holder.arrawTv.setVisibility(View.INVISIBLE);
            holder.modeTv.setTextColor(context.getResources().getColor(R.color.tooth_mode_line));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.length;
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            positionId = (Integer) v.getTag();
            notifyDataSetChanged();
            itemClickListener.onItemClick(positionId,type);
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    public interface OnItemClickListener{
        void onItemClick(int position,int type);
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView modeTv;
        TextView arrawTv;

        public ViewHolder(View itemView){
            super(itemView);
            modeTv = itemView.findViewById(R.id.tv_tooth_mode);
            arrawTv = itemView.findViewById(R.id.iv_tooth_mode);
        }
    }

}
