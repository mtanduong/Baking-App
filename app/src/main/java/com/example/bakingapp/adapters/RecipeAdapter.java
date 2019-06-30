package com.example.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.activities.RecipeActivity;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.models.Steps;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>
{

    private static final String TAG = RecipeAdapter.class.getSimpleName();
    private final Recipe[] recipeList;

    public static final String ID = "mId";
    public static final String NAME = "mName";
    public static final String INGREDIENTS = "mIngredients";
    public static final String INGREDIENTS_BUNDLE = "ingredientsBundle";
    public static final String STEPS = "mSteps";
    public static final String STEPS_BUNDLE = "stepsBundle";
    public static final String SERVINGS = "mServings";

    public RecipeAdapter(Recipe[] recipes)
    {
        this.recipeList = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new RecipeAdapter.RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        holder.mId = recipeList[position].getmId();
        holder.mName = recipeList[position].getmName();
        holder.mIngredients = recipeList[position].getmIngredients();
        holder.mSteps = recipeList[position].getmSteps();
        holder.mServings = recipeList[position].getmServings();
        String imageUrl = recipeList[position].getmImage();
        holder.recipeName.setText(String.valueOf(holder.mName));
        holder.stepCountText.setText(String.valueOf(holder.mSteps.size()));
        holder.ingredientCountText.setText(String.valueOf(holder.mIngredients.size()));

        if (imageUrl != null && imageUrl.isEmpty()) {

            switch (holder.mId) {
                case 1:
                    Picasso.get().load(R.drawable.nutella_pie).into(holder.recipeImage);
                    break;
                case 2:
                    Picasso.get().load(R.drawable.brownies).into(holder.recipeImage);
                    break;
                case 3:
                    Picasso.get().load(R.drawable.yellow_cake).into(holder.recipeImage);
                    break;
                case 4:
                    Picasso.get().load(R.drawable.cheese_cake).into(holder.recipeImage);
                    break;
            }
        } else {
            Picasso.get().load(imageUrl).into(holder.recipeImage);
        }
    }

    @Override
    public int getItemCount() {

        if (recipeList == null)
            return 0;
        return recipeList.length;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        private int mId;
        private String mName;
        private List<Ingredient> mIngredients;
        private List<Steps> mSteps;
        private int mServings;

        @BindView(R.id.recipe_item) ImageView recipeImage;
        @BindView(R.id.recipe_name) TextView recipeName;
        @BindView(R.id.recipe_step_count) TextView stepCountText;
        @BindView(R.id.recipe_ingredient_count) TextView ingredientCountText;

        public RecipeViewHolder(View itemView) {

            super(itemView);

            ButterKnife.bind(this, itemView);

            recipeImage.setOnClickListener(v ->
            {
                Intent intent = new Intent(v.getContext(), RecipeActivity.class);
                intent.putExtra(ID, mId);
                intent.putExtra(NAME, mName);
                intent.putExtra(INGREDIENTS_BUNDLE, putIngredients());
                intent.putExtra(STEPS_BUNDLE, putSteps());
                intent.putExtra(SERVINGS, mServings);

                v.getContext().startActivity(intent);
            });
        }

        Bundle putIngredients() {

            Ingredient[] ingredients = mIngredients.toArray(new Ingredient[mIngredients.size()]);
            Bundle ingredientsBundle = new Bundle();
            ingredientsBundle.putParcelableArray(INGREDIENTS, ingredients);
            return ingredientsBundle;
        }

        Bundle putSteps() {

            Steps[] steps = mSteps.toArray(new Steps[mSteps.size()]);
            Bundle stepsBundle = new Bundle();
            stepsBundle.putParcelableArray(STEPS, steps);
            return stepsBundle;
        }
    }

}