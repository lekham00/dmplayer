package com.lk.dmplayer.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lk.dmplayer.R;
import com.lk.dmplayer.adapter.DrawerAdapter;
import com.lk.dmplayer.fragments.FragmentAllSongs;
import com.lk.dmplayer.fragments.FragmentLibrary;
import com.lk.dmplayer.manager.MediaController;
import com.lk.dmplayer.manager.MusicPreferance;
import com.lk.dmplayer.manager.NotificationManager;
import com.lk.dmplayer.models.DrawerItem;
import com.lk.dmplayer.models.SongDetail;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;
import com.lk.dmplayer.recyclerviewutil.ItemClickSupport;
import com.lk.dmplayer.slidinguppanelhelper.SlidingUpPanelLayout;
import com.lk.dmplayer.uicomponent.PlayPauseView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import java.util.ArrayList;

/**
 * Created by Le Kham on 10/19/2016.
 */
public class DMPlayerBaseActivity extends ActionBarActivity implements View.OnClickListener, NotificationManager.NotificationCenterDelegate, SeekBar.OnSeekBarChangeListener, SlidingUpPanelLayout.PanelSlideListener {
    private final String TAG = getClass().getSimpleName();
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private RecyclerView recyclerViewDrawer;
    private DrawerAdapter drawerAdapter;
    private SharedPreferences sharedPreferences;
    private String[] arrayTitle;
    private ImageView mImgBottomSlideOne;
    private ImageView mImageSongAlbumBgMid;
    private TextView mTxtPlayeSongName;
    private TextView mTxtSongArtistName;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions optionsBottomSlideOne, optionsImageSongAlbumBgMid;
    private PlayPauseView mPlayPauseView, mBtnPlay;
    private TextView mTimeProgress;
    private TextView mTimeTotal;
    private SeekBar mAudioProgressControl;
    private ImageView mBtnBackWard;
    private ImageView mBtnSuffel;
    private ImageView mBtnForward;
    private ImageView mBtnToggle;
    private RelativeLayout mSlidePanelChildTwoTopViewOne, mSlidePanelChildTwoTopViewTwo;
    private SlidingUpPanelLayout mPanelLayout;
    private ImageView mImgBottomSlideTwo;
    private TextView mTxtPlayeSongNameTwo;
    private TextView mTxtSongArtistNameTwo;
    private ImageView mBottomBarImgFavorite;
    private ImageView mBottomBarMoreIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setThem();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmplayerbase);
        optionsBottomSlideOne = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.bg_default_album_art).showImageForEmptyUri(R.mipmap.bg_default_album_art).showImageOnFail(R.mipmap.bg_default_album_art).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        optionsImageSongAlbumBgMid = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.drawer_header).showImageForEmptyUri(R.mipmap.drawer_header).showImageOnFail(R.mipmap.drawer_header).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        toolBarStatusBar();
        initiSlidingUpPanel();
        navigationDrawer();
        setFragment(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case FragmentAllSongs.MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setFragment(0);
                }
                break;
            default:
                break;
        }
    }

    private void toolBarStatusBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

    }

    private void setThem() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);

    }

    private void navigationDrawer() {

      /*  set drawer icon*/
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mActionBarDrawerToggle.syncState();
       /* statustbar color behind navigaion drawer*/
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        int colorStatusBarBg = typedValue.data;
        mDrawerLayout.setStatusBarBackgroundColor(colorStatusBarBg);

        /*set color title in drawer*/
        TypedValue typedValueDrawer = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValueDrawer, true);
        final int color = typedValueDrawer.data;

      /*  set recyclerview inside drawer*/
        recyclerViewDrawer = (RecyclerView) findViewById(R.id.recyclerViewDrawer);
//        recyclerViewDrawer.setHasFixedSize(true);
        recyclerViewDrawer.setLayoutManager(new LinearLayoutManager(DMPlayerBaseActivity.this));

        ArrayList<DrawerItem> drawerItems = new ArrayList<>();
        arrayTitle = getResources().getStringArray(R.array.drawer);
        TypedArray typedArrayIcon = getResources().obtainTypedArray(R.array.drawerIcons);
        for (int i = 0; i < arrayTitle.length; i++) {
            drawerItems.add(new DrawerItem(arrayTitle[i], typedArrayIcon.getDrawable(i)));
        }
        typedArrayIcon.recycle();
        drawerAdapter = new DrawerAdapter(drawerItems);
        recyclerViewDrawer.setAdapter(drawerAdapter);
        recyclerViewDrawer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for (int i = 0; i < arrayTitle.length; i++) {
                    setColorDrawerSelect(i, sharedPreferences.getInt("FRAGMENT", 0), color);
                }
                recyclerViewDrawer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        /*RecyclerView for listener*/
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recyclerViewDrawer);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                for (int i = 0; i < arrayTitle.length; i++) {
                    setColorDrawerSelect(i, position, color);

                }
                setFragment(position);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    private void setColorDrawerSelect(int i, int position, int color) {
        TextView textViewTitle = (TextView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.textViewDrawerItemTitle);
        ImageView imageViewIcon = (ImageView) recyclerViewDrawer.getChildAt(i).findViewById(R.id.imageViewDrawerIcon);
        RelativeLayout relativeLayout = (RelativeLayout) recyclerViewDrawer.getChildAt(i).findViewById(R.id.relativeLayoutDrawerItem);
        if (i == position) {
            textViewTitle.setTextColor(color);
            imageViewIcon.setColorFilter(color);
            TypedValue typedValueColorSelectBg = new TypedValue();
            getTheme().resolveAttribute(R.attr.colorPrimary, typedValueColorSelectBg, true);
            int colorSelectBg = typedValueColorSelectBg.data;
            colorSelectBg = (colorSelectBg & 0x00FFFFFF) | 0x30000000;
            relativeLayout.setBackgroundColor(colorSelectBg);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textViewTitle.setTextColor(getResources().getColor(R.color.md_text, null));
                imageViewIcon.setColorFilter(getResources().getColor(R.color.md_text, null));
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.md_white_1000, null));
            } else {
                textViewTitle.setTextColor(getResources().getColor(R.color.md_text));
                imageViewIcon.setColorFilter(getResources().getColor(R.color.md_text));
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
            }
        }
    }


    private void setFragment(int position) {
        switch (position) {
            case 0:
                addFragment(position, new FragmentAllSongs(), arrayTitle[position].toString());
                break;
            case 1:
                addFragment(position, new FragmentLibrary(), arrayTitle[position].toString());
                break;
            case 2:
                addFragment(position, new FragmentAllSongs(), arrayTitle[position].toString());
                break;
            case 3:
                addFragment(position, new FragmentAllSongs(), arrayTitle[position].toString());
                break;
            case 4:
                addFragment(position, new FragmentAllSongs(), arrayTitle[position].toString());
                break;
            case 5:
                addFragment(position, new FragmentAllSongs(), arrayTitle[position].toString());
                break;


        }
    }

    private void addFragment(int position, Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        sharedPreferences.edit().putInt("FRAGMENT", position).apply();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(title);

    }

    private void initiSlidingUpPanel() {
        mTimeProgress = (TextView) findViewById(R.id.slidepanel_time_progress);
        mTimeTotal = (TextView) findViewById(R.id.slidepanel_time_total);
        mAudioProgressControl = (SeekBar) findViewById(R.id.audio_progress_control);
        mImgBottomSlideOne = (ImageView) findViewById(R.id.img_bottom_slideone);
        mImageSongAlbumBgMid = (ImageView) findViewById(R.id.image_songAlbumbg_mid);
        mTxtPlayeSongName = (TextView) findViewById(R.id.txt_playesongname);
        mTxtSongArtistName = (TextView) findViewById(R.id.txt_songartistname);
        mPlayPauseView = (PlayPauseView) findViewById(R.id.bottombar_play);
        mBtnPlay = (PlayPauseView) findViewById(R.id.btn_play);
        mBtnBackWard = (ImageView) findViewById(R.id.btn_backward);
        mBtnForward = (ImageView) findViewById(R.id.btn_forward);
        mBtnSuffel = (ImageView) findViewById(R.id.btn_suffel);
        mBtnToggle = (ImageView) findViewById(R.id.btn_toggle);
        mImgBottomSlideTwo = (ImageView) findViewById(R.id.img_bottom_slidetwo);
        mTxtPlayeSongNameTwo = (TextView) findViewById(R.id.txt_playesongname_two);
        mTxtSongArtistNameTwo = (TextView) findViewById(R.id.txt_songartistname_two);
        mSlidePanelChildTwoTopViewOne = (RelativeLayout) findViewById(R.id.slidepanelchildtwo_topviewone);
        mSlidePanelChildTwoTopViewTwo = (RelativeLayout) findViewById(R.id.slidepanelchildtwo_topviewtwo);
        mBottomBarImgFavorite = (ImageView) findViewById(R.id.bottombar_img_favorite);
        mBottomBarMoreIcon = (ImageView) findViewById(R.id.bottombar_moreicon);
        mPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mPlayPauseView.setOnClickListener(this);
        mAudioProgressControl.setOnSeekBarChangeListener(this);
        mBtnPlay.setOnClickListener(this);
        mBtnBackWard.setOnClickListener(this);
        mBtnForward.setOnClickListener(this);
        mBtnSuffel.setOnClickListener(this);
        mBtnToggle.setOnClickListener(this);
        mPanelLayout.setPanelSlideListener(this);
        mBottomBarImgFavorite.setOnClickListener(this);
        mBottomBarMoreIcon.setOnClickListener(this);
        setColorTextView();
        MediaController.getInstance().setShuffleMusic(MusicPreferance.getShuffleFlagSong(this));
        MediaController.getInstance().setRepeatMode(MusicPreferance.getRepeatModeSong(this));

    }


    public void loadSongDetails(SongDetail songDetail) {
        mTxtPlayeSongName.setText(songDetail.getTitle());
        mTxtSongArtistName.setText(songDetail.getArtist());
        mTxtPlayeSongNameTwo.setText(songDetail.getTitle());
        mTxtSongArtistNameTwo.setText(songDetail.getArtist());
        String contentURI = "content://media/external/audio/media/" + songDetail.getId() + "/albumart";
        imageLoader.displayImage(contentURI, mImgBottomSlideOne, optionsBottomSlideOne);
        imageLoader.displayImage(contentURI, mImgBottomSlideTwo, optionsBottomSlideOne);
        imageLoader.displayImage(contentURI, mImageSongAlbumBgMid, optionsImageSongAlbumBgMid);
        long duration = Long.valueOf(songDetail.getDuration());
        mTimeTotal.setText(duration != 0 ? DMPlayerUtility.getAudioDuration(Long.valueOf(songDetail.getDuration())) : "-:--");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottombar_play:
                playPauseView();
                break;
            case R.id.btn_play:
                playPauseView();
                break;
            case R.id.btn_backward:
                playBackSong();
                break;
            case R.id.btn_forward:
                playNextSong(true);
                break;
            case R.id.btn_suffel:
                playSongShuffle();
                break;
            case R.id.btn_toggle:
                playSongRepeat();
                break;
            case R.id.bottombar_img_favorite:
                onLayoutFavorite(view);
                break;
            case R.id.bottombar_moreicon:
                break;
        }
    }

    private void onLayoutFavorite(View view)
    {
        view.setSelected(view.isSelected()?false:true);
        DMPlayerUtility.animateHeartButton(view);
    }

    private void onBarMoreICon()
    {

    }
    private void playSongShuffle() {
        if (MusicPreferance.playingSongDetail != null) {
            if (MediaController.getInstance().isShuffleMusic()) {
                mBtnSuffel.setColorFilter(null);
                MediaController.getInstance().setShuffleMusic(false);
                MusicPreferance.saveShuffleFlagSong(this, false);
            } else {
                MediaController.getInstance().setShuffleMusic(true);
                MusicPreferance.saveShuffleFlagSong(this, true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    mBtnSuffel.setColorFilter(getResources().getColor(R.color.colorAccent, null));
                else
                    mBtnSuffel.setColorFilter(getResources().getColor(R.color.colorAccent));
            }
        }
    }

    private void playSongRepeat() {
        if (MusicPreferance.playingSongDetail != null) {
            if (MediaController.getInstance().getRepeatMode() == MediaController.NON_REPEAT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    mBtnToggle.setColorFilter(getResources().getColor(R.color.colorAccent, null));
                else
                    mBtnToggle.setColorFilter(getResources().getColor(R.color.colorAccent));
                MediaController.getInstance().setRepeatMode(MediaController.ALWAYS_REPEAT);
                MusicPreferance.saveRepeatModeSong(this, MediaController.ALWAYS_REPEAT);
            } else if (MediaController.getInstance().getRepeatMode() == MediaController.ALWAYS_REPEAT) {
                MediaController.getInstance().setRepeatMode(MediaController.ONE_REPEAT);
                MusicPreferance.saveRepeatModeSong(this, MediaController.ONE_REPEAT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    mBtnToggle.setColorFilter(getResources().getColor(R.color.colorAccent, null));
                else
                    mBtnToggle.setColorFilter(getResources().getColor(R.color.colorAccent));
                mBtnToggle.setImageResource(R.mipmap.ic_repeat_dark_select);
            } else {
                MediaController.getInstance().setRepeatMode(MediaController.NON_REPEAT);
                MusicPreferance.saveRepeatModeSong(this, MediaController.NON_REPEAT);
                mBtnToggle.setColorFilter(null);
                mBtnToggle.setImageResource(R.mipmap.ic_repeat_dark);
            }
        }
    }

    private void playNextSong(boolean isNext) {
        if (MusicPreferance.playingSongDetail != null) {
            MediaController.getInstance().playNextSong(isNext);
        }
    }

    private void playBackSong() {
        if (MusicPreferance.playingSongDetail != null) {
            MediaController.getInstance().playBackSong();
        }
    }

    private void setColorTextView() {
        mTxtPlayeSongName.setTextColor(Color.WHITE);
        mTxtSongArtistName.setTextColor(Color.WHITE);
        mTxtPlayeSongNameTwo.setTextColor(Color.WHITE);
        mTxtSongArtistNameTwo.setTextColor(Color.WHITE);
        mTimeProgress.setTextColor(Color.WHITE);
        mTimeTotal.setTextColor(Color.WHITE);
    }

    private void playPauseView() {
        if (MusicPreferance.playingSongDetail == null)
            return;
        if (MediaController.getInstance().isPauseAudio()) {
            mPlayPauseView.Play();
            mBtnPlay.Play();
            MediaController.getInstance().playAudio(MusicPreferance.playingSongDetail);
        } else {
            mPlayPauseView.Pause();
            mBtnPlay.Pause();
            MediaController.getInstance().pauseAudio();
        }
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationManager.audioDidStarted || id == NotificationManager.audioPlayStateChanged) {
            updateTitle(false);
        } else if (id == NotificationManager.audioProgressDidChanged) {
            updateProgress((SongDetail) args[0]);
        }
    }

    private void updateProgress(SongDetail songDetail) {
        if (mAudioProgressControl != null) {
            mAudioProgressControl.setProgress((int) (songDetail.getAudioProgress() * 100));
            mTimeProgress.setText(DMPlayerUtility.getAudioDuration(songDetail.getAudioProgressSec()));
        }
    }

    private void updateTitle(Boolean shutdown) {
        if (MediaController.getInstance().isPauseAudio()) {
            mPlayPauseView.Pause();
            mBtnPlay.Pause();
        } else {
            mPlayPauseView.Play();
            mBtnPlay.Play();
        }
        loadSongDetails(MusicPreferance.playingSongDetail);
    }


    @Override
    public void newSongLoaded(Object... args) {

    }

    public void addObserver() {
        NotificationManager.getInstance().addObserver(this, NotificationManager.audioPlayStateChanged);
        NotificationManager.getInstance().addObserver(this, NotificationManager.audioDidStarted);
        NotificationManager.getInstance().addObserver(this, NotificationManager.audioProgressDidChanged);

    }

    public void removeObserver() {
        NotificationManager.getInstance().removeObserver(this, NotificationManager.audioPlayStateChanged);
        NotificationManager.getInstance().removeObserver(this, NotificationManager.audioDidStarted);
        NotificationManager.getInstance().removeObserver(this, NotificationManager.audioProgressDidChanged);

    }

    @Override
    protected void onResume() {
        super.onResume();
        addObserver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeObserver();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b)
            MediaController.getInstance().seekToProgress(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        Log.d(TAG, "onPanelSlide slideOffset: " + slideOffset);
        if (slideOffset == 0) {
            mSlidePanelChildTwoTopViewOne.setVisibility(View.VISIBLE);
            mSlidePanelChildTwoTopViewTwo.setVisibility(View.INVISIBLE);
        } else if (slideOffset == 1) {
            mSlidePanelChildTwoTopViewOne.setVisibility(View.INVISIBLE);
            mSlidePanelChildTwoTopViewTwo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPanelCollapsed(View panel) {
        Log.d(TAG, "onPanelCollapsed");
    }

    @Override
    public void onPanelExpanded(View panel) {
        Log.d(TAG, "onPanelExpanded");
    }

    @Override
    public void onPanelAnchored(View panel) {
        Log.d(TAG, "onPanelAnchored");
    }

    @Override
    public void onPanelHidden(View panel) {
        Log.d(TAG, "onPanelHidden");
    }
}
