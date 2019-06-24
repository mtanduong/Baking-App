package com.example.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Step;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.StepViewHolder>{

    private static final String TAG = "StepAdapter";
    private Context context;
    private List<Step> stepData;
    public int[] tracker;

    public StepRecyclerViewAdapter(Context context, List<Step> stepData, int[] tracker) {
        this.context = context;
        this.stepData = stepData;
        this.tracker = tracker;
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {

//        public TextView stepText;
//        public RelativeLayout parentLayout;
        @BindView(R.id.step_list_item) TextView stepText;
        @BindView(R.id.step_parent_layout) RelativeLayout parentLayout;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);

//            stepText = itemView.findViewById(R.id.step_list_item);
//            parentLayout = itemView.findViewById(R.id.step_parent_layout);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.step_list_item, parent, false);

        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {

        final Step currentStep = stepData.get(position);

        holder.stepText.setText(currentStep.getShortDescription());

        if(tracker[position] == 1) {
            holder.parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.stepText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.stepText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "StepRecyclerView onBindViewHolder/setOnClickListener: " + position + " " + stepData.get(position).getShortDescription(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepData.size();
    }
}
