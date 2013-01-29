package com.dinduks.CPUSpeedWatcher;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    private Intent resultIntent;
    private TaskStackBuilder stackBuilder;
    private Notification.Builder notificationBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultIntent = new Intent(this, MainActivity.class);
        stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class).addNextIntent(resultIntent);

        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notif_area_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notif_drawer_icon))
                .setContentTitle("CPU Speed Watcher");

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new CPUSWRunnable(this), 0, 1, TimeUnit.SECONDS);

        finish();
    }

    public Notification.Builder getNotificationBuilder() {
        return notificationBuilder;
    }
}
