package com.qw.freemusic.view.fragment;

import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qw.freemusic.R;
import com.qw.freemusic.foundation.BaseActivity;
import com.qw.freemusic.foundation.BaseFragment;
import com.qw.freemusic.main.MusicPlayer;
import com.qw.freemusic.main.MusicService;
import com.qw.freemusic.main.MusicStateListener;
import com.qw.freemusic.utils.ResourceUtil;
import com.qw.freemusic.utils.TimberUtils;
import com.qw.freemusic.view.widget.PlayPauseDrawable;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

/**
 * created by QY
 * description:
 */
public class NowPlayingFragment extends BaseFragment implements MusicStateListener {

    private MaterialIconView previous, next;
    private PlayPauseDrawable playPauseDrawable = new PlayPauseDrawable();
    private FloatingActionButton playPauseFloating;

    private int overflowcounter = 0;
    private TextView songtitle, songalbum, songartist, songduration, elapsedtime;
    private SeekBar mProgress;
    boolean fragmentPaused = false;
    private boolean duetoplaypause = false;

    public ImageView albumart, shuffle, repeat;
    public int accentColor;

    @Override
    protected String TAG() {
        return "NowPlayingFragment";
    }

    @Override
    protected void initView(View view) {
        ((BaseActivity) getActivity()).setMusicStateListenerListener(this);

        albumart = (ImageView) view.findViewById(R.id.album_art);
        shuffle = (ImageView) view.findViewById(R.id.shuffle);
        repeat = (ImageView) view.findViewById(R.id.repeat);
        next = (MaterialIconView) view.findViewById(R.id.next);
        previous = (MaterialIconView) view.findViewById(R.id.previous);
        playPauseFloating = (FloatingActionButton) view.findViewById(R.id.playpausefloating);
        playPauseFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                MusicPlayer.playOrPause();
            }
        });

        songtitle = (TextView) view.findViewById(R.id.song_title);
        songalbum = (TextView) view.findViewById(R.id.song_album);
        songartist = (TextView) view.findViewById(R.id.song_artist);
        songduration = (TextView) view.findViewById(R.id.song_duration);
        elapsedtime = (TextView) view.findViewById(R.id.song_elapsed_time);

        mProgress = (SeekBar) view.findViewById(R.id.song_progress);

        songtitle.setSelected(true);

        if (playPauseFloating != null) {
            playPauseDrawable.setColorFilter(TimberUtils.getBlackWhiteColor(accentColor), PorterDuff.Mode.MULTIPLY);
            playPauseFloating.setImageDrawable(playPauseDrawable);
            if (MusicPlayer.isPlaying())
                playPauseDrawable.transformToPause(false);
            else playPauseDrawable.transformToPlay(false);
        }

        setSongDetails();
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_now_playing;
    }

    @Override
    public void restartLoader() {

    }

    @Override
    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {
        updateSongDetails();
    }

    private void setSongDetails() {
        updateSongDetails();
        setSeekBarListener();
        if (next != null) {
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
        }
        if (previous != null) {
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
        }

        updateShuffleState();
        updateRepeatState();
    }

    public void updateShuffleState() {
        if (shuffle != null && getActivity() != null) {
            MaterialDrawableBuilder builder = MaterialDrawableBuilder.with(getActivity())
                    .setIcon(MaterialDrawableBuilder.IconValue.SHUFFLE)
                    .setSizeDp(30);

            if (getActivity() != null) {
                if (MusicPlayer.getShuffleMode() == 0) {
                    builder.setColor(ResourceUtil.getColor(R.color.music_model_color_no));
                } else builder.setColor(ResourceUtil.getColor(R.color.music_model_color));
            }

            shuffle.setImageDrawable(builder.build());
            shuffle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicPlayer.cycleShuffle();
                    updateShuffleState();
                    updateRepeatState();
                }
            });
        }
    }

    public void updateRepeatState() {
        if (repeat != null && getActivity() != null) {
            MaterialDrawableBuilder builder = MaterialDrawableBuilder.with(getActivity())
                    .setSizeDp(30);

            if (MusicPlayer.getRepeatMode() == MusicService.REPEAT_NONE) {
                builder.setIcon(MaterialDrawableBuilder.IconValue.REPEAT);
                builder.setColor(ResourceUtil.getColor(R.color.music_model_color_no));
            } else if (MusicPlayer.getRepeatMode() == MusicService.REPEAT_CURRENT) {
                builder.setIcon(MaterialDrawableBuilder.IconValue.REPEAT_ONCE);
                builder.setColor(ResourceUtil.getColor(R.color.music_model_color));
            } else if (MusicPlayer.getRepeatMode() == MusicService.REPEAT_ALL) {
                builder.setColor(ResourceUtil.getColor(R.color.music_model_color));
                builder.setIcon(MaterialDrawableBuilder.IconValue.REPEAT);
            }


            repeat.setImageDrawable(builder.build());
            repeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicPlayer.cycleRepeat();
                    updateRepeatState();
                    updateShuffleState();
                }
            });
        }
    }

    public void updateSongDetails() {
        //do not reload image if it was a play/pause change
        if (!duetoplaypause) {
            if (songtitle != null && MusicPlayer.getTrackName() != null) {
                songtitle.setText(MusicPlayer.getTrackName());
                if(MusicPlayer.getTrackName().length() <= 23){
                    songtitle.setTextSize(25);
                }
                else if(MusicPlayer.getTrackName().length() >= 30){
                    songtitle.setTextSize(18);
                }
                else{
                    songtitle.setTextSize(18 + (MusicPlayer.getTrackName().length() - 24));
                }
                Log.v("BaseNowPlayingFrag", "Title Text Size: " + songtitle.getTextSize());
            }
            if (songartist != null) {
                songartist.setText(MusicPlayer.getArtistName());
            }
            if (songalbum != null)
                songalbum.setText(MusicPlayer.getAlbumName());

        }
        duetoplaypause = false;
        if (playPauseFloating != null)
            updatePlayPauseFloatingButton();
        if (songduration != null && getActivity() != null)
            songduration.setText(TimberUtils.makeShortTimeString(getActivity(), MusicPlayer.duration() / 1000));
        if (mProgress != null) {
            mProgress.setMax((int) MusicPlayer.duration());
            if (mUpdateProgress != null) {
                mProgress.removeCallbacks(mUpdateProgress);
            }
            mProgress.postDelayed(mUpdateProgress, 10);
        }
    }

    public void updatePlayPauseFloatingButton() {
        if (MusicPlayer.isPlaying()) {
            playPauseDrawable.transformToPause(false);
        } else {
            playPauseDrawable.transformToPlay(false);
        }
    }

    private void setSeekBarListener() {
        if (mProgress != null)
            mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
    }

    public Runnable mUpdateProgress = new Runnable() {

        @Override
        public void run() {

            long position = MusicPlayer.position();
            if (mProgress != null) {
                mProgress.setProgress((int) position);
                if (elapsedtime != null && getActivity() != null)
                    elapsedtime.setText(TimberUtils.makeShortTimeString(getActivity(), position / 1000));
            }
            overflowcounter--;
            int delay = 250; //not sure why this delay was so high before
            if (overflowcounter < 0 && !fragmentPaused) {
                overflowcounter++;
                mProgress.postDelayed(mUpdateProgress, delay); //delay
            }
        }
    };
}
