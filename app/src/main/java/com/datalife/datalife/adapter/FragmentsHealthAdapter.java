package com.datalife.datalife.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.widget.TextView;

import com.datalife.datalife.base.BaseHealthFragment;
import com.datalife.datalife.fragment.MeasureFragment;

/**
 * Created by LG on 2018/1/18.
 */

public class FragmentsHealthAdapter extends FragmentPagerAdapter {

    private SparseArray<BaseHealthFragment> fragmentSparseArr;
    private TextView textView;

    public FragmentsHealthAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(SparseArray<BaseHealthFragment> fragmentSparseArray) {
        this.fragmentSparseArr = fragmentSparseArray;
        notifyDataSetChanged();
    }

    public void onTextViewTitle(TextView textView){
        this.textView = textView;
    }

    @Override
    public Fragment getItem(int position) {
        if (null != fragmentSparseArr) return fragmentSparseArr.get(position);
        return null;
    }

    @Override
    public int getCount() {
        if (null != fragmentSparseArr) return fragmentSparseArr.size();
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (fragmentSparseArr == null)
            return "";
        if (fragmentSparseArr.get(position) == null)
            return "";
        return fragmentSparseArr.get(position).getTitle();
    }
}
