package com.datalife.datalife.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.datalife.datalife.R;
import com.datalife.datalife.bean.ToothTestBean;
import com.datalife.datalife.dao.BrushBean;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.widget.ChartView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LG on 2018/6/19.
 */

public class MyChartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<BrushBean> brushBeans ;
    private ArrayList<ChartView> chartViews = new ArrayList<>();
    private int ClickId = -1;
    private OnItemClickListener mItemClickListener;
    private int currentId = 0;
    private int startid = 1;
    private int endId= 0;

    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_END = -1;

    private LayoutInflater mInflater = null;

    public MyChartAdapter(Context mContext , List<BrushBean> brushBeans){
        this.mContext = mContext;
        this.brushBeans = brushBeans;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setEndId(int endId){
        this.endId = endId;
    }

    public void setBrush(List<BrushBean> brush,int endId){
        this.endId = endId;
        this.brushBeans = brush;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;

        if (ITEM_TYPE_HEADER == viewType || ITEM_TYPE_END == viewType){
            view = mInflater.inflate(R.layout.recycler_head, parent, false);
            viewHolder = new HeaderViewHolder(view);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.lv_mychart,parent,false);
            viewHolder = new MyChartAdapter.ViewHolder(view);
            view.setOnClickListener(this);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ViewHolder) {
            position--;
            MyChartAdapter.ViewHolder holder = (MyChartAdapter.ViewHolder) viewHolder;
            holder.itemView.setTag(position);
            if (position <= brushBeans.size()) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM月dd");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                Date testDate = new Date();
                try {
                    testDate = simpleDateFormat.parse(brushBeans.get(position).getStartTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                holder.chartView.setList(IDatalifeConstant.getCalculation(mContext, brushBeans.get(position)), position, simpleDateFormat1.format(testDate), simpleDateFormat2.format(testDate));

                if (chartViews.size() <= position) {
                    chartViews.add(holder.chartView);
                }

                if (startid == 1) {
                    chartViews.get(0).setColor(mContext.getResources().getColor(R.color.tooth_yellow));
                    startid = 0;
                }

                holder.chartView.setListener(new ChartView.getPositionListener() {
                    @Override
                    public void setPosition(int position) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClick(position);
                        }
                        if (ClickId == position) {
                        } else {
                            if (ClickId == -1) {
                            } else {
                                chartViews.get(ClickId).setColor(mContext.getResources().getColor(R.color.tooth_blue));
                            }
                            ClickId = position;
                        }
                    }
                });
            }
        }
    }

    /*@Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        if (position <= brushBeans.size()){

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM月dd");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
            Date testDate = new Date();
            try {
                testDate = simpleDateFormat.parse(brushBeans.get(position).getStartTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }


            holder.chartView.setList(IDatalifeConstant.getCalculation(mContext,brushBeans.get(position)),position,simpleDateFormat1.format(testDate),simpleDateFormat2.format(testDate));

            if (chartViews.size() <= position){chartViews.add(holder.chartView);
            }

//            if (!chartViews.contains(holder.chartView)) {
//                chartViews.add(holder.chartView);
//            }

            if (startid == 1){
                chartViews.get(0).setColor(mContext.getResources().getColor(R.color.tooth_yellow));
                startid = 0;
            }

            holder.chartView.setListener(new ChartView.getPositionListener() {
                @Override
                public void setPosition(int position) {
                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(position);
                    }
                    if (ClickId == position){
                    }else {
                        if (ClickId == -1){
                        }else {
                            chartViews.get(ClickId).setColor(mContext.getResources().getColor(R.color.tooth_blue));
                        }
                        ClickId = position;
                    }
                }
            });
        }
    }*/

    @Override
    public int getItemCount() {
        return brushBeans.size()+1 + endId;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return ITEM_TYPE_HEADER;
        }else{
            if (endId ==1) {
                if (position == brushBeans.size()+1) {
                    return ITEM_TYPE_END;
                }
                return position;
            }else {
                return position;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag());
            int id = (Integer) v.getTag();
            if (currentId != id){
                chartViews.get(currentId).setColor(mContext.getResources().getColor(R.color.tooth_blue));
                currentId = id;
                chartViews.get(id).setColor(mContext.getResources().getColor(R.color.tooth_yellow));
            }
//            chartViews.get((Integer) v.getTag() + 1).setColor(mContext.getResources().getColor(R.color.tooth_yellow));
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ChartView chartView;

        public ViewHolder(View itemView) {
            super(itemView);
            chartView = (ChartView) itemView.findViewById(R.id.my_chart);
        }
    }

    //头部 ViewHolder
    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        View view = null;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.view_head);
            int width = IDatalifeConstant.display(mContext).getWidth();

            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = width*2/5-30;
            view.setLayoutParams(layoutParams);

        }
    }
}
