package org.pursuit.usolo.map.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.jetbrains.annotations.NotNull;
import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Group;
import org.pursuit.firebasetools.model.Zone;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class ZoneViewModel extends ViewModel {
    public static final double METER_RADIUS = 200d;

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
    public Observable<Polygon> polygonCircleForCoordinate(LatLng location) {
        return Observable.just(location)
          .subscribeOn(Schedulers.computation())
          .map(loc -> Polygon.fromLngLats(getPointsLists(loc)))
          .observeOn(AndroidSchedulers.mainThread());
    }

    @NotNull
    private List<List<Point>> getPointsLists(LatLng location) {
        int degreesBetweenPoints = 8;
        int numberOfPointers = (int) Math.floor(360 / degreesBetweenPoints);
        double distRadians = METER_RADIUS / 6371000.0;
        double centerLatRadians = location.getLatitude() * Math.PI / 180;
        double centerLonRadians = location.getLongitude() * Math.PI / 180;
        List<Point> coordinates = new ArrayList<>(numberOfPointers);
        for (int i = 0; i < numberOfPointers; i++) {
            double degrees = (double) i * degreesBetweenPoints;
            double degreeRadians = degrees * Math.PI / 180;
            double pointLatRadians =
              Math.asin(Math.sin(centerLatRadians)
                * Math.cos(distRadians) + Math.cos(centerLatRadians)
                * Math.sin(distRadians) * Math.cos(degreeRadians));
            double pointLonRadians =
              centerLonRadians + Math.atan2(Math.sin(degreeRadians)
                  * Math.sin(distRadians) * Math.cos(centerLatRadians),
                Math.cos(distRadians) - Math.sin(centerLatRadians) * Math.sin(pointLatRadians));
            double pointLat = pointLatRadians * 180 / Math.PI;
            double pointLong = pointLonRadians * 180 / Math.PI;
            Point point = Point.fromLngLat(pointLong, pointLat);
            coordinates.add(point);
        }
        List<List<Point>> points = new ArrayList<>();
        points.add(coordinates);
        return points;
    }

}
