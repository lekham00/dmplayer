package com.lk.dmplayer.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.lk.dmplayer.models.SongDetail;
import com.lk.dmplayer.untilily.ApplicationDMPlayer;

import java.util.ArrayList;

/**
 * Created by Kham on 12/22/2016.
 */

public class FavoritePlayTableHelper {
    public static final String TABLENAME = "ResentPlay";

    public static final String ID = "_id";
    public static final String ALBUM_ID = "album_id";
    public static final String ARTIST = "artist";
    public static final String TITLE = "title";
    public static final String DISPLAY_NAME = "display_name";
    public static final String DURATION = "duration";
    public static final String PATH = "path";
    public static final String AUDIOPROGRESS = "audioProgress";
    public static final String AUDIOPROGRESSSEC = "audioProgressSec";
    public static final String LastPlayTime = "lastplaytime";
    public static final String ISFAVORITE = "isfavorite";

    private Context context;
    public static FavoritePlayTableHelper mInstance;
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public static FavoritePlayTableHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FavoritePlayTableHelper(context);
        }
        return mInstance;
    }

    public FavoritePlayTableHelper(Context context) {
        this.context = context;
        if (dbHelper == null)
            dbHelper = ((ApplicationDMPlayer) context.getApplicationContext())._DBHelper;
        sqLiteDatabase = dbHelper.getDB();
    }

    public void insertSong(SongDetail songDetail, int isFav) {
        sqLiteDatabase.beginTransaction();
        String sql = "Insert or Replace into " + TABLENAME + " " +
                "values(?,?,?,?,?,?,?,?,?,?,?);";
        SQLiteStatement sqLInsert = sqLiteDatabase.compileStatement(sql);
        try {

            if (songDetail != null) {
                sqLInsert.clearBindings();
                sqLInsert.bindLong(1, songDetail.getId());
                sqLInsert.bindLong(2, songDetail.getAlbum_id());
                sqLInsert.bindString(3, songDetail.getArtist());
                sqLInsert.bindString(4, songDetail.getTitle());
                sqLInsert.bindString(5, songDetail.getDisplay_name());
                sqLInsert.bindString(6, songDetail.getDuration());
                sqLInsert.bindString(7, songDetail.getPath());
                sqLInsert.bindString(8, songDetail.getAudioProgress() + "");
                sqLInsert.bindString(9, songDetail.getAudioProgressSec() + "");
                sqLInsert.bindString(10, System.currentTimeMillis() + "");
                sqLInsert.bindLong(11, isFav);
                sqLInsert.execute();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    public Cursor getCursorListSongFavorite() {
        Cursor cursor = null;
        try {
            String sql = "Select * from " + TABLENAME + " where " + ISFAVORITE + "=1";
            cursor = sqLiteDatabase.rawQuery(sql, null);
        } catch (Exception ex) {
            closeCursor(cursor);
            ex.printStackTrace();
        }
        return cursor;
    }

    public boolean isCheckSongFav(long id) {
        Cursor cursor = null;
        try {
            String sql = "Select * from " + TABLENAME + " where " + ID + "=" + id + " and " + ISFAVORITE + "=1";
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor.getCount() > 0)
                return true;
        } catch (Exception ex) {
            closeCursor(cursor);
            ex.printStackTrace();
        }
        return false;
    }
}
