package com.datalife.datalife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.util.AllDevEnum;

/**
 * Created by LG on 2018/9/17.
 */

public class ToothDevAdapter extends RecyclerView.Adapter<ToothDevAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private AllDevEnum[] allDevEnums = AllDevEnum.values();
    private OnItemClickListener mItemClickListener;

    public ToothDevAdapter(Context context){
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bluetooth_dev,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.itemView.setTag(position);
            holder.devImage.setImageResource(allDevEnums[position].getSrcImage());
            holder.devText.setText(allDevEnums[position].getTypeName());
    }

    @Override
    public int getItemCount() {
        return allDevEnums.length;
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

        private ImageView devImage;
        private TextView devText;

        public ViewHolder(View itemView) {
            super(itemView);
            devImage = (ImageView) itemView.findViewById(R.id.iv_dev_bg);
            devText = (TextView) itemView.findViewById(R.id.tv_dev_name);
        }
    }


}
