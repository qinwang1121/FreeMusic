package com.qw.freemusic.entity;

/**
 * created by QY at 2024/5/13
 * description:
 */
public class Artist {

    /*专辑数*/
    public final int albumCount;
    /*id*/
    public final long id;
    /*艺术家名称*/
    public final String name;
    /*歌曲数*/
    public final int songCount;

    public Artist() {
        this.id = -1;
        this.name = "";
        this.songCount = -1;
        this.albumCount = -1;
    }

    public Artist(long _id, String _name, int _albumCount, int _songCount) {
        this.id = _id;
        this.name = _name;
        this.songCount = _songCount;
        this.albumCount = _albumCount;
    }
}
