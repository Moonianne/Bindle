package org.pursuit.usolo.map.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Group;
import org.pursuit.firebasetools.model.Zone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;

public final class ZoneViewModel extends ViewModel {
    private FireRepo fireRepo = FireRepo.getInstance();
    private List<String> zoneNames = new ArrayList<>();

    public Maybe<Zone> getZone() {
        return fireRepo
          .getZone()
          .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<LatLng> getZoneLocation() {
        return fireRepo
          .getZone()
          .map(Zone::getLocation)
          .observeOn(AndroidSchedulers.mainThread());
    }

    public void loginToFireBase(String email, String password) {
        fireRepo.loginToFireBase(email, password);
    }

    public Maybe<Group> getGroup(String chatKey) {
        return fireRepo.getGroup(chatKey);
    }


    public void addUserToZoneCount(String zoneName) {
        fireRepo.addUserToCount(zoneName);
    }

    public Flowable<Zone> getAllZones(@NonNull Context context) {
        return fireRepo.getAllZones(context)
          .doOnNext(zone -> zoneNames
            .addAll(Collections.singleton(zone.getName())));
    }

    public String getZoneName(int id) {
        return zoneNames.get(id);
    }
}
