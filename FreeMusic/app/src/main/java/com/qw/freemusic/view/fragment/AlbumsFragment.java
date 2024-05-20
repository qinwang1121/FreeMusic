package com.qw.freemusic.view.fragment;

import android.os.AsyncTask;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.qw.freemusic.R;
import com.qw.freemusic.adapter.AlbumAdapter;
import com.qw.freemusic.dataloaders.AlbumLoader;
import com.qw.freemusic.foundation.BaseFragment;
import com.qw.freemusic.utils.PreferencesUtility;
import com.qw.freemusic.utils.ResourceUtil;
import com.qw.freemusic.view.widget.MusicRecyclerView;

/**
 * created by QY
 * description:
 */
public class AlbumsFragment extends BaseFragment {

    private MusicRecyclerView mRecyclerView;
    private AlbumAdapter mAdapter;

    private PreferencesUtility mPreferences;
    private boolean isGrid;
    private GridLayoutManager layoutManager;

    @Override
    protected String TAG() {
        return "AlbumsFragment";
    }

    @Override
    protected void initView(View pView) {
        mPreferences = PreferencesUtility.getInstance();
        isGrid = mPreferences.isAlbumsInGrid();

        mRecyclerView = (MusicRecyclerView) pView.findViewById(R.id.mrv_albums);
        layoutManager = isGrid ? new GridLayoutManager(getActivity(), 2) : new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setEmptyView(getActivity(), pView.findViewById(R.id.list_empty),
                ResourceUtil.getString(R.string.no_albums_found),
                ResourceUtil.getColor(R.color.empty_listview_text_colo));
        new loadAlbums().execute("");
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_albums;
    }

    class loadAlbums extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... pStrings) {
            mAdapter = new AlbumAdapter(AlbumLoader.getAllAlbums(getActivity()));
            return "Executed";
        }

        @Override
        protected void onPostExecute(String pS) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
