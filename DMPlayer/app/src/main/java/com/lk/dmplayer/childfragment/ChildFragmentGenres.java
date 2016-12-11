package com.lk.dmplayer.childfragment;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lk.dmplayer.R;
import com.lk.dmplayer.activities.AlbumAndArtisDetailsActivity;
import com.lk.dmplayer.manager.LKBaseChildFragment;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;
import com.lk.dmplayer.phonemidea.PhoneMediaControl;

/**
 * Created by dlkham on 12/2/2016.
 */
public class ChildFragmentGenres extends LKBaseChildFragment {
    @Override
    public Cursor onCursor(AsyncQueryHandler async) {
        String[] cols = new String[]{MediaStore.Audio.Genres._ID, MediaStore.Audio.Genres.NAME};

        Uri uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
        Cursor ret = null;
        if (async != null) {
            async.startQuery(0, null, uri, cols, null, null, null);
        } else {
            ret = DMPlayerUtility.query(getActivity(), uri, cols, null, null, null,0);
        }
        return ret;
    }

    @Override
    public int getIndexLine1(Cursor cursor) {
        if(cursor == null)
            return -1;
        return cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
    }

    @Override
    public int getIndexLine2(Cursor cursor) {
        return -1;
    }

    @Override
    public int getIndexLine3(Cursor cursor) {
        return -1;
    }

    @Override
    public String getContentURI(Cursor cursor) {
        return null;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onMoveDetail(long id, int position) {
        Intent intent = new Intent(getContext(), AlbumAndArtisDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(AlbumAndArtisDetailsActivity.ID, id);
        bundle.putInt(AlbumAndArtisDetailsActivity.POSITION, position);
        bundle.putInt(AlbumAndArtisDetailsActivity.TAGFOR, PhoneMediaControl.SonLoadFor.Gener.ordinal());
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

    public static ChildFragmentGenres newInstance(Context context) {
        return new ChildFragmentGenres();
    }
}
