package com.lk.dmplayer.childfragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lk.dmplayer.R;
import com.lk.dmplayer.adapter.SongsListAdapter;
import com.lk.dmplayer.models.SongDetail;
import com.lk.dmplayer.phonemidea.PhoneMediaControl;

import java.util.ArrayList;

/**
 * Created by dlkham on 12/2/2016.
 */
public class ChildFragmentMostPlay extends Fragment {
    public static final String TAG = ChildFragmentMostPlay.class.getSimpleName();
    private ListView recycler_songslist;
    private SongsListAdapter allSongsListAdapter;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        allSongsListAdapter = new SongsListAdapter(getActivity());
        allSongsListAdapter.setType(PhoneMediaControl.SonLoadFor.MostPlay.ordinal());
        recycler_songslist.setAdapter(allSongsListAdapter);
    }
    private void loadAllSongs() {
        PhoneMediaControl phoneMediaControl = PhoneMediaControl.getInstance();
        phoneMediaControl.setPhoneMediaControlINterface(new PhoneMediaControl.PhoneMediaControlINterface() {
            @Override
            public void loadSongsComplete(ArrayList<SongDetail> songDetails, Cursor cursor) {
                allSongsListAdapter.setSongDetail(songDetails);
                allSongsListAdapter.notifyDataSetChanged();
            }
        });
        phoneMediaControl.loadMusicList(getActivity(), -1, PhoneMediaControl.SonLoadFor.MostPlay, "");
    }
    public static ChildFragmentMostPlay newInstance(Context context) {
        return new ChildFragmentMostPlay();
    }
}
