package com.lk.dmplayer.activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.lk.dmplayer.fragments.FragmentEquilizer;
import com.lk.dmplayer.fragments.FragmentFavorite;
import com.lk.dmplayer.fragments.FragmentLibrary;
import com.lk.dmplayer.fragments.FragmentSendFeedback;
import com.lk.dmplayer.fragments.FragmentSettings;
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
public class DMPlayerBaseActivity extends MainActivity  {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private RecyclerView recyclerViewDrawer;
    private DrawerAdapter drawerAdapter;
    private String[] arrayTitle;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dmplayerbase);
        super.onCreate(savedInstanceState);
        navigationDrawer();
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            setFragment(0);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setFragment(0);
                }
                break;
            default:
                break;
        }
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
            public void onItemClick(RecyclerView parent, View view, final int position, long id) {
                for (int i = 0; i < arrayTitle.length; i++) {
                    setColorDrawerSelect(i, position, color);

                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setFragment(position);
                        if (isPanelExpanded) {
                            mPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        }
                    }
                }, 100);
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
                addFragment(position, new FragmentFavorite(), arrayTitle[position].toString());
                break;
            case 3:
                addFragment(position, new FragmentSettings(), arrayTitle[position].toString());
                break;
            case 4:
                addFragment(position, new FragmentEquilizer(), arrayTitle[position].toString());
                break;
            case 5:
                addFragment(position, new FragmentSendFeedback(), arrayTitle[position].toString());
                break;


        }
    }

    private void addFragment(int position, Fragment fragment, String title) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        sharedPreferences.edit().putInt("FRAGMENT", position).apply();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commitAllowingStateLoss();
        getSupportActionBar().setTitle(title);

    }

}
