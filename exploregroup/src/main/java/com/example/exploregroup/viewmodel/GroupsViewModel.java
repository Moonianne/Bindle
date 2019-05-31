package com.example.exploregroup.viewmodel;

import android.arch.lifecycle.ViewModel;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Group;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

public final class GroupsViewModel extends ViewModel {

    private FireRepo fireRepo;

    public GroupsViewModel() {
        this.fireRepo = FireRepo.getInstance();
    }

    public Maybe<Group> getGroup() {
        return fireRepo.getGroup("Let's Learn to Code")
          .subscribeOn(Schedulers.io());
    }

    public Flowable<Group> getActiveGroups() {
        return fireRepo
          .getGroups()
          .filter(group -> group.getUserCount() >= 4);
    }

    public Flowable<Group> getPendingGroups() {
        return fireRepo
          .getGroups()
          .filter(group -> group.getUserCount() < 4);
    }

}
