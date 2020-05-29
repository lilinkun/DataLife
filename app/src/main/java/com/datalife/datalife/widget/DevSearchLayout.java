package com.datalife.datalife.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.datalife.datalife.R;

/**
 * Created by LG on 2018/5/22.
 */

public class DevSearchLayout extends LinearLayout {

    private Context mContext;

    public DevSearchLayout(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public DevSearchLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public DevSearchLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_devsearch,null);


    }

}
