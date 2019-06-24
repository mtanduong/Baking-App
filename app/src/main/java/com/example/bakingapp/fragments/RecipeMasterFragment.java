package com.example.bakingapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.bakingapp.R;
import com.example.bakingapp.adapters.IngredientRecyclerViewAdapter;
import com.example.bakingapp.adapters.StepRecyclerViewAdapter;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeMasterFragment extends Fragment {

    private static final String TAG = "MasterFragment";

    @BindView(R.id.ingredient_recyclerview) RecyclerView ingredientRecyclerView;
    @BindView(R.id.step_recyclerview) RecyclerView stepRecyclerView;
    private List<Step> stepList;
    private List<Ingredient> ingredientList;
    private boolean isTablet;
    private int ingredientViewPos;
    private int stepViewPos;
    private int index;
    private int[] tracker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recipe_master_fragment, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState == null) {

            Bundle extra = getArguments();
            ingredientList = extra.getParcelableArrayList("ingredients");
            stepList = extra.getParcelableArrayList("steps");
            isTablet = extra.getBoolean("tablet", false);
            ingredientViewPos = extra.getInt("ingredientViewPos");
            stepViewPos = extra.getInt("stepViewPos");
            index = 0;
        } else {

            ingredientList = savedInstanceState.getParcelableArrayList("ingredients");
            stepList = savedInstanceState.getParcelableArrayList("steps");
            isTablet = savedInstanceState.getBoolean("tablet", false);
            ingredientViewPos = savedInstanceState.getInt("ingredientViewPos");
            stepViewPos = savedInstanceState.getInt("stepViewPos");
            index = savedInstanceState.getInt("index");
        }

        Log.d(TAG, "stepRecylcer pre stepList size call");
        tracker = new int[stepList.size()];
        Log.d(TAG, "Post stepRecycler post size call, size is: " + stepList.size());

        if(isTablet) {
            tracker[index] = 1;
        }

        Log.d(TAG, "Pre set Ingredient adapter");
        IngredientRecyclerViewAdapter ingredientAdapter = new IngredientRecyclerViewAdapter(getActivity(), ingredientList);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ingredientRecyclerView.setAdapter(ingredientAdapter);
        Log.d(TAG, "Post set Ingredient adapter");

        if(ingredientViewPos != 0) {
            ingredientRecyclerView.getLayoutManager().scrollToPosition(ingredientViewPos);
        }

        Log.d(TAG, "Pre set Step adapter");
        StepRecyclerViewAdapter stepAdapter = new StepRecyclerViewAdapter(getActivity(), stepList, tracker);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        stepRecyclerView.setAdapter(stepAdapter);
        Log.d(TAG, "Post set Step adapter");



        if(stepViewPos != 0) {
            stepRecyclerView.getLayoutManager().scrollToPosition(stepViewPos);
        }

        //TODO - Item Touch Listener
        stepRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                Toast.makeText(getActivity(), "RecipeMasterFragment/addOnItemTouchListener/onInterceptTouchEvent: " + index, Toast.LENGTH_SHORT).show();
                updateView(0);
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                Toast.makeText(getActivity(), "onTouchEvent clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });

        if(isTablet) {
            //updateView(index);
            //listener.setStep(index, stepList);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("ingredients", (ArrayList<? extends Parcelable>) ingredientList);
        outState.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) stepList);
        outState.putBoolean("tablet", isTablet);
        outState.putInt("ingredientViewPos", ((LinearLayoutManager)ingredientRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
        outState.putInt("stepViewPos", ((LinearLayoutManager)stepRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
        outState.putInt("index", index);
    }

    public void updateView(int index)
    {
        Log.d(TAG, "updateView: isTablet " + isTablet);
        this.index = index;
        if(!isTablet)
        {
            Log.d(TAG, "updateView isTablet is false: returning");
            return;
        }
        tracker = new int[stepList.size()];
        try{
            tracker[index]=1;
            ((StepRecyclerViewAdapter)stepRecyclerView.getAdapter()).tracker = tracker;
            stepRecyclerView.getAdapter().notifyDataSetChanged();
            stepRecyclerView.scrollToPosition(index);
        }catch (ArrayIndexOutOfBoundsException E) {

        }
    }
}
