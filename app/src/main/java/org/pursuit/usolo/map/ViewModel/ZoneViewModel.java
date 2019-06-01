package org.pursuit.usolo.map.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Zone;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;

public final class ZoneViewModel extends ViewModel {
    private FireRepo fireRepo = FireRepo.getInstance();

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

    public void addUserToZoneCount(String zoneName) {
        fireRepo.addUserToCount(zoneName);
    }

    public Flowable<Zone> getAllZones(@NonNull Context context) {
        return fireRepo.getAllZones(context);
    }
}
