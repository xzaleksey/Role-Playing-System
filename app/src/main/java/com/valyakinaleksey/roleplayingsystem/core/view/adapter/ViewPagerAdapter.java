package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.valyakinaleksey.roleplayingsystem.core.utils.Tuple;

import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Tuple<String, Fragment>> fragmentTitlePairs;

    public ViewPagerAdapter(FragmentManager fm, List<Tuple<String, Fragment>> fragmentTitlePairs) {
        super(fm);
        this.fragmentTitlePairs = fragmentTitlePairs;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentTitlePairs.get(position).second;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitlePairs.get(position).first;
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentTitlePairs.add(new Tuple<>(title, fragment));
    }

    @Override
    public int getCount() {
        return fragmentTitlePairs.size();
    }
}
