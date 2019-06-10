package org.pursuit.usolo.map.utils;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.pursuit.usolo.map.services.GeoFencingIntentService;

import io.reactivex.annotations.NonNull;

public final class GeoFenceCreator {

    private static final String TAG = "GeoFenceCreator";
    public static final String LOCATION_NAME = "Location Name";
    private GeofencingClient geofencingClient;
    private Geofence geofence;
    private Context context;
    private LatLng latLng;
    private String locationName;

    public GeoFenceCreator(@NonNull final Context context,
                           @NonNull final LatLng latLng,
                           @NonNull final String locationName) {
        this.context = context;
        this.latLng = latLng;
        this.locationName = locationName;
    }

    private void initGeoFenceClient() {
        geofencingClient = LocationServices.getGeofencingClient(context);
    }

    private void buildGeoFence() {
        geofence = new Geofence.Builder()
          .setRequestId(locationName)
          .setCircularRegion(latLng.getLatitude(), latLng.getLongitude(), 300f)
          .setExpirationDuration(Geofence.NEVER_EXPIRE)
          .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
          .build();
    }

    private void addGeoFenceToClient() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            geofencingClient.addGeofences(getGeoFencingRequest(), getGeoFencePendingIntent())
              .addOnSuccessListener(aVoid -> {
                  tearDown();
                  Log.d(TAG, "onSuccess: added fence");
              })
              .addOnFailureListener(e -> {
                  tearDown();
                  Log.d(TAG, "onFailure: " + e);
              });
        }
    }

    private GeofencingRequest getGeoFencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private PendingIntent getGeoFencePendingIntent() {
        Intent geofenceIntent = new Intent(context, GeoFencingIntentService.class);
        geofenceIntent.putExtra(LOCATION_NAME, locationName);
        return PendingIntent.getService(context, 0, geofenceIntent,
          PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void createGeoFence() {
        initGeoFenceClient();
        buildGeoFence();
        addGeoFenceToClient();
    }

    public void tearDown() {
        context = null;
    }
}
