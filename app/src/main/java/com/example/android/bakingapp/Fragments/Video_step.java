package com.example.android.bakingapp.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Retrofit.Model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Video_step extends Fragment implements SimpleExoPlayer.EventListener {
    private static final String CURRENT_PLAYER_POSITION = "current_player_position";
    private static final String CURRENT_PLAYER_STATE = "current_player_state";
    private static final String HIDE_NAVIGATION = "hide_navigation";
    private static final String STEP_POSITION = "current_step_position";
    private static final String STEP_ARRAY = "step_array";
    private static MediaSessionCompat mMediaSession;
    @BindView(R.id.playerView)
    SimpleExoPlayerView video_step_pv;
    @BindView(R.id.step_description)
    TextView step_description_tv;
    @BindView(R.id.next_button)
    Button next_step_bt;
    @BindView(R.id.prev_button)
    Button prev_step_bt;
    private ArrayList<Step> mSteps;
    private int currentPosition;
    private String video_URL;
    private String thumbnail_URL;
    private SimpleExoPlayer mExoPlayer;
    private long currentMediaPlayerPosition;
    private boolean currentMediaPlayerState;
    private boolean mHideNavigation;
    private PlaybackStateCompat.Builder mStateBuilder;


    public Video_step() {
    }

    public static float[] getScreenSize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        return new float[]{
                (float) displayMetrics.widthPixels,
                (float) displayMetrics.heightPixels,
                displayMetrics.density};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(STEP_ARRAY);
            currentPosition = savedInstanceState.getInt(STEP_POSITION);
            currentMediaPlayerPosition = savedInstanceState.getLong(CURRENT_PLAYER_POSITION, 0);
            currentMediaPlayerState = savedInstanceState.getBoolean(CURRENT_PLAYER_STATE);
            mHideNavigation = savedInstanceState.getBoolean(HIDE_NAVIGATION, false);

        }

        final View rootView = inflater.inflate(R.layout.fragment_video_step, container, false);
        ButterKnife.bind(this, rootView);

        initializeMediaSession(getContext());

        setStepContent();

        if (currentPosition == mSteps.size() - 1) {
            next_step_bt.setVisibility(View.INVISIBLE);
        }
        if (currentPosition == 0) {
            prev_step_bt.setVisibility(View.INVISIBLE);
        }

        next_step_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //          releasePlayer();

                currentPosition++;
                if (currentPosition == mSteps.size()) {
                    currentPosition = 0;
                }
                prev_step_bt.setVisibility(View.VISIBLE);

                if (currentPosition == mSteps.size() - 1) {
                    next_step_bt.setVisibility(View.INVISIBLE);
                }
                currentMediaPlayerPosition = 0;
                setStepContent();

            }
        });

        prev_step_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentPosition--;
                if (currentPosition < 0) {
                    currentPosition = 0;
                }
                next_step_bt.setVisibility(View.VISIBLE);
                if (currentPosition == 0) {
                    prev_step_bt.setVisibility(View.INVISIBLE);
                }
                currentMediaPlayerPosition = 0;
                setStepContent();

            }
        });


        return rootView;
    }

    private void initializePlayer(Context context, Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            video_step_pv.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
        }

        String userAgent = Util.getUserAgent(context, "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                context, userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);

        if (currentMediaPlayerPosition != 0) {
            mExoPlayer.seekTo(currentMediaPlayerPosition);
            mExoPlayer.setPlayWhenReady(currentMediaPlayerState);
        } else {
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void initializeMediaSession(Context context) {
        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(context, "BakingApp");

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            currentMediaPlayerState = mExoPlayer.getPlayWhenReady();
            currentMediaPlayerPosition = mExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mSteps.get(currentPosition).getVideoURL())) {
            initializePlayer(getContext(), Uri.parse(mSteps.get(currentPosition).getVideoURL()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaSession.setActive(false);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP_ARRAY, mSteps);
        outState.putInt(STEP_POSITION, currentPosition);
        outState.putBoolean(HIDE_NAVIGATION, mHideNavigation);
        if (!TextUtils.isEmpty(mSteps.get(currentPosition).getVideoURL())) {
            //      currentMediaPlayerPosition = mExoPlayer.getCurrentPosition();
            outState.putLong(CURRENT_PLAYER_POSITION, currentMediaPlayerPosition);
            outState.putBoolean(CURRENT_PLAYER_STATE, currentMediaPlayerState);
        }
    }

    private void setStepContent() {

        step_description_tv.setText(mSteps.get(currentPosition).getDescription());

        if (!TextUtils.isEmpty(mSteps.get(currentPosition).getVideoURL())) {
            video_step_pv.setVisibility(View.VISIBLE);
            initializePlayer(
                    getContext(),
                    Uri.parse(mSteps.get(currentPosition).getVideoURL()));
            mExoPlayer.setPlayWhenReady(true);
        } else {
            video_step_pv.setVisibility(View.GONE);
            releasePlayer();
            if (!TextUtils.isEmpty(mSteps.get(currentPosition).getThumbnailURL())) {
                loadThumbnail();
            }

        }

        if (getActivity() != null && !mHideNavigation)
            getActivity().setTitle(mSteps.get(currentPosition).getShortDescription());
    }

    private void loadThumbnail() {
        if (!TextUtils.isEmpty(mSteps.get(currentPosition).getThumbnailURL())) {
            video_step_pv.setVisibility(View.VISIBLE);
            initializePlayer(
                    getContext(),
                    Uri.parse(mSteps.get(currentPosition).getThumbnailURL()));
            mExoPlayer.setPlayWhenReady(true);
        } else {
            video_step_pv.setVisibility(View.GONE);
            releasePlayer();
        }
    }

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
    }

    public void setPosition(int position) {
        currentPosition = position;
    }

    public void hideNavigation(boolean hideButtons) {
        mHideNavigation = hideButtons;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == SimpleExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(),
                    1f);

            // If starting to play a video or play button is clicked and if in landscape mode
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !mHideNavigation) {
                if (getActivity() != null) {
                    hideSystemUI();

                    float[] screenSize = getScreenSize(getActivity());
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) video_step_pv.getLayoutParams();
                    params.width = (int) screenSize[0]; // params.MATCH_PARENT;
                    params.height = (int) screenSize[1]; //params.MATCH_PARENT;
                    video_step_pv.setLayoutParams(params);
                }
            }

        } else if ((playbackState == SimpleExoPlayer.STATE_READY)) {
            mStateBuilder.setState(
                    PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(),
                    1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private void hideSystemUI() {
        if (getActivity() != null) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(

                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

}
