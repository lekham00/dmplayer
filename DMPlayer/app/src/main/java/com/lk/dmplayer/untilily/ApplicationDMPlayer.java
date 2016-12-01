package com.lk.dmplayer.untilily;

import android.app.Application;
import android.os.Handler;

/**
 * Created by Le Kham on 11/26/2016.
 */
public class ApplicationDMPlayer extends Application {
    public static Handler handler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(getMainLooper());
    }
}
