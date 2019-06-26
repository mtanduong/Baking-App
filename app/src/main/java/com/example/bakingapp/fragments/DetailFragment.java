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

public class DetailFragment extends Fragment {

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ImageView mNoVideoImageView;
    private TextView mStepDescriptionTextView;
    private ImageView leftArrowImageView;
    private ImageView rightArrowImageView;
    private DetailFragment detailFragment;
    private Uri mMediaUri;
    public static long position = -1;
    public static int clickedPosition;
    private final String TAG = getClass().getSimpleName();
    public static boolean isDetailFragmentCreated = false;
    public static long pausePosition = -1;
    private int orientation;
    public static boolean isOnBackPressed = false;
    public static boolean isOnNavigationUpPressed = false;

    public DetailFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        clickedPosition = getArguments().getInt("clickedPosition");

        if (getArguments().getLong("pausePosition") >= 0)
            pausePosition = getArguments().getLong("pausePosition");

        mPlayerView = rootView.findViewById(R.id.playerView);
        mNoVideoImageView = rootView.findViewById(R.id.noVideoImageView);

        //handle all the possible fragment view cases needed
        if (!isLandscape())
        {
            mStepDescriptionTextView = rootView.findViewById(R.id.stepDescriptionTextView);
            leftArrowImageView = rootView.findViewById(R.id.leftArrowImageView);
            rightArrowImageView = rootView.findViewById(R.id.rightArrowImageView);
            adjustArrowAsNeeded();
            setArrowImageClickListeners();
        } else if (isLandscape() && !RecipeActivity.isTablet(getContext()))
        {
            setVideoResourceAndImage();
        } else if (isLandscape() && RecipeActivity.isTablet(getContext()))
        {
            mStepDescriptionTextView = rootView.findViewById(R.id.stepDescriptionTextView);
        }
        isDetailFragmentCreated = true;
        orientation = getContext().getResources().getConfiguration().orientation;
        return rootView;
    }


    /**
     * method will set image if there is no video and the text.
     *
     * @return true if there is a video,false otherwise
     */
    private boolean setVideoResourceAndText()
    {
        mStepDescriptionTextView.setText(RecipeActivity.mSteps[clickedPosition].getmRecipeDescription());
        return setVideoResourceAndImage();
    }

    /**
     * method will initialize the ExoPlayer
     *
     * @param mediaUri the Uri to played within the ExoPlayer
     */
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

    /**
     * method will release our player when the view the fragment is destroyed
     */
    private void releasePlayer()
    {
        if (mExoPlayer != null)
        {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
        mExoPlayer = null;
    }

    /**
     * method will add the left and right arrows to the fragment,
     * on the first and last position of recipe step left and right arrows will adjust accordingly.
     */
    private void adjustArrowAsNeeded()
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

    /**
     * set the fragments replacement logic behind the arrows
     */
    private void setArrowImageClickListeners()
    {
        if (leftArrowImageView != null)
            if (clickedPosition != 0)
                leftArrowImageView.setOnClickListener(v ->
                {
                    --clickedPosition;
                    replaceFragment();
                });
        if (rightArrowImageView != null)
            if (RecipeActivity.mPosition != RecipeActivity.mSteps.length - 1)
                rightArrowImageView.setOnClickListener(v ->
                {
                    ++clickedPosition;
                    replaceFragment();
                });
    }

    /**
     * will replace the fragments according to the arrow that has been clicked.
     */
    void replaceFragment()
    {
        detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isReplaced", true);
        bundle.putInt("clickedPosition", clickedPosition);
        detailFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.testframe, detailFragment).commit();
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
            if (setVideoResourceAndText())
                initializePlayer(mMediaUri);
        } else if (isLandscape() && !RecipeActivity.isTablet(getContext()))
        {
            setVideoResourceAndImage();
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


    /**
     * method will check if the orientation is landscape
     *
     * @return true if landscape,false otherwise
     */
    private boolean isLandscape()
    {
        int orientation = getActivity().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            return true;
        return false;
    }

    /**
     * method will set the video and the image.
     *
     * @return true if there is video,false otherwise
     */
    private boolean setVideoResourceAndImage()
    {
        if (RecipeActivity.mSteps[clickedPosition].getmVideoURL().isEmpty())
        {
            if (RecipeActivity.mSteps[clickedPosition].getmThumbnailURL().isEmpty())
            {
                mPlayerView.setVisibility(View.GONE);
                mNoVideoImageView.setVisibility(View.VISIBLE);
                //Picasso.with(getContext()).load(R.drawable.oven).into(mNoVideoImageView);
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
                            //Picasso.with(getContext()).load(R.drawable.oven).into(mNoVideoImageView);
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
