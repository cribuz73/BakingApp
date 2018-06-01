package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

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

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {


    private Recipe_Interface recipe_interface;
    private static final String TAG = "Main Activity";
    public ArrayList<Recipe> recipes = new ArrayList<>();
    public ArrayList<Ingredient> ingredients = new ArrayList<>();
    public ArrayList<Step> steps = new ArrayList<>();
    public Recipe recipe;
    public Ingredient ingredient;
    public Step step;

    private RecyclerView recipesNameRV;
    public RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recipesNameRV = findViewById(R.id.recipe_name_RV);
        recipesNameRV.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));

        adapter = new RecipeAdapter(this);
        recipesNameRV.setAdapter(adapter);

        getRecipesData();

    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 1) return 1;
        return nColumns;
    }

    public void getRecipesData() {

        recipe_interface = API_Trailer.getClient().create(Recipe_Interface.class);
        String a = recipe_interface.getAnswer().request().url().toString();
        recipe_interface.getAnswer().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                final String recipesString = response.body().toString();

                retriveRecipes(recipesString);

                adapter.setRecipes(recipes);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
    }


    @Override
    public void onClick(int position) {
        Recipe clickedRecipe = recipes.get(position);
        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
        intent.putExtra("recipe", clickedRecipe);
        startActivity(intent);
    }

    private List<Recipe> retriveRecipes(String responseJson) {
        try {
            JSONArray jsonArray = new JSONArray(responseJson);
            for (int i = 0; i <= jsonArray.length(); i++) {
                JSONObject recipeJson = jsonArray.getJSONObject(i);
                int id = recipeJson.optInt("id");
                String name = recipeJson.opt("name").toString();

                ingredients = retriveIngredients(recipeJson);
                steps = retriveSteps(recipeJson);

                int servingsNumber = recipeJson.getInt("servings");
                String recipeImage = recipeJson.opt("image").toString();

                recipe = new Recipe(id, name, ingredients, steps, servingsNumber, recipeImage);
                recipes.add(recipe);
            }

        } catch (org.json.JSONException e) {
            Log.e(TAG, "eroare");
        }

        return recipes;
    }

    private ArrayList<Ingredient> retriveIngredients(JSONObject recipe) {

        ingredients = new ArrayList<>();

        try {
            JSONArray ingredientsList = recipe.getJSONArray("ingredients");
            int a = ingredientsList.length();
            for (int j = 0; j <= ingredientsList.length(); j++) {

                JSONObject ingredientJson = ingredientsList.getJSONObject(j);
                Double ingredientQuantity = ingredientJson.optDouble("quantity");
                String ingredientMeasure = ingredientJson.optString("measure");
                String ingredientName = ingredientJson.optString("ingredient");
                ingredient = new Ingredient(ingredientQuantity, ingredientMeasure, ingredientName);
                ingredients.add(ingredient);
            }
        } catch (org.json.JSONException e) {
            Log.e(TAG, "eroare");
        }

        return ingredients;
    }

    private ArrayList<Step> retriveSteps(JSONObject recipe) {
        steps = new ArrayList<>();

        try {
            JSONArray stepsList = recipe.getJSONArray("steps");
            for (int k = 0; k <= stepsList.length(); k++) {
                JSONObject stepJson = stepsList.getJSONObject(k);
                int stepId = stepJson.optInt("id");
                String stepShortDescription = stepJson.optString("shortDescription");
                String stepDescription = stepJson.optString("description");
                String stepVideo = stepJson.optString("videoURL");
                String stepImage = stepJson.optString("thumbnailURL");
                step = new Step(stepId, stepShortDescription, stepDescription, stepVideo, stepImage);
                steps.add(step);
            }

        } catch (org.json.JSONException e) {
            Log.e(TAG, "eroare");
        }
        return steps;
    }

}
