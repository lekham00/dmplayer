package com.lk.dmplayer.activities;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.lk.dmplayer.R;
import com.lk.dmplayer.models.SongDetail;
import com.lk.dmplayer.phonemidea.PhoneMediaControl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by dlkham on 12/6/2016.
 */
public class AlbumAndArtisDetailsActivity extends MainActivity {
    Toolbar toolbar;
    public static final String ID = "ID";
    public static final String POSITION = "POSITION";
    public static final String TAGFOR = "TAG_FOR";
    private ImageView mImgBanner;
    private TextView mTvAlbumName;
    private TextView mTvArtistName;
    private  TextView mTvNumberSong;
    private long id ;
    private int position;
    private int tagFor;
    private String title;
    private String artistName;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mImgBanner = (ImageView) findViewById(R.id.banner);
        mTvAlbumName = (TextView) findViewById(R.id.tv_albumname);
        mTvArtistName = (TextView) findViewById(R.id.tv_artist_name);
        mTvNumberSong= (TextView) findViewById(R.id.tv_number_song);
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
        position = bundle.getInt(POSITION);
        tagFor = bundle.getInt(TAGFOR);
        mTvAlbumName.setText(title);
        mTvArtistName.setText(artistName);
        if (tagFor == PhoneMediaControl.SonLoadFor.Album.ordinal()) {
            this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.bg_default_album_art).showImageForEmptyUri(R.mipmap.bg_default_album_art).showImageOnFail(R.mipmap.bg_default_album_art).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
            loadData(id, PhoneMediaControl.SonLoadFor.Album);
        } else if (tagFor == PhoneMediaControl.SonLoadFor.Artis.ordinal()) {
            loadData(id, PhoneMediaControl.SonLoadFor.Artis);
        } else if (tagFor == PhoneMediaControl.SonLoadFor.Gener.ordinal()) {
            loadData(id, PhoneMediaControl.SonLoadFor.Gener);
        }
    }

    private void loadData(final long id, PhoneMediaControl.SonLoadFor typeData) {
        PhoneMediaControl phoneMediaControl = PhoneMediaControl.getInstance();
        PhoneMediaControl.setPhoneMediaControlINterface(new PhoneMediaControl.PhoneMediaControlINterface() {
            @Override
            public void loadSongsComplete(ArrayList<SongDetail> songDetails, Cursor cursor) {
                    if (tagFor == PhoneMediaControl.SonLoadFor.Album.ordinal()) {
                        if (songDetails != null) {
                            mTvAlbumName.setText(songDetails.get(0).getAlbum());
                            mTvArtistName.setText(songDetails.get(0).getArtist());
                            mTvNumberSong.setText((songDetails.size() <= 1 ? getResources().getString(R.string.one_song, String.valueOf(songDetails.size())) : getResources().getString(R.string.more_song, String.valueOf(songDetails.size()))));
                            String contentURI = "content://media/external/audio/albumart/" + id;
                            imageLoader.displayImage(contentURI, mImgBanner, options);
                        }
                    }
            }
        });
        phoneMediaControl.loadMusicList(this, id, typeData, "");
    }
}
