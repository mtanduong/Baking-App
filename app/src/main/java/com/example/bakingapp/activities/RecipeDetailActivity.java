package com.example.bakingapp.activities;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

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

            recipeMasterFragment = new RecipeMasterFragment();
            //Related to Widget
            //recipeMasterFragment.setFragmentListener(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            recipeMasterFragment.setArguments(extras);
            fragmentManager.beginTransaction().add(R.id.master_fragment_holder, recipeMasterFragment).commit();
            //recipeMasterFragment.setArguments(extras);
            //recipeMasterFragment.getFragmentManager().beginTransaction().add(R.id.master_fragment_holder, recipeMasterFragment).commit();

            //checking if screen size greater than 600dp
            if (detailFragment == null) {
                isTablet = false;
            } else {
                //Related to Widget
                //this.setStep(0, steps);
            }
        } else {
            recipeMasterFragment.getFragmentManager().getFragment(savedInstanceState, "main");
            //Deprecated
//            recipeMasterFragment = (RecipeMasterFragment) getFragmentManager().getFragment(savedInstanceState,"main");
            //Related to Widget
            //recipeMasterFragment.setFragmentListener(this);


            if (!recipeMasterFragment.isAdded())
                recipeMasterFragment.getFragmentManager().beginTransaction().add(R.id.master_fragment_holder, recipeMasterFragment).commit();
            //Deprecated
            //getFragmentManager().beginTransaction().add(R.id.master_fragment_holder, recipeMasterFragment).commit();

            if(recipeDetailFragment !=null)
            {
                recipeDetailFragment.getFragmentManager().getFragment(savedInstanceState, "detail");
                //Deprecated
                //recipeDetailFragment =  (RecipeDetailFragment) getFragmentManager().getFragment(savedInstanceState,"detail");
                recipeDetailFragment.getFragmentManager().beginTransaction().replace(R.id.detail_fragment_holder, recipeDetailFragment).commit();
                //Deprecated
                //getFragmentManager().beginTransaction().replace(R.id.detail_fragment_holder, recipeDetailFragment).commit();
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
        recipeMasterFragment.getFragmentManager().putFragment(outState, "main", recipeMasterFragment);
        //Deprecated
        //getFragmentManager().putFragment(outState, "main", recipeMasterFragment);

        if (isTablet && detailFragment!=null)
        {
            try{
                recipeDetailFragment.getFragmentManager().putFragment(outState, "detail", recipeDetailFragment);
                //Deprecated
                //getFragmentManager().putFragment(outState, "detail", recipeDetailFragment);
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
