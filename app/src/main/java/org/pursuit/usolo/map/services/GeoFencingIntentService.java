package org.pursuit.usolo.map.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import org.pursuit.usolo.map.utils.Notification;


public class GeoFencingIntentService extends IntentService {
    private static final String TAG = "GeoFencingIntentService";

    public GeoFencingIntentService() {
        super("GeoFencingIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: intentFired!");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        int TransitionType = geofencingEvent.getGeofenceTransition();

        switch (TransitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                sendNotification("Just Entered Fence");
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                sendNotification("Just Exited Fence");
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                sendNotification("Just Dwelling Fence");
                break;
            default:
                sendNotification("Invalid Transition Type");
                break;
        }

    }

    private void sendNotification(String message) {
        new Notification(this, message);
    }


}
