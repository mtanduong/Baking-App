package com.example.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.example.bakingapp.R;
import com.example.bakingapp.adapters.IngredientsAdapter;
import com.example.bakingapp.adapters.RecipeAdapter;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.widgets.WidgetUpdateService;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientsActivity extends AppCompatActivity {

    private Ingredient[] ingredientList;
    private RecyclerView recyclerView;
    LinearLayoutManager mLinearLayoutManager = null;
    public static int ingredientsLengthForTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        if (savedInstanceState == null) {

            getIntentExtras();
            initializeScreen();
        } else {

            getSavedInstanceData(savedInstanceState);
            initializeScreen();
        }

        startWidgetService();

        ingredientsLengthForTest = ingredientList.length;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelableArray(RecipeAdapter.INGREDIENTS, ingredientList);
    }

    //Call WidgetUpdateService class to update widget to last recipe user observed
    void startWidgetService() {

        Intent intent = new Intent(this, WidgetUpdateService.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArray(MainActivity.INGREDIENTS, ingredientList);
        intent.putExtra(MainActivity.BUNDLE, bundle);
        intent.setAction(WidgetUpdateService.WIDGET_UPDATE_ACTION);
        startService(intent);
    }

    //Initialize IngredientActivity screen with Ingredient Recycler View
    private void initializeScreen() {

        recyclerView = findViewById(R.id.recyclerview_ingredients);
        mLinearLayoutManager = new LinearLayoutManager(IngredientsActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredientList);
        recyclerView.setAdapter(ingredientsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(IngredientsActivity.this, DividerItemDecoration.VERTICAL));
    }

    private void getSavedInstanceData(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {

            Parcelable[] ingredientParcel = savedInstanceState.getParcelableArray(RecipeAdapter.INGREDIENTS);
            if (ingredientParcel != null) {

                ingredientList = new Ingredient[ingredientParcel.length];
                for (int i = 0; i < ingredientParcel.length; i++) {
                    ingredientList[i] = (Ingredient) ingredientParcel[i];
                }
            }
        }
    }

    private void getIntentExtras() {

        if (getIntent().getExtras() != null) {

            Bundle bundle = getIntent().getExtras().getBundle(RecipeAdapter.INGREDIENTS_BUNDLE);
            if (bundle != null) {

                Parcelable[] ingredientParcel = bundle.getParcelableArray(RecipeAdapter.INGREDIENTS);
                if (ingredientParcel != null) {

                    ingredientList = new Ingredient[ingredientParcel.length];
                    for (int i = 0; i < ingredientParcel.length; i++) {

                        ingredientList[i] = (Ingredient) ingredientParcel[i];
                    }
                }
            }
        }
    }
}
