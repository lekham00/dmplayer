package com.lk.dmplayer.childfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lk.dmplayer.R;

/**
 * Created by dlkham on 12/2/2016.
 */
public class ChildFragmentArtists extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentchild,null);
        return view;
    }

    public static ChildFragmentArtists newInstance(Context context) {
        return new ChildFragmentArtists();
    }
}
