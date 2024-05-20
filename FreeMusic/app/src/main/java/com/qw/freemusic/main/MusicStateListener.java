package com.qw.freemusic.main;

/**
 * created by QY
 * description:
 */
public interface MusicStateListener {

    void restartLoader();
    void onPlaylistChanged();
    void onMetaChanged();
}
