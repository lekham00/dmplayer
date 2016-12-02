package com.lk.dmplayer.adapter;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lk.dmplayer.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by dlkham on 12/2/2016.
 */
public class MyRecyclerAdapter extends CursorRecyclerViewAdapter<MyRecyclerAdapter.ViewHolder> {

    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private AsyncQueryHandler asyncQueryHandler;

    public MyRecyclerAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.bg_default_album_art).showImageForEmptyUri(R.mipmap.bg_default_album_art).showImageOnFail(R.mipmap.bg_default_album_art).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        this.asyncQueryHandler = new QueryHandler(getContext().getContentResolver());
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


    private class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
//            ini(cursor);
        }
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
