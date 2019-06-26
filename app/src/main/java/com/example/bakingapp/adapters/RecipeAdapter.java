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

    //class name,mainly for debugging
    private static final String TAG = RecipeAdapter.class.getSimpleName();
    //parsed JSON
    private final Recipe[] mRecipes;

    public static final String ID = "mId";
    public static final String NAME = "mName";
    public static final String INGREDIENTS = "mIngredients";
    public static final String INGREDIENTS_BUNDLE = "ingredientsBundle";
    public static final String STEPS = "mSteps";
    public static final String STEPS_BUNDLE = "stepsBundle";
    public static final String SERVINGS = "mServings";


    public RecipeAdapter(Recipe[] recipes)
    {
        this.mRecipes = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recepie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new RecipeAdapter.RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position)
    {
        holder.mId = mRecipes[position].getmId();
        holder.mName = mRecipes[position].getmName();
        holder.mIngredients = mRecipes[position].getmIngredients();
        holder.mSteps = mRecipes[position].getmSteps();
        holder.mServings = mRecipes[position].getmServings();
        String imageUrl = mRecipes[position].getmImage();
        Context context = holder.itemView.getContext();
        holder.recipeName.setText(String.valueOf(holder.mName));
        holder.stepCountText.setText(String.valueOf(holder.mSteps.size()));
        holder.ingredientCountText.setText(String.valueOf(holder.mIngredients.size()));

        //The Json that we query has 4 recipes,we will use a different photo for each recipe
        if (imageUrl != null && imageUrl.isEmpty())
        {
            switch (holder.mId)
            {
                //Nutella Pie
                case 1:
                    //Picasso.with(context).load(R.drawable.nutella_pie).into(holder.imageView);
                    Picasso.get().load(R.drawable.nutella_pie).into(holder.recipeImage);
                    break;
                //Brownies
                case 2:
                    //Picasso.with(context).load(R.drawable.brownies).into(holder.imageView);
                    Picasso.get().load(R.drawable.brownies).into(holder.recipeImage);
                    break;
                //Yellow Cake
                case 3:
                    //Picasso.with(context).load(R.drawable.yellow_cake).into(holder.imageView);
                    Picasso.get().load(R.drawable.yellow_cake).into(holder.recipeImage);
                    break;
                //Cheesecake
                case 4:
                    //Picasso.with(context).load(R.drawable.cheese_cake).into(holder.imageView);
                    Picasso.get().load(R.drawable.cheese_cake).into(holder.recipeImage);
                    break;
            }
        } else
        {
            //Picasso.with(context).load(imageUrl).into(holder.imageView);
            Picasso.get().load(imageUrl).into(holder.recipeImage);
        }
    }

    @Override
    public int getItemCount()
    {
        if (mRecipes == null)
            return 0;
        return mRecipes.length;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder
    {
        //recipe id
        private int mId;

        //recipe name
        private String mName;

        //recipe ingredients,will map an array of ingredients
        private List<Ingredient> mIngredients;

        //recipe steps,will map an array of steps to make the recipe
        private List<Steps> mSteps;

        //number of servings
        private int mServings;

        //image url
        private String mImage;

        //we will use a custom image if the image url is empty
        private ImageView imageView;

        @BindView(R.id.recipe_item) ImageView recipeImage;
        @BindView(R.id.recipe_name) TextView recipeName;
        @BindView(R.id.recipe_step_count) TextView stepCountText;
        @BindView(R.id.recipe_ingredient_count) TextView ingredientCountText;

        public RecipeViewHolder(View itemView)
        {
            super(itemView);

            //imageView = itemView.findViewById(R.id.recipe_item);
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

        Bundle putIngredients()
        {
            Ingredient[] ingredients = mIngredients.toArray(new Ingredient[mIngredients.size()]);
            Bundle ingredientsBundle = new Bundle();
            ingredientsBundle.putParcelableArray(INGREDIENTS, ingredients);
            return ingredientsBundle;
        }

        Bundle putSteps()
        {
            Steps[] steps = mSteps.toArray(new Steps[mSteps.size()]);
            Bundle stepsBundle = new Bundle();
            stepsBundle.putParcelableArray(STEPS, steps);
            return stepsBundle;
        }
    }

}