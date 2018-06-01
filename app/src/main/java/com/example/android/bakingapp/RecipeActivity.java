package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.Adapters.StepAdapter;
import com.example.android.bakingapp.Fragments.MasterFragment;
import com.example.android.bakingapp.Fragments.StepsFragment;
import com.example.android.bakingapp.Retrofit.Model.Ingredient;
import com.example.android.bakingapp.Retrofit.Model.Recipe;
import com.example.android.bakingapp.Retrofit.Model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements StepAdapter.StepAdapterOnClickHandler {

    @BindView(R.id.recipe_ingredients_details)
    TextView recipe_ingredients_tv;
    @BindView(R.id.recipe_steps_recycler)
    RecyclerView recipe_steps_rv;
    private Recipe dRecipe;
    private Fragment mFragment;
    private MasterFragment masterFragment;
    private StepsFragment stepsFragment;
    private StepAdapter stepAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);

        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        dRecipe = data.getParcelable("recipe");
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        String name = dRecipe.getName();
        setTitle(name);

        StringBuilder recipeIngredients = new StringBuilder();
        List<Ingredient> ingredients = dRecipe.getIngredients();

        for (int i = 0; i < ingredients.size(); i++) {
            recipeIngredients.append(ingredients.get(i).getIngredient());
            recipeIngredients.append(",\n");
        }
        recipe_ingredients_tv.setText(recipeIngredients.toString());

        List<Step> recipeSteps = dRecipe.getSteps();
        recipe_steps_rv.setLayoutManager(new LinearLayoutManager(this));
        stepAdapter = new StepAdapter(this);
        stepAdapter.setSteps(recipeSteps);
        recipe_steps_rv.setAdapter(stepAdapter);


        //      masterFragment = new MasterFragment();
        //      masterFragment.setRecipe(dRecipe);
        //      stepsFragment = new StepsFragment();

        ////    if (savedInstanceState != null) {
        //       mFragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
        //   } else {
        //       getSupportFragmentManager().beginTransaction()
        //               .add(R.id.recipe_fragment, masterFragment)
        //              .commit();
        //  }


//        getSupportFragmentManager().beginTransaction()
        //            .replace(R.id.recipe_fragment, mFragment)
        //              .commit();

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Error !!!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(int position) {

    }
}
