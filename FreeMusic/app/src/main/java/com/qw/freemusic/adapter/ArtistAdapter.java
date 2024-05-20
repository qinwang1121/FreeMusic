package com.qw.freemusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.qw.freemusic.MainApplication;
import com.qw.freemusic.R;
import com.qw.freemusic.entity.Artist;
import com.qw.freemusic.utils.PreferencesUtility;
import com.qw.freemusic.utils.TimberUtils;

import java.util.List;

/**
 * created by QY
 * description:
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ItemHolder> {

    private List<Artist> arraylist;
    private boolean isGrid;

    public ArtistAdapter(List<Artist> arraylist) {
        this.arraylist = arraylist;
        this.isGrid = PreferencesUtility.getInstance().isArtistsInGrid();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (isGrid) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_artist_grid, null);
            ItemHolder ml = new ItemHolder(v);
            return ml;
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_artist, null);
            ItemHolder ml = new ItemHolder(v);
            return ml;
        }
    }

    @Override
    public void onBindViewHolder(final ItemHolder itemHolder, int i) {
        final Artist localItem = arraylist.get(i);
        itemHolder.name.setText(localItem.name);
        String albumNmber = TimberUtils.makeLabel(MainApplication.sContext, R.plurals.Nalbums, localItem.albumCount);
        String songCount = TimberUtils.makeLabel(MainApplication.sContext, R.plurals.Nsongs, localItem.songCount);
        itemHolder.albums.setText(TimberUtils.makeCombinedString(MainApplication.sContext, albumNmber, songCount));
    }

    @Override
    public long getItemId(int position) {
        return arraylist.get(position).id;
    }

    @Override
    public int getItemCount() {
        return (null != arraylist ? arraylist.size() : 0);
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView name, albums;
        protected ImageView artistImage;
        protected View footer;

        public ItemHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.artist_name);
            this.albums = (TextView) view.findViewById(R.id.album_song_count);
            this.artistImage = (ImageView) view.findViewById(R.id.artistImage);
            this.footer = view.findViewById(R.id.footer);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
