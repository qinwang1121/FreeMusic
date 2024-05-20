package com.qw.freemusic.entity;

/**
 * created by QY at 2024/5/13
 * description:
 */
public class Song {

    /*专辑Id*/
    public final long albumId;
    /*专辑名称*/
    public final String albumName;
    /*艺术家Id*/
    public final long artistId;
    /*艺术家名称*/
    public final String artistName;
    /*时间*/
    public final int duration;
    /*id*/
    public final long id;
    /*标题*/
    public final String title;
    /*曲目编号*/
    public final int trackNumber;

    public Song() {
        this.id = -1;
        this.albumId = -1;
        this.artistId = -1;
        this.title = "";
        this.artistName = "";
        this.albumName = "";
        this.duration = -1;
        this.trackNumber = -1;
    }

    public Song(long _id, long _albumId, long _artistId, String _title, String _artistName, String _albumName, int _duration, int _trackNumber) {
        this.id = _id;
        this.albumId = _albumId;
        this.artistId = _artistId;
        this.title = _title;
        this.artistName = _artistName;
        this.albumName = _albumName;
        this.duration = _duration;
        this.trackNumber = _trackNumber;
    }

    @Override
    public String toString() {
        return "Song{" +
                "albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                ", duration=" + duration +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", trackNumber=" + trackNumber +
                '}';
    }
}
