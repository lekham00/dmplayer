package com.lk.dmplayer.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.lk.dmplayer.R;
import com.lk.dmplayer.activities.DMPlayerBaseActivity;
import com.lk.dmplayer.activities.MainActivity;
import com.lk.dmplayer.manager.MediaController;
import com.lk.dmplayer.models.SongDetail;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;
import com.lk.dmplayer.phonemidea.PhoneMediaControl;
import com.lk.dmplayer.uicomponent.PlayPauseView;
import com.lk.dmplayer.untilily.SideSelector;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Le Kham on 10/24/2016.
 */
public class FragmentAllSongs extends Fragment {
    public static final String TAG = FragmentAllSongs.class.getSimpleName();
    private ListView recycler_songslist;
    private ArrayList<SongDetail> songAllDetails = new ArrayList<>();
    private AllSongsListAdapter allSongsListAdapter;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private AlphabetIndexer mIndexer;
    private LinearLayout mIndexerLayout;
    private int lastSelectedPosition = -1;

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
        mIndexerLayout= (LinearLayout) v.findViewById(R.id.indexer_layout);
        for(int i = 0; i < alphabet.length(); i++) {
            TextView letterTextView = new TextView(getActivity());
            letterTextView.setText(alphabet.charAt(i)+"");

            letterTextView.setTextSize(14f);
            letterTextView.setGravity(Gravity.CENTER);
            letterTextView.setTextColor(Color.parseColor("#0075F8"));
            LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(35, 0, 1.0f);
            letterTextView.setLayoutParams(paramss);
            letterTextView.setPadding(4, 0, 4, 0);
            mIndexerLayout.addView(letterTextView);
        }
    }

    private void loadAllSongs() {
        PhoneMediaControl phoneMediaControl = PhoneMediaControl.getInstance();
        phoneMediaControl.setPhoneMediaControlINterface(new PhoneMediaControl.PhoneMediaControlINterface() {
            @Override
            public void loadSongsComplete(ArrayList<SongDetail> songDetails, Cursor cursor) {
                songAllDetails = songDetails;
                mIndexer = new AlphabetIndexer(cursor, 2, alphabet);
                allSongsListAdapter.notifyDataSetChanged();
                mIndexerLayout.setOnTouchListener(mOnTouchListener);
            }
        });
        phoneMediaControl.loadMusicList(getActivity(), -1, PhoneMediaControl.SonLoadFor.All, "");
    }


    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @SuppressLint("NewApi")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float alphabetHeight = mIndexerLayout.getHeight();
            float y = event.getY();
            int sectionPosition = (int) ((y / alphabetHeight) / (1f / 27f));
            if (sectionPosition < 0) {
                sectionPosition = 0;
            } else if (sectionPosition > 26) {
                sectionPosition = 26;
            }
            if(lastSelectedPosition != sectionPosition) {
                if(-1 != lastSelectedPosition){
                    ((TextView) mIndexerLayout.getChildAt(lastSelectedPosition)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                lastSelectedPosition = sectionPosition;
            }
            String sectionLetter = String.valueOf(alphabet.charAt(sectionPosition));
            int position = mIndexer.getPositionForSection(sectionPosition);
            TextView textView = (TextView) mIndexerLayout.getChildAt(sectionPosition);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
//                    mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
//                    textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
//                    mSectionToastLayout.setVisibility(View.VISIBLE);
//                    mSectionToastText.setText(sectionLetter);
                    recycler_songslist.smoothScrollToPositionFromTop(position,0,1);
                    break;
                case MotionEvent.ACTION_MOVE:
//                    mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
//                    textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
//                    mSectionToastLayout.setVisibility(View.VISIBLE);
//                    mSectionToastText.setText(sectionLetter);
                    recycler_songslist.smoothScrollToPositionFromTop(position,0,1);
                    break;
                case MotionEvent.ACTION_UP:
//                    mSectionToastLayout.setVisibility(View.GONE);
                default:
//                    mSectionToastLayout.setVisibility(View.GONE);
                    break;
            }
            return true;
        }

    };


    public class AllSongsListAdapter extends BaseAdapter  {
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
            mViewHolder.textViewSongArtisNameAndDuration.setText(getResources().getString(R.string.artist_duration, duration, songDetail.getArtist()));
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
                    ((MainActivity) getActivity()).loadSongDetails(songDetail);
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
