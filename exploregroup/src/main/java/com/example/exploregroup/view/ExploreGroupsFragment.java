package com.example.exploregroup.view;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exploregroup.R;
import com.example.exploregroup.view.viewpager.adapter.GroupsViewPagerAdapter;
import com.example.exploregroup.view.viewpager.utils.FragmentListGenerator;
import com.example.exploregroup.viewmodel.GroupsViewModel;
import com.example.exploregroup.viewmodel.utils.GroupsListMapGenerator;


public final class ExploreGroupsFragment extends Fragment {

    private View rootView;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public ExploreGroupsFragment() {}

    public static ExploreGroupsFragment newInstance(){
        return new ExploreGroupsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_explore_groups, container, false);
        initViewPager();
        initTabLayout();
        return rootView;
    }

    private void initViewPager() {
        viewPager = rootView.findViewById(R.id.groups_view_pager);
        viewPager.setAdapter(new GroupsViewPagerAdapter(getChildFragmentManager(),
                FragmentListGenerator.getViewPagerFragmentList()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tabLayout.getTabAt(i).select();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initTabLayout() {
        tabLayout = rootView.findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GroupsListMapGenerator.clear();
    }
}
