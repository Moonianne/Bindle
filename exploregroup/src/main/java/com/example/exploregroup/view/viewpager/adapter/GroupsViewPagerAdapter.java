package com.example.exploregroup.view.viewpager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupsViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;

    public GroupsViewPagerAdapter(FragmentManager childFragmentManager, List<Fragment> viewPagerFragmentList) {
        super(childFragmentManager);
        this.fragmentList = viewPagerFragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
