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

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.adapters.RecipeRecyclerViewAdapter;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.utils.BakingApi;
import com.example.bakingapp.utils.BakingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.main_recyclerview) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.reload_button) Button reloadButton;
    @BindView(R.id.no_network_iv) ImageView noConnection;
    @BindView(R.id.no_server_iv) ImageView noServer;
    @BindView(R.id.error_text) TextView errorText;

    private BakingService retrofitService = BakingApi.createService();
    private List<Recipe> recipeList;
    private int index;

    //private CountingIdlingResource mIdlingResource = new CountingIdlingResource("Loading_Data");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        noConnection.setVisibility(View.GONE);
        noServer.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
        reloadButton.setVisibility(View.GONE);

        reloadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Reload Button: Clicked");
                //TODO call API to retrieve recipe list
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        if(savedInstanceState == null) {
            //mIdlingResource.increment();
            //TODO call API to retrieve recipe list
        }

        Call<List<Recipe>> callRecipe = retrofitService.getRecipes();

        callApi(callRecipe);
        /*
        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(MainActivity.this, recyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent details=new Intent(HomeActivity.this,RecipeDetailsActivity.class);

                Bundle bundle=new Bundle();
                bundle.putParcelableArrayList("steps",
                        (ArrayList<? extends Parcelable>) recipes.get(position).getSteps());
                bundle.putParcelableArrayList("ingredients",
                        (ArrayList<? extends Parcelable>) recipes.get(position).getIngredients());
                bundle.putString("recipe_name",recipes.get(position).getName());
                details.putExtra("bundle",bundle);

                startActivity(details);

            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        */

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList("recipes",(ArrayList<? extends Parcelable>) recipeList);
        if(recyclerView.getTag().equals("tablet"))
        {
            outState.putInt("index",((GridLayoutManager)recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition());
        }
        else
        {
            outState.putInt("index",((LinearLayoutManager)recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition());
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        recipeList = savedInstanceState.getParcelableArrayList("recipes");
        index = savedInstanceState.getInt("index");
        startRecyclerView(recipeList);
        progressBar.setVisibility(GONE);
    }

    private void callApi(Call<List<Recipe>> call) {

        Log.d(TAG, "called callApi");
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, "callApi server error: " + response.code());
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(R.string.server_error);
                    progressBar.setVisibility(View.GONE);
                    noServer.setVisibility(View.VISIBLE);
                    return;
                }
                Log.d(TAG, "pre parseRecipes response.body().size: " + response.body().size());

                progressBar.setVisibility(View.VISIBLE);
                errorText.setVisibility(View.GONE);
                noConnection.setVisibility(View.GONE);
                noServer.setVisibility(View.GONE);
                reloadButton.setVisibility(View.GONE);

                startRecyclerView(response.body());

                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "post parseRecipes");

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "callApi oncall error: " + t.getMessage());

                errorText.setText("");
                errorText.setVisibility(View.VISIBLE);
                reloadButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                if(t instanceof IOException) {
                    errorText.setText(R.string.network_error);
                    noConnection.setVisibility(View.VISIBLE);
                } else {
                    errorText.setText(R.string.parse_error);
                }
            }
        });
    }

    private void startRecyclerView(List<Recipe> recipeList) {
        if(recyclerView.getTag().equals("tablet"))
        {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        else
        {
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        recyclerView.setAdapter(new RecipeRecyclerViewAdapter(this, recipeList));
        recyclerView.getLayoutManager().scrollToPosition(index);
    }

    /*
    private void reload(View view) {
        progressBar.setVisibility(View.VISIBLE);
        reloadButton.setVisibility(View.GONE);
        //Call Api
        BakingApi.createService();
    }
    */
}
