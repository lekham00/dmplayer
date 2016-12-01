package com.lk.dmplayer.childfragment;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
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
import com.lk.dmplayer.adapter.CursorRecyclerViewAdapter;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Le Kham on 11/30/2016.
 */
public class ChildFragmentAlbum extends Fragment {
    public static final String TAG = "ChildFragmentAlbum";
    private RecyclerView mRecyclerView;
    private AlbumRecyclerAdapter adapter = null;
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

    public static ChildFragmentAlbum newInstance(Context context) {
        ChildFragmentAlbum childFragmentAlbum = new ChildFragmentAlbum();
        return childFragmentAlbum;
    }

    private void setUpView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        populateData();
    }

    private void populateData() {
        adapter = (AlbumRecyclerAdapter) getActivity().getLastCustomNonConfigurationInstance();
        if (adapter == null) {
            adapter = new AlbumRecyclerAdapter(getContext(), cursorAlbum);
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

    public class AlbumRecyclerAdapter extends CursorRecyclerViewAdapter<AlbumRecyclerAdapter.ViewHolder> {
        private DisplayImageOptions options;
        private ImageLoader imageLoader = ImageLoader.getInstance();
        private AsyncQueryHandler asyncQueryHandler;

        protected AlbumRecyclerAdapter(Context context, Cursor cursor) {
            super(context, cursor);
            this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.bg_default_album_art).showImageForEmptyUri(R.mipmap.bg_default_album_art).showImageOnFail(R.mipmap.bg_default_album_art).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            this.asyncQueryHandler = new QueryHandler(getContext().getContentResolver());
        }

        private class QueryHandler extends AsyncQueryHandler {
            public QueryHandler(ContentResolver cr) {
                super(cr);
            }

            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                super.onQueryComplete(token, cookie, cursor);
                ini(cursor);
            }
        }

        @Override
        public void changeCursor(Cursor cursor) {
            if (getActivity().isFinishing() && cursor != null) {
                cursor.close();
                cursor = null;
            }
            if (cursor != cursorAlbum) {
                cursorAlbum = cursor;
                super.changeCursor(cursor);
            }

        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
            viewHolder.mTextViewAlbumName.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
            viewHolder.mTextViewArtistName.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)));
            String contentURI = "content://media/external/audio/albumart/" + cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
            imageLoader.displayImage(contentURI, viewHolder.mIcon, options);
        }

        public AsyncQueryHandler getAsyncQueryHandler() {
            return asyncQueryHandler;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_grild_item, parent, false));
            return viewHolder;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView mIcon;
            private TextView mTextViewAlbumName;
            private TextView mTextViewArtistName;

            public ViewHolder(View view) {
                super(view);
                mIcon = (ImageView) view.findViewById(R.id.icon);
                mTextViewAlbumName = (TextView) view.findViewById(R.id.item_line1);
                mTextViewArtistName = (TextView) view.findViewById(R.id.item_line2);
            }
        }
    }
}
