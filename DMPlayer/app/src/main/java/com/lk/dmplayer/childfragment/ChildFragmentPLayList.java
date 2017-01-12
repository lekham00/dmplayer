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
 * Created by dlkham on 1/12/2017.
 */
public class ChildFragmentPLayList extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allsongs, null);
        return view;
    }
    public static ChildFragmentPLayList newInstance(Context context) {
        return new ChildFragmentPLayList();
    }
}
