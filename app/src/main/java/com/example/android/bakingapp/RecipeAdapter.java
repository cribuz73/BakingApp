package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Retrofit.Model.Recipe;

import java.util.List;

import butterknife.BindView;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> mrecipesList;
    public RecipeAdapterOnClickHandler mClickHandler;
  //  public Context mContext;

    public RecipeAdapter(List<Recipe> recipesList,RecipeAdapterOnClickHandler clickHandler) {
        mrecipesList = recipesList;
        mClickHandler = clickHandler;
    //    mContext = context;
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {

            Recipe recipe = mrecipesList.get(position);
            holder.recipe_name_tv.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_name)
        TextView recipe_name_tv;


        public ViewHolder(View itemView) {
            super(itemView);
            recipe_name_tv = itemView.findViewById(R.id.recipe_name);
        }
    }

    public interface RecipeAdapterOnClickHandler {
        void onClick(int position);

    }
}
