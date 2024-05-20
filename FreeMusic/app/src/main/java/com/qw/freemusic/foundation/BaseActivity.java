package com.qw.freemusic.foundation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.qw.freemusic.R;
import com.qw.freemusic.main.MusicService;
import com.qw.freemusic.main.MusicStateListener;
import com.qw.freemusic.view.fragment.QuickControlsFragment;
import com.qw.freemusic.view.widget.SlidingUpPanelLayout;

import java.util.ArrayList;

/**
 * created by QY
 * description:
 */
public abstract class BaseActivity extends FragmentActivity{
    private final ArrayList<MusicStateListener> mMusicStateListener = new ArrayList<>();
    private PlaybackStatus mPlaybackStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOGD("onCreate");
        setContentView(getViewId());
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LOGD("onStart");
        mPlaybackStatus = new PlaybackStatus();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(MusicService.PLAYSTATE_CHANGED);
        // Track changes
        filter.addAction(MusicService.META_CHANGED);
        // Update a list, probably the playlist fragment's
        filter.addAction(MusicService.REFRESH);
        // If a playlist has changed, notify us
        filter.addAction(MusicService.PLAYLIST_CHANGED);
        // If there is an error playing a track
        filter.addAction(MusicService.TRACK_ERROR);
        registerReceiver(mPlaybackStatus, filter, RECEIVER_EXPORTED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LOGD("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LOGD("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LOGD("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LOGD("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LOGD("onDestroy");
    }

    public void onMetaChanged() {
        // Let the listener know to the meta chnaged
        for (final MusicStateListener listener : mMusicStateListener) {
            if (listener != null) {
                listener.onMetaChanged();
            }
        }
    }

    public void restartLoader() {
        // Let the listener know to update a list
        for (final MusicStateListener listener : mMusicStateListener) {
            if (listener != null) {
                listener.restartLoader();
            }
        }
    }

    public void onPlaylistChanged() {
        // Let the listener know to update a list
        for (final MusicStateListener listener : mMusicStateListener) {
            if (listener != null) {
                listener.onPlaylistChanged();
            }
        }
    }

    public void setMusicStateListenerListener(final MusicStateListener status) {
        if (status == this) {
            throw new UnsupportedOperationException("Override the method, don't add a listener");
        }

        if (status != null) {
            mMusicStateListener.add(status);
        }
    }

    public void removeMusicStateListenerListener(final MusicStateListener status) {
        if (status != null) {
            mMusicStateListener.remove(status);
        }
    }

    protected abstract String TAG();
    protected abstract int getViewId();
    protected abstract void initView();

    protected void LOGI(String msg) {
        Log.i(TAG(), msg);
    }

    protected void LOGE(String msg) {
        Log.e(TAG(), msg);
    }

    protected void LOGD(String msg) {
        Log.d(TAG(), msg);
    }

    protected void showToastL(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        LOGD("showToastL: " + msg);
    }

    protected void showToastS(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        LOGD("showToastS: " + msg);
    }

    public void setPanelSlideListeners(SlidingUpPanelLayout panelLayout) {
        panelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                View nowPlayingCard = QuickControlsFragment.topContainer;
                if (nowPlayingCard != null)
                    nowPlayingCard.setAlpha(1 - slideOffset);
            }

            @Override
            public void onPanelCollapsed(View panel) {
                View nowPlayingCard = QuickControlsFragment.topContainer;
                if (nowPlayingCard != null)
                    nowPlayingCard.setAlpha(1);
            }

            @Override
            public void onPanelExpanded(View panel) {
                View nowPlayingCard = QuickControlsFragment.topContainer;
                if (nowPlayingCard != null)
                    nowPlayingCard.setAlpha(0);
            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });
    }

    class PlaybackStatus extends BroadcastReceiver {

        public PlaybackStatus() {

        }

        @Override
        public void onReceive(Context pContext, Intent pIntent) {
            final String action = pIntent.getAction();
            if (action.equals(MusicService.META_CHANGED)) {
                onMetaChanged();
            } else if (action.equals(MusicService.REFRESH)) {
                restartLoader();
            } else if (action.equals(MusicService.PLAYLIST_CHANGED)) {
                onPlaylistChanged();
            }
        }
    }

    public class initQuickControls extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            QuickControlsFragment fragment1 = new QuickControlsFragment();
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            fragmentManager1.beginTransaction()
                    .replace(R.id.quickcontrols_container, fragment1).commitAllowingStateLoss();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }
    }
}

