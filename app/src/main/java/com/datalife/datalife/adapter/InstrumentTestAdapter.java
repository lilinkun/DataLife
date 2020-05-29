package com.datalife.datalife.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.bean.TestEnum;
import com.datalife.datalife.fragment.HomePageFragment;

import java.util.ArrayList;

/**
 * Created by LG on 2018/6/12.
 */

public class InstrumentTestAdapter extends RecyclerView.Adapter<InstrumentTestAdapter.MyAdapter> {

    private Context context;
    private TestEnum[] testEnums;
    private Handler myHandler;


    public InstrumentTestAdapter(Context context, TestEnum[] testEnums, Handler handler){
        this.context = context;
        this.testEnums = testEnums;
        this.myHandler = handler;
    }


    @Override
    public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_instrument,parent,false);

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        RecyclerView.LayoutParams linearParams =(RecyclerView.LayoutParams) v.getLayoutParams();
        linearParams.width = (int)(width/2.2) - 10;
//        linearParams.width = width/2-28;
        v.setLayoutParams(linearParams);

        MyAdapter insAdapter = new MyAdapter(v);

        return insAdapter;
    }

    @Override
    public void onBindViewHolder(MyAdapter holder, final int position) {
//        holder.tv_testname.setText(arrayList.get(position).getTestName());
//        rectRoundBitmap(holder.iv_testname,arrayList.get(position).getTestRes());
        View itemView = ((LinearLayout) holder.itemView).getChildAt(0);
        holder.tv_testname.setText(testEnums[position].getTestName());
        rectRoundBitmap(holder.iv_testname,testEnums[position].getTestRes());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                message.setData(bundle);
                message.what = HomePageFragment.TESTHANDLER;
                myHandler.sendMessage(message);
            }
        });

    }

    @Override
    public int getItemCount() {
        return testEnums.length;
    }

    class MyAdapter extends RecyclerView.ViewHolder{
        ImageView iv_testname;
        TextView tv_testname;

        public MyAdapter(View itemView) {
            super(itemView);
            iv_testname = (ImageView)itemView.findViewById(R.id.iv_testname);
            tv_testname = (TextView) itemView.findViewById(R.id.tv_testname);
        }
    }

    /*
     * 获取圆角图片
     */
    private void rectRoundBitmap(ImageView imageView,int res){
        //得到资源文件的BitMap
        Bitmap image= BitmapFactory.decodeResource(context.getResources(),res);
        //创建RoundedBitmapDrawable对象
        RoundedBitmapDrawable roundImg = RoundedBitmapDrawableFactory.create(context.getResources(),image);
        //抗锯齿
        roundImg.setAntiAlias(true);
        //设置圆角半径
        roundImg.setCornerRadius(15);
        //设置显示图片
        imageView.setImageDrawable(roundImg);
    }
}
