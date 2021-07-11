package com.example.root.studynote;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by root on 1/1/18.
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String noiDung = intent.getStringExtra("noiDung");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLights(0,0,0)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle("StudyNote")
                .setContentText(noiDung)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}
