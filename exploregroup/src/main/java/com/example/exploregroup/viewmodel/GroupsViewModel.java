package com.example.exploregroup.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.google.firebase.database.ChildEventListener;

import org.pursuit.firebasetools.Repository.FireRepo;

public class GroupsViewModel extends ViewModel {

    private FireRepo fireRepo;

    public GroupsViewModel() {
        this.fireRepo = FireRepo.getInstance();
    }

    public void getGroup(FireRepo.OnGroupUpdateEmittedListener listener){
        fireRepo.getGroup("Let's Learn to Code", listener);
    }

    public void getGroups(ChildEventListener listener){
        fireRepo.getGroups(listener);
    }
}
