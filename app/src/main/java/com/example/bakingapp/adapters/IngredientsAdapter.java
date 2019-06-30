package com.example.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Ingredient;

import androidx.recyclerview.widget.RecyclerView;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>
{

    private static final String TAG = RecipeAdapter.class.getSimpleName();
    private final Ingredient[] ingredientList;

    public IngredientsAdapter(Ingredient[] mIngredients)
    {
        this.ingredientList = mIngredients;
    }


    @Override
    public IngredientsAdapter.IngredientViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.ingredient_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new IngredientsAdapter.IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.IngredientViewHolder holder, int position) {

        holder.mQuantity = ingredientList[position].getmQuantity();
        holder.mIngredient = ingredientList[position].getmIngredient();
        holder.mMeasure = ingredientList[position].getmMeasure();
        holder.ingredientTextView.setText(holder.mIngredient);

        String quantityAndMeasure = holder.mQuantity + " " + holder.mMeasure;
        holder.quantityAndMeasureTextView.setText(quantityAndMeasure);
    }

    @Override
    public int getItemCount() {

        if (ingredientList == null)
            return 0;
        return ingredientList.length;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        private double mQuantity;
        private String mMeasure;
        private String mIngredient;
        private final TextView ingredientTextView;
        private final TextView quantityAndMeasureTextView;

        public IngredientViewHolder(View itemView) {

            super(itemView);

            ingredientTextView = itemView.findViewById(R.id.ingredient);
            quantityAndMeasureTextView = itemView.findViewById(R.id.quantity);
        }
    }
}
