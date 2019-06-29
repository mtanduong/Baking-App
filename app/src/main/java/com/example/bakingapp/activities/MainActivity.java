package com.example.bakingapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bakingapp.IdlingResource.MyIdlingResource;
import com.example.bakingapp.R;
import com.example.bakingapp.adapters.RecipeAdapter;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.utils.BakingApi;
import com.example.bakingapp.utils.BakingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = getClass().getSimpleName();
    private ArrayList<Recipe> mRecipes;
    private Recipe[] mRecipesArr;
    private RecyclerView mRecyclerView;
    private boolean tabletFlag = false;
    private static final String KEY_RECIPES = "recipes";
    public static final String BUNDLE = "bundle";
    public static final String INGREDIENTS = "ingredients";
    LinearLayoutManager mLinearLayoutManager = null;
    GridLayoutManager mGridlayoutManager = null;
    private BakingService retrofitService = BakingApi.createService();

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.error_text) TextView errorText;
    @BindView(R.id.no_network) ImageView noNetwork;
    @BindView(R.id.no_server) ImageView noServer;
    @BindView(R.id.retry_button) Button retryButton;

    @Nullable
    private MyIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link MyIdlingResource}.
     */
    /*
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource()
    {
        if (mIdlingResource == null)
        {
            mIdlingResource = new MyIdlingResource();
        }
        return mIdlingResource;
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
        noNetwork.setVisibility(View.GONE);
        noServer.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);

        //getIdlingResource();

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });

        if (savedInstanceState == null)
        {
            /*
            if (mIdlingResource != null)
            {
                //mIdlingResource.setIdleState(false);
            } */
            fetchData();
        } else
        {
            Parcelable[] recipes = savedInstanceState.getParcelableArray(KEY_RECIPES);
            if (recipes != null)
            {
                mRecipesArr = new Recipe[recipes.length];
                for (int i = 0; i < recipes.length; i++)
                {
                    mRecipesArr[i] = (Recipe) recipes[i];
                }
            }
            initializeScreen();
        }
    }

    /*
    @Override
    protected void onStart()
    {
        super.onStart();

        //this is our test thread,it will wait 5 seconds after the onCreate finishes to
        //ensure that the RecyclerView is fully capable of click handling.
        if (mIdlingResource != null)
        {
            Handler handler = new Handler();
            Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    long futureTime = System.currentTimeMillis() + 5000;
                    while (System.currentTimeMillis() < futureTime)
                    {
                        synchronized (this)
                        {
                            try
                            {
                                wait(futureTime - System.currentTimeMillis());
                                if (mIdlingResource != null)
                                {
                                    //mIdlingResource.setIdleState(true);
                                }

                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    handler.sendEmptyMessage(0);
                }
            };
            Thread testThread = new Thread(runnable);
            testThread.start();
        }
    }
*/
    /**
     * method will trigger a Json response via a Retrofit client
     */
    void fetchData()
    {

        progressBar.setVisibility(View.VISIBLE);
        Call<List<Recipe>> call = retrofitService.getRecipes();

        call.enqueue(new Callback<List<Recipe>>()
        {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response)
            {
                mRecipes = (ArrayList<Recipe>) response.body();

                if (!response.isSuccessful())
                {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(R.string.server_error);
                    progressBar.setVisibility(View.GONE);
                    noServer.setVisibility(View.VISIBLE);
                    return;
                }

                if (mRecipes != null)
                {
                    mRecipesArr = mRecipes.toArray(new Recipe[mRecipes.size()]);
                    mRecipes.clear();
                    initializeScreen();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t)
            {
                Log.e(MainActivity.this.getClass().getSimpleName(), "" + t.getMessage());

                errorText.setVisibility(View.VISIBLE);
                errorText.setText(R.string.network_error);
                progressBar.setVisibility(View.GONE);
                noNetwork.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);

                if (t instanceof IOException)
                {
                    errorText.setText(R.string.network_error);
                    noNetwork.setVisibility(View.VISIBLE);
                } else
                {
                    errorText.setText(R.string.parse_error);
                }
            }
        });

    }

    /**
     * method will initialize the screen of the MainActivity
     */
    private void initializeScreen()
    {
        //mobile case
        if (!isTablet(this))
        {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                mLinearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                //mLinearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
            else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                mGridlayoutManager = new GridLayoutManager(MainActivity.this, 2);
        }
        //tablet case
        else
        {
            tabletFlag = true;
            mGridlayoutManager = new GridLayoutManager(MainActivity.this, 2);
        }

        mRecyclerView = findViewById(R.id.recyclerview_recipes);

        if (!tabletFlag)
        {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
            else
                mRecyclerView.setLayoutManager(mGridlayoutManager);
        } else
        {
            mRecyclerView.setLayoutManager(mGridlayoutManager);
        }


        mRecyclerView.setHasFixedSize(true);
        RecipeAdapter recipeAdapter = new RecipeAdapter(mRecipesArr);
        mRecyclerView.setAdapter(recipeAdapter);
    }

    /**
     * method will check if the device that the app runs on is a tablet or a mobile phone
     *
     * @param context app context
     * @return true if it is a tablet,false otherwise
     */
    private boolean isTablet(Context context)
    {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putParcelableArray(KEY_RECIPES, mRecipesArr);
    }
}
