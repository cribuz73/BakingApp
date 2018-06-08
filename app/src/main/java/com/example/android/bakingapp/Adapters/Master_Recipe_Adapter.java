package com.example.android.bakingapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Retrofit.Model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Master_Recipe_Adapter extends RecyclerView.Adapter<Master_Recipe_Adapter.StepViewHolder> {

    private final ArrayList<Step> mSteps;
    private final ItemClickListener mOnClickListener;

    public Master_Recipe_Adapter(ArrayList<Step> steps, ItemClickListener listener) {
        mSteps = steps;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public Master_Recipe_Adapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_card, parent, false);
        view.setFocusable(false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Master_Recipe_Adapter.StepViewHolder holder, int position) {

        if (position == 0) {
            holder.step_id_tv.setText(null);
            holder.step_short_description_tv.setText(mSteps.get(position).getShortDescription());
        } else {
            holder.step_id_tv.setText(mSteps.get(position).getId().toString());
            holder.step_short_description_tv.setText(mSteps.get(position).getShortDescription());

        }
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 0;
        return mSteps.size();
    }

    public interface ItemClickListener {
        void onItemClicked(int stepClicked);
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_id)
        TextView step_id_tv;
        @BindView(R.id.step_short_description)
        TextView step_short_description_tv;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onItemClicked(clickedPosition);
        }
    }
}
