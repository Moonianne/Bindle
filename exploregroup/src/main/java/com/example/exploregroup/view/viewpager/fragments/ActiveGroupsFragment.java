package com.example.exploregroup.view.viewpager.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.group.model.firebase.Group;
import com.example.exploregroup.R;
import com.example.exploregroup.view.recyclerview.GroupsAdapter;

import java.util.ArrayList;
import java.util.List;

public final class ActiveGroupsFragment extends Fragment {

    private View rootView;

    public ActiveGroupsFragment(){}

    public static ActiveGroupsFragment newInstance() {
        return new ActiveGroupsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_active_groups, container, false);
        initActiveGroupsRecylerView();
        return rootView;
    }

    private void initActiveGroupsRecylerView() {
        RecyclerView activeRecyclerView = rootView.findViewById(R.id.active_groups_recycler_view);
        GroupsAdapter adapter = new GroupsAdapter();
        activeRecyclerView.setAdapter(adapter);
        activeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Group> groupList = new ArrayList<>();
        groupList.add(new Group.Builder("Test1","Arcade",getString(R.string.description_lorem)).build());
        groupList.add(new Group.Builder("Test2","Night Life",getString(R.string.description_lorem)).build());
        groupList.add(new Group.Builder("Test3","Site seeing",getString(R.string.description_lorem)).build());
        groupList.add(new Group.Builder("Test4","Dancing",getString(R.string.description_lorem)).build());
        groupList.add(new Group.Builder("Test5","Drugs",getString(R.string.description_lorem)).build());
        adapter.setData(groupList);
    }

}
