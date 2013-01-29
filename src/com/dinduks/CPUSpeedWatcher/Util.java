package com.dinduks.CPUSpeedWatcher;

import android.content.res.Resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Util {
    /**
     * Given a resource ID, this method the CPU frenquency in Mhz stored in that resource
     *
     * @param resources The resources of the activity
     * @param resId     The resource ID
     * @return The CPU frequency in Mhz
     * @throws FileNotFoundException
     */
    protected static int getFrequencyFromResId(Resources resources, int resId) throws FileNotFoundException {
        return streamToInt(new FileInputStream(resources.getString(resId))) / 1000;
    }

    /**
     * Returns an {@code int} contained in an {@code InputStream}
     * Inspired from: http://stackoverflow.com/a/5445161/604041
     *
     * @param inputStream An {@code InputStream} containing an integer value
     * @return The integer contained in the {@code InputStream}
     */
    public static int streamToInt(java.io.InputStream inputStream) {
        java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\n");
        return s.hasNext() ? Integer.parseInt(s.next()) : 0;
    }
}
