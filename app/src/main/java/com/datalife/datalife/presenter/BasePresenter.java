package com.datalife.datalife.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by LG on 2018/3/28.
 */

public abstract class BasePresenter implements Presenter {

    protected void showDialog(Context context,int res){
        AlertDialog alertDialog = new AlertDialog.Builder(context).setMessage(res).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

}
