package com.lk.dmplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lk.dmplayer.R;
import com.lk.dmplayer.childfragment.ChildFragmentAlbum;

/**
 * Created by Le Kham on 11/30/2016.
 */
public class FragmentLibrary extends Fragment {
    public String[] TITLE = {"ALBUMS", "ARTISTS", "GENRES", "MOSTPLAY"};
    ViewPager mViewPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, null);
        setUpView(view);
        return view;

    }

    private void setUpView(View view) {
        mViewPage = (ViewPager) view.findViewById(R.id.viewPage);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPage.setAdapter(myPagerAdapter);
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position];
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return ChildFragmentAlbum.newInstance(getActivity());
                case 1:
                    return ChildFragmentAlbum.newInstance(getActivity());
                case 2:
                    return ChildFragmentAlbum.newInstance(getActivity());
                case 3:
                    return ChildFragmentAlbum.newInstance(getActivity());
            }
            return null;
        }

        @Override
        public int getCount() {
            return TITLE.length;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
