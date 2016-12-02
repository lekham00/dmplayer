package com.lk.dmplayer.manager;

import android.content.AsyncQueryHandler;
import android.database.Cursor;
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

import com.lk.dmplayer.R;
import com.lk.dmplayer.adapter.MyRecyclerAdapter;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;


/**
 * Created by dlkham on 12/2/2016.
 */
public class LKBaseChildFragment extends Fragment {
    public static final String TAG = "ChildFragmentAlbum";
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter = null;
    private Cursor cursorAlbum = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentchild, null);
        setUpView(view);
        return view;
    }
    private void setUpView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        populateData();
    }
    private void populateData() {
        adapter = (MyRecyclerAdapter) getActivity().getLastCustomNonConfigurationInstance();
        if (adapter == null) {
            adapter = new MyRecyclerAdapter(getActivity(),cursorAlbum);
            mRecyclerView.setAdapter(adapter);
            getAlbumCursor(adapter.getAsyncQueryHandler());
        } else {
            mRecyclerView.setAdapter(adapter);
            cursorAlbum = adapter.getCursor();
            if(cursorAlbum != null)
                ini(cursorAlbum);
        }
    }
    private Cursor getAlbumCursor(AsyncQueryHandler asyncQueryHandler) {
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

    private void ini(Cursor cursor) {
        if (getActivity() == null)
            return;
        adapter.changeCursor(cursor);
    }
}
