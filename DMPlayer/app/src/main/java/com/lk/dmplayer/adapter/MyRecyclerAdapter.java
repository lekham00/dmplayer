package com.lk.dmplayer.adapter;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lk.dmplayer.R;
import com.lk.dmplayer.childfragment.ChildFragmentAlbum;
import com.lk.dmplayer.childfragment.ChildFragmentArtists;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by dlkham on 12/2/2016.
 */
public class MyRecyclerAdapter extends CursorRecyclerViewAdapter<MyRecyclerAdapter.ViewHolder> {

    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private int indexCursorLine1;
    private int indexCursorLine2;
    private int indexCursorLine3;
    private String contentURI;
    Context context;
    private Fragment fragment;

    public MyRecyclerAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.bg_default_album_art).showImageForEmptyUri(R.mipmap.bg_default_album_art).showImageOnFail(R.mipmap.bg_default_album_art).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        if (fragment instanceof ChildFragmentArtists) {
            updateUITextView(viewHolder.mTextViewLine1, indexCursorLine1, cursor.getString(indexCursorLine1), cursor);
            int album = cursor.getInt(indexCursorLine2);
            updateUITextView(viewHolder.mTextViewLine2, indexCursorLine2, album == 1 ? context.getResources().getString(R.string.one_album, 1) : context.getResources().getString(R.string.more_album, album), cursor);
            int song = cursor.getInt(indexCursorLine3);
            updateUITextView(viewHolder.mTextViewLine3, indexCursorLine3, " / " + (song == 1 ? context.getResources().getString(R.string.one_song, 1) : context.getResources().getString(R.string.more_song, song)), cursor);
            viewHolder.mIcon.setImageResource(R.mipmap.illo_default_artistradio_portrait);
        } else {
            updateUITextView(viewHolder.mTextViewLine1, indexCursorLine1, null, cursor);
            updateUITextView(viewHolder.mTextViewLine2, indexCursorLine2, null, cursor);
            imageLoader.displayImage(contentURI + cursor.getLong(0), viewHolder.mIcon, options);
        }
         viewHolder.mLlBottom.setVisibility(viewHolder.mTextViewLine2.getVisibility());
    }

    private void updateUITextView(TextView textView, int index, String value, Cursor cursor) {
        if (index == -1) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(value == null ? cursor.getString(index) : value);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_grild_item, parent, false));
        return viewHolder;
    }

    public void setIndexCursorLine1(int indexCursorLine1) {
        this.indexCursorLine1 = indexCursorLine1;
    }

    public void setIndexCursorLine2(int indexCursorLine2) {
        this.indexCursorLine2 = indexCursorLine2;
    }

    public void setContentURI(String contentURI) {
        this.contentURI = contentURI;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setIndexCursorLine3(int indexCursorLine3) {
        this.indexCursorLine3 = indexCursorLine3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIcon;
        private TextView mTextViewLine1;
        private TextView mTextViewLine2;
        private TextView mTextViewLine3;
        private LinearLayout mLlBottom;

        public ViewHolder(View view) {
            super(view);
            mIcon = (ImageView) view.findViewById(R.id.icon);
            mTextViewLine1 = (TextView) view.findViewById(R.id.item_line1);
            mTextViewLine2 = (TextView) view.findViewById(R.id.item_line2);
            mTextViewLine3 = (TextView) view.findViewById(R.id.item_line3);
            mLlBottom = (LinearLayout) view.findViewById(R.id.llBottom);
        }
    }
}
