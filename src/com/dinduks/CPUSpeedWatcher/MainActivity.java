package com.dinduks.CPUSpeedWatcher;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent resultIntent;
        TaskStackBuilder stackBuilder;

        final PendingIntent resultPendingIntent;
        final Notification.Builder notificationBuilder;

        super.onCreate(savedInstanceState);

        resultIntent = new Intent(this, MainActivity.class);
        stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class).addNextIntent(resultIntent);

        resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notif_area_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notif_drawer_icon))
                .setContentTitle("CPU Speed Watcher");

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            private int curFrequency;
            private int minFrequency;
            private int maxFrequency;

            @Override
            public void run() {
                try {
                    curFrequency = getFrequencyFromResId(R.string.cur_freq_filename);
                    minFrequency = getFrequencyFromResId(R.string.min_freq_filename);
                    maxFrequency = getFrequencyFromResId(R.string.max_freq_filename);

                    notificationBuilder.setContentText(String.format("Min: %d / Cur: %d / Max: %d",
                            minFrequency, curFrequency, maxFrequency));

                    notificationBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(this.hashCode(), notificationBuilder.build());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

        finish();
    }

    private int getFrequencyFromResId(int resId) throws FileNotFoundException {
        return streamToInt(new FileInputStream(getString(resId))) / 1000;
    }

    // Inspired from: http://stackoverflow.com/a/5445161/604041
    public static int streamToInt(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\n");
        return s.hasNext() ? Integer.parseInt(s.next()) : 0;
    }
}
