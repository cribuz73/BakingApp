package com.example.android.bakingapp.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Adapters.Master_Recipe_Adapter;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Retrofit.Model.Ingredient;
import com.example.android.bakingapp.Retrofit.Model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Master_Recipe_Fragment extends Fragment implements Master_Recipe_Adapter.ItemClickListener {

    @BindView(R.id.recipe_ingredients_details)
    TextView ingredients_tv;
    @BindView(R.id.recipe_steps_list)
    RecyclerView stepList_rv;
    private ArrayList<Step> mSteps;
    private List<Ingredient> mIngredients;
    private StepClickListener mCallback;

    public Master_Recipe_Fragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (StepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_recipe, container, false);

        ButterKnife.bind(this, rootView);


        StringBuilder recipeIngredients = new StringBuilder();

        for (int i = 0; i < mIngredients.size(); i++) {
            recipeIngredients.append(mIngredients.get(i).getIngredient());
            recipeIngredients.append(",\n");
        }
        ingredients_tv.setText(recipeIngredients.toString());

        stepList_rv.setLayoutManager(new LinearLayoutManager(container.getContext()));
        stepList_rv.setAdapter(new Master_Recipe_Adapter(mSteps, this));


        return rootView;
    }

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    @Override
    public void onItemClicked(int stepClicked) {
        mCallback.onStepSelected(stepClicked);

    }

    public interface StepClickListener {
        void onStepSelected(int position);
    }
}
