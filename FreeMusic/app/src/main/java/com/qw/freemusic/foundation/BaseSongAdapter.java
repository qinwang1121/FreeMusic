package com.qw.freemusic.foundation;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qw.freemusic.MainApplication;
import com.qw.freemusic.entity.IdType;
import com.qw.freemusic.entity.Song;
import com.qw.freemusic.main.MusicPlayer;

import java.util.List;

/**
 * created by QY
 * description:
 */
public abstract class BaseSongAdapter <V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V>{

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull V holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(View view) {
            super(view);
        }

    }

    public void playAll(final long[] list, int position) {
        MusicPlayer.playAll(MainApplication.sContext, list, position, -1, IdType.NA, false);
    }

    public void removeSongAt(int i){}

    public void updateDataSet(List<Song> arraylist) {}
}
