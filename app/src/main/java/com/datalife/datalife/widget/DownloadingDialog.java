package com.datalife.datalife.widget;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

import com.datalife.datalife.R;

import cn.bingoogolapple.progressbar.BGAProgressBar;

/**
 * Created by LG on 2018/3/19.
 */

public class DownloadingDialog extends AppCompatDialog {
    private BGAProgressBar mProgressBar;

    public DownloadingDialog(Context context) {
        super(context, R.style.AppDialogTheme);
        setContentView(R.layout.dialog_downloading);
        mProgressBar = (BGAProgressBar) findViewById(R.id.pb_downloading_content);
        setCancelable(false);
    }

    public void setProgress(long progress, long maxProgress) {
        mProgressBar.setMax((int) maxProgress);
        mProgressBar.setProgress((int) progress);
    }

    @Override
    public void show() {
        super.show();
        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);
    }
}