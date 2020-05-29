package com.datalife.datalife.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.base.BaseRecordFragment;

/**
 * Created by LG on 2018/2/5.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private SparseArray<BaseRecordFragment> fragmentSparseArr;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(SparseArray<BaseRecordFragment> fragmentSparseArray) {
        this.fragmentSparseArr = fragmentSparseArray;
        notifyDataSetChanged();
    }

    public void setBundle(Bundle bundle){
        for(int i = 0; i <fragmentSparseArr.size() ;i++){
            fragmentSparseArr.get(i).setArguments(bundle);
        }
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
