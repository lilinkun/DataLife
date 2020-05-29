package com.datalife.datalife.adapter.vh;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by LG on 2018/1/18.
 */

public class BindingViewHolder extends RecyclerView.ViewHolder {

    public final ViewDataBinding binding;

    public BindingViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
