package org.pursuit.usolo.map.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import org.pursuit.firebasetools.Repository.FireRepo;
import org.pursuit.usolo.map.utils.GeoFenceCreator;
import org.pursuit.usolo.map.utils.Notification;


public final class GeoFencingIntentService extends IntentService {
    private static final String TAG = "GeoFencingIntentService";

    public GeoFencingIntentService() {
        super("GeoFencingIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: intentFired!");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        int transitionType = geofencingEvent.getGeofenceTransition();
        String locationName = intent.getStringExtra(GeoFenceCreator.LOCATION_NAME);
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                sendNotification("Just Entered " + locationName);
                FireRepo.getInstance().addUserToCount(locationName);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                sendNotification("Just Exited " + locationName);
                FireRepo.getInstance().removeUserFromCount(locationName);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                sendNotification("Just Chillin at " + locationName);
                break;
            default:
                Log.d(TAG, "onHandleIntent: Invalid transition type");
                break;
        }

    }

    private void sendNotification(String message) {
        new Notification(this, message);
    }


}
