package com.lk.dmplayer.untilily;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.lk.dmplayer.db.DBHelper;

/**
 * Created by Le Kham on 11/26/2016.
 */
public class ApplicationDMPlayer extends Application {
    public static Handler handler = null;
    public static Context applicationContext = null;
    public DBHelper _DBHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        handler = new Handler(applicationContext.getMainLooper());
        initilizeDB();
    }

    private void initilizeDB() {
        if (_DBHelper == null) {
            _DBHelper = new DBHelper(applicationContext);
        }
        _DBHelper.getWritableDatabase();
        _DBHelper.openDatabase();
    }
}
