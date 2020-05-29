package com.datalife.datalife.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.datalife.datalife.BR;
import com.datalife.datalife.R;
import com.datalife.datalife.adapter.vh.BindingViewHolder;
import com.datalife.datalife.binding.ObservableString;
import com.datalife.datalife.util.UToast;

import lib.linktop.common.CssSubscriber;
import lib.linktop.obj.Device;
import lib.linktop.sev.CssServerApi;

/**
 * Created by LG on 2018/1/18.
 */

public class BindDevListAdapter extends DataBindingAdapter<Device> {

    private final ObservableString id;

    public BindDevListAdapter(Context context, ObservableString id) {
        super(context, R.layout.item_bind_dev);
        this.id = id;
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        super.onBindViewHolder(holder, position);
        holder.binding.setVariable(BR.id, id);
        holder.binding.setVariable(BR.clickGetDevActiveInfo, new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final String devId = getItem(position).getDevId();
                CssServerApi.getDevActiveInfo(devId)
                        .subscribe(new CssSubscriber<String[]>() {
                            @Override
                            public void onNextRequestSuccess(String[] strings) {
                                final String s = "Device key:" + strings[0] + ",\nQR code:" + strings[1];
                                UToast.show(v.getContext(), s);
                            }

                            @Override
                            public void onNextRequestFailed(int status) {

                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            }
        });
    }
}