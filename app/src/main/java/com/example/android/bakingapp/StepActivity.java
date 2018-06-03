package com.example.android.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.Retrofit.Model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.EmptyStackException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {

    private static final String TAG = "verificare video";
    @BindView(R.id.playerView)
    SimpleExoPlayerView video_step_pv;
    @BindView(R.id.step_description)
    TextView step_description_tv;
    @BindView(R.id.next_button)
    Button next_step_bt;
    @BindView(R.id.prev_button)
    Button prev_step_bt;
    private ArrayList<Step> dStep;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private int position;
    private int currentPosition;
    private long currentMediaPlayerPosition;
    private String video_URL;
    private String thumbnail_URL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_detail_activity);
        ButterKnife.bind(this);

        int config = this.getResources().getConfiguration().orientation;
        if (config == Configuration.ORIENTATION_LANDSCAPE) {
            step_description_tv.setVisibility(View.GONE);
            next_step_bt.setVisibility(View.GONE);
            prev_step_bt.setVisibility(View.GONE);
            //       this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        if (savedInstanceState == null) {
            Bundle data = getIntent().getExtras();
            dStep = data.getParcelableArrayList("steps");
            position = data.getInt("position");
            Intent intent = getIntent();
            if (intent == null) {
                closeOnError();
                currentMediaPlayerPosition = 0;
            }
        } else {
            dStep = savedInstanceState.getParcelableArrayList("list");
            position = savedInstanceState.getInt("position");
        }

        currentPosition = position;
        setTitle(dStep.get(position).getShortDescription());
        step_description_tv.setText(dStep.get(position).getDescription());
        video_URL = dStep.get(position).getVideoURL();
        thumbnail_URL = dStep.get(position).getThumbnailURL();

        if (savedInstanceState == null) {
            if (video_URL != null && !video_URL.isEmpty()) {
                video_step_pv.setVisibility(View.VISIBLE);
                initializePlayer(Uri.parse(video_URL));
            } else if (thumbnail_URL != null && !thumbnail_URL.isEmpty()) {
                initializePlayer(Uri.parse(thumbnail_URL));
            } else {
                video_step_pv.setVisibility(View.GONE);
            }
        }

        next_step_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releasePlayer();

                currentPosition++;
                if (currentPosition > dStep.size()) {
                    currentPosition = 0;
                }
                setTitle(dStep.get(currentPosition).getShortDescription());
                step_description_tv.setText(dStep.get(currentPosition).getDescription());
                video_URL = dStep.get(currentPosition).getVideoURL();
                thumbnail_URL = dStep.get(currentPosition).getThumbnailURL();


                if (video_URL != null && !video_URL.isEmpty()) {
                    video_step_pv.setVisibility(View.VISIBLE);
                    initializePlayer(Uri.parse(video_URL));
                } else if (thumbnail_URL != null && !thumbnail_URL.isEmpty()) {
                    initializePlayer(Uri.parse(thumbnail_URL));
                } else {
                    video_step_pv.setVisibility(View.GONE);
                }

            }
        });

        prev_step_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releasePlayer();

                currentPosition--;
                if (currentPosition < 0) {
                    currentPosition = 0;
                }
                setTitle(dStep.get(currentPosition).getShortDescription());
                step_description_tv.setText(dStep.get(currentPosition).getDescription());
                video_URL = dStep.get(currentPosition).getVideoURL();
                thumbnail_URL = dStep.get(currentPosition).getThumbnailURL();


                if (video_URL != null && !video_URL.isEmpty()) {
                    video_step_pv.setVisibility(View.VISIBLE);
                    initializePlayer(Uri.parse(video_URL));
                } else if (thumbnail_URL != null && !thumbnail_URL.isEmpty()) {
                    initializePlayer(Uri.parse(thumbnail_URL));
                } else {
                    video_step_pv.setVisibility(View.GONE);
                }

            }
        });
    }


    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            video_step_pv.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        }
    }

    private void initializePlayer() {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            video_step_pv.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(video_URL), new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(currentMediaPlayerPosition);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            step_description_tv.setVisibility(View.GONE);
            next_step_bt.setVisibility(View.GONE);
            prev_step_bt.setVisibility(View.GONE);

            try {
                initializePlayer(Uri.parse(video_URL));
            } catch (EmptyStackException a) {
                Log.d(TAG, "empty video");
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            step_description_tv.setVisibility(View.VISIBLE);
            next_step_bt.setVisibility(View.VISIBLE);
            prev_step_bt.setVisibility(View.VISIBLE);
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Error !!!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("list", dStep);
        outState.putInt("position", position);
        outState.putString("currentUrl", video_URL);
        outState.putInt("currentPosition", currentPosition);
        currentMediaPlayerPosition = mExoPlayer.getCurrentPosition();
        outState.putLong("currentMediaPosition", currentMediaPlayerPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        dStep = savedInstanceState.getParcelableArrayList("list");
        position = savedInstanceState.getInt("position");
        currentPosition = savedInstanceState.getInt("currentPosition");
        video_URL = savedInstanceState.getString("currentUrl");
        currentMediaPlayerPosition = savedInstanceState.getLong("currentMediaPosition");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (video_URL != null && !video_URL.isEmpty()) {
            video_step_pv.setVisibility(View.VISIBLE);
            initializePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }
}
