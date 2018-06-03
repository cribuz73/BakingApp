package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.Adapters.StepAdapter;
import com.example.android.bakingapp.Fragments.MasterFragment;
import com.example.android.bakingapp.Fragments.StepsFragment;
import com.example.android.bakingapp.Retrofit.Model.Ingredient;
import com.example.android.bakingapp.Retrofit.Model.Recipe;
import com.example.android.bakingapp.Retrofit.Model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity {

    @BindView(R.id.recipe_ingredients_details)
    TextView recipe_ingredients_tv;
    public int stepPosition;
    private Recipe dRecipe;
    private Fragment mFragment;
    private MasterFragment masterFragment;
    private StepsFragment stepsFragment;
    private StepAdapter stepAdapter;
    public ArrayList<Step> stepsArray;
    @BindView(R.id.recipe_steps_list)
    ListView recipe_steps_lv;
    private Step clickedStep;



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

        final List<Step> recipeSteps = dRecipe.getSteps();

        stepAdapter = new StepAdapter(this, 0, recipeSteps);
        recipe_steps_lv.setAdapter(stepAdapter);
        recipe_steps_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stepPosition = Integer.valueOf(position);
                stepsArray = returnArrayList(recipeSteps);
                Intent intent = new Intent(getApplicationContext(), StepActivity.class);
                intent.putParcelableArrayListExtra("steps", stepsArray);
                intent.putExtra("position", stepPosition);
                startActivity(intent);
            }
        });




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

    public ArrayList<Step> returnArrayList(List<Step> steps) {
        stepsArray = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            stepsArray.add(step);
        }
        return stepsArray;
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Error !!!!!", Toast.LENGTH_SHORT).show();
    }


}
