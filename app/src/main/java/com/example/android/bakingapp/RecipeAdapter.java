package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private static RecipeAdapterOnClickHandler mClickHandler;
    private ArrayList<String> mrecipesName;

    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {

        String name = mrecipesName.get(position);
        holder.recipe_name_tv.setText(name);
    }

    @Override
    public int getItemCount() {
        return (mrecipesName == null) ? 0 : mrecipesName.size();

    }

    public void setRecipesNames(ArrayList<String> recipesNames) {
        mrecipesName = recipesNames;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_name)
        TextView recipe_name_tv;


        private ViewHolder(View itemView) {
            super(itemView);
            recipe_name_tv = itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    public interface RecipeAdapterOnClickHandler {
        void onClick(int position);

    }
}
