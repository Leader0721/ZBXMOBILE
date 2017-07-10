package com.zbxn.pub.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.zbxn.pub.frame.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        if (fm == null)
            throw new NullPointerException("The FragmentManager is null!");
        if (mFragmentList == null)
            mFragmentList = new ArrayList<Fragment>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragmentList.get(position);
        Logger.i("FragmentAdapter--->getItem(): fragmentTag=" + fragment.getTag());
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = mFragmentList.get(position);
        String title;
        if (fragment instanceof BaseFragment) {
            title = ((BaseFragment) fragment).mTitle;
        } else {
            title = fragment.getTag();
        }
        return title;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /**
     * 将一个页面添加到Adapter中
     *
     * @param fragment
     * @return 是否添加成功
     */
    public boolean addFragment(Fragment fragment) {
        if (mFragmentList.contains(fragment))
            return false;
        try {
            mFragmentList.add(fragment);
            notifyDataSetChanged();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 将多个页面添加到Adapter中
     *
     * @param fragments
     * @return 添加成功了几个
     */
    public int addFragment(Fragment... fragments) {
        int count = 0;
        for (Fragment fragment : fragments) {
            if (mFragmentList.contains(fragment))
                continue;
            try {
                mFragmentList.add(fragment);
                count++;
            } catch (Exception e) {

            }
        }
        notifyDataSetChanged();
        return count;
    }

}
