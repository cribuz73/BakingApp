package com.example.android.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Retrofit.Model.Step;

import java.util.List;

import butterknife.BindView;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private static StepAdapter.StepAdapterOnClickHandler mClickHandler;
    private List<Step> msteps;

    public StepAdapter(StepAdapter.StepAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.step_card, parent, false);
        return new StepAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepAdapter.ViewHolder holder, int position) {

        int id_nr = msteps.get(position).getId();
        int id_real = id_nr + 1;
        String id = String.valueOf(id_real);

        holder.step_id_tv.setText(id);
        String shortDescription = msteps.get(position).getShortDescription();
        holder.step_short_description_tv.setText(shortDescription);
    }

    @Override
    public int getItemCount() {
        return (msteps == null) ? 0 : msteps.size();

    }

    public void setSteps(List<Step> steps) {
        msteps = steps;
        notifyDataSetChanged();
    }

    public interface StepAdapterOnClickHandler {
        void onClick(int position);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_id)
        TextView step_id_tv;
        @BindView(R.id.step_short_description)
        TextView step_short_description_tv;

        private ViewHolder(View itemView) {
            super(itemView);
            step_id_tv = itemView.findViewById(R.id.step_id);
            step_short_description_tv = itemView.findViewById(R.id.step_short_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }
}

