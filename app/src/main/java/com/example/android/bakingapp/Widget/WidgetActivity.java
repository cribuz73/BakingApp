package com.example.android.bakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.bakingapp.R;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WidgetActivity extends AppCompatActivity implements WidgetAdapter.WidgetAdapterOnClickHandler {

    private static final String TAG = "Widget Activity";
    public WidgetAdapter widgetAdapter;
    @BindView(R.id.widget_recipes)
    RecyclerView widget_recipes_RV;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private static final String WIDGET_INGREDIENTS = "widget_ingredients";
    private Context mContext;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Ingredient> ingredientsArray;

    private ArrayList<Recipe> recipes;
    private ArrayList<Step> steps;
    private String responseJson;
    private String widgetIngredientsText;
    private Ingredient ingredient;
    private int widgetID;
    private Recipe recipe;
    private Step wStep;
    private Recipe_Interface recipe_interface_widget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.selection_widget);
        ButterKnife.bind(this);


        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        widget_recipes_RV.setLayoutManager(new LinearLayoutManager(this));
        widgetAdapter = new WidgetAdapter(this);
        widget_recipes_RV.setAdapter(widgetAdapter);
        getRecipesData();

    }

    public void getRecipesData() {

        recipe_interface_widget = API_Trailer.getClient().create(Recipe_Interface.class);
        String a = recipe_interface_widget.getAnswer().request().url().toString();
        recipe_interface_widget.getAnswer().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                final String recipesString = response.body().toString();

                retriveRecipes(recipesString);

                widgetAdapter.setRecipes(recipes);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
    }

    private List<Recipe> retriveRecipes(String responseJson) {
        try {
            JSONArray jsonArray = new JSONArray(responseJson);
            recipes = new ArrayList<>();

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
                wStep = new Step(stepId, stepShortDescription, stepDescription, stepVideo, stepImage);
                steps.add(wStep);
            }

        } catch (org.json.JSONException e) {
            Log.e(TAG, "eroare");
        }
        return steps;
    }
    @Override
    public void onClick(int position) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(WidgetActivity.this);
        ingredientsArray = new ArrayList<>();
        ingredientsArray = returnIngredientArrayList(recipes.get(position).getIngredients());
        WidgetProvider.updateAppWidget(WidgetActivity.this, appWidgetManager, appWidgetId,
                position,
                recipes.get(position).getName(),
                ingredientsArray,
                recipes);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        resultValue.putExtra(WIDGET_INGREDIENTS, ingredients);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public ArrayList<Ingredient> returnIngredientArrayList(List<Ingredient> ingredients) {
        ingredientsArray = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            ingredientsArray.add(ingredient);
        }
        return ingredientsArray;
    }

}
