package com.example.android.bakingapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingapp.Fragments.Master_Recipe_Fragment;
import com.example.android.bakingapp.Fragments.Video_step;
import com.example.android.bakingapp.Retrofit.Model.API_Trailer;
import com.example.android.bakingapp.Retrofit.Model.Ingredient;
import com.example.android.bakingapp.Retrofit.Model.Recipe;
import com.example.android.bakingapp.Retrofit.Model.Recipe_Interface;
import com.example.android.bakingapp.Retrofit.Model.Step;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity implements Master_Recipe_Fragment.StepClickListener {


    public int stepPosition;
    private Recipe dRecipe;
    public static final String RECIPE_ID = "recipe_id";
    public static final String INGREDIENTS_ARRAY_LIST = "ingredients";
    public static final String RECIPE_NAME = "recipe_name";
    private static String recipeName;

    public int mRecipeId;
    public ArrayList<Step> stepsArray;
    private List<Ingredient> ingredients;
    private static final String TAG = "RecipeActivity - error";
    private ArrayList<Recipe> allRecipes;

    public static final String STEPS_ARRAY_LIST = "steps_ArrayList";
    public static final String STEP_POSITION = "step_position";
    private static final String TWO_PANE = "two_pane";
    private ArrayList<Ingredient> ingredientsArray;
    private boolean mTwoPane;
    private int recipePosition;
    private Recipe_Interface m_recipe_interface;
    private Recipe mRecipe;
    private Step mStep;
    private Ingredient mIngredient;
    private ArrayList<Step> mArraySteps;
    private ArrayList<Ingredient> mArrayIngredients;

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
                    //       MainActivity ma = new MainActivity();
                    //      ma.getRecipesData();
                    //      allRecipes = ma.retriveRecipes(MainActivity.recipesJsonString);
                    getRecipesDetailedData();
                }

                recipePosition = data.getInt(MainActivity.RECIPE_POSITION);
                dRecipe = allRecipes.get(recipePosition);
                setTitle(dRecipe.getName());
                ingredients = dRecipe.getIngredients();
                List<Step> steps = dRecipe.getSteps();
                stepsArray = returnStepArrayList(steps);
                ingredientsArray = returnIngredientArrayList(ingredients);

            } else {
                closeOnError();
            }
        }
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Master_Recipe_Fragment masterRecipeFragment = new Master_Recipe_Fragment();
            masterRecipeFragment.setIngredients(ingredientsArray);
            masterRecipeFragment.setSteps(stepsArray);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.master_fragment_container, masterRecipeFragment).commit();
        } else {
        }

        if (findViewById(R.id.step_detail_container) != null) {
            mTwoPane = true;
            FragmentManager fragmentManager = getSupportFragmentManager();
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
        stepPosition = Integer.valueOf(position);
        Intent intent = new Intent(getApplicationContext(), StepActivity.class);
        intent.putParcelableArrayListExtra(STEPS_ARRAY_LIST, stepsArray);
        intent.putExtra(STEP_POSITION, stepPosition);
        startActivity(intent);

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        dRecipe = allRecipes.get(recipePosition);

        //     dRecipe = getIntent().getExtras().getParcelable("selected_recipe");
        outState.putInt(RecipeActivity.RECIPE_ID, dRecipe.getId());
        outState.putString(RECIPE_NAME, dRecipe.getName());
        outState.putParcelableArrayList(STEPS_ARRAY_LIST, stepsArray);
        outState.putParcelableArrayList(INGREDIENTS_ARRAY_LIST, ingredientsArray);
        outState.putBoolean(TWO_PANE, mTwoPane);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mRecipeId = savedInstanceState.getInt(RECIPE_ID);
        stepsArray = savedInstanceState.getParcelableArrayList(STEPS_ARRAY_LIST);
        ingredientsArray = savedInstanceState.getParcelableArrayList(INGREDIENTS_ARRAY_LIST);
        mTwoPane = savedInstanceState.getBoolean(TWO_PANE);
        recipeName = savedInstanceState.getString(RECIPE_NAME);
        setTitle(recipeName);
    }

    private void getRecipesDetailedData() {
        m_recipe_interface = API_Trailer.getClient().create(Recipe_Interface.class);
        String a = m_recipe_interface.getAnswer().request().url().toString();
        m_recipe_interface.getAnswer().enqueue(new Callback<JsonArray>() {

            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                final String recipesString = response.body().toString();
                retriveRecipes(recipesString);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });

    }

    public List<Recipe> retriveRecipes(String responseJson) {
        try {
            JSONArray jsonArray = new JSONArray(responseJson);
            for (int i = 0; i <= jsonArray.length(); i++) {
                JSONObject recipeJson = jsonArray.getJSONObject(i);
                int id = recipeJson.optInt("id");
                String name = recipeJson.opt("name").toString();

                mArrayIngredients = retriveIngredients(recipeJson);
                mArraySteps = retriveSteps(recipeJson);

                int servingsNumber = recipeJson.getInt("servings");
                String recipeImage = recipeJson.opt("image").toString();

                mRecipe = new Recipe(id, name, mArrayIngredients, mArraySteps, servingsNumber, recipeImage);
                allRecipes.add(mRecipe);
            }

        } catch (org.json.JSONException e) {
            Log.e(TAG, "eroare");
        }

        return allRecipes;
    }

    private ArrayList<Ingredient> retriveIngredients(JSONObject recipe) {

        mArrayIngredients = new ArrayList<>();

        try {
            JSONArray ingredientsList = recipe.getJSONArray("ingredients");
            int a = ingredientsList.length();
            for (int j = 0; j <= ingredientsList.length(); j++) {

                JSONObject ingredientJson = ingredientsList.getJSONObject(j);
                Double ingredientQuantity = ingredientJson.optDouble("quantity");
                String ingredientMeasure = ingredientJson.optString("measure");
                String ingredientName = ingredientJson.optString("ingredient");
                mIngredient = new Ingredient(ingredientQuantity, ingredientMeasure, ingredientName);
                mArrayIngredients.add(mIngredient);
            }
        } catch (org.json.JSONException e) {
            Log.e(TAG, "eroare");
        }

        return mArrayIngredients;
    }

    private ArrayList<Step> retriveSteps(JSONObject recipe) {
        mArraySteps = new ArrayList<>();

        try {
            JSONArray stepsList = recipe.getJSONArray("steps");
            for (int k = 0; k <= stepsList.length(); k++) {
                JSONObject stepJson = stepsList.getJSONObject(k);
                int stepId = stepJson.optInt("id");
                String stepShortDescription = stepJson.optString("shortDescription");
                String stepDescription = stepJson.optString("description");
                String stepVideo = stepJson.optString("videoURL");
                String stepImage = stepJson.optString("thumbnailURL");
                mStep = new Step(stepId, stepShortDescription, stepDescription, stepVideo, stepImage);
                mArraySteps.add(mStep);
            }

        } catch (org.json.JSONException e) {
            Log.e(TAG, "eroare");
        }
        return mArraySteps;
    }

}
