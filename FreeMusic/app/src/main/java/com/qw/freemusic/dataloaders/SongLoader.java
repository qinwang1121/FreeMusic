package com.qw.freemusic.dataloaders;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.qw.freemusic.MainApplication;
import com.qw.freemusic.entity.Song;
import com.qw.freemusic.utils.PreferencesUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * created by QY
 * description:
 */
public class SongLoader {
    private static final long[] sEmptyList = new long[0];
    public static ArrayList<Song> getSongsForCursor(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String artist = cursor.getString(2);
                String album = cursor.getString(3);
                int duration = cursor.getInt(4);
                int trackNumber = cursor.getInt(5);
                long artistId = cursor.getInt(6);
                long albumId = cursor.getLong(7);
                arrayList.add(new Song(id, albumId, artistId, title, artist, album, duration, trackNumber));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static Song getSongForCursor(Cursor cursor) {
        Song song = new Song();
        if ((cursor != null) && (cursor.moveToFirst())) {
            long id = cursor.getLong(0);
            String title = cursor.getString(1);
            String artist = cursor.getString(2);
            String album = cursor.getString(3);
            int duration = cursor.getInt(4);
            int trackNumber = cursor.getInt(5);
            long artistId = cursor.getInt(6);
            long albumId = cursor.getLong(7);

            song = new Song(id, albumId, artistId, title, artist, album, duration, trackNumber);
        }

        if (cursor != null)
            cursor.close();
        return song;
    }

    public static final long[] getSongListForCursor(Cursor cursor) {
        if (cursor == null) {
            return sEmptyList;
        }
        final int len = cursor.getCount();
        final long[] list = new long[len];
        cursor.moveToFirst();
        int columnIndex = -1;
        try {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.AUDIO_ID);
        } catch (final IllegalArgumentException notaplaylist) {
            columnIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID);
        }
        for (int i = 0; i < len; i++) {
            list[i] = cursor.getLong(columnIndex);
            cursor.moveToNext();
        }
        cursor.close();
        cursor = null;
        return list;
    }

    public static Song getSongFromPath(String songPath, Context context) {
        ContentResolver cr = context.getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.DATA;
        String[] selectionArgs = {songPath};
        String[] projection = new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id"};
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = cr.query(uri, projection, selection + "=?", selectionArgs, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            Song song = getSongForCursor(cursor);
            cursor.close();
            return song;
        }
        else return new Song();
    }

    public static ArrayList<Song> getAllSongs(Context context) {
        return getSongsForCursor(makeSongCursor(context, null, null));
    }

    public static Song getSongForID(Context context, long id) {
        return getSongForCursor(makeSongCursor(context, "_id=" + String.valueOf(id), null));
    }

    public static List<Song> searchSongs(Context context, String searchString, int limit) {
        ArrayList<Song> result = getSongsForCursor(makeSongCursor(context, "title LIKE ?", new String[]{searchString + "%"}));
        if (result.size() < limit) {
            result.addAll(getSongsForCursor(makeSongCursor(context, "title LIKE ?", new String[]{"%_" + searchString + "%"})));
        }
        return result.size() < limit ? result : result.subList(0, limit);
    }

    public static Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString) {
        final String songSortOrder = PreferencesUtility.getInstance().getSongSortOrder();
        return makeSongCursor(context, selection, paramArrayOfString, songSortOrder);
    }
    private static Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString, String sortOrder) {
        String selectionStatement = "is_music=1 AND title != ''";
        if (!TextUtils.isEmpty(selection)) {
            selectionStatement = selectionStatement + " AND " + selection;
        }
        Cursor lCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id"},
                selectionStatement, paramArrayOfString, sortOrder);
        return lCursor;
    }
}
