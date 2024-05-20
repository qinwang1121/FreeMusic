package com.qw.freemusic.entity;

/**
 * created by QY at 2024/5/13
 * description:
 */
public class Album {

    /*专辑Id*/
    public final long artistId;
    /*专辑名称*/
    public final String artistName;
    /*id*/
    public final long id;
    /*歌曲数量*/
    public final int songCount;
    /*标题*/
    public final String title;
    public final int year;

    public Album() {
        this.id = -1;
        this.title = "";
        this.artistName = "";
        this.artistId = -1;
        this.songCount = -1;
        this.year = -1;
    }

    public Album(long _id, String _title, String _artistName, long _artistId, int _songCount, int _year) {
        this.id = _id;
        this.title = _title;
        this.artistName = _artistName;
        this.artistId = _artistId;
        this.songCount = _songCount;
        this.year = _year;
    }
}
