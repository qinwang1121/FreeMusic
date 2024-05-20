package com.qw.freemusic.view.fragment;

import android.os.AsyncTask;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.qw.freemusic.R;
import com.qw.freemusic.adapter.ArtistAdapter;
import com.qw.freemusic.dataloaders.ArtistLoader;
import com.qw.freemusic.foundation.BaseFragment;
import com.qw.freemusic.utils.PreferencesUtility;
import com.qw.freemusic.utils.ResourceUtil;
import com.qw.freemusic.view.widget.MusicRecyclerView;

/**
 * created by QY
 * description:
 */
public class ArtistsFragment extends BaseFragment {

    private MusicRecyclerView mRecyclerView;
    private ArtistAdapter mAdapter;
    private PreferencesUtility mPreferences;
    private boolean isGrid;
    private GridLayoutManager layoutManager;

    @Override
    protected String TAG() {
        return "ArtistsFragment";
    }

    @Override
    protected void initView(View pView) {
        mPreferences = PreferencesUtility.getInstance();
        isGrid = mPreferences.isArtistsInGrid();
        mRecyclerView = (MusicRecyclerView) pView.findViewById(R.id.mrv_artists);
        layoutManager = isGrid ? new GridLayoutManager(getActivity(), 2) : new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setEmptyView(getActivity(), pView.findViewById(R.id.list_empty), ResourceUtil.getString(R.string.no_artists_found), ResourceUtil.getColor(R.color.empty_listview_text_colo));
        new loadArtists().execute("");
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_artists;
    }

    class loadArtists extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... pStrings) {
            mAdapter = new ArtistAdapter(ArtistLoader.getAllArtists(getActivity()));
            return "Executed";
        }

        @Override
        protected void onPostExecute(String pS) {
            if (mAdapter != null) {
                mAdapter.setHasStableIds(true);
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }
}
