package com.lk.dmplayer.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lk.dmplayer.R;
import com.lk.dmplayer.models.SongDetail;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;
import com.lk.dmplayer.untilily.CustomerDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by admin on 1/9/17.
 */

public class DialogControllerSong {
    private Context mContext;
    private SongDetail mSongDetail;
    private TextView mTxtPlayeSongName;
    private TextView mTxtSongArtistName;
    private ImageView imageSongThm;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public DialogControllerSong(Context mContext, SongDetail mSongDetail) {
        this.mContext = mContext;
        this.mSongDetail = mSongDetail;
        this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.bg_default_album_art).showImageForEmptyUri(R.mipmap.bg_default_album_art).showImageOnFail(R.mipmap.bg_default_album_art).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
    }

    public Dialog onDialogController() {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.dialog_controller, null);
        ini(view);
        updataData();
        CustomerDialog customerDialog = new CustomerDialog(mContext, view, R.style.CustomDialogAnimation);
        return customerDialog;
    }

    private void ini(View view) {
        mTxtPlayeSongName = (TextView) view.findViewById(R.id.txt_playesongname);
        mTxtSongArtistName = (TextView) view.findViewById(R.id.txt_songartistname);
        imageSongThm = (ImageView) view.findViewById(R.id.img_bottom_slideone);
    }

    private void updataData() {
        mTxtPlayeSongName.setText(mSongDetail.getTitle());
        mTxtSongArtistName.setText( mSongDetail.getArtist());
        String contentURI = "content://media/external/audio/media/" + mSongDetail.getId() + "/albumart";
        imageLoader.displayImage(contentURI, imageSongThm, options);
    }
}
