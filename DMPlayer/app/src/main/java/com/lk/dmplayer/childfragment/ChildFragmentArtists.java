package com.lk.dmplayer.childfragment;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lk.dmplayer.R;
import com.lk.dmplayer.manager.LKBaseChildFragment;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;

/**
 * Created by dlkham on 12/2/2016.
 */
public class ChildFragmentArtists extends LKBaseChildFragment {
    public static final String TAG = "ChildFragmentArtists";
    public Fragment fragment;
    @Override
    public Cursor onCursor(AsyncQueryHandler asyncQueryHandler) {
        String[] column = {MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists.NUMBER_OF_ALBUMS, MediaStore.Audio.Artists.NUMBER_OF_TRACKS, MediaStore.Audio.Artists._ID};
        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor cursor = null;
        if (asyncQueryHandler != null) {
            asyncQueryHandler.startQuery(0, null, uri, column, null, null, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);
        } else {
            cursor = DMPlayerUtility.query(getActivity(), uri, column, null, null, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER, 0);
        }

        return cursor;
    }

    @Override
    public int getIndexLine1(Cursor cursor) {
        if (cursor == null)
            return -1;
        return cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST);
    }

    @Override
    public int getIndexLine2(Cursor cursor) {
        if (cursor == null)
            return -1;
        return cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
    }

    @Override
    public int getIndexLine3(Cursor cursor) {
        if (cursor == null)
            return -1;
        return cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
    }

    @Override
    public String getContentURI(Cursor cursor) {
            return null;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    public static ChildFragmentArtists newInstance(Context context) {
        return new ChildFragmentArtists();
    }
}
