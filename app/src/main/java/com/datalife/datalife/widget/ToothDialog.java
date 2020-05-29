package com.datalife.datalife.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.datalife.datalife.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LG on 2018/9/11.
 */

public class ToothDialog extends Dialog {
    private Context context;
    private String mCountTimer;
    private TextView mTvCount;
    private int counttime = 15;

    Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(111);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
                  switch (msg.what){
                      case 111:
                          counttime--;

                          if (counttime == 0){
                              cancel();
                              return;
                          }

                          mTvCount.setText("请在" + counttime +"秒内确认连接");
                          break;
                  }
        }
    };


    public ToothDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public ToothDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected ToothDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tooth_auth_dialog,null);
        setContentView(view);
        mTvCount = (TextView) view.findViewById(R.id.tv_sure_connect);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.95); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        timer.schedule(timerTask,1000,1000);
    }
}
