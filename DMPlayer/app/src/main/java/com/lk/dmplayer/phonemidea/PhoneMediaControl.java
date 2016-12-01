package com.lk.dmplayer.phonemidea;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.lk.dmplayer.models.SongDetail;

import java.util.ArrayList;

/**
 * Created by Le Kham on 10/25/2016.
 */
public class PhoneMediaControl {

    private static PhoneMediaControl Instance = null;
    private Cursor cursor;

    public static enum SonLoadFor {
        All, Gener, Artis, Album, Musicintent, MostPlay, Favorite, ResecntPlay
    }

    public static PhoneMediaControl getInstance() {
        if (Instance == null)
            Instance = new PhoneMediaControl();
        return Instance;
    }

    public void loadMusicList(final Context context, final long id, final SonLoadFor sonLoadFor, final String path) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            ArrayList<SongDetail> songDetails = null;

            @Override
            protected Void doInBackground(Void... voids) {
                songDetails = getList(context, id, sonLoadFor, path);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (phoneMediaControlINterface != null)
                    phoneMediaControlINterface.loadSongsComplete(songDetails);
            }
        };
        task.execute();
    }

    private final String[] projectionSongs = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION};

    private ArrayList<SongDetail> getList(Context context, long id, SonLoadFor sonLoadFor, String path) {
        ArrayList<SongDetail> songDetails = new ArrayList<>();
        switch (sonLoadFor) {
            case All:
                String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
                String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
                cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projectionSongs, selection, null, sortOrder);
                songDetails = getSongFromCursor(cursor);
                break;
            case Album:
                break;
            case Artis:
                break;
            case Favorite:
                break;
            case Gener:
                break;
            case MostPlay:
                break;
            case Musicintent:
                break;
            case ResecntPlay:
                break;
        }
        return songDetails;
    }

    private ArrayList<SongDetail> getSongFromCursor(Cursor cursor) {
        ArrayList<SongDetail> songDetails = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                songDetails.add(new SongDetail(id, albumID, artist, title, path, display_name, duration));
            }

        }
        return songDetails;
    }

    public static PhoneMediaControlINterface phoneMediaControlINterface;

    public static void setPhoneMediaControlINterface(PhoneMediaControlINterface phoneMediaControlINterface) {
        PhoneMediaControl.phoneMediaControlINterface = phoneMediaControlINterface;
    }

    public interface PhoneMediaControlINterface {
        public void loadSongsComplete(ArrayList<SongDetail> songDetails);
    }
}
