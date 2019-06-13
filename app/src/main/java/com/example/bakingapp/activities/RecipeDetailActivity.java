package com.example.bakingapp.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.bakingapp.R;
import com.example.bakingapp.fragments.RecipeDetailFragment;
import com.example.bakingapp.fragments.RecipeMasterFragment;
import com.example.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    FrameLayout detailFragment;
    boolean isTablet;
    private List<Step> steps;

    String name;

    RecipeDetailFragment recipeDetailFragment;
    RecipeMasterFragment recipeMasterFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        detailFragment = (FrameLayout) findViewById(R.id.detail_fragment_holder);
        isTablet = true;
        Bundle extras = getIntent().getBundleExtra("bundle");
        name = extras.getString("recipe_name");
        getSupportActionBar().setTitle(name);
        steps = extras.getParcelableArrayList("steps");
        extras.putBoolean("tablet", (detailFragment != null));

        if (savedInstanceState == null) {
            //recipeMasterFragment = new StepFragment();
            //recipeMasterFragment.setFragmentListener(this);
            recipeMasterFragment.setArguments(extras);
            getFragmentManager().beginTransaction().add(R.id.master_fragment_holder, recipeMasterFragment).commit();
            //checking if screen size greater than 600dp
            if (detailFragment == null) {
                isTablet = false;
            } else {
                //this.setStep(0, steps);
            }
        } else {
            recipeMasterFragment = (RecipeMasterFragment) getFragmentManager().getFragment(savedInstanceState,"main");
            //recipeMasterFragment.setFragmentListener(this);


            if (!recipeMasterFragment.isAdded())
                getFragmentManager().beginTransaction().add(R.id.master_fragment_holder, recipeMasterFragment).commit();

            if(recipeDetailFragment !=null)
            {
                recipeDetailFragment =  (RecipeDetailFragment) getFragmentManager().getFragment(savedInstanceState,"detail");
                getFragmentManager().beginTransaction().replace(R.id.detail_fragment_holder, recipeDetailFragment).commit();
            }
        }




    }

    //TODO widget methods
    /*
    @Override
    public void setStep(int index, ArrayList<Step> steps) {
        if (!isTablet) {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra("steps", steps);
            intent.putExtra("current", index);
            intent.putExtra("name", name);
            startActivity(intent);
        } else {
            recipeDetailFragment = new StepDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("steps", steps);
            recipeDetailFragment.setFragmentListener(this);
            bundle.putInt("current", index);
            bundle.putBoolean("tablet", true);
            recipeDetailFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fragmentTwo, recipeDetailFragment).commit();
        }
    }

    @Override
    public void setCurrent(int index) {
        if (isTablet) {
            recipeMasterFragment.updateView(index);
        }
    }
    */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentManager().putFragment(outState, "main", recipeMasterFragment);

        if (isTablet && detailFragment!=null)
        {
            try{
                getFragmentManager().putFragment(outState, "detail", recipeDetailFragment);
            }catch (NullPointerException e) {}
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (detailFragment == null) {
            isTablet = false;
        }
    }
}
