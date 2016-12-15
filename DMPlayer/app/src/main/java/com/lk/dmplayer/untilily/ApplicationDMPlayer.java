package com.lk.dmplayer.untilily;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by Le Kham on 11/26/2016.
 */
public class ApplicationDMPlayer extends Application {
    public static Handler handler = null;
    public static Context applicationContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        handler = new Handler(applicationContext.getMainLooper());
    }
}
