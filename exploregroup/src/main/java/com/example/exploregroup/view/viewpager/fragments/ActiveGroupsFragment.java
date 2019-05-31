package com.example.exploregroup.view.viewpager.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public final class ActiveGroupsFragment extends Fragment {
    private GroupsViewModel groupsViewModel;
    private Disposable disposable;

    public static ActiveGroupsFragment newInstance() {
        return new ActiveGroupsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupsViewModel = ViewModelProviders.of(this).get(GroupsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_active_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView activeRecyclerView = view.findViewById(R.id.active_groups_recycler_view);
        GroupsAdapter adapter = new GroupsAdapter(Collections.emptyList());
        activeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        activeRecyclerView.setAdapter(adapter);
        disposable = groupsViewModel.getActiveGroups()
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(adapter::addData);
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
