package com.qw.freemusic.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qw.freemusic.MainApplication;
import com.qw.freemusic.R;
import com.qw.freemusic.entity.Song;
import com.qw.freemusic.foundation.BaseSongAdapter;
import com.qw.freemusic.main.MusicPlayer;
import com.qw.freemusic.utils.ResourceUtil;
import com.qw.freemusic.view.widget.MusicVisualizer;

import java.util.List;

/**
 * created by QY
 * description:
 */
public class SongsListAdapter extends BaseSongAdapter<SongsListAdapter.ItemHolder> {

    public int currentlyPlayingPosition;
    private List<Song> arraylist;
    private long[] songIDs;
    private int lastPosition = -1;
    private long playlistId;

    public SongsListAdapter(List<Song> arraylist) {
        this.arraylist = arraylist;
        this.songIDs = getSongIds();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, null);
        ItemHolder lItemHolder = new ItemHolder(lView);
        return lItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int position) {
        Song localItem = arraylist.get(position);
        itemHolder.title.setText(localItem.title);
        itemHolder.artist.setText(localItem.artistName);
//        ImageLoader.getInstance().displayImage(TimberUtils.getAlbumArtUri(localItem.albumId).toString(),
//                itemHolder.albumArt, new DisplayImageOptions.Builder().cacheInMemory(true)
//                        .showImageOnLoading(R.drawable.ic_empty_music2)
//                        .resetViewBeforeLoading(true).build());
        if (MusicPlayer.getCurrentAudioId() == localItem.id) {
            itemHolder.title.setTextColor(ResourceUtil.getColor(R.color.item_song_title_color_playing));
            if (MusicPlayer.isPlaying()) {
                itemHolder.visualizer.setColor(ResourceUtil.getColor(R.color.item_song_visualizer_color));
                itemHolder.visualizer.setVisibility(View.VISIBLE);
            } else {
                itemHolder.visualizer.setVisibility(View.GONE);
            }
        } else {
            itemHolder.visualizer.setVisibility(View.GONE);
            itemHolder.title.setTextColor(ResourceUtil.getColor(R.color.item_song_title_color));
        }

//        setOnPopupMenuListener(itemHolder, position);
    }

    @Override
    public int getItemCount() {
        return (null != arraylist ? arraylist.size() : 0);
    }

    @Override
    public void updateDataSet(List<Song> arraylist) {
        this.arraylist = arraylist;
        this.songIDs = getSongIds();
    }

    @Override
    public void removeSongAt(int i) {
        arraylist.remove(i);
        updateDataSet(arraylist);
    }

    public long[] getSongIds() {
        long[] ret = new long[getItemCount()];
        for (int i = 0; i < getItemCount(); i++) {
            ret[i] = arraylist.get(i).id;
        }
        return ret;
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title, artist;
        private ImageView albumArt, popupMenu;
        private MusicVisualizer visualizer;

        public ItemHolder(View pView) {
            super(pView);
            this.title = (TextView) pView.findViewById(R.id.song_title);
            this.artist = (TextView) pView.findViewById(R.id.song_artist);
            this.albumArt = (ImageView) pView.findViewById(R.id.albumArt);
            this.popupMenu = (ImageView) pView.findViewById(R.id.popup_menu);
            visualizer = (MusicVisualizer) pView.findViewById(R.id.visualizer);
            pView.setOnClickListener(this);
        }

        @Override
        public void onClick(View pView) {
            Handler lHandler = new Handler();
            lHandler.postDelayed(() -> {
                playAll(songIDs, getAdapterPosition());
                Handler lHandler1 = new Handler();
                lHandler1.postDelayed(() -> {
                    notifyItemChanged(currentlyPlayingPosition);
                    notifyItemChanged(getAdapterPosition());
                }, 50);
            }, 100);
        }
    }
}
