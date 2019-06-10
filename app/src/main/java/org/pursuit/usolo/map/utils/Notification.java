package org.pursuit.usolo.map.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import org.pursuit.usolo.R;
import org.pursuit.usolo.view.HostActivity;

public class Notification {

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

    private PendingIntent getPendingIntent(){
        return PendingIntent.getActivity(context,3,
          new Intent(context, HostActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Map Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification() {
        notificationManager.notify(NOTIFICATION_ID, buildNotification());
        context = null;
    }

}
