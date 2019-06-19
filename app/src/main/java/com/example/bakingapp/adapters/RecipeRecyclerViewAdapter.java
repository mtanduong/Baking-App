package com.example.bakingapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> {

    private static final String TAG = "RecipeAdapter";
    private Context context;
    private List<Recipe> recipeData;


    public RecipeRecyclerViewAdapter(Context context, List<Recipe> recipeData) {
        this.context = context;
        this.recipeData = recipeData;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

//        public ImageView recipeImage;
//        public TextView recipeName;
        @BindView(R.id.recipe_list_item_image) ImageView recipeImage;
        @BindView(R.id.recipe_list_item_name) TextView recipeText;
        @BindView(R.id.recipe_servings_value) TextView servingsText;
        @BindView(R.id.recipe_steps_value) TextView stepsText;
        @BindView(R.id.recipe_parent_layout)
        RelativeLayout parentLayout;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

//            recipeImage = itemView.findViewById(R.id.step_list_image);
//            recipeText = itemView.findViewById(R.id.step_list_name);
//            parentLayout = itemView.findViewById(R.id.step_parent_layout);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

        Recipe currentRecipe = recipeData.get(position);

        holder.recipeText.setText(currentRecipe.getName());
        holder.stepsText.setText(String.valueOf(currentRecipe.getSteps().size()));
        holder.servingsText.setText(String.valueOf(currentRecipe.getServings()));

        Log.d(TAG, "onBindViewHolder/getImage(): " + recipeData.get(position).getImage());

        if(recipeData.get(position).getImage().isEmpty()) {
            Picasso.get()
                    .load(R.drawable.default_recipe_image2)
                    .into(holder.recipeImage);
        } else{
            Picasso.get()
//                .load(currentRecipe.getImage())
                    .load(recipeData.get(position).getImage())
                    .placeholder(R.drawable.default_recipe_image2)
                    .error(R.drawable.default_recipe_image2)
                    .into(holder.recipeImage);
        }

    }

    @Override
    public int getItemCount() {
        return recipeData.size();
    }
}
