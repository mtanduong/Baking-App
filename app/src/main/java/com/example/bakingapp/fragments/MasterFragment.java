package com.example.bakingapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.R;
import com.example.bakingapp.activities.RecipeActivity;
import com.example.bakingapp.adapters.RecipeAdapter;
import com.example.bakingapp.adapters.ShortDescAdapter;
import com.example.bakingapp.models.Steps;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MasterFragment extends Fragment {

    public static int top = -1;
    public static LinearLayoutManager mLayoutManager;
    private ArrayList<Steps> stepList;
    RecyclerView recyclerView;

    public MasterFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_master, container, false);
        recyclerView = rootView.findViewById(R.id.list_view_master);
        stepList = getStepsArrayList();
        ShortDescAdapter shortDescAdapter = new ShortDescAdapter(RecipeActivity.shortDescription, stepList);
        recyclerView.setAdapter(shortDescAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        return rootView;
    }

    @Override
    public void onResume()
    {
        mLayoutManager.scrollToPosition(RecipeActivity.positionRecycler);
        super.onResume();
        View v = recyclerView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - recyclerView.getPaddingTop());
    }

    ArrayList<Steps> getStepsArrayList()
    {
        Steps[] stepsArr = null;
        Parcelable[] steps = getArguments().getParcelableArray(RecipeAdapter.STEPS);
        if (steps != null)
        {
            stepsArr = new Steps[steps.length];
            for (int i = 0; i < steps.length; i++)
            {
                stepsArr[i] = (Steps) steps[i];
            }
        }
        return new ArrayList<>(Arrays.asList(stepsArr));
    }
}
