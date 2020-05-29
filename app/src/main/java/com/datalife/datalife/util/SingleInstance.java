package com.datalife.datalife.util;

import android.content.Context;

/**
 * Created by LG on 2018/6/14.
 */

public class SingleInstance {

        private Context mContext;
        private static SingleInstance sInstance;

        private SingleInstance(Context mContext){
            this.mContext = mContext;
        }

        public static SingleInstance getInstance(Context context){
            if(sInstance == null){
                sInstance = new SingleInstance(context.getApplicationContext());
            }
            return sInstance;
        }

}
