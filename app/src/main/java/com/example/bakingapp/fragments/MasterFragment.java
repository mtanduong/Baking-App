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
import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterFragment extends Fragment {

    public static int top = -1;
    public static LinearLayoutManager layoutManager;
    private ArrayList<Steps> stepList;

    @BindView(R.id.list_view_master) RecyclerView recyclerView;

    public MasterFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_master, container, false);

        ButterKnife.bind(this, view);

        stepList = getStepsArrayList();
        ShortDescAdapter shortDescAdapter = new ShortDescAdapter(RecipeActivity.shortDescription, stepList);
        recyclerView.setAdapter(shortDescAdapter);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onResume() {

        layoutManager.scrollToPosition(RecipeActivity.positionRecycler);
        super.onResume();
        View view = recyclerView.getChildAt(0);
        top = (view == null) ? 0 : (view.getTop() - recyclerView.getPaddingTop());
    }

    ArrayList<Steps> getStepsArrayList() {

        Steps[] stepList = null;
        Parcelable[] steps = getArguments().getParcelableArray(RecipeAdapter.STEPS);
        if (steps != null) {

            stepList = new Steps[steps.length];
            for (int i = 0; i < steps.length; i++) {
                stepList[i] = (Steps) steps[i];
            }
        }
        return new ArrayList<>(Arrays.asList(stepList));
    }
}
