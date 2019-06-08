package org.pursuit.usolo.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Group;
import org.pursuit.firebasetools.model.Zone;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

public final class HostViewModel extends ViewModel {

    private FireRepo fireRepo;

    public HostViewModel() {
        fireRepo = FireRepo.getInstance();
    }

    public Flowable<Zone> getAllZones(@NonNull final Context context) {
        return fireRepo.getAllZones(context);
    }

    public Flowable<Group> getAllGroups() {
        return fireRepo.getGroups();
    }
}
