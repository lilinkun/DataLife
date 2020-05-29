package com.datalife.datalife.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/2/5.
 */

public class MyPagerAdapter  extends PagerAdapter {
    private List<View> mList ;

    private Context context ;

    private String[] titles;

    public MyPagerAdapter(Context context, List<View> mList,String[] titles){
        this.context = context;
        this.mList = mList;
        this.titles = titles;
    }

    //提供标题的内容
    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return titles[position];
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0==arg1;
    }
    //有多少个切换页
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    //对超出范围的资源进行销毁
    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        // TODO Auto-generated method stub
        //super.destroyItem(container, position, object);
        container.removeView(mList.get(position));
    }
    //对显示的资源进行初始化
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        //return super.instantiateItem(container, position);
        container.addView(mList.get(position));
        return mList.get(position);
    }
}
