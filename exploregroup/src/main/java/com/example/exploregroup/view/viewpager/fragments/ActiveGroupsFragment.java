package com.example.exploregroup.view.viewpager.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exploregroup.R;
import com.example.exploregroup.view.recyclerview.GroupsAdapter;
import com.example.exploregroup.viewmodel.GroupsViewModel;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Group;

import java.util.ArrayList;
import java.util.List;

public final class ActiveGroupsFragment extends Fragment {

    private View rootView;
    private GroupsViewModel groupsViewModel;

    public ActiveGroupsFragment() {
    }

    public static ActiveGroupsFragment newInstance() {
        return new ActiveGroupsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_active_groups, container, false);
        groupsViewModel = ViewModelProviders.of(this).get(GroupsViewModel.class);
        initActiveGroupsRecylerView();
        return rootView;
    }

    private void initActiveGroupsRecylerView() {
        RecyclerView activeRecyclerView = rootView.findViewById(R.id.active_groups_recycler_view);
        GroupsAdapter adapter = new GroupsAdapter();
        activeRecyclerView.setAdapter(adapter);
        activeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Group> groupList = new ArrayList<>();
        groupsViewModel.getGroups(new FireRepo.OnGroupUpdateEmittedListener() {
            @Override
            public void onGroupUpdateEmitted(Group group) {
                groupList.add(group);
                adapter.setData(groupList);
            }
        });
    }

}
