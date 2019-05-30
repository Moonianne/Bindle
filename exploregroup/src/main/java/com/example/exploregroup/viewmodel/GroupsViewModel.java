package com.example.exploregroup.viewmodel;

import android.arch.lifecycle.ViewModel;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Group;

import java.util.List;

import io.reactivex.Maybe;

public class GroupsViewModel extends ViewModel {

    private FireRepo fireRepo;
    private OnActiveGroupEmittedListener activeListener;
    private OnPendingGroupEmittedListener pendingListener;

    public GroupsViewModel() {
        this.fireRepo = FireRepo.getInstance();
    }

    public void getGroup(FireRepo.OnGroupUpdateEmittedListener listener) {
        fireRepo.getGroup("Let's Learn to Code", listener);
    }

    public Maybe<Group> getActiveGroups() {
        return fireRepo
          .getGroups()
          .filter(group -> group.getUserCount() >= 4);
    }

    public Maybe<Group> getPendingGroups() {
        return fireRepo
          .getGroups()
          .filter(group -> group.getUserCount() < 4);
    }

    public void setActiveListener(OnActiveGroupEmittedListener activeListener) {
        this.activeListener = activeListener;
    }

    public void setPendingListener(OnPendingGroupEmittedListener pendingListener) {
        this.pendingListener = pendingListener;
    }

    public interface OnActiveGroupEmittedListener {
        void activeGroupEmitted(List<Group> activeGroupsList);

    }

    public interface OnPendingGroupEmittedListener {
        void pendingGroupEmitted(List<Group> pendingGroupsList);
    }

}
