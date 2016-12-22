package com.lk.dmplayer.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.lk.dmplayer.models.SongDetail;
import com.lk.dmplayer.phonemidea.PhoneMediaControl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Le Kham on 11/23/2016.
 */
public class MusicPreferance {
    public static final String NAME_PREFERANCE = "DMPLAYER";
    private static final String LAST_SONG = "LastSong";
    private static final String REAPEAT_SONG = "ReapeatSong";
    private static final String SHUFFLE_SONG = "ShuffleSong";
    private static final String LAST_SONG_LIST_TYPE = "LastSongListType";
    private static final String LAST_SONG_LIST_ID = "LastSongListId";
    private static final String LAST_SONG_POSITION = "LastSongPosition";
    private static final String LAST_SONG_PATH = "LastSongPath";
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

    public static SongDetail getLastSong(Context context) {
        if (playingSongDetail == null) {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            String lastSong = sharedPreferences.getString(LAST_SONG, "");
            Gson gson = new Gson();
            playingSongDetail = gson.fromJson(lastSong, SongDetail.class);
        }
        return playingSongDetail;
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
    public static void saveLastSongListType(Context context , int type)
    {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(LAST_SONG_LIST_TYPE,type).commit();
    }

    public static int getLastSongListType(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getInt(LAST_SONG_LIST_TYPE, 0);
    }

    public static void saveLastSongListId(Context context, long id) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(LAST_SONG_LIST_ID, id).commit();
    }

    public static long getLastSongListId(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getLong(LAST_SONG_LIST_ID, -1);
    }

    public static void saveLastSongPosition(Context context, int position) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(LAST_SONG_POSITION, position).commit();
    }

    public static int getLastSongPosition(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getInt(LAST_SONG_POSITION, 0);
    }

    public static void saveLastSongPath(Context context, String path) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(LAST_SONG_PATH, path).commit();
    }

    public static String getLastSongPath(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(LAST_SONG_PATH, "");
    }
    public static ArrayList<SongDetail> getListSongDetail(Context context)
    {
        if(arrayListSong == null || arrayListSong.isEmpty())
        {
            MediaController.getInstance().currentIndexSong = getLastSongPosition(context);
            MediaController.getInstance().mId = getLastSongListId(context);
            MediaController.getInstance().mType = getLastSongListType(context);
            MediaController.getInstance().mPath =getLastSongPath(context);
            arrayListSong = PhoneMediaControl.getInstance().getList(context,MediaController.getInstance().mId,PhoneMediaControl.SonLoadFor.values()[MediaController.getInstance().mType] ,MediaController.getInstance().mPath);
        }
        return arrayListSong;
    }
}
