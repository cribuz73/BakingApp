package com.example.android.bakingapp.Fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterRecipeFragment extends Fragment implements Master_Recipe_Adapter.ItemClickListener {

    @BindView(R.id.recipe_ingredients_details)
    TextView ingredients_tv;
    private static final String FRAGMENT_STEP_ARRAY = "fragment_step_array";
    private ArrayList<Step> mSteps;
    private static final String FRAGMENT_INGREDIENT_ARRAY = "fragment_ingredient_array";
    private StepClickListener mCallback;
    @BindView(R.id.recipe_steps_recycler)
    RecyclerView stepList_rv;
    private ArrayList<Ingredient> mIngredients;


    public MasterRecipeFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(FRAGMENT_STEP_ARRAY);
            mIngredients = savedInstanceState.getParcelableArrayList(FRAGMENT_INGREDIENT_ARRAY);
        }

        final View rootView = inflater.inflate(R.layout.fragment_master_recipe, container, false);

        ButterKnife.bind(this, rootView);


        StringBuilder recipeIngredients = new StringBuilder();

        for (int i = 0; i < mIngredients.size(); i++) {

            double quantity = mIngredients.get(i).getQuantity();
            String stringQuantity;
            if (quantity - (int) quantity != 0) {
                stringQuantity = String.valueOf(quantity);
            } else {
                stringQuantity = String.valueOf((int) quantity);
            }

            recipeIngredients.append(stringQuantity);
            recipeIngredients.append(" ");
            recipeIngredients.append(mIngredients.get(i).getMeasure());
            recipeIngredients.append(" ");
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

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(FRAGMENT_STEP_ARRAY, mSteps);
        outState.putParcelableArrayList(FRAGMENT_INGREDIENT_ARRAY, mIngredients);
    }

    @Override
    public void onItemClicked(int stepClicked) {
        mCallback.onStepSelected(stepClicked);
    }

    public interface StepClickListener {
        void onStepSelected(int position);
    }
}
