package com.lk.dmplayer.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by Kham on 12/15/2016.
 */

public class MusicPlayerReceiver extends BroadcastReceiver {
    public final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            if (intent.getExtras() == null)
                return;
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
            if (keyEvent == null)
                return;
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    Log.d(TAG, "KEYCODE_MEDIA_PLAY_PAUSE");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    Log.d(TAG, "KEYCODE_MEDIA_PLAY");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    Log.d(TAG, "KEYCODE_MEDIA_PAUSE");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    Log.d(TAG, "KEYCODE_MEDIA_PREVIOUS");
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    Log.d(TAG, "KEYCODE_MEDIA_STOP");
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    Log.d(TAG, "KEYCODE_MEDIA_NEXT");
                    break;
            }
        } else {
            if (intent.getAction().equals(MusicPlayerService.NOTIFY_CLOSE)) {
                MediaController.getInstance().clearUpPlayer(true);
            } else if (intent.getAction().equals(MusicPlayerService.NOTIFY_NEXT)) {
                MediaController.getInstance().playNextSong(true);
            } else if (intent.getAction().equals(MusicPlayerService.NOTIFY_PAUSE)) {
                MediaController.getInstance().pauseAudio(MediaController.getInstance().getPlayingSongDetail());
            } else if (intent.getAction().equals(MusicPlayerService.NOTIFY_PLAY)) {
                MediaController.getInstance().playAudio(MediaController.getInstance().getPlayingSongDetail());
            } else if (intent.getAction().equals(MusicPlayerService.NOTIFY_PREVIOUS)) {
                MediaController.getInstance().playBackSong();
            }
        }
    }
}
