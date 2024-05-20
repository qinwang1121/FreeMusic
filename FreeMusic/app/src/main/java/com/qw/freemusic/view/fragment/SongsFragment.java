package com.qw.freemusic.view.fragment;

import android.os.AsyncTask;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.qw.freemusic.R;
import com.qw.freemusic.adapter.SongsListAdapter;
import com.qw.freemusic.foundation.BaseActivity;
import com.qw.freemusic.foundation.BaseFragment;
import com.qw.freemusic.main.MusicStateListener;
import com.qw.freemusic.utils.ResourceUtil;
import com.qw.freemusic.dataloaders.SongLoader;
import com.qw.freemusic.view.widget.MusicRecyclerView;

/**
 * created by QY
 * description:
 */
public class SongsFragment extends BaseFragment implements MusicStateListener{

    private MusicRecyclerView mRecyclerView;
    private SongsListAdapter mAdapter;
    @Override
    protected String TAG() {
        return "SongsFragment";
    }

    @Override
    protected void initView(View pView) {
        mRecyclerView = (MusicRecyclerView) pView.findViewById(R.id.mrv_songs);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setEmptyView(getActivity(),
                pView.findViewById(R.id.list_empty),
                ResourceUtil.getString(R.string.no_songs_found),
                ResourceUtil.getColor(R.color.empty_listview_text_colo));
        new loadSongs().execute("");
        ((BaseActivity)getActivity()).setMusicStateListenerListener(this);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_songs;
    }

    @Override
    public void restartLoader() {

    }

    @Override
    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    class loadSongs extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... pStrings) {
            mAdapter = new SongsListAdapter(SongLoader.getAllSongs(getActivity()));
            return "Executed";
        }

        @Override
        protected void onPostExecute(String pS) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}