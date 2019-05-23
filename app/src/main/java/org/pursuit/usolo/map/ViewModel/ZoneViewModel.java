package org.pursuit.usolo.map.ViewModel;

import android.arch.lifecycle.ViewModel;

import org.pursuit.usolo.map.data.ZoneRepository;
import org.pursuit.usolo.map.model.Zone;

public final class ZoneViewModel extends ViewModel {
    ZoneRepository zoneRepository = new ZoneRepository();

    public void setZone(Zone zone) {
        zoneRepository.getZoneLocationReference().setValue(zone);
    }

    public void loginToFireBase() {
        zoneRepository.loginToFirebase("metalraidernt@gmail.com", "password123");
    }
}
