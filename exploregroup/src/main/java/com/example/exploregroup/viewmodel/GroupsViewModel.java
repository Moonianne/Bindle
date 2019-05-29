package com.example.exploregroup.viewmodel;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.exploregroup.viewmodel.utils.GroupsListMapGenerator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Group;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    public void getGroups() {
        fireRepo.getGroups(new ChildEventListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Observable.just(dataSnapshot.getValue(Group.class))
                        .subscribeOn(Schedulers.io())
                        .map(GroupsListMapGenerator::getMapOfGroupLists)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(stringListMap -> {
                            if (activeListener != null) {
                                activeListener.activeGroupEmitted(stringListMap.get(GroupsListMapGenerator.ACTIVE_GROUPS));
                            }
                            if (pendingListener != null) {
                                pendingListener.pendingGroupEmitted(stringListMap.get(GroupsListMapGenerator.PENDING_GROUPS));
                            }
                        });
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
