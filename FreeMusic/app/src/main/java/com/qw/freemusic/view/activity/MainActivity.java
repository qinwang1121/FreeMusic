package com.qw.freemusic.view.activity;

import static com.qw.freemusic.main.MusicPlayer.mService;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.qw.freemusic.ITimberService;
import com.qw.freemusic.R;
import com.qw.freemusic.foundation.BaseActivity;
import com.qw.freemusic.main.MusicPlayer;
import com.qw.freemusic.main.MusicService;
import com.qw.freemusic.view.fragment.MainFragment;
import com.qw.freemusic.view.widget.SlidingUpPanelLayout;


public class MainActivity extends BaseActivity {

    public static MainActivity sMainActivity;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName pComponentName, IBinder pIBinder) {
            mService = ITimberService.Stub.asInterface(pIBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName pComponentName) {

        }
    };

    private SlidingUpPanelLayout panelLayout;

    @Override
    protected String TAG() {
        return "MainActivity";
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        sMainActivity = this;
        if (verPermissions()) {
            setView();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean verPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent lIntent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                lIntent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(lIntent, 1024);
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private void setView() {
        Fragment fragment = new MainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
        panelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        Intent lIntent = new Intent(this, MusicService.class);
        bindService(lIntent, mServiceConnection, BIND_AUTO_CREATE);
        setPanelSlideListeners(panelLayout);
        if (!panelLayout.isPanelHidden() && MusicPlayer.getTrackName() == null ) {
            panelLayout.hidePanel();
        }
        new initQuickControls().execute("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1024 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setView();
        }  else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (panelLayout.isPanelExpanded()) {
            panelLayout.collapsePanel();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMetaChanged() {
        super.onMetaChanged();
        if (panelLayout.isPanelHidden() && MusicPlayer.getTrackName() != null) {
            panelLayout.showPanel();
        }
    }
}