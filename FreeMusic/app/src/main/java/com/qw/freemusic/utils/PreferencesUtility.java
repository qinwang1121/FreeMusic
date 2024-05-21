package com.qw.freemusic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qw.freemusic.MainApplication;

public class PreferencesUtility {

    public static final String SONG_SORT_ORDER = "song_sort_order";
    public static final String ALBUM_SORT_ORDER = "album_sort_order";
    public static final String ARTIST_SORT_ORDER = "artist_sort_order";
    private static final String TOGGLE_ALBUM_GRID = "toggle_album_grid";
    private static final String TOGGLE_ARTIST_GRID = "toggle_artist_grid";
    private static final String TOGGLE_HEADPHONE_PAUSE = "toggle_headphone_pause";

    private static final String SHOW_LOCKSCREEN_ALBUMART = "show_albumart_lockscreen";
    private static final String TOGGLE_XPOSED_TRACKSELECTOR = "toggle_xposed_trackselector";

    private static final String IS_LOGIN = "login";

    private static PreferencesUtility sInstance;
    private Context mContext = MainApplication.sContext;

    private PreferencesUtility() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public static PreferencesUtility getInstance() {
        if (sInstance == null) {
            synchronized (PreferencesUtility.class) {
                if (sInstance == null) {
                    sInstance = new PreferencesUtility();
                }
            }
        }
        return sInstance;
    }

    private static SharedPreferences mPreferences;

    public final String getSongSortOrder() {
        return mPreferences.getString(SONG_SORT_ORDER, SortOrder.SongSortOrder.SONG_A_Z);
    }

    public final String getAlbumSortOrder() {
        return mPreferences.getString(ALBUM_SORT_ORDER, SortOrder.AlbumSortOrder.ALBUM_A_Z);
    }

    public final String getArtistSortOrder() {
        return mPreferences.getString(ARTIST_SORT_ORDER, SortOrder.ArtistSortOrder.ARTIST_A_Z);
    }

    public boolean isAlbumsInGrid() {
        return mPreferences.getBoolean(TOGGLE_ALBUM_GRID, true);
    }

    public boolean isArtistsInGrid() {
        return mPreferences.getBoolean(TOGGLE_ARTIST_GRID, true);
    }

    public boolean pauseEnabledOnDetach() {
        return mPreferences.getBoolean(TOGGLE_HEADPHONE_PAUSE, true);
    }

    public boolean getSetAlbumartLockscreen() {
        return mPreferences.getBoolean(SHOW_LOCKSCREEN_ALBUMART, true);
    }

    public boolean getXPosedTrackselectorEnabled() {
        return mPreferences.getBoolean(IS_LOGIN, false);
    }

    public boolean isLogin() {
        return mPreferences.getBoolean(IS_LOGIN, false);
    }

    public void setLogin(boolean state) {
        mPreferences.edit().putBoolean(IS_LOGIN, state).commit();
    }
}