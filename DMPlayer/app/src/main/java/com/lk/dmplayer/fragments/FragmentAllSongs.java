package com.lk.dmplayer.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lk.dmplayer.R;
import com.lk.dmplayer.activities.DMPlayerBaseActivity;
import com.lk.dmplayer.manager.MediaController;
import com.lk.dmplayer.models.SongDetail;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;
import com.lk.dmplayer.phonemidea.PhoneMediaControl;
import com.lk.dmplayer.uicomponent.PlayPauseView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by Le Kham on 10/24/2016.
 */
public class FragmentAllSongs extends Fragment {
    private ListView recycler_songslist;
    private ArrayList<SongDetail> songAllDetails = new ArrayList<>();
    private AllSongsListAdapter allSongsListAdapter;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_allsongs, null);
        setupInitialViews(v);
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            loadAllSongs();
        }
        return v;
    }

    private void setupInitialViews(View v) {
        recycler_songslist = (ListView) v.findViewById(R.id.recycler_allSongs);
        allSongsListAdapter = new AllSongsListAdapter(getActivity());
        recycler_songslist.setAdapter(allSongsListAdapter);
    }

    private void loadAllSongs() {
        PhoneMediaControl phoneMediaControl = PhoneMediaControl.getInstance();
        phoneMediaControl.setPhoneMediaControlINterface(new PhoneMediaControl.PhoneMediaControlINterface() {
            @Override
            public void loadSongsComplete(ArrayList<SongDetail> songDetails) {
                songAllDetails = songDetails;
                allSongsListAdapter.notifyDataSetChanged();
            }
        });
        phoneMediaControl.loadMusicList(getActivity(), -1, PhoneMediaControl.SonLoadFor.All, "");
    }

    public class AllSongsListAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private DisplayImageOptions options;
        private ImageLoader imageLoader = ImageLoader.getInstance();

        public AllSongsListAdapter(Context context) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            this.options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.bg_default_album_art).showImageForEmptyUri(R.mipmap.bg_default_album_art).showImageOnFail(R.mipmap.bg_default_album_art).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }

        @Override
        public int getCount() {
            return songAllDetails.size();
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
            final SongDetail songDetail = songAllDetails.get(i);
            mViewHolder.textViewSongName.setText(songDetail.getTitle());
            String duration = "";
            try {
                duration = DMPlayerUtility.getAudioDuration(Long.valueOf(songDetail.getDuration()));
            } catch (Exception ex) {
            }
            mViewHolder.textViewSongArtisNameAndDuration.setText(String.format(getResources().getString(R.string.artist_duration, duration, songDetail.getArtist())));
            String contentURI = "content://media/external/audio/media/" + songDetail.getId() + "/albumart";
            imageLoader.displayImage(contentURI, mViewHolder.imageSongThm, options);
            mViewHolder.imageMoreIcon.setColorFilter(R.color.colorPrimary);
            mViewHolder.imageMoreIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(mContext, view);
                    popupMenu.getMenuInflater().inflate(R.menu.list_item_option, popupMenu.getMenu());
                    popupMenu.show();
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((DMPlayerBaseActivity) getActivity()).loadSongDetails(songDetail);
                    MediaController.getInstance().setPlayerList(songAllDetails, songDetail);
                }
            });
            return view;
        }

        class ViewHolder {
            TextView textViewSongName;
            ImageView imageSongThm, imageMoreIcon;
            TextView textViewSongArtisNameAndDuration;
            LinearLayout song_row;
        }
    }
}
