package com.example.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Ingredient;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.IngredientViewHolder> {

    private static final String TAG = "IngredientAdapter";
    private Context context;
    private List<Ingredient> ingredientData;

    public IngredientRecyclerViewAdapter(Context context, List<Ingredient> ingredientData) {
        this.context = context;
        this.ingredientData =ingredientData;
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {

//        public TextView measureText;
//        public TextView quantityText;
//        public TextView ingredientText;
//        public RelativeLayout parentLayout;
        @BindView(R.id.ingredient_list_item_measure_text) TextView measureText;
        @BindView(R.id.ingredient_list_item_quantity_text) TextView quantityText;
        @BindView(R.id.ingredient_list_item_text) TextView ingredientText;
        @BindView(R.id.ingredient_parent_layout) RelativeLayout parentLayout;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

//            measureText = itemView.findViewById(R.id.ingredient_list_item_measure_text);
//            quantityText = itemView.findViewById(R.id.ingredient_list_item_quantity_text);
//            ingredientText =itemView.findViewById(R.id.ingredient_list_item_text);
//            parentLayout = itemView.findViewById(R.id.ingredient_parent_layout);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.ingredient_list_item, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {

        Ingredient currentIngredient = ingredientData.get(position);

        holder.measureText.setText(currentIngredient.getMeasure());
        holder.quantityText.setText(String.valueOf(currentIngredient.getQuantity()));
        holder.ingredientText.setText(currentIngredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        return ingredientData.size();
    }
}
