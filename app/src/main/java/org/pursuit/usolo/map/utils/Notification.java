package org.pursuit.usolo.map.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.pursuit.usolo.R;
import org.pursuit.usolo.view.HostActivity;

public class Notification {

    private static final String TAG = "Notification";
    private static final String CHANNEL_ID = "testMap";
    private static final int NOTIFICATION_ID = 2;
    private Context context;
    private String message;
    private NotificationManager notificationManager;

    public Notification(Context context, String message) {
        this.context = context;
        this.message = message;
        createNotificationChannel();
        sendNotification();
    }

    private android.app.Notification buildNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
          .setSmallIcon(R.drawable.bindle_logo)
          .setContentTitle("Bindle Point Nearby!")
          .setContentText(message)
          .setContentIntent(getPendingIntent())
          .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder.build();
    }

    private PendingIntent getPendingIntent() {
        return PendingIntent.getActivity(context, 3,
          new Intent(context, HostActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void createNotificationChannel() {
        CharSequence name = "Map Notification";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = context.getSystemService(NotificationManager.class);
        } else {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
              new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    private void sendNotification() {
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, buildNotification());
            context = null;
        } else {
            Log.d(TAG, "could not send notification " + message + " because" +
              "of null notification manager");
        }
    }

}
