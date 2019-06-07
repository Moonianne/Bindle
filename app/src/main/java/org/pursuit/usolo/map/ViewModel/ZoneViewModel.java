package org.pursuit.usolo.map.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;

import org.jetbrains.annotations.NotNull;
import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.firebasetools.model.Group;
import org.pursuit.firebasetools.model.Zone;
import org.pursuit.usolo.model.MapFeature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public final class ZoneViewModel extends ViewModel {
    public static final double METER_RADIUS = 300d;

    private FireRepo fireRepo = FireRepo.getInstance();
    private List<String> zoneNames = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();

    public Maybe<Zone> getZone(@NonNull final String zoneKey) {
        return fireRepo
          .getZone(zoneKey)
          .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<LatLng> getZoneLocation() {
        return fireRepo
          .getZone("pursuit")
          .map(Zone::getLocation)
          .observeOn(AndroidSchedulers.mainThread());
    }

    public void loginToFireBase(String email, String password) {
        fireRepo.loginToFireBase(email, password);
    }

    public void logoutFireBase() {
        fireRepo.logOutFireBase();
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

    public List<String> getZoneNames() {
        return zoneNames;
    }

    @NotNull
    public Polygon getGeometry(MapFeature mapFeature) {
        return Polygon.fromLngLats(mapFeature.points);
    }

    @NotNull
    public RectF getRectF(PointF pointf) {
        return new RectF(pointf.x - 10, pointf.y - 10, pointf.x + 10, pointf.y + 10);
    }

    @NotNull
    public MapFeature getMapFeature(Zone zone) {
        return new MapFeature(zone.getName(), zone.getLocation(), getPointsLists(zone.getLocation()));
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

    public CameraPosition getPosition(Symbol symbol) {
        return new CameraPosition.Builder()
          .target(new LatLng(symbol.getLatLng()))
          .zoom(12)
          .tilt(40)
          .build();
    }

    public Single<List<Group>> getGroups() {
        return fireRepo.getGroups()
          .doOnNext(group -> groups.addAll(Collections.singleton(group)))
          .take(5)
          .toList();
    }

    public List<Group> getRecentGroupList() {
        return this.groups;
    }
}
