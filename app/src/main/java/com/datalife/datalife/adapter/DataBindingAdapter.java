package com.datalife.datalife.adapter;

import android.content.Context;
import android.databinding.ObservableInt;
import android.view.ViewGroup;

import com.datalife.datalife.BR;
import com.datalife.datalife.adapter.vh.BindingViewHolder;

/**
 * Created by LG on 2018/1/18.
 */

public class DataBindingAdapter <T> extends BaseRecyclerAdapter<T, BindingViewHolder> {

    private final int layoutId;
    private ObservableInt listSize = new ObservableInt(0);

    public DataBindingAdapter(Context context, int layoutId) {
        super(context);
        this.layoutId = layoutId;
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getBindingViewHolder(layoutId, parent);
    }

    private BindingViewHolder getBindingViewHolder(int layoutResId, ViewGroup viewGroup) {
        return new BindingViewHolder(getViewDataBinding(layoutResId, viewGroup));
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        holder.binding.setVariable(BR.item, getItem(position));
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        listSize.set(itemCount);
        return itemCount;
    }

    public ObservableInt getListSize() {
        return listSize;
    }
}
