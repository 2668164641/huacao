package com.qttx.toolslibrary.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class BaseFmtStateAdapter extends FragmentStatePagerAdapter {

    private List<? extends BaseFmt> mFmts;
    private List<String> mTitles;

    public void setList(List<? extends BaseFmt> fmts, List<String> titles) {
        if (null != fmts && null != titles) {
            this.mFmts = fmts;
            this.mTitles = titles;
            notifyDataSetChanged();
        }
    }

    public BaseFmtStateAdapter(FragmentManager fm, List<? extends BaseFmt> fmtList, List<String> titles) {
        super(fm);
        this.mFmts = fmtList;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFmts.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public int getCount() {
        return mFmts.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
