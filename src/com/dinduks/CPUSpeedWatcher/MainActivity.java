package com.dinduks.CPUSpeedWatcher;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private static final int NOTIFICATION_ID = 42;
    private String CUR = "cur";
    private String MIN = "min";
    private String MAX = "max";
    private HashMap<String, String> frequenciesFilesNames = new HashMap<String, String>() {{
        this.put(CUR, "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
        this.put(MIN, "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq");
        this.put(MAX, "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
    }};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        HashMap<String, FileInputStream> frequenciesFiles = new HashMap<String, FileInputStream>();
        HashMap<String, Float> frequencies = new HashMap<String, Float>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            frequenciesFiles.put(CUR, new FileInputStream(frequenciesFilesNames.get(CUR)));
            frequenciesFiles.put(MIN, new FileInputStream(frequenciesFilesNames.get(MIN)));
            frequenciesFiles.put(MAX, new FileInputStream(frequenciesFilesNames.get(MAX)));

            for (Map.Entry<String, FileInputStream> entry : frequenciesFiles.entrySet())
                frequencies.put(entry.getKey(), streamToInt(entry.getValue()) / 1000);

            String contentText = String.format("Min: %.0f Mhz / Cur: %.0fMhz / Max: %.0fMhz",
                    (frequencies.get(MIN)),
                    (frequencies.get(CUR)),
                    (frequencies.get(MAX)));

            createNotification(
                    new Notification.Builder(this)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle("CPU Speed Watcher")
                            .setContentText(contentText)
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createNotification(Notification.Builder mBuilder) {
        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    // From http://stackoverflow.com/a/5445161/604041
    public static Float streamToInt(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? new Float(s.next()) : 0;
    }
}
