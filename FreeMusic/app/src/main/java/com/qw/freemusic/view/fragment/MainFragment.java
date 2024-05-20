package com.qw.freemusic.view.fragment;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.qw.freemusic.R;
import com.qw.freemusic.adapter.FragmentAdapter;
import com.qw.freemusic.foundation.BaseFragment;
import com.qw.freemusic.utils.ResourceUtil;

/**
 * created by QY
 * description:
 */
public class MainFragment extends BaseFragment {
    private ViewPager mViewPager;

    @Override
    protected String TAG() {
        return "MainFragment";
    }

    @Override
    protected void initView(View pView) {
        initViewPager(pView);
        TabLayout tabLayout = pView.findViewById(R.id.music_tab);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_main;
    }

    private void initViewPager(View pView){
        mViewPager = pView.findViewById(R.id.music_vp);
        FragmentAdapter lFragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        lFragmentAdapter.addFragment(new SongsFragment(), ResourceUtil.getString(R.string.songs));
        lFragmentAdapter.addFragment(new AlbumsFragment(), ResourceUtil.getString(R.string.albums));
        lFragmentAdapter.addFragment(new ArtistsFragment(), ResourceUtil.getString(R.string.artists));
        mViewPager.setAdapter(lFragmentAdapter);
        mViewPager.setOffscreenPageLimit(2);
    }
}
