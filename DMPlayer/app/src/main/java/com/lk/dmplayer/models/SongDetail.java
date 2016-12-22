package com.lk.dmplayer.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

/**
 * Created by Le Kham on 10/24/2016.
 */
public class SongDetail {
    private int id;
    private int album_id;
    private String artist;
    private String title;
    private String display_name;
    private String duration;
    private String path;
    private float audioProgress = 0.0f;
    private int audioProgressSec = 0;

    public SongDetail(int _id, int album_id, String _artist, String _title, String _path, String _display_name, String _duration) {
        this.id = _id;
        this.album_id = album_id;
        this.artist = _artist;
        this.title = _title;
        this.path = _path;
        this.display_name = _display_name;
        this.duration = TextUtils.isEmpty(_duration) ? "0" : _duration;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float getAudioProgress() {
        return audioProgress;
    }

    public void setAudioProgress(float audioProgress) {
        this.audioProgress = audioProgress;
    }

    public int getAudioProgressSec() {
        return audioProgressSec;
    }

    public void setAudioProgressSec(int audioProgressSec) {
        this.audioProgressSec = audioProgressSec;
    }


    public Bitmap getSmallCover(Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap bitmap = null;

        try {
            Uri uri = Uri.parse("content://media/external/audio/media/" + getId() + "/albumart");
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            if (parcelFileDescriptor != null) {
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
