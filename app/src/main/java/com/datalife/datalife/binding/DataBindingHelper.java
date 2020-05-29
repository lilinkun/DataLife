package com.datalife.datalife.binding;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife.util.BarcodeImage;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by LG on 2018/1/18.
 * DataBinding 的BindingAdapter类
 */
public class DataBindingHelper {

    @BindingAdapter("recyclerAdapter")
    public static void setRecyclerAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        if (adapter != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }
    }

    @BindingAdapter("qrImage")
    public static void setQrImage(final ImageView imageView, final String qrCode) {
        Observable.just(qrCode)
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        if (TextUtils.isEmpty(s)) {
                            return null;
                        }
                        return BarcodeImage.bitmap(imageView.getContext(), s);
                    }
                })
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                });
    }

    @BindingAdapter("addTextChangeListener")
    public static void addTextChangeListener(TextView view, TextWatcher watcher) {
        view.addTextChangedListener(watcher);
    }

    @BindingAdapter("onViewClick")
    public static void setOnViewClick(View view, View.OnClickListener listener) {
        view.setOnClickListener(listener);
    }
}