package com.qw.freemusic.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.qw.freemusic.R;
import com.qw.freemusic.foundation.BaseActivity;
import com.qw.freemusic.foundation.BaseFragment;
import com.qw.freemusic.main.MusicPlayer;
import com.qw.freemusic.main.MusicStateListener;
import com.qw.freemusic.utils.ImageUtils;
import com.qw.freemusic.utils.ResourceUtil;
import com.qw.freemusic.utils.TimberUtils;
import com.qw.freemusic.view.activity.NowPlayingActivity;
import com.qw.freemusic.view.widget.PlayPauseButton;

import net.steamcrafted.materialiconlib.MaterialIconView;

/**
 * created by QY
 * description:
 */
public class QuickControlsFragment extends BaseFragment implements MusicStateListener {

    public static View topContainer;
    private ProgressBar mProgress;
    private SeekBar mSeekBar;
    private int overflowcounter = 0;
    private PlayPauseButton mPlayPause, mPlayPauseExpanded;
    private TextView mTitle, mTitleExpanded;
    private TextView mArtist, mArtistExpanded;
    private ImageView mAlbumArt, mBlurredArt;
    private View rootView;
    private View playPauseWrapper, playPauseWrapperExpanded;
    private MaterialIconView previous, next;
    private boolean duetoplaypause = false;
    private boolean fragmentPaused = false;
    @Override
    protected String TAG() {
        return "QuickControlsFragment";
    }

    @Override
    protected void initView(View rootView) {
        this.rootView = rootView;
        mPlayPause = (PlayPauseButton) rootView.findViewById(R.id.play_pause);
        mPlayPauseExpanded = (PlayPauseButton) rootView.findViewById(R.id.playpause);
        playPauseWrapper = rootView.findViewById(R.id.play_pause_wrapper);
        playPauseWrapperExpanded = rootView.findViewById(R.id.playpausewrapper);
        playPauseWrapper.setOnClickListener(mPlayPauseListener);
        playPauseWrapperExpanded.setOnClickListener(mPlayPauseExpandedListener);
        mProgress = (ProgressBar) rootView.findViewById(R.id.song_progress_normal);
        mSeekBar = (SeekBar) rootView.findViewById(R.id.song_progress);
        mTitle = (TextView) rootView.findViewById(R.id.title);
        mArtist = (TextView) rootView.findViewById(R.id.artist);
        mTitleExpanded = (TextView) rootView.findViewById(R.id.song_title);
        mArtistExpanded = (TextView) rootView.findViewById(R.id.song_artist);
        mAlbumArt = (ImageView) rootView.findViewById(R.id.album_art_nowplayingcard);
        mBlurredArt = (ImageView) rootView.findViewById(R.id.blurredAlbumart);
        next = (MaterialIconView) rootView.findViewById(R.id.next);
        previous = (MaterialIconView) rootView.findViewById(R.id.previous);
        topContainer = rootView.findViewById(R.id.topContainer);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mProgress.getLayoutParams();
        mProgress.measure(0, 0);
        layoutParams.setMargins(0, -(mProgress.getMeasuredHeight() / 2), 0, 0);
        mProgress.setLayoutParams(layoutParams);

        mPlayPause.setColor(ResourceUtil.getColor(R.color.quick_play_stop_color));
        mPlayPauseExpanded.setColor(Color.WHITE);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    MusicPlayer.seek((long) i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MusicPlayer.next();
                    }
                }, 200);

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MusicPlayer.previous(getActivity(), false);
                    }
                }, 200);

            }
        });
        ((BaseActivity) getActivity()).setMusicStateListenerListener(this);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_playback_controls;
    }

    @Override
    public void onResume() {
        super.onResume();
        topContainer = rootView.findViewById(R.id.topContainer);
        topContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                Intent intent = new Intent(requireActivity(), NowPlayingActivity.class);
                requireActivity().startActivity(intent);
            }
        });
        fragmentPaused = false;
        if (mProgress != null)
            mProgress.postDelayed(mUpdateProgress, 10);
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentPaused = true;
    }

    @Override
    public void restartLoader() {

    }

    @Override
    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {
        updateNowplayingCard();
        updateState();
    }

    public Runnable mUpdateProgress = new Runnable() {

        @Override
        public void run() {
            long position = MusicPlayer.position();
            mProgress.setProgress((int) position);
            mSeekBar.setProgress((int) position);
            overflowcounter--;
            if (MusicPlayer.isPlaying()) {
                int delay = (int) (1500 - (position % 1000));
                if (overflowcounter < 0 && !fragmentPaused) {
                    overflowcounter++;
                    mProgress.postDelayed(mUpdateProgress, delay);
                }
            } else mProgress.removeCallbacks(this);

        }
    };

    private final View.OnClickListener mPlayPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            duetoplaypause = true;
            if (!mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(true);
                mPlayPause.startAnimation();
            } else {
                mPlayPause.setPlayed(false);
                mPlayPause.startAnimation();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MusicPlayer.playOrPause();
                }
            }, 200);

        }
    };

    private final View.OnClickListener mPlayPauseExpandedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            duetoplaypause = true;
            if (!mPlayPauseExpanded.isPlayed()) {
                mPlayPauseExpanded.setPlayed(true);
                mPlayPauseExpanded.startAnimation();
            } else {
                mPlayPauseExpanded.setPlayed(false);
                mPlayPauseExpanded.startAnimation();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MusicPlayer.playOrPause();
                }
            }, 200);
        }
    };

    public void updateNowplayingCard() {
        mTitle.setText(MusicPlayer.getTrackName());
        mArtist.setText(MusicPlayer.getArtistName());
        mTitleExpanded.setText(MusicPlayer.getTrackName());
        mArtistExpanded.setText(MusicPlayer.getArtistName());
//        if (!duetoplaypause) {
//            ImageLoader.getInstance().displayImage(TimberUtils.getAlbumArtUri(MusicPlayer.getCurrentAlbumId()).toString(), mAlbumArt,
//                    new DisplayImageOptions.Builder().cacheInMemory(true)
//                            .showImageOnFail(R.drawable.ic_empty_music2)
//                            .resetViewBeforeLoading(true)
//                            .build(), new ImageLoadingListener() {
//                        @Override
//                        public void onLoadingStarted(String imageUri, View view) {
//
//                        }
//
//                        @Override
//                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                            Bitmap failedBitmap = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.ic_empty_music2);
//                            if (getActivity() != null){
//                                new setBlurredAlbumArt().execute(failedBitmap);
//                            }
//                        }
//
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            if (getActivity() != null){
//                                new setBlurredAlbumArt().execute(loadedImage);
//                            }
//                        }
//
//                        @Override
//                        public void onLoadingCancelled(String imageUri, View view) {
//
//                        }
//                    });
//        }
        duetoplaypause = false;
        mProgress.setMax((int) MusicPlayer.duration());
        mSeekBar.setMax((int) MusicPlayer.duration());
        mProgress.postDelayed(mUpdateProgress, 10);
    }

    public void updateState() {
        if (MusicPlayer.isPlaying()) {
            if (!mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(true);
                mPlayPause.startAnimation();
            }
            if (!mPlayPauseExpanded.isPlayed()) {
                mPlayPauseExpanded.setPlayed(true);
                mPlayPauseExpanded.startAnimation();
            }
        } else {
            if (mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(false);
                mPlayPause.startAnimation();
            }
            if (mPlayPauseExpanded.isPlayed()) {
                mPlayPauseExpanded.setPlayed(false);
                mPlayPauseExpanded.startAnimation();
            }
        }
    }

    class setBlurredAlbumArt extends AsyncTask<Bitmap, Void, Drawable> {

        @Override
        protected Drawable doInBackground(Bitmap... loadedImage) {
            Drawable drawable = null;
            try {
                drawable = ImageUtils.createBlurredImageFromBitmap(loadedImage[0], getActivity(), 6);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                if (mBlurredArt.getDrawable() != null) {
                    final TransitionDrawable td =
                            new TransitionDrawable(new Drawable[]{
                                    mBlurredArt.getDrawable(),
                                    result
                            });
                    mBlurredArt.setImageDrawable(td);
                    td.startTransition(400);

                } else {
                    mBlurredArt.setImageDrawable(result);
                }
            }
        }

        @Override
        protected void onPreExecute() {
        }
    }
}
