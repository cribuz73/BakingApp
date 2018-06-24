package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.bakingapp.Fragments.Master_Recipe_Fragment;
import com.example.android.bakingapp.Fragments.Video_step;
import com.example.android.bakingapp.Retrofit.Model.Ingredient;
import com.example.android.bakingapp.Retrofit.Model.Recipe;
import com.example.android.bakingapp.Retrofit.Model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class RecipeActivity extends AppCompatActivity implements Master_Recipe_Fragment.StepClickListener {


    public static final String RECIPE_ID = "recipe_id";
    public static final String INGREDIENTS_ARRAY_LIST = "ingredients";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String STEPS_ARRAY_LIST = "steps_ArrayList";
    public static final String STEP_POSITION = "step_position";
    private static final String TWO_PANE = "two_pane";
    private static String recipeName;
    private static final String TAG = "RecipeActivity - error";

    public int stepPosition;
    private Recipe dRecipe;
    public int mRecipeId;
    public ArrayList<Step> stepsArray;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private ArrayList<Recipe> allRecipes;
    private ArrayList<Ingredient> ingredientsArray;
    private boolean mTwoPane;
    private int recipePosition;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);


        if (savedInstanceState == null) {

            Intent intent = getIntent();
            if (intent != null) {
                Bundle data = getIntent().getExtras();
                allRecipes = data.getParcelableArrayList(MainActivity.ALL_RECIPES);
                if (allRecipes == null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Recipe>>() {
                    }.getType();
                    String strObj = getIntent().getStringExtra("RECIPES_WIDGET_GSON");
                    ArrayList<Recipe> allRecipesGson = new ArrayList<>();
                    allRecipesGson = gson.fromJson(strObj, type);
                    allRecipes = allRecipesGson;
                }

                recipePosition = data.getInt(MainActivity.RECIPE_POSITION);
                dRecipe = allRecipes.get(recipePosition);
                recipeName = dRecipe.getName();
                setTitle(recipeName);
                ingredients = dRecipe.getIngredients();
                steps = dRecipe.getSteps();
                stepsArray = returnStepArrayList(steps);
                ingredientsArray = returnIngredientArrayList(ingredients);

            } else {
                closeOnError();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            Master_Recipe_Fragment masterRecipeFragment = new Master_Recipe_Fragment();
            masterRecipeFragment.setIngredients(ingredientsArray);
            masterRecipeFragment.setSteps(stepsArray);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.master_fragment_container, masterRecipeFragment, "first").commit();


            if (findViewById(R.id.step_detail_container) != null) {
                mTwoPane = true;
                fragmentManager = getSupportFragmentManager();
                Video_step mStepFragment = new Video_step();
                mStepFragment.setSteps(stepsArray);
                mStepFragment.hideNavigation(mTwoPane);
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, mStepFragment)
                        .commit();
            } else {
                mTwoPane = false;
            }
        }
    }

    public ArrayList<Step> returnStepArrayList(List<Step> steps) {
        stepsArray = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            stepsArray.add(step);
        }
        return stepsArray;
    }

    public ArrayList<Ingredient> returnIngredientArrayList(List<Ingredient> ingredients) {
        ingredientsArray = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            ingredientsArray.add(ingredient);
        }
        return ingredientsArray;
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
        } else {
            stepPosition = position;
            Intent intent = new Intent(getApplicationContext(), StepActivity.class);
            intent.putParcelableArrayListExtra(STEPS_ARRAY_LIST, stepsArray);
            intent.putExtra(STEP_POSITION, stepPosition);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RECIPE_NAME, recipeName);
        outState.putParcelableArrayList(STEPS_ARRAY_LIST, stepsArray);
        outState.putParcelableArrayList(INGREDIENTS_ARRAY_LIST, ingredientsArray);
        outState.putBoolean(TWO_PANE, mTwoPane);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        stepsArray = savedInstanceState.getParcelableArrayList(STEPS_ARRAY_LIST);
        ingredientsArray = savedInstanceState.getParcelableArrayList(INGREDIENTS_ARRAY_LIST);
        mTwoPane = savedInstanceState.getBoolean(TWO_PANE, false);
        recipeName = savedInstanceState.getString(RECIPE_NAME);
        setTitle(recipeName);
    }
}
