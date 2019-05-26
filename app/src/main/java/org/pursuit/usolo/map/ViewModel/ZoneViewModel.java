package org.pursuit.usolo.map.ViewModel;

import android.arch.lifecycle.ViewModel;

import org.pursuit.firebasetools.Repository.FireRepo;

public final class ZoneViewModel extends ViewModel {
    private FireRepo fireRepo = FireRepo.getInstance();

    public void getZone(FireRepo.OnZoneUpdateEmittedListener listener) {
        fireRepo.getZone(listener);
    }

    public void loginToFireBase() {
        fireRepo.loginToFireBase("metalraidernt@gmail.com", "password123");
    }

    public void addUserToZoneCount(String zoneName) {
        fireRepo.addUserToCount(zoneName);
    }

}
