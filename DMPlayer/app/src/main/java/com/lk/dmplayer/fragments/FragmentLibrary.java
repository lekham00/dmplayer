package com.lk.dmplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import com.lk.dmplayer.childfragment.ChildFragmentArtists;
import com.lk.dmplayer.childfragment.ChildFragmentGenres;
import com.lk.dmplayer.childfragment.ChildFragmentMostPlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Le Kham on 11/30/2016.
 */
public class FragmentLibrary extends Fragment {
    public String[] TITLE = {"ALBUMS", "ARTISTS","GENRES","MOSTPLAY"};
    ViewPager mViewPage;
    TabLayout mTabs;
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
        setFragment();
        mTabs = (TabLayout) view.findViewById(R.id.tabs);
        mTabs.setupWithViewPager(mViewPage);
    }
    private void setFragment()
    {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        myPagerAdapter.addFragment(ChildFragmentAlbum.newInstance(getActivity()),TITLE[0]);
        myPagerAdapter.addFragment(ChildFragmentArtists.newInstance(getActivity()),TITLE[1]);
        myPagerAdapter.addFragment(ChildFragmentGenres.newInstance(getActivity()),TITLE[2]);
        myPagerAdapter.addFragment(ChildFragmentMostPlay.newInstance(getActivity()),TITLE[3]);
        mViewPage.setAdapter(myPagerAdapter);
    }
    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void addFragment(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
