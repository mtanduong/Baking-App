package com.example.bakingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.example.bakingapp.R;
import com.example.bakingapp.adapters.RecipeAdapter;
import com.example.bakingapp.fragments.DetailFragment;
import com.example.bakingapp.fragments.MasterFragment;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Steps;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RecipeActivity extends AppCompatActivity
{
    private int mId;
    private String mName;
    private Ingredient[] mIngredients;
    public static Steps[] mSteps;
    public static ArrayList<String> shortDescription;
    private int mServings;
    private CardView mCardView;
    public static int positionRecycler = -1;
    public static int mPosition;
    private MasterFragment masterFragment;
    private DetailFragment detailFragment;
    private int clickedPosition;
    private long pausePosition;
    private boolean isDetailFragmentCreated;
    public static boolean backPressed = false;
    public static int stepLengthForTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
        {
            getIntentExtras();
        } else
        {
            getSavedInstanceData(savedInstanceState);
        }
        putShortDescription();

        if (savedInstanceState != null)
        {
            if (masterFragment == null)
            {
                masterFragment = savedInstanceState.getParcelable("fragment");
            }
            if (savedInstanceState.getInt("positionRecycler") >= 0)
            {
                positionRecycler = savedInstanceState.getInt("positionRecycler");
            }

            if (savedInstanceState.getBoolean("isDetailFragmentCreated"))
            {
                isDetailFragmentCreated = savedInstanceState.getBoolean("isDetailFragmentCreated");
            }

            if (savedInstanceState.getLong("pausePosition") >= 0)
            {
                pausePosition = savedInstanceState.getLong("pausePosition");
            }

            if (savedInstanceState.getInt("clickedPosition") >= 0)
            {
                clickedPosition = savedInstanceState.getInt("clickedPosition");
            }
        }

        setContentView(R.layout.activity_recipe);
        ingredientsOnClickListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putInt(RecipeAdapter.ID, mId);
        outState.putString(RecipeAdapter.NAME, mName);
        outState.putParcelableArray(RecipeAdapter.INGREDIENTS, mIngredients);
        outState.putParcelableArray(RecipeAdapter.STEPS, mSteps);
        outState.putInt(RecipeAdapter.SERVINGS, mServings);
        outState.putInt("positionRecycler", MasterFragment.mLayoutManager.findFirstCompletelyVisibleItemPosition());
        outState.putBoolean("backPressed", backPressed);

        outState.putBoolean("isDetailFragmentCreated", DetailFragment.isDetailFragmentCreated);
        outState.putLong("pausePosition", DetailFragment.pausePosition);
        outState.putInt("clickedPosition", DetailFragment.clickedPosition);

    }

    @Override
    protected void onStart()
    {
        if (masterFragment == null)
        {
            masterFragment = new MasterFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArray(RecipeAdapter.STEPS, mSteps);
            masterFragment.setArguments(bundle);
        }

        if (!isTablet(this) && !DetailFragment.isDetailFragmentCreated)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.testframe, masterFragment).commit();

        } else if (!isTablet(this) && DetailFragment.isDetailFragmentCreated)
        {
            detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("clickedPosition", DetailFragment.clickedPosition);
            bundle.putLong("pausePosition", DetailFragment.pausePosition);
            detailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.testframe, detailFragment).commit();
            findViewById(R.id.recipe_card).setVisibility(View.GONE);
        } else
        {
            getSupportFragmentManager().beginTransaction().add(R.id.testframe, masterFragment).commit();
            detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("clickedPosition", 0);
            detailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.tabFrameLayout600, detailFragment).commit();
        }

        super.onStart();
    }

    @Override
    protected void onPause()
    {
        getSupportFragmentManager().beginTransaction().remove(masterFragment).commit();
        super.onPause();
    }

    public static boolean isTablet(Context context)
    {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    @Override
    public void onBackPressed()
    {

        backPressed = true;
        DetailFragment.isOnBackPressed = true;
        positionRecycler = 0;
        DetailFragment.isDetailFragmentCreated = false;
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp()
    {

        backPressed = true;
        DetailFragment.isOnNavigationUpPressed = true;
        positionRecycler = 0;
        DetailFragment.isDetailFragmentCreated = false;
        return super.onSupportNavigateUp();
    }

    void getSavedInstanceData(@Nullable Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            mId = savedInstanceState.getInt(RecipeAdapter.ID);
            mName = savedInstanceState.getString(RecipeAdapter.NAME);

            Parcelable[] ingredientsParcel = savedInstanceState.getParcelableArray(RecipeAdapter.INGREDIENTS);
            if (ingredientsParcel != null)
            {
                mIngredients = new Ingredient[ingredientsParcel.length];
                for (int i = 0; i < ingredientsParcel.length; i++)
                {
                    mIngredients[i] = (Ingredient) ingredientsParcel[i];
                }
            }

            Parcelable[] stepsParcel = savedInstanceState.getParcelableArray(RecipeAdapter.STEPS);
            if (stepsParcel != null)
            {
                mSteps = new Steps[stepsParcel.length];
                stepLengthForTest = mSteps.length;
                for (int i = 0; i < stepsParcel.length; i++)
                {
                    mSteps[i] = (Steps) stepsParcel[i];
                }
            }

            mServings = savedInstanceState.getInt(RecipeAdapter.SERVINGS);


            if (savedInstanceState.containsKey("backPressed"))
            {
                backPressed = savedInstanceState.getBoolean("backPressed");
            }

        }
    }

    void getIntentExtras()
    {
        if (getIntent().getExtras() != null)
        {
            mId = getIntent().getExtras().getInt(RecipeAdapter.ID);
            mName = getIntent().getExtras().getString(RecipeAdapter.NAME);

            Bundle ingredientsBundle = getIntent().getExtras().getBundle(RecipeAdapter.INGREDIENTS_BUNDLE);
            if (ingredientsBundle != null)
            {
                Parcelable[] ingredientsParcelableArray = ingredientsBundle.getParcelableArray(RecipeAdapter.INGREDIENTS);
                if (ingredientsParcelableArray != null)
                {
                    mIngredients = new Ingredient[ingredientsParcelableArray.length];
                    for (int i = 0; i < ingredientsParcelableArray.length; i++)
                    {
                        mIngredients[i] = (Ingredient) ingredientsParcelableArray[i];
                    }
                }
            }

            Bundle stepsBundle = getIntent().getExtras().getBundle(RecipeAdapter.STEPS_BUNDLE);
            if (stepsBundle != null)
            {
                Parcelable[] stepsParcelableArray = stepsBundle.getParcelableArray(RecipeAdapter.STEPS);
                if (stepsParcelableArray != null)
                {
                    mSteps = new Steps[stepsParcelableArray.length];
                    stepLengthForTest = mSteps.length;
                    for (int i = 0; i < stepsParcelableArray.length; i++)
                    {
                        mSteps[i] = (Steps) stepsParcelableArray[i];
                    }
                }
            }

            mServings = getIntent().getExtras().getInt(RecipeAdapter.SERVINGS);
        }
    }

    void ingredientsOnClickListener()
    {
        mCardView = findViewById(R.id.recipe_card);
        mCardView.setOnClickListener(v ->
        {
            Intent intent = new Intent(this, IngredientsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArray(RecipeAdapter.INGREDIENTS, mIngredients);
            intent.putExtra(RecipeAdapter.INGREDIENTS_BUNDLE, bundle);
            startActivity(intent);
        });
    }

    private void putShortDescription()
    {
        shortDescription = new ArrayList<>();
        for (int i = 0; i < mSteps.length; i++)
        {
            shortDescription.add(mSteps[i].getmShortDescription());
        }
    }

}