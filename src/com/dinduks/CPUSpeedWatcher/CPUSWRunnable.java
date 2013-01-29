package com.dinduks.CPUSpeedWatcher;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;

class CPUSWRunnable implements Runnable {
    private int curFrequency;
    private int minFrequency;
    private int maxFrequency;

    private MainActivity activity;

    public CPUSWRunnable(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            curFrequency = Util.getFrequencyFromResId(activity.getResources(), R.string.cur_freq_filename);
            minFrequency = Util.getFrequencyFromResId(activity.getResources(), R.string.min_freq_filename);
            maxFrequency = Util.getFrequencyFromResId(activity.getResources(), R.string.max_freq_filename);

            activity.getNotificationBuilder().setContentText(String.format("Min: %d / Cur: %d / Max: %d",
                    minFrequency, curFrequency, maxFrequency));

            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(this.hashCode(), activity.getNotificationBuilder().build());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

