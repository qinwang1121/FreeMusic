package com.qw.freemusic.view.activity;

import androidx.fragment.app.FragmentManager;

import com.qw.freemusic.R;
import com.qw.freemusic.foundation.BaseActivity;
import com.qw.freemusic.view.fragment.NowPlayingFragment;

/**
 * created by QY
 * description:
 */
public class NowPlayingActivity extends BaseActivity {
    @Override
    protected String TAG() {
        return "NowPlayingActivity";
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_now_playing;
    }

    @Override
    protected void initView() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new NowPlayingFragment()).commit();
    }
}
