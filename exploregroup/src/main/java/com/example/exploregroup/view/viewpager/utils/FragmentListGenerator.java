package com.example.exploregroup.view.viewpager.utils;

import android.support.v4.app.Fragment;

import com.example.exploregroup.view.viewpager.fragments.ActiveGroupsFragment;
import com.example.exploregroup.view.viewpager.fragments.PendingGroupsFragment;

import java.util.ArrayList;
import java.util.List;

public final class FragmentListGenerator {

    public static List<Fragment> getViewPagerFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(ActiveGroupsFragment.newInstance());
        fragmentList.add(PendingGroupsFragment.newInstance());
        return fragmentList;
    }
}
