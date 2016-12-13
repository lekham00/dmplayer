package com.lk.dmplayer.childfragment;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lk.dmplayer.R;
import com.lk.dmplayer.activities.AlbumAndArtisDetailsActivity;
import com.lk.dmplayer.adapter.CursorRecyclerViewAdapter;
import com.lk.dmplayer.manager.LKBaseChildFragment;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;
import com.lk.dmplayer.phonemidea.PhoneMediaControl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Le Kham on 11/30/2016.
 */
public class ChildFragmentAlbum extends LKBaseChildFragment {
    public static final String TAG = "ChildFragmentAlbum";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static ChildFragmentAlbum newInstance(Context context) {
        ChildFragmentAlbum childFragmentAlbum = new ChildFragmentAlbum();
        return childFragmentAlbum;
    }
    @Override
    public Cursor onCursor(AsyncQueryHandler asyncQueryHandler) {
        String[] column = {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.ALBUM_ART};
        Cursor cursor = null;
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        if (asyncQueryHandler != null) {
            asyncQueryHandler.startQuery(0, null, uri, column, null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        } else {
            cursor = DMPlayerUtility.query(getContext(), uri, column, null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER, 0);
        }
        return cursor;
    }

    @Override
    public int getIndexLine1(Cursor cursor) {
        if (cursor == null)
            return -1;
        return cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM);
    }

    @Override
    public int getIndexLine2(Cursor cursor) {
        if (cursor == null)
            return -1;
        return cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST);
    }

    @Override
    public int getIndexLine3(Cursor cursor) {
        return 0;
    }

    @Override
    public String getContentURI(Cursor cursor) {
        if (cursor == null)
            return null;
        return "content://media/external/audio/albumart/";
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onMoveDetail(long id, String title) {
        Intent intent = new Intent(getContext(), AlbumAndArtisDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(AlbumAndArtisDetailsActivity.ID, id);
        bundle.putInt(AlbumAndArtisDetailsActivity.TAGFOR, PhoneMediaControl.SonLoadFor.Album.ordinal());
        bundle.putString(AlbumAndArtisDetailsActivity.TITLE, title);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }
}
