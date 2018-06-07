package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.Adapters.StepAdapter;
import com.example.android.bakingapp.Fragments.Master_Recipe_Fragment;
import com.example.android.bakingapp.Fragments.Video_step;
import com.example.android.bakingapp.Retrofit.Model.Ingredient;
import com.example.android.bakingapp.Retrofit.Model.Recipe;
import com.example.android.bakingapp.Retrofit.Model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements Master_Recipe_Fragment.StepClickListener {

    @BindView(R.id.recipe_ingredients_details)
    TextView recipe_ingredients_tv;
    public int stepPosition;
    private Recipe dRecipe;
    private StepAdapter stepAdapter;
    public ArrayList<Step> stepsArray;
    @BindView(R.id.recipe_steps_list)
    ListView recipe_steps_lv;
    private Step clickedStep;
    public static final String STEPS_ARRAY_LIST = "steps_ArrayList";
    public static final String STEP_POSITION = "step_position";
    private static final String TWO_PANE = "two_pane";
    private boolean mTwoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

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


        FragmentManager fragmentManager = getSupportFragmentManager();

        Master_Recipe_Fragment mrf = new Master_Recipe_Fragment();
        mrf.setIngredients(ingredients);
        mrf.setSteps(stepsArray);
        fragmentManager.beginTransaction()
                .add(R.id.master_fragment_container, mrf)
                .commit();

        if (findViewById(R.id.step_detail_container) != null) {
            mTwoPane = true;

            Video_step mStepFragment = new Video_step();
            mStepFragment.setSteps(stepsArray);
            mStepFragment.hideNavigation(mTwoPane);
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, mStepFragment)
                    .commit();
        } else {
            mTwoPane = false;
        }


        for (int i = 0; i < ingredients.size(); i++) {
            recipeIngredients.append(ingredients.get(i).getIngredient());
            recipeIngredients.append(",\n");
        }
        recipe_ingredients_tv.setText(recipeIngredients.toString());

        final List<Step> recipeSteps = dRecipe.getSteps();

        stepAdapter = new StepAdapter(this, 0, recipeSteps);
        recipe_steps_lv.setAdapter(stepAdapter);
        recipe_steps_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stepPosition = Integer.valueOf(position);
                stepsArray = returnArrayList(recipeSteps);
                Intent intent = new Intent(getApplicationContext(), StepActivity.class);
                intent.putParcelableArrayListExtra(STEPS_ARRAY_LIST, stepsArray);
                intent.putExtra(STEP_POSITION, stepPosition);
                startActivity(intent);
            }
        });

    }

    public ArrayList<Step> returnArrayList(List<Step> steps) {
        stepsArray = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            stepsArray.add(step);
        }
        return stepsArray;
    }

    protected void closeOnError() {
        finish();
        Toast.makeText(this, "Error !!!!!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onStepSelected(int position) {
        if (mTwoPane) {
            Video_step newFragment = new Video_step();
            newFragment.setSteps(stepsArray);
            newFragment.setPosition(position);
            newFragment.hideNavigation(mTwoPane);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, newFragment)
                    .commit();

        }

    }
}
