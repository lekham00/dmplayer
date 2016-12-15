package com.lk.dmplayer.manager;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.lk.dmplayer.models.SongDetail;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;
import com.lk.dmplayer.untilily.ApplicationDMPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Le Kham on 11/21/2016.
 */
public class MediaController implements SensorEventListener {

    public static MediaController Instance = null;

    private MediaPlayer mediaPlayer = null;
    private boolean isPause = true;
    private Timer progressTimer = null;
    private int currentIndexSong;
    private boolean shuffleMusic = false;
    private int repeatMode = 0;
    public static final int NON_REPEAT = 0;
    public static final int ALWAYS_REPEAT = 1;
    public static final int ONE_REPEAT = 2;
    private int mCountRepeat = 0;

    public static MediaController getInstance() {
        if (Instance == null) {
            Instance = new MediaController();
        }
        return Instance;
    }

    public boolean setPlayerList(ArrayList<SongDetail> songDetails, SongDetail songDetail) {
        if (MusicPreferance.playingSongDetail == songDetail) {
            return playAudio(songDetail);
        }
        if (songDetails != null && songDetails.size() > 0) {
            MusicPreferance.arrayListSong.clear();
            MusicPreferance.arrayListSong.addAll(songDetails);
        }
        currentIndexSong = MusicPreferance.arrayListSong.indexOf(songDetail);
        return playAudio(songDetail);
    }

    public boolean playAudio(SongDetail songDetail) {
        if (songDetail == null)
            return false;
        if (mediaPlayer != null && MusicPreferance.playingSongDetail != null && MusicPreferance.playingSongDetail.getId() == songDetail.getId()) {
            if (isPause) {
                resumeAudio();
            }
            return true;
        }
        clearUpPlayer();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(songDetail.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (currentIndexSong < MusicPreferance.arrayListSong.size() - 1)
                        playNextSong(false);
                    else
                        clearUpPlayer();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        startProgressTime();
        isPause = false;
        MusicPreferance.playingSongDetail = songDetail;
        currentIndexSong = MusicPreferance.arrayListSong.indexOf(songDetail);
        NotificationManager.getInstance().postNotificationName(NotificationManager.audioDidStarted, songDetail);
        if (MusicPreferance.playingSongDetail != null) {
            Intent intent = new Intent(ApplicationDMPlayer.applicationContext, MusicPlayerService.class);
            ApplicationDMPlayer.applicationContext.startService(intent);
        } else {
            Intent intent = new Intent(ApplicationDMPlayer.applicationContext, MusicPlayerService.class);
            ApplicationDMPlayer.applicationContext.stopService(intent);
        }
        return true;
    }

    public boolean pauseAudio() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.pause();
            } catch (Exception ex) {
            }
        }
        isPause = true;
        return true;
    }

    public boolean resumeAudio() {
        if (mediaPlayer != null) {
            startProgressTime();
            try {
                mediaPlayer.start();
                isPause = false;
                NotificationManager.getInstance().postNotificationName(NotificationManager.audioPlayStateChanged, MusicPreferance.playingSongDetail);
            } catch (Exception ex) {
            }
        }

        return true;
    }

    public boolean isPauseAudio() {
        return isPause;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public SongDetail getPlayingSongDetail() {
        return MusicPreferance.playingSongDetail;
    }

    private void clearUpPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception ex) {
            }
        }
    }

    private void startProgressTime() {
        if (progressTimer != null) {
            progressTimer.cancel();
            progressTimer = null;
        }
        progressTimer = new Timer();
        progressTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                DMPlayerUtility.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (MusicPreferance.playingSongDetail != null && mediaPlayer != null && !isPause) {
                            int progress = 0;
                            float value;
                            if (mediaPlayer != null) {
                                progress = mediaPlayer.getCurrentPosition();
                                value = (float) progress / (float) mediaPlayer.getDuration();
                                if (progress >= mediaPlayer.getDuration())
                                    return;
                                MusicPreferance.playingSongDetail.setAudioProgress(value);
                                MusicPreferance.playingSongDetail.setAudioProgressSec(progress / 1000);
                                NotificationManager.getInstance().postNotificationName(NotificationManager.audioProgressDidChanged, MusicPreferance.playingSongDetail);
                            }
                        }
                    }
                });
            }
        }, 0, 17);
    }

    public boolean seekToProgress(int value) {
        try {
            if (mediaPlayer != null) {
                int seek = (int) (mediaPlayer.getDuration() * ((Float.valueOf(value) / 100)));
                mediaPlayer.seekTo(seek);
            }
        } catch (Exception ex) {

        }
        return true;
    }

    public void playNextSong(boolean isNext) {
        MusicPreferance.playingSongDetail.setAudioProgressSec(0);
        MusicPreferance.playingSongDetail.setAudioProgress(0);
        if (!isNext) {
            if (repeatMode == ONE_REPEAT && mCountRepeat == 0 || repeatMode == ALWAYS_REPEAT) {
                clearUpPlayer();
                playAudio(MusicPreferance.arrayListSong.get(currentIndexSong));
                if (repeatMode == ONE_REPEAT) {
                    mCountRepeat++;
                }
                return;
            } else {
                if (repeatMode == ONE_REPEAT)
                    mCountRepeat = 0;
            }

        } else
            mCountRepeat = 0;

        currentIndexSong++;
        if (currentIndexSong > MusicPreferance.arrayListSong.size() - 1) {
            currentIndexSong = 0;
        }
        playAudio(MusicPreferance.arrayListSong.get(shuffleMusic ? getIndexSongShuffle() : currentIndexSong));
    }

    public void playBackSong() {
        currentIndexSong--;
        if (currentIndexSong < 0) {
            currentIndexSong = MusicPreferance.arrayListSong.size() - 1;
        }
        MusicPreferance.playingSongDetail.setAudioProgressSec(0);
        MusicPreferance.playingSongDetail.setAudioProgress(0);
        playAudio(MusicPreferance.arrayListSong.get(shuffleMusic ? getIndexSongShuffle() : currentIndexSong));
    }

    public boolean isShuffleMusic() {
        return shuffleMusic;
    }

    public void setShuffleMusic(boolean shuffleMusic) {
        this.shuffleMusic = shuffleMusic;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(int repeatMode) {
        mCountRepeat = 0;
        this.repeatMode = repeatMode;
    }

    private int getIndexSongShuffle() {
        Random random = new Random();
        return random.nextInt((MusicPreferance.arrayListSong.size() - 1) - 0 + 1) + 0;
    }
}
