package com.vitech.bakersbook;


import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
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
import com.squareup.picasso.Picasso;
import com.vitech.bakersbook.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstructionFragment extends Fragment implements ExoPlayer.EventListener{
    public static final String ARG_INSTRUCTION = "instruction";
    private static final String ARG_POSITION = "position";

@BindView(R.id.instruction_video)
 SimpleExoPlayerView instructionVideo;
@BindView(R.id.instruction_description)
 TextView instructionDescription;
@BindView(R.id.thumbnail)
 ImageView thumbNail;
    private SimpleExoPlayer player;
    private boolean HAS_VIDEO = true;
    private long currentPosition = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(ARG_POSITION)){
                currentPosition = savedInstanceState.getLong(ARG_POSITION);
            }
        }
    }

    public InstructionFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View contentView =  inflater.inflate(R.layout.fragment_video_player, container, false);
        ButterKnife.bind(this,contentView);
        try{
            JSONObject instruction = new JSONObject(getArguments().getString(ARG_INSTRUCTION));
            instructionDescription.setText(instruction.getString("description"));
            if(instruction.getString("videoURL").equals("")){
                HAS_VIDEO = false;
                instructionVideo.setVisibility(View.GONE);
                return contentView;
            }

            thumbNail.setImageResource(R.drawable.ic_cookies);

            if(!TextUtils.isEmpty(instruction.getString("thumbnailURL"))){
                Picasso.with(getContext()).load(instruction.getString("thumbnailURL")).error(R.drawable.ic_cookies).into(thumbNail);

            }
            TrackSelector trackSelector = new DefaultTrackSelector();
            player =  ExoPlayerFactory.newSimpleInstance(getContext(),trackSelector);
            instructionVideo.setPlayer(player);

            Uri mediaUri = Uri.parse(instruction.getString("videoURL"));
            MediaSource source = new ExtractorMediaSource(mediaUri,new DefaultDataSourceFactory(getContext(),"Agent"),new DefaultExtractorsFactory(),null,null);
            player.prepare(source);

        }catch (JSONException j){
            j.printStackTrace();
        }
        return contentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(HAS_VIDEO) {
            player.setPlayWhenReady(true);
            player.seekTo(currentPosition);

        }    }

    @Override
    public void onPause() {
        super.onPause();
        if(HAS_VIDEO) {
            currentPosition = player.getCurrentPosition();
            player.setPlayWhenReady(false);
            player.stop();
            player.release();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(HAS_VIDEO) {
       outState.putLong(ARG_POSITION,player.getCurrentPosition());
        }
        super.onSaveInstanceState(outState);
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
        if(playbackState==PlaybackState.STATE_BUFFERING){
            Log.d("state","BUFFERING");
        }
        if(playbackState==ExoPlayer.STATE_READY&&playWhenReady){
            Log.d("state","Playing");
        }
        if(playbackState==ExoPlayer.STATE_READY&&!playWhenReady){
            Log.d("state","PAUSED");
        }

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }



}
