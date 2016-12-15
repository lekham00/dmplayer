package com.lk.dmplayer.manager;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.util.Log;
import android.widget.RemoteViews;

import com.lk.dmplayer.R;
import com.lk.dmplayer.models.SongDetail;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;
import com.lk.dmplayer.untilily.ApplicationDMPlayer;

/**
 * Created by Kham on 12/15/2016.
 */

public class MusicPlayerService extends Service {
    private final String TAG = getClass().getSimpleName();
    public static final String NOTIFY_PREVIOUS = "musicplayer.previous";
    public static final String NOTIFY_CLOSE = "musicplayer.close";
    public static final String NOTIFY_PAUSE = "musicplayer.pause";
    public static final String NOTIFY_PLAY = "musicplayer.play";
    public static final String NOTIFY_NEXT = "musicplayer.next";

    private AudioManager audioManager;
    private PhoneStateListener phoneStateListener;
    private RemoteControlClient remoteControlClient;
    private static boolean supportBigNotifications = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    private static boolean supportLockScreenControls = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            SongDetail songDetail = MediaController.getInstance().getPlayingSongDetail();
            if (songDetail == null) {
                DMPlayerUtility.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        stopSelf();
                    }
                });
                return START_STICKY;
            }
            ComponentName componentName = new ComponentName(getApplicationContext(), MusicPlayerReceiver.class.getName());
            if (remoteControlClient == null) {
                audioManager.registerMediaButtonEventReceiver(componentName);
                Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                mediaButtonIntent.setComponent(componentName);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
                remoteControlClient = new RemoteControlClient(pendingIntent);
                audioManager.registerRemoteControlClient(remoteControlClient);
            }
            remoteControlClient.setTransportControlFlags(RemoteControlClient.FLAG_KEY_MEDIA_PLAY | RemoteControlClient.FLAG_KEY_MEDIA_PAUSE | RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE | RemoteControlClient.FLAG_KEY_MEDIA_STOP | RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS
                    | RemoteControlClient.FLAG_KEY_MEDIA_NEXT);
            createNotification(songDetail);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage().toString());
        }
        return START_STICKY;

    }

    private void createNotification(SongDetail songDetail) {
        String songName = songDetail.getTitle();
        String authorName = songDetail.getArtist();
        RemoteViews simpleContentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.player_small_notification);
        RemoteViews expandedView = null;
        if (supportBigNotifications) {
            expandedView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.player_big_notification);
        }
        Intent intent = new Intent(ApplicationDMPlayer.applicationContext, ApplicationDMPlayer.class);
        intent.setAction("openplayer");
        intent.setFlags(32768);
        PendingIntent pendingIntent = PendingIntent.getActivity(ApplicationDMPlayer.applicationContext, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(getApplication()).setSmallIcon(R.mipmap.player).setContentIntent(pendingIntent).setContentTitle(songName).build();
        notification.contentView = simpleContentView;
        if (supportBigNotifications)
            notification.contentView = expandedView;

        startForeground(5, notification);

    }
}
