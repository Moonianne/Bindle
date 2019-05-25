package org.pursuit.usolo.map.ViewModel;

import android.arch.lifecycle.ViewModel;

import org.pursuit.usolo.map.data.ZoneRepository;
import org.pursuit.usolo.map.model.Zone;

public final class ZoneViewModel extends ViewModel {
    ZoneRepository zoneRepository = ZoneRepository.getInstance();

    public void getZone (ZoneRepository.OnUpdatesEmittedListener listener) {
        zoneRepository.getZone(listener);
    }
    public void setZone(Zone zone) {
        zoneRepository.getZoneLocationReference().setValue(zone);
    }

    public void loginToFireBase() {
        zoneRepository.loginToFirebase("metalraidernt@gmail.com", "password123");
    }

    public void addUserToZoneCount(String zoneName) {
        zoneRepository.addUserToCount(zoneName);
    }

}
