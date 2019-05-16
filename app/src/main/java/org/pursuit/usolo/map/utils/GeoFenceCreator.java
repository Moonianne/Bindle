package org.pursuit.usolo.map.utils;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.pursuit.usolo.map.services.GeoFencingIntentService;

public class GeoFenceCreator {

    private static final String TAG = "GeoFenceCreator";
    private GeofencingClient geofencingClient;
    private Geofence geofence;
    private Context context;
    private LatLng latLng;

    public GeoFenceCreator(Context context, LatLng latLng) {
        this.context = context;
        this.latLng = latLng;
    }

    public void initGeoFenceClient() {
        geofencingClient = LocationServices.getGeofencingClient(context);
    }

    public void buildGeoFence() {
        geofence = new Geofence.Builder()
          .setRequestId("TestFence")
          .setCircularRegion(latLng.getLatitude(), latLng.getLongitude(), 20f)
          .setExpirationDuration(Geofence.NEVER_EXPIRE)
          .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
          .build();
    }

    public void addGeoFenceToClient() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            geofencingClient.addGeofences(getGeoFencingRequest(), getGeoFencePendingIntent())
              .addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: added fence"))
              .addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e));
        }
    }

    private GeofencingRequest getGeoFencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private PendingIntent getGeoFencePendingIntent() {
        return PendingIntent.getService(context, 0,
          new Intent(context, GeoFencingIntentService.class),
          PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void tearDown() {
        context = null;
    }
}
