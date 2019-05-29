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

import org.pursuit.firebasetools.model.Group;

import java.util.ArrayList;
import java.util.List;

public final class PendingGroupsFragment extends Fragment {

    private View rootView;
    private GroupsViewModel groupsViewModel;

    public PendingGroupsFragment() {
    }

    public static PendingGroupsFragment newInstance() {
        return new PendingGroupsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pending_groups, container, false);
        initViewModel();
        initRecyclerView();
        return rootView;
    }

    private void initViewModel() {
        groupsViewModel = ViewModelProviders.of(getParentFragment()).get(GroupsViewModel.class);
    }

    private void initRecyclerView() {
        RecyclerView pendingRecyclerView = rootView.findViewById(R.id.pending_groups_recycler_view);
        GroupsAdapter adapter = new GroupsAdapter();
        pendingRecyclerView.setAdapter(adapter);
        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        groupsViewModel.setPendingListener(pendingGroupsList -> adapter.setData(pendingGroupsList));
    }

}
