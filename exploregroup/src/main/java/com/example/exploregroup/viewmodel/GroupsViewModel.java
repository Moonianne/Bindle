package com.example.exploregroup.viewmodel;

import android.arch.lifecycle.ViewModel;

import org.pursuit.firebasetools.Repository.FireRepo;

public class GroupsViewModel extends ViewModel {

    private FireRepo fireRepo;

    public GroupsViewModel() {
        this.fireRepo = FireRepo.getInstance();
    }

    public void getGroups(FireRepo.OnGroupUpdateEmittedListener listener){
        fireRepo.getGroup("Let's Learn to Code", listener);
    }
}
