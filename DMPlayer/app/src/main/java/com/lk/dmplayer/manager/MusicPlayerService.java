package com.lk.dmplayer.manager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataEditor;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.telephony.PhoneStateListener;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.lk.dmplayer.R;
import com.lk.dmplayer.activities.DMPlayerBaseActivity;
import com.lk.dmplayer.models.SongDetail;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;
import com.lk.dmplayer.untilily.ApplicationDMPlayer;

/**
 * Created by Kham on 12/15/2016.
 */

public class MusicPlayerService extends Service implements AudioManager.OnAudioFocusChangeListener, NotificationManager.NotificationCenterDelegate {
    private final String TAG = getClass().getSimpleName();
    public static final String NOTIFY_PREVIOUS = "musicplayer.previous";
    public static final String NOTIFY_CLOSE = "musicplayer.close";
    public static final String NOTIFY_PAUSE = "musicplayer.pause";
    public static final String NOTIFY_PLAY = "musicplayer.play";
    public static final String NOTIFY_NEXT = "musicplayer.next";

    private AudioManager audioManager;
    private PhoneStateListener phoneStateListener;
    private RemoteControlClient remoteControlClient = null;
    private static boolean supportBigNotifications = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    private static boolean supportLockScreenControls = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    private ComponentName remoteComponentName;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        NotificationManager.getInstance().addObserver(this, NotificationManager.audioPlayStateChanged);
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
            if (supportLockScreenControls) {
                RegisterRemoteClient();
            }
            createNotification(songDetail);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage().toString());
        }
        return START_STICKY;

    }

    @SuppressLint("NewApi")
    private void createNotification(SongDetail songDetail) {
        String songName = songDetail.getTitle();
        String authorName = songDetail.getArtist();
        RemoteViews simpleContentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.player_small_notification);
        RemoteViews expandedView = null;
        if (supportBigNotifications) {
            expandedView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.player_big_notification);
        }
        Intent intent = new Intent(ApplicationDMPlayer.applicationContext, DMPlayerBaseActivity.class);
//        intent.setAction("openplayer");
//        intent.setFlags(32768);
        PendingIntent pendingIntent = PendingIntent.getActivity(ApplicationDMPlayer.applicationContext, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(getApplication()).setSmallIcon(R.mipmap.player).setContentIntent(pendingIntent).setContentTitle(songName).build();
        notification.contentView = simpleContentView;

        setListener(simpleContentView);
        if (supportBigNotifications)
            setListener(expandedView);

        if (supportBigNotifications)
            notification.bigContentView = expandedView;
        Bitmap bitmap = songDetail != null ? songDetail.getSmallCover(ApplicationDMPlayer.applicationContext) : null;
        if (bitmap != null) {
            notification.contentView.setImageViewBitmap(R.id.player_album_art, bitmap);
            if (supportBigNotifications) {
                notification.bigContentView.setImageViewBitmap(R.id.player_album_art, bitmap);
            }
        } else {
            notification.contentView.setImageViewResource(R.id.player_album_art, R.mipmap.bg_default_album_art);
            if (supportBigNotifications) {
                notification.bigContentView.setImageViewResource(R.id.player_album_art, R.mipmap.bg_default_album_art);
            }
        }
        notification.contentView.setViewVisibility(R.id.player_progress_bar, View.GONE);
        notification.contentView.setViewVisibility(R.id.player_next, View.VISIBLE);
        notification.contentView.setViewVisibility(R.id.player_previous, View.VISIBLE);

        if (MediaController.getInstance().isPauseAudio()) {
            notification.contentView.setViewVisibility(R.id.player_pause, View.GONE);
            notification.contentView.setViewVisibility(R.id.player_play, View.VISIBLE);
            if (supportBigNotifications) {
                notification.bigContentView.setViewVisibility(R.id.player_pause, View.GONE);
                notification.bigContentView.setViewVisibility(R.id.player_play, View.VISIBLE);
            }
        } else {
            notification.contentView.setViewVisibility(R.id.player_pause, View.VISIBLE);
            notification.contentView.setViewVisibility(R.id.player_play, View.GONE);
            if (supportBigNotifications) {
                notification.bigContentView.setViewVisibility(R.id.player_pause, View.VISIBLE);
                notification.bigContentView.setViewVisibility(R.id.player_play, View.GONE);
            }
        }

        if (supportBigNotifications) {
            notification.bigContentView.setViewVisibility(R.id.player_next, View.VISIBLE);
            notification.bigContentView.setViewVisibility(R.id.player_previous, View.VISIBLE);
            notification.bigContentView.setViewVisibility(R.id.player_progress_bar, View.GONE);
        }
        notification.contentView.setTextViewText(R.id.player_song_name, songName);
        notification.contentView.setTextViewText(R.id.player_author_name, authorName);
        if (supportBigNotifications) {
            notification.bigContentView.setTextViewText(R.id.player_song_name, songName);
            notification.bigContentView.setTextViewText(R.id.player_author_name, authorName);
        }
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        startForeground(10, notification);
        UpdateMetadata(songDetail);

    }

    private void setListener(RemoteViews views) {
        setListener(views, new Intent(NOTIFY_PREVIOUS), R.id.player_previous);
        setListener(views, new Intent(NOTIFY_CLOSE), R.id.player_close);
        setListener(views, new Intent(NOTIFY_NEXT), R.id.player_next);
        setListener(views, new Intent(NOTIFY_PAUSE), R.id.player_pause);
        setListener(views, new Intent(NOTIFY_PLAY), R.id.player_play);
    }

    private void setListener(RemoteViews views, Intent intent, int viewID) {
        try {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(viewID, pendingIntent);
        } catch (Exception ex) {
        }

    }

    private void RegisterRemoteClient() {
        remoteComponentName = new ComponentName(getApplicationContext(), MusicPlayerReceiver.class.getName());
        try {
            if (remoteControlClient == null) {
                audioManager.registerMediaButtonEventReceiver(remoteComponentName);
                Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                mediaButtonIntent.setComponent(remoteComponentName);
                PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
                remoteControlClient = new RemoteControlClient(mediaPendingIntent);
                audioManager.registerRemoteControlClient(remoteControlClient);
            }
            remoteControlClient.setTransportControlFlags(
                    RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
                            RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_STOP |
                            RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS |
                            RemoteControlClient.FLAG_KEY_MEDIA_NEXT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void UpdateMetadata(SongDetail songDetail) {
        if (remoteControlClient != null) {
            RemoteControlClient.MetadataEditor metadataEditor = remoteControlClient.editMetadata(true);
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, songDetail.getArtist());
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, songDetail.getTitle());
            if (songDetail != null && songDetail.getSmallCover(getApplication()) != null) {
                metadataEditor.putBitmap(RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, songDetail.getSmallCover(getApplication()));
            }
            metadataEditor.apply();
            audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }

    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationManager.audioPlayStateChanged) {
            SongDetail songDetail = (SongDetail) args[0];
            if (songDetail != null)
                createNotification((SongDetail) args[0]);
            else
                stopSelf();
        }
    }

    @Override
    public void newSongLoaded(Object... args) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationManager.getInstance().removeObserver(this, NotificationManager.audioPlayStateChanged);
    }
}
