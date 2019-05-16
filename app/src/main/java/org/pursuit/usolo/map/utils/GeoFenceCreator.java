package org.pursuit.usolo.map.utils;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.pursuit.usolo.map.services.GeoFencingIntentService;

public class GeoFenceCreator {

    private static final String TAG = "GeoFenceCreator";
    private GeofencingClient geofencingClient;
    private Geofence geofence;
    private GoogleMap mMap;
    private Activity activity;

    public GeoFenceCreator(GoogleMap mMap, Activity activity) {
        this.mMap = mMap;
        this.activity = activity;
    }

    public void initGeoFenceClient() {
        geofencingClient = LocationServices.getGeofencingClient(activity);
    }

    public void buildGeoFence(LatLng latLng) {
        geofence = new Geofence.Builder()
                .setRequestId("TestFence")
                .setCircularRegion(latLng.latitude, latLng.longitude, 20f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    public void createVisualGeoFence(LatLng latLng) {
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(latLng.latitude, latLng.longitude))
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .clickable(true)
                .radius(20f));
    }

    public void addGeoFenceToClient() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
        geofencingClient.addGeofences(getGeoFencingRequest(), getGeoFencePendingIntent())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: added fence"))
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e));
    }

    private GeofencingRequest getGeoFencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private PendingIntent getGeoFencePendingIntent() {
        PendingIntent geoFencePendingIntent;
        Intent intent = new Intent(activity, GeoFencingIntentService.class);
        geoFencePendingIntent = PendingIntent.getService(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geoFencePendingIntent;
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1020);
        } else {
            mMap.setMyLocationEnabled(true);
        }
    }

    public void tearDown(){

    }
}
