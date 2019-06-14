package com.example.bakingapp.activities;

import android.os.Bundle;

import com.example.bakingapp.R;
import com.example.bakingapp.fragments.RecipeDetailFragment;

import androidx.appcompat.app.AppCompatActivity;

public class StepDetailActivity extends AppCompatActivity {

    RecipeDetailFragment detailsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if(savedInstanceState == null)
        {
            Bundle bundle = getIntent().getExtras();

            if(bundle.containsKey("name"))
            {
                getSupportActionBar().setTitle(bundle.getString("name")+" Steps");
            }
            bundle.putBoolean("tablet",false);

            detailsFragment = new RecipeDetailFragment();
            detailsFragment.setArguments(bundle);
            //Deprecated in API 28, use FragmentActivity.getSupportFragmentManager()
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_step_detail_framelayout, detailsFragment)
                    .commit();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getFragmentManager().putFragment(outState,"fragment",detailsFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        detailsFragment = (RecipeDetailFragment) getFragmentManager().getFragment(savedInstanceState,"fragment");
        if(detailsFragment.isAdded())
        {
            return;
        }
        getFragmentManager().beginTransaction()
                .add(R.id.activity_step_detail_framelayout, detailsFragment)
                .commit();
    }

}
