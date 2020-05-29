package com.datalife.datalife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.datalife.datalife.R;
import com.datalife.datalife.bean.TestBean;
import com.datalife.datalife.util.IDatalifeConstant;

import java.util.List;

/**
 * Created by LG on 2018/5/2.
 */

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.ViewHolder> {

    private List<TestBean> testBeans;
    private Context mContext;

    public TestListAdapter(List<TestBean> testBean, Context context){
        this.testBeans = testBean;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.testitem,parent,false);

        int width = IDatalifeConstant.display(mContext).getWidth();

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_item);
        RecyclerView.LayoutParams linearParams =(RecyclerView.LayoutParams) relativeLayout.getLayoutParams();
        linearParams.width = width/3;
        relativeLayout.setLayoutParams(linearParams);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TestBean testBean = testBeans.get(position);

        holder.testValue.setText(testBean.getTestValue());
        holder.testTitle.setText(testBean.getTestTitle());
        if (testBean.getTextArraw() == 1){
            holder.arrawImage.setImageResource(0);
        }else if (testBean.getTextArraw() == 0){
            holder.arrawImage.setImageResource(R.mipmap.ic_arraw_bottom);
        }else if(testBean.getTextArraw() == 2){
            holder.arrawImage.setImageResource(R.mipmap.ic_arraw_top);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return testBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView testValue;
        TextView testTitle;
        ImageView arrawImage;

        public ViewHolder(View itemView){
            super(itemView);
            testValue = (TextView) itemView.findViewById(R.id.test_value);
            testTitle = (TextView) itemView.findViewById(R.id.test_title);
            arrawImage = (ImageView) itemView.findViewById(R.id.ic_arraw);
        }
    }
}
