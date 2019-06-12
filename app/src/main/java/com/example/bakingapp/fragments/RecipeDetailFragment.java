package com.example.bakingapp.fragments;

import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.detail_text) TextView detailTextView;
    @BindView(R.id.current_step_text) TextView currentStepTextView;
    @BindView(R.id.back_button) FloatingActionButton backButton;
    @BindView(R.id.forward_button) FloatingActionButton forwardButton;
    @BindView(R.id.exoplayer) SimpleExoPlayerView exoPlayer;
    @BindView(R.id.detail_fragment_missing_text) TextView missingTextView;
    @BindView(R.id.fragment_linearlayout) LinearLayout linearLayout;
    @BindView(R.id.media_framelayout) FrameLayout mediaFrameLayout;
    @BindView(R.id.text_framelayout) FrameLayout textFrameLayout;
    @BindView(R.id.missing_image) ImageView missingImage;

    private List<Step> stepList;
    private SimpleExoPlayer mediaPlayer;
    private boolean isTablet;

    int currentIndex;
    int width;
    int height;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recipe_detail_fragment, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState == null) {

            Bundle extra = getArguments();
            stepList = extra.getParcelableArrayList("steps");
            isTablet = extra.getBoolean("tablet");
            currentIndex = extra.getInt("currentIndex");
        } else {

            stepList = savedInstanceState.getParcelableArrayList("steps");
            isTablet = savedInstanceState.getBoolean("tablet", false);
            currentIndex = savedInstanceState.getInt("currentIndex");
        }

        show();
        backButton.setOnClickListener(this);
        forwardButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.back_button) {
            if(currentIndex == 0) {
                return;
            }

            currentIndex--;
            show();
        } else if(id == R.id.forward_button) {
            if(currentIndex == stepList.size() - 1) {
                return;
            }

            currentIndex++;
            show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(isTablet) {
            return;
        }

//        hideSystemUi();
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mediaFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            textFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        } else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mediaFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(height, width / 2));
            textFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(height, width / 2));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(isTablet) {
            return;
        }

        ViewTreeObserver tree = linearLayout.getViewTreeObserver();
        tree.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                int track = getResources().getConfiguration().orientation;
                if (track != 1) {
                    width = linearLayout.getMeasuredWidth();
                    height = linearLayout.getMeasuredHeight();
                    mediaFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    textFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                } else {
                    height = linearLayout.getMeasuredWidth();
                    width = linearLayout.getMeasuredHeight();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();

        if(Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {

        super.onStop();
        if(Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) stepList);
        outState.putInt("currentIndex", currentIndex);
        outState.putBoolean("tablet", isTablet);
    }

    private void show() {

        if(currentIndex <= 0) {
//            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) backButton.getLayoutParams();
//            p.setAnchorId(View.NO_ID);
//            backButton.setLayoutParams(p);
//            backButton.setVisibility(View.INVISIBLE);
            backButton.hide();
        } else {
//            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) backButton.getLayoutParams();
//            p.setAnchorId(View.NO_ID);
//            backButton.setLayoutParams(p);
//            backButton.setVisibility(View.VISIBLE);
            backButton.show();
        }

//        if (listener != null) {
//            listener.setCurrent(currentIndex);
//        }

        if(currentIndex >= stepList.size() - 1) {
//            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) forwardButton.getLayoutParams();
//            p.setAnchorId(View.NO_ID);
//            forwardButton.setLayoutParams(p);
//            forwardButton.setVisibility(View.GONE);
            forwardButton.hide();
        } else {
//            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) forwardButton.getLayoutParams();
//            p.setAnchorId(View.NO_ID);
//            forwardButton.setLayoutParams(p);
//            forwardButton.setVisibility(View.VISIBLE);
            forwardButton.show();
        }

        releasePlayer();
        if(stepList.get(currentIndex).getVideoURL().isEmpty() && stepList.get(currentIndex).getThumbnailURL().isEmpty()) {

            exoPlayer.setVisibility(View.GONE);
            missingImage.setVisibility(View.GONE);
            missingTextView.setVisibility(View.VISIBLE);
        } else if(!stepList.get(currentIndex).getVideoURL().isEmpty()) {

            String videoUrl = stepList.get(currentIndex).getVideoURL();
            exoPlayer.setVisibility(View.VISIBLE);
            missingImage.setVisibility(View.GONE);
            missingTextView.setVisibility(View.GONE);
            initializePlayer(Uri.parse(videoUrl));
        } else {

            String imageUrl = stepList.get(currentIndex).getThumbnailURL();
            missingTextView.setVisibility(View.GONE);
            exoPlayer.setVisibility(View.GONE);
            missingImage.setVisibility(View.VISIBLE);

            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.default_image)
                    .into(missingImage);
        }

        hideSystemUi();
        detailTextView.setText(stepList.get(currentIndex).getDescription());
        currentStepTextView.setText((currentIndex + 1) + "/" + stepList.size());
    }

    private void initializePlayer(Uri uri) {
//TODO reimplement player
        DefaultRenderersFactory drf = new DefaultRenderersFactory(getActivity());
        DefaultTrackSelector dts = new DefaultTrackSelector();
        DefaultLoadControl dlc = new DefaultLoadControl();

        if(mediaPlayer == null) {
            mediaPlayer = ExoPlayerFactory.newSimpleInstance(drf, dts, dlc);
            exoPlayer.setPlayer(mediaPlayer);               mediaPlayer.setPlayWhenReady(true);
            MediaSource mediaSource = buildMediaSource(uri);
            mediaPlayer.prepare(mediaSource, true, false);
        }
    }

    private void releasePlayer() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri, new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    private void hideSystemUi() {
        if(isTablet)
        {
            return;
        }
        exoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        missingTextView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        missingImage.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }
}
