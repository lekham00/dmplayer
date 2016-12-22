package com.lk.dmplayer.activities;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lk.dmplayer.R;
import com.lk.dmplayer.adapter.SongsListAdapter;
import com.lk.dmplayer.models.SongDetail;
import com.lk.dmplayer.observablelib.ScrollUtils;
import com.lk.dmplayer.phonemidea.PhoneMediaControl;
import com.lk.dmplayer.uicomponent.ExpandableHeightListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by dlkham on 12/6/2016.
 */
public class AlbumAndArtisDetailsActivity extends MainActivity {
    public final String TAG = getClass().getSimpleName();
    public static final String ID = "ID";
    public static final String POSITION = "POSITION";
    public static final String TAGFOR = "TAG_FOR";
    public static final String TITLE = "TITLE";
    private ImageView mImgBanner;
    private TextView mTvAlbumName;
    private TextView mTvArtistName;
    private  TextView mTvNumberSong;
    private long id ;
    private int tagFor;
    private String title;
    private String artistName;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ExpandableHeightListView recycler_songslist;
    private SongsListAdapter songsListAdapter;
    private ScrollView scrollView;
    private int color = 0xFFFFFF;
    private int mParallaxImageHeight;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_albumandartisdetails);
        super.onCreate(savedInstanceState);
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        ini();
        getInfoDetailFromBundle();
    }

    private void ini()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mImgBanner = (ImageView) findViewById(R.id.banner);
        mTvAlbumName = (TextView) findViewById(R.id.tv_albumname);
        mTvArtistName = (TextView) findViewById(R.id.tv_artist_name);
        mTvNumberSong= (TextView) findViewById(R.id.tv_number_song);
        recycler_songslist = (ExpandableHeightListView) findViewById(R.id.recycler_allSongs);
        songsListAdapter = new SongsListAdapter(this);
        recycler_songslist.setAdapter(songsListAdapter);
        scrollView = (ScrollView) findViewById(R.id.scroll);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); // For ScrollView
                int scrollX = scrollView.getScrollX(); // For HorizontalScrollView
                Log.d(TAG, "scrollX : " + scrollX + " scrollY: " + scrollY );
                int baseColor = color;
                float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
            }
        });
        // Setup RecyclerView inside drawer
        mParallaxImageHeight = (int) getResources().getDimension(R.dimen.parallax_image_height);
        final TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        color = typedValue.data;
        mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0,color));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
        finish();
    }

    private void getInfoDetailFromBundle() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong(ID);
        tagFor = bundle.getInt(TAGFOR);
        title = bundle.getString(TITLE);
        mTvAlbumName.setText(title);
        mTvArtistName.setText(artistName);
        if (tagFor == PhoneMediaControl.SonLoadFor.Album.ordinal()) {
            this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.bg_default_album_art).showImageForEmptyUri(R.mipmap.bg_default_album_art).showImageOnFail(R.mipmap.bg_default_album_art).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
            loadData(id, PhoneMediaControl.SonLoadFor.Album);
        } else if (tagFor == PhoneMediaControl.SonLoadFor.Artis.ordinal()) {
            this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.illo_default_artistradio_portrait).showImageForEmptyUri(R.mipmap.illo_default_artistradio_portrait).showImageOnFail(R.mipmap.illo_default_artistradio_portrait).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
            loadData(id, PhoneMediaControl.SonLoadFor.Artis);
        } else if (tagFor == PhoneMediaControl.SonLoadFor.Gener.ordinal()) {
            loadData(id, PhoneMediaControl.SonLoadFor.Gener);
        }
        songsListAdapter.setId(id);
        songsListAdapter.setType(tagFor);
    }

    private void loadData(final long id, PhoneMediaControl.SonLoadFor typeData) {
        PhoneMediaControl phoneMediaControl = PhoneMediaControl.getInstance();
        PhoneMediaControl.setPhoneMediaControlINterface(new PhoneMediaControl.PhoneMediaControlINterface() {
            @Override
            public void loadSongsComplete(ArrayList<SongDetail> songDetails, Cursor cursor) {
                if (songDetails != null) {
                    songsListAdapter.setSongDetail(songDetails);
                    songsListAdapter.notifyDataSetChanged();
                    if (tagFor == PhoneMediaControl.SonLoadFor.Album.ordinal()) {
                        mTvArtistName.setText(songDetails.get(0).getArtist());
                        String contentURI = "content://media/external/audio/albumart/" + id;
                        imageLoader.displayImage(contentURI, mImgBanner, options);
                    } else if (tagFor == PhoneMediaControl.SonLoadFor.Artis.ordinal()) {
                        mTvArtistName.setText("All My Songs");
                        mImgBanner.setImageResource(R.mipmap.illo_default_artistradio_portrait);
                    } else if (tagFor == PhoneMediaControl.SonLoadFor.Gener.ordinal()) {
                        mTvArtistName.setText("All My Songs");
                        mImgBanner.setImageResource(R.mipmap.bg_default_album_art);
                    }
                    mTvNumberSong.setText((songDetails.size() <= 1 ? getResources().getString(R.string.one_song, String.valueOf(songDetails.size())) : getResources().getString(R.string.more_song, String.valueOf(songDetails.size()))));
                }
            }
        });
        phoneMediaControl.loadMusicList(this, id, typeData, "");
    }
}
