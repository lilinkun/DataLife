package com.datalife.datalife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.base.BaseHealthFragment;

/**
 * Created by LG on 2018/1/15.
 */

public class FragmentsAdapter extends FragmentPagerAdapter {

    private SparseArray<BaseFragment> fragmentSparseArr;

    public FragmentsAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(SparseArray<BaseFragment> fragmentSparseArray) {
        this.fragmentSparseArr = fragmentSparseArray;
        notifyDataSetChanged();
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

    /*@Override
    public CharSequence getPageTitle(int position) {
        if (fragmentSparseArr == null)
            return "";
        if (fragmentSparseArr.get(position) == null)
            return "";
        return fragmentSparseArr.get(position).getTitle();
    }*/
}
