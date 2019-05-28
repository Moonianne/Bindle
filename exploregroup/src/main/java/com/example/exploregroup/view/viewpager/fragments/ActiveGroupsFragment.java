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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

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
        groupsViewModel.getGroups(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                groupList.add(dataSnapshot.getValue(Group.class));
                adapter.setData(groupList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
