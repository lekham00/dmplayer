package com.lk.dmplayer.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lk.dmplayer.R;

/**
 * Created by Kham on 12/22/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private String DATABASE_NAME;
    private int DATABASE_VERSION;
    private String DATABASE_PATH;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public DBHelper(Context context) {
        super(context, context.getResources().getString(R.string.DataBaseName), null, Integer.parseInt(context.getResources().getString(R.string.DataBaseName_Version)));
        DATABASE_NAME = context.getResources().getString(R.string.DataBaseName);
        DATABASE_VERSION = Integer.parseInt(context.getResources().getString(R.string.DataBaseName_Version));
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
        context.openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlForCreateMostPlay());
        sqLiteDatabase.execSQL(sqlForCreateFavoritePlay());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void openDatabase() throws SQLException {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    public SQLiteDatabase getDB() {
        return sqLiteDatabase;
    }

    public String sqlForCreateMostPlay() {
        String sql = "CREATE TABLE " + MostAndRecentPlayTableHelper.TABLENAME + " (" + MostAndRecentPlayTableHelper.ID + " INTEGER NOT NULL PRIMARY KEY,"
                + MostAndRecentPlayTableHelper.ALBUM_ID + " INTEGER NOT NULL,"

                + MostAndRecentPlayTableHelper.ARTIST + " TEXT NOT NULL,"
                + MostAndRecentPlayTableHelper.TITLE + " TEXT NOT NULL,"
                + MostAndRecentPlayTableHelper.DISPLAY_NAME + " TEXT NOT NULL,"
                + MostAndRecentPlayTableHelper.DURATION + " TEXT NOT NULL,"
                + MostAndRecentPlayTableHelper.PATH + " TEXT NOT NULL,"
                + MostAndRecentPlayTableHelper.AUDIOPROGRESS + " TEXT NOT NULL,"
                + MostAndRecentPlayTableHelper.AUDIOPROGRESSSEC + " INTEGER NOT NULL,"
                + MostAndRecentPlayTableHelper.LastPlayTime + " TEXT NOT NULL,"
                + MostAndRecentPlayTableHelper.PLAYCOUNT + " INTEGER NOT NULL);";
        return sql;
    }

    public String sqlForCreateFavoritePlay() {
        String sql = "CREATE TABLE " + FavoritePlayTableHelper.TABLENAME + " (" + FavoritePlayTableHelper.ID + " INTEGER NOT NULL PRIMARY KEY,"
                + FavoritePlayTableHelper.ALBUM_ID + " INTEGER NOT NULL,"

                + FavoritePlayTableHelper.ARTIST + " TEXT NOT NULL,"
                + FavoritePlayTableHelper.TITLE + " TEXT NOT NULL,"
                + FavoritePlayTableHelper.DISPLAY_NAME + " TEXT NOT NULL,"
                + FavoritePlayTableHelper.DURATION + " TEXT NOT NULL,"
                + FavoritePlayTableHelper.PATH + " TEXT NOT NULL,"
                + FavoritePlayTableHelper.AUDIOPROGRESS + " TEXT NOT NULL,"
                + FavoritePlayTableHelper.AUDIOPROGRESSSEC + " INTEGER NOT NULL,"
                + FavoritePlayTableHelper.LastPlayTime + " TEXT NOT NULL,"
                + FavoritePlayTableHelper.ISFAVORITE + " INTEGER NOT NULL);";
        return sql;
    }
}
