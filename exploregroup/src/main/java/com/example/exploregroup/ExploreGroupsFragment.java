package com.example.exploregroup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public final class ExploreGroupsFragment extends Fragment {

    private View rootView;

    public ExploreGroupsFragment() {}

    public ExploreGroupsFragment newInstance(){
        return new ExploreGroupsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_explore_groups, container, false);
        return rootView;
    }

}
