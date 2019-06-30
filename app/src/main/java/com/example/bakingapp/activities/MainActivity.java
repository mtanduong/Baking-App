package com.example.bakingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private RecyclerView recyclerView;
    private Recipe[] recipeList;
    private ArrayList<Recipe> mRecipes;
    public static final String BUNDLE = "bundle";
    public static final String INGREDIENTS = "ingredients";
    private static final String KEY_RECIPES = "recipes";
    private boolean isTablet = false;
    private BakingService retrofitService = BakingApi.createService();

    LinearLayoutManager mLinearLayoutManager = null;
    GridLayoutManager mGridlayoutManager = null;

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.error_text) TextView errorText;
    @BindView(R.id.no_network) ImageView noNetwork;
    @BindView(R.id.no_server) ImageView noServer;
    @BindView(R.id.retry_button) Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
        noNetwork.setVisibility(View.GONE);
        noServer.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);

        //Set Retry Button to attempt to call API service again
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi();
            }
        });

        //If there is no previous saved state call API service otherwise retrieve previous savedInstanceState
        if (savedInstanceState == null) {
            callApi();
        } else {

            Parcelable[] recipes = savedInstanceState.getParcelableArray(KEY_RECIPES);
            if (recipes != null) {

                recipeList = new Recipe[recipes.length];
                for (int i = 0; i < recipes.length; i++) {
                    recipeList[i] = (Recipe) recipes[i];
                }
            }
            initializeScreen();
        }
    }

    //API service call
    void callApi() {

        progressBar.setVisibility(View.VISIBLE);
        Call<List<Recipe>> call = retrofitService.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {

                mRecipes = (ArrayList<Recipe>) response.body();

                if (!response.isSuccessful()) {

                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(R.string.server_error);
                    progressBar.setVisibility(View.GONE);
                    noServer.setVisibility(View.VISIBLE);
                    return;
                }

                if (mRecipes != null) {

                    recipeList = mRecipes.toArray(new Recipe[mRecipes.size()]);
                    mRecipes.clear();
                    initializeScreen();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {

                Log.e(MainActivity.this.getClass().getSimpleName(), "" + t.getMessage());

                errorText.setVisibility(View.VISIBLE);
                errorText.setText(R.string.network_error);
                progressBar.setVisibility(View.GONE);
                noNetwork.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);

                if (t instanceof IOException) {

                    errorText.setText(R.string.network_error);
                    noNetwork.setVisibility(View.VISIBLE);
                } else {

                    errorText.setText(R.string.parse_error);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putParcelableArray(KEY_RECIPES, recipeList);
    }

    //Initialize MainActivity screen with Recipe Recycler View
    private void initializeScreen() {

        //Mobile mode
        if (!isTablet(this)) {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                mLinearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                //mLinearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
            else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                mGridlayoutManager = new GridLayoutManager(MainActivity.this, 2);
        } else {

            //Tablet mode
            isTablet = true;
            mGridlayoutManager = new GridLayoutManager(MainActivity.this, 2);
        }

        recyclerView = findViewById(R.id.recyclerview_recipes);

        if (!isTablet) {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                recyclerView.setLayoutManager(mLinearLayoutManager);
            else
                recyclerView.setLayoutManager(mGridlayoutManager);
        } else {

            recyclerView.setLayoutManager(mGridlayoutManager);
        }

        recyclerView.setHasFixedSize(true);
        RecipeAdapter recipeAdapter = new RecipeAdapter(recipeList);
        recyclerView.setAdapter(recipeAdapter);
    }

    //Checks if device is on a mobile (false) or tablet (true)
    private boolean isTablet(Context context) {

        boolean isTablet = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean isMobile = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (isTablet || isMobile);
    }
}
