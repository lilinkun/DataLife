package com.datalife.datalife.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.datalife.datalife.R;
import com.datalife.datalife.bean.MeasureRecordBean;
import com.datalife.datalife.bean.TestBean;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.MeasureNorm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/5/2.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private ArrayList<MeasureRecordBean> measureRecordBeans;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;//声明接口
    private List<RecyclerView> recyclerViews = new ArrayList<>();
    private String[] array = new String[]{"体重","BMI","体脂率","肌肉率","水分","去脂体重","骨量","内脏体脂指数","基础代谢率"};
    private ArrayList<String> strs = null;
    private ArrayList<Integer> arrows = null;
    private List<TestBean> testBeans = null;
    private TestListAdapter testListAdapter = null;
    private FamilyUserInfo familyUserInfo;

    public TestAdapter(Context context, ArrayList<MeasureRecordBean> measureRecordBeans, FamilyUserInfo familyUserInfo){
        this.mContext = context;
        this.measureRecordBeans = measureRecordBeans;
        this.familyUserInfo = familyUserInfo;
    }

    public void setMeasureRecordBeans(ArrayList<MeasureRecordBean> measureRecordBeans, FamilyUserInfo familyUserInfo){
        this.measureRecordBeans = measureRecordBeans ;
        this.familyUserInfo = familyUserInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        View itemView = ((LinearLayout) holder.itemView).getChildAt(0);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.fatTestDateTv.setText(measureRecordBeans.get(position).getCreateDate());
//        holder.recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        if (strs == null) {
            strs = new ArrayList<>();
        }else {
            strs.clear();
        }
        strs.add(measureRecordBeans.get(position).getCheckValue3());
        strs.add(measureRecordBeans.get(position).getCheckValue4());
        strs.add(measureRecordBeans.get(position).getCheckValue5());
        strs.add(measureRecordBeans.get(position).getCheckValue6());
        strs.add(measureRecordBeans.get(position).getCheckValue7());
        strs.add(measureRecordBeans.get(position).getCheckValue8());
        strs.add(measureRecordBeans.get(position).getCheckValue9());
        strs.add(measureRecordBeans.get(position).getCheckValue10());
        strs.add(measureRecordBeans.get(position).getCheckValue11());

            if (arrows == null) {
                arrows = new ArrayList<>();
            }else {
                arrows.clear();
            }
//            arrows.add(compare(measureRecordBeans.get(position).getCheckValue3(),measureRecordBeans.get(position-1).getCheckValue3()));
//            arrows.add(compare(measureRecordBeans.get(position).getCheckValue4(),measureRecordBeans.get(position-1).getCheckValue4()));
//            arrows.add(compare(measureRecordBeans.get(position).getCheckValue5(),measureRecordBeans.get(position-1).getCheckValue5()));
//            arrows.add(compare(measureRecordBeans.get(position).getCheckValue6(),measureRecordBeans.get(position-1).getCheckValue6()));
//            arrows.add(compare(measureRecordBeans.get(position).getCheckValue7(),measureRecordBeans.get(position-1).getCheckValue7()));
//            arrows.add(compare(measureRecordBeans.get(position).getCheckValue8(),measureRecordBeans.get(position-1).getCheckValue8()));
//            arrows.add(compare(measureRecordBeans.get(position).getCheckValue9(),measureRecordBeans.get(position-1).getCheckValue9()));
//            arrows.add(compare(measureRecordBeans.get(position).getCheckValue10(),measureRecordBeans.get(position-1).getCheckValue10()));
//            arrows.add(compare(measureRecordBeans.get(position).getCheckValue11(),measureRecordBeans.get(position-1).getCheckValue11()));
            if (measureRecordBeans.get(position).getCheckValue5().equals("0.0")) {
                arrows.add(0);
            }else {
                arrows.add(MeasureNorm.NormBfr(Integer.valueOf(familyUserInfo.getMember_Sex()),Double.valueOf(measureRecordBeans.get(position).getCheckValue5())/100));
            }
            arrows.add(MeasureNorm.NormBMI(Double.parseDouble(measureRecordBeans.get(position).getCheckValue4())));
            if (measureRecordBeans.get(position).getCheckValue5().equals("0.0")) {
                arrows.add(0);
            }else {
                arrows.add(MeasureNorm.NormBfr(Integer.valueOf(familyUserInfo.getMember_Sex()),Double.valueOf(measureRecordBeans.get(position).getCheckValue5())/100));
            }
            arrows.add(MeasureNorm.NormFat(DataLifeUtil.getAgeByBirthDay(familyUserInfo.getMember_DateOfBirth()),Integer.valueOf(familyUserInfo.getMember_Sex()),Double.valueOf(measureRecordBeans.get(position).getCheckValue6())));
            arrows.add(MeasureNorm.NormWet(DataLifeUtil.getAgeByBirthDay(familyUserInfo.getMember_DateOfBirth()),Integer.valueOf(familyUserInfo.getMember_Sex()),Double.valueOf(measureRecordBeans.get(position).getCheckValue7())));
            if (measureRecordBeans.get(position).getCheckValue5().equals("0.0")) {
                arrows.add(0);
            }else {
                arrows.add(MeasureNorm.NormBfr(Integer.valueOf(familyUserInfo.getMember_Sex()),Double.valueOf(measureRecordBeans.get(position).getCheckValue5())/100));
            }
            arrows.add(MeasureNorm.NormBONE(Double.valueOf(measureRecordBeans.get(position).getCheckValue3()),Integer.valueOf(familyUserInfo.getMember_Sex()),Double.valueOf(measureRecordBeans.get(position).getCheckValue9())));
            arrows.add(MeasureNorm.NormVISCERA(Double.valueOf(measureRecordBeans.get(position).getCheckValue10())));
            arrows.add(MeasureNorm.Normmetabolize(DataLifeUtil.getAgeByBirthDay(familyUserInfo.getMember_DateOfBirth()),Integer.valueOf(familyUserInfo.getMember_Sex()),Double.valueOf(measureRecordBeans.get(position).getCheckValue11()),Double.valueOf(measureRecordBeans.get(position).getCheckValue3())));

        if (testBeans == null) {
            testBeans = new ArrayList<>();
        }else if(testBeans != null){
            testBeans.clear();
        }
        for (int i = 0;i<9;i++){
            TestBean testBean = new TestBean();
            testBean.setTestTitle(array[i]);
            testBean.setTestValue(strs.get(i));
            testBean.setTextArraw(arrows.get(i));
            testBeans.add(testBean);
        }

        holder.recyclerView.setAdapter(new TestListAdapter(testBeans,mContext));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView,position);
            }
        });
        if(recyclerViews.size() != measureRecordBeans.size()) {
            recyclerViews.add(holder.recyclerView);
            holder.recyclerView.addOnScrollListener(
                    new OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    // note: scrollBy() not trigger OnScrollListener
                    for (int i = 0; i < recyclerViews.size(); i++) {
                        if (i != position) {
                            if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
//                                recyclerViews.get(i).stopScroll();
                            }
                        }
                    }

                    for (int i = 0; i < recyclerViews.size(); i++) {
                        if (i != position) {
                            if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                                recyclerViews.get(i).scrollBy(dx, dy);
                            }
                        }
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return measureRecordBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onLongClick(View view, int posotion);
    }

    public int compare(String oldStr,String newStr){
        if (Double.parseDouble(oldStr) > Double.parseDouble(newStr)){
            return 1;
        }else if (Double.parseDouble(oldStr) < Double.parseDouble(newStr)){
            return 0;
        }else {
            return 2;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;
        TextView fatTestDateTv;

        public ViewHolder(View itemView){
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rv_testlist);
            fatTestDateTv = (TextView) itemView.findViewById(R.id.tv_fat_test_date);
        }
    }
}
