package com.lk.dmplayer.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.lk.dmplayer.models.SongDetail;

import java.util.ArrayList;

/**
 * Created by Le Kham on 11/23/2016.
 */
public class MusicPreferance {
    public static final String NAME_PREFERANCE = "DMPLAYER";
    private static final String LAST_SONG = "LastSong";
    private static final String REAPEAT_SONG = "ReapeatSong";
    private static final String SHUFFLE_SONG = "ShuffleSong";
    public static SongDetail playingSongDetail = null;
    public static ArrayList<SongDetail> arrayListSong = new ArrayList<>();

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(NAME_PREFERANCE, Activity.MODE_PRIVATE);
    }

    public static void saveLastSong(Context context, SongDetail songDetail) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        Gson gson = new Gson();
        editor.putString(LAST_SONG, gson.toJson(songDetail));
        editor.commit();
    }

    public static void getLastSong(Context context) {
        if (playingSongDetail == null) {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            String lastSong = sharedPreferences.getString(LAST_SONG, "");
            Gson gson = new Gson();
            playingSongDetail = gson.fromJson(lastSong, SongDetail.class);
        }
    }

    public static void saveRepeatModeSong(Context context, int reapeatMode) {
        SharedPreferences.Editor edit = getSharedPreferences(context).edit();
        edit.putInt(REAPEAT_SONG, reapeatMode).commit();
    }

    public static int getRepeatModeSong(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getInt(REAPEAT_SONG, 0);
    }

    public static void saveShuffleFlagSong(Context context, boolean shuffleFlag) {
        SharedPreferences.Editor edit = getSharedPreferences(context).edit();
        edit.putBoolean(SHUFFLE_SONG, shuffleFlag).commit();
    }

    public static boolean getShuffleFlagSong(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean(SHUFFLE_SONG, false);
    }
}
