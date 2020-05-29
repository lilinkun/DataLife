package com.datalife.datalife.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.datalife.datalife.R;


/**
 * Created by LG on 2018/2/9.
 */

public final class AlertDialogBuilder extends AlertDialog.Builder {

    public AlertDialogBuilder(Context context) {
        this(context, R.style.AppDialogStyle);
    }

    public AlertDialogBuilder(Context context, int theme) {
        super(context, theme);
    }
}