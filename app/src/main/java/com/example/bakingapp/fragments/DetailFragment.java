package com.example.bakingapp.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.activities.RecipeActivity;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private SimpleExoPlayer mExoPlayer;
    private TextView mStepDescriptionTextView;
    private ImageView leftArrowImageView;
    private ImageView rightArrowImageView;
    private DetailFragment detailFragment;
    private Uri mMediaUri;
    public static long position = -1;
    public static int clickedPosition;
    public static boolean isDetailFragmentCreated = false;
    public static long pausePosition = -1;
    public static boolean isOnBackPressed = false;
    public static boolean isOnNavigationUpPressed = false;
    private int orientation;

    @BindView(R.id.noVideoImageView) ImageView mNoVideoImageView;
    @BindView(R.id.playerView) SimpleExoPlayerView mPlayerView;


    public DetailFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        clickedPosition = getArguments().getInt("clickedPosition");

        ButterKnife.bind(this, rootView);

        if (getArguments().getLong("pausePosition") >= 0)
            pausePosition = getArguments().getLong("pausePosition");


        if (!isLandscape())
        {
            mStepDescriptionTextView = rootView.findViewById(R.id.stepDescriptionTextView);
            leftArrowImageView = rootView.findViewById(R.id.leftArrowImageView);
            rightArrowImageView = rootView.findViewById(R.id.rightArrowImageView);
            setNavigation();
            navigationOnClickListener();
        } else if (isLandscape() && !RecipeActivity.isTablet(getContext()))
        {
            setImageVideoResource();
        } else if (isLandscape() && RecipeActivity.isTablet(getContext()))
        {
            mStepDescriptionTextView = rootView.findViewById(R.id.stepDescriptionTextView);
        }
        isDetailFragmentCreated = true;
        orientation = getContext().getResources().getConfiguration().orientation;
        return rootView;
    }

    @Override
    public void onPause()
    {
        if (mExoPlayer != null)
            pausePosition = mExoPlayer.getCurrentPosition();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        super.onPause();
        if (Util.SDK_INT <= 23)
        {
            releasePlayer();
        }

    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (Util.SDK_INT > 23)
        {
            releasePlayer();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!isLandscape() || (isLandscape() && RecipeActivity.isTablet(getContext())))
        {
            if (setMedia())
                initializePlayer(mMediaUri);
        } else if (isLandscape() && !RecipeActivity.isTablet(getContext()))
        {
            setImageVideoResource();
            initializePlayer(mMediaUri);
        }

    }

    @Override
    public void onDestroyView()
    {
        if (mExoPlayer != null)
        {
            position = mExoPlayer.getCurrentPosition();
        }
        super.onDestroyView();
    }

    //Set the text and media of the fragment and returns boolean if video is present or not
    private boolean setMedia()
    {
        mStepDescriptionTextView.setText(RecipeActivity.mSteps[clickedPosition].getmRecipeDescription());
        return setImageVideoResource();
    }

    //Initialize the media Exoplayer
    private void initializePlayer(Uri mediaUri)
    {
        if (mExoPlayer == null)
        {
            try
            {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
                String userAgent = Util.getUserAgent(getContext(), "BakingApp");
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                        getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                mPlayerView.setPlayer(mExoPlayer);
                if (pausePosition != -1)
                    mExoPlayer.seekTo(pausePosition);
                else
                    mExoPlayer.seekTo(0);

                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);

                mPlayerView.hideController();

            } catch (Exception e)
            {
                Log.e(TAG, "exoplayer error" + e.toString());
            }
        }
    }

    //Release media Exoplayer after fragment is destroyed
    private void releasePlayer()
    {
        if (mExoPlayer != null)
        {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
        mExoPlayer = null;
    }

    //Adjusts navigation arrows depending on order of fragment clicked
    private void setNavigation()
    {
        if (leftArrowImageView != null)
        {
            if (clickedPosition == 0)
                leftArrowImageView.setVisibility(View.GONE);
        }
        if (rightArrowImageView != null)
        {
            if (clickedPosition == (RecipeActivity.mSteps.length - 1))
                rightArrowImageView.setVisibility(View.GONE);
        }
    }

    //Configure onClickListener for navigation buttons
    private void navigationOnClickListener()
    {
        if (leftArrowImageView != null)
            if (clickedPosition != 0)
                leftArrowImageView.setOnClickListener(v ->
                {
                    --clickedPosition;
                    setFragment();
                });
        if (rightArrowImageView != null)
            if (RecipeActivity.mPosition != RecipeActivity.mSteps.length - 1)
                rightArrowImageView.setOnClickListener(v ->
                {
                    ++clickedPosition;
                    setFragment();
                });
    }

    //Set fragment based on navigation
    void setFragment()
    {
        detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isReplaced", true);
        bundle.putInt("clickedPosition", clickedPosition);
        detailFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.testframe, detailFragment).commit();
    }

    //Check to see if orientation of device is landscape
    private boolean isLandscape()
    {
        int orientation = getActivity().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            return true;
        return false;
    }

    //Sets the video and image of fragment and returns boolean if video is present or not
    private boolean setImageVideoResource()
    {
        if (RecipeActivity.mSteps[clickedPosition].getmVideoURL().isEmpty())
        {
            if (RecipeActivity.mSteps[clickedPosition].getmThumbnailURL().isEmpty())
            {
                mPlayerView.setVisibility(View.GONE);
                mNoVideoImageView.setVisibility(View.VISIBLE);
                Picasso.get().load(R.drawable.default_image).into(mNoVideoImageView);
                return false;
            }
            //the case where there is a thumbnailUrl
            else
            {
                mPlayerView.setVisibility(View.GONE);
                mNoVideoImageView.setVisibility(View.VISIBLE);
                Picasso picasso = new Picasso.Builder(this.getContext())
                        .listener((picasso1, uri, exception) ->
                        {
                            Log.e(TAG, "error loading image resource,setting default image," + exception.getMessage());
                            Picasso.get().load(R.drawable.default_image).into(mNoVideoImageView);
                        })
                        .build();
                picasso.load(Uri.parse(RecipeActivity.mSteps[clickedPosition].getmThumbnailURL()))
                        .into(mNoVideoImageView);
                return true;
            }
        } else
        {
            mMediaUri = Uri.parse(RecipeActivity.mSteps[clickedPosition].getmVideoURL());
            return true;
        }
    }
}
