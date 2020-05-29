package com.datalife.datalife.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

import com.datalife.datalife.R;

/**
 * Created by LG on 2018/6/6.
 */

public class NetworkBroadcast extends BroadcastReceiver {

    public static boolean isNet = true;
    private Handler handler = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED == mobileState) {
            // 手机网络连接成功
            isNet = true;
//            Toast.makeText(context,"手机网络连接成功",Toast.LENGTH_SHORT).show();
        } else if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED != mobileState) {
            // 手机没有任何的网络
            isNet = false;

        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
            // 无线网络连接成功
            isNet = true;
//            Toast.makeText(context,"无线网络连接成功",Toast.LENGTH_SHORT).show();
        }

        if (isNet == false){
            handler.sendEmptyMessage(8878);
        }
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }
}
