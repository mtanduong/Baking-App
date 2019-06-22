package com.example.bakingapp.activities;

import android.os.Bundle;

import com.example.bakingapp.R;
import com.example.bakingapp.fragments.RecipeDetailFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

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
            FragmentManager fragmentManager = getSupportFragmentManager();
            detailsFragment.setArguments(bundle);
            fragmentManager.beginTransaction().add(R.id.activity_step_detail_framelayout, detailsFragment).commit();

            //detailsFragment.getFragmentManager().beginTransaction().add(R.id.activity_step_detail_framelayout, detailsFragment).commit();


            //Deprecated
//            getFragmentManager().beginTransaction()
//                    .add(R.id.activity_step_detail_framelayout, detailsFragment)
//                    .commit();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        detailsFragment.getFragmentManager().putFragment(outState, "fragment", detailsFragment);
        //Deprecated
//        getFragmentManager().putFragment(outState,"fragment",detailsFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        detailsFragment.getFragmentManager().getFragment(savedInstanceState, "fragment");
        //Deprecated
//        detailsFragment = (RecipeDetailFragment) getFragmentManager().getFragment(savedInstanceState,"fragment");
        if(detailsFragment.isAdded())
        {
            return;
        }
        detailsFragment.getFragmentManager().beginTransaction().add(R.id.activity_step_detail_framelayout, detailsFragment);
        //Deprecated
//        getFragmentManager().beginTransaction()
//                .add(R.id.activity_step_detail_framelayout, detailsFragment)
//                .commit();
    }

}
