package com.lk.dmplayer.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lk.dmplayer.R;
import com.lk.dmplayer.activities.MainActivity;
import com.lk.dmplayer.dialog.DialogController;
import com.lk.dmplayer.fragments.FragmentAllSongs;
import com.lk.dmplayer.manager.MediaController;
import com.lk.dmplayer.models.SongDetail;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by Kham on 12/13/2016.
 */

public class SongsListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private DisplayImageOptions options;
    private ArrayList<SongDetail> detailArrayList = new ArrayList<>();
    private long mId = -1;
    private int mType = 0;

    public SongsListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.bg_default_album_art).showImageForEmptyUri(R.mipmap.bg_default_album_art).showImageOnFail(R.mipmap.bg_default_album_art).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    private ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public int getCount() {
        return detailArrayList.size();
    }

    public void setSongDetail(ArrayList<SongDetail> detailArrayList) {
        this.detailArrayList = detailArrayList;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mViewHolder;
        if (view == null) {
            mViewHolder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.inflate_allsongsitem, null);
            mViewHolder.textViewSongName = (TextView) view.findViewById(R.id.inflate_allsong_textsongname);
            mViewHolder.textViewSongArtisNameAndDuration = (TextView) view.findViewById(R.id.inflate_allsong_textsongArtisName_duration);
            mViewHolder.imageSongThm = (ImageView) view.findViewById(R.id.inflate_allsong_imgSongThumb);
            mViewHolder.song_row = (LinearLayout) view.findViewById(R.id.inflate_allsong_row);
            mViewHolder.imageMoreIcon = (ImageView) view.findViewById(R.id.img_moreicon);

            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        final SongDetail songDetail = detailArrayList.get(i);
        mViewHolder.textViewSongName.setText(songDetail.getTitle());
        String duration = "";
        try {
            duration = DMPlayerUtility.getAudioDuration(Long.valueOf(songDetail.getDuration()));
        } catch (Exception ex) {
        }
        mViewHolder.textViewSongArtisNameAndDuration.setText(mContext.getResources().getString(R.string.artist_duration, duration, songDetail.getArtist()));
        String contentURI = "content://media/external/audio/media/" + songDetail.getId() + "/albumart";
        imageLoader.displayImage(contentURI, mViewHolder.imageSongThm, options);
        mViewHolder.imageMoreIcon.setColorFilter(R.color.colorPrimary);
        mViewHolder.imageMoreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PopupMenu popupMenu = new PopupMenu(mContext, view);
//                popupMenu.getMenuInflater().inflate(R.menu.list_item_option, popupMenu.getMenu());
//                popupMenu.show();
                onDialogController().show();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mContext).loadSongDetails(songDetail);
                MediaController.getInstance().setPlayerList(detailArrayList, songDetail, mType, mId);
            }
        });
        return view;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public void setType(int type) {
        this.mType = type;
    }

    class ViewHolder {
        TextView textViewSongName;
        ImageView imageSongThm, imageMoreIcon;
        TextView textViewSongArtisNameAndDuration;
        LinearLayout song_row;
    }

    public Dialog onDialogController() {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.dialog_controller, null);
        DialogController dialogController = new DialogController(mContext, view);
        dialogController.o
        return dialogController;
    }
}
