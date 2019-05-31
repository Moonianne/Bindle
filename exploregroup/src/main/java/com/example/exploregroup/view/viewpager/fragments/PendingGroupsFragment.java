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

import java.util.Collections;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public final class PendingGroupsFragment extends Fragment {

    private GroupsViewModel groupsViewModel;
    private Disposable disposable;

    public static PendingGroupsFragment newInstance() {
        return new PendingGroupsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pending_groups, container, false);
        initViewModel();
        initRecyclerView(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }

    private void initViewModel() {
        groupsViewModel = ViewModelProviders.of(this).get(GroupsViewModel.class);
    }

    private void initRecyclerView(View rootView) {
        RecyclerView pendingRecyclerView = rootView.findViewById(R.id.pending_groups_recycler_view);
        GroupsAdapter adapter = new GroupsAdapter(Collections.emptyList());
        pendingRecyclerView.setAdapter(adapter);
        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        disposable = groupsViewModel.getPendingGroups()
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(adapter::addData);
    }

}
