package com.example.android.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakingapp.Retrofit.Model.API_Trailer;
import com.example.android.bakingapp.Retrofit.Model.Recipe;
import com.example.android.bakingapp.Retrofit.Model.Recipe_Interface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {


    private Recipe_Interface recipe_interface;
    public List<Recipe> recipes = new ArrayList<>();
    private RecyclerView recipesNameRV;
    public RecipeAdapter adapter;
    public static List<String> recipesName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipesNameRV = findViewById(R.id.recipe_name_RV);
        recipesNameRV.setLayoutManager(new LinearLayoutManager(this));
        getRecipesData();


        adapter = new RecipeAdapter(recipes,this);
        recipesNameRV.setAdapter(adapter);

    }

    public Activity getActivity(){

        Context context = this;
        while (context instanceof ContextWrapper){
            if(context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return  null;
    }


    public void getRecipesData() {

        recipe_interface = API_Trailer.getClient().create(Recipe_Interface.class);
        String a = recipe_interface.getAnswer().request().url().toString();
        recipe_interface.getAnswer().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                String recipesString = response.body().toString();
                java.lang.reflect.Type listType = new TypeToken<List<Recipe>>() {
                }.getType();
                recipes = getRecipesFromJSON(recipesString, listType);

                //      Type listType = new TypeToken<List<Recipe>>() {}.getType();
                //      List<Recipe> recipesList = new Gson().fromJson(jsonBody, listType);
                //       List<Recipe> obtainedRecipes = response.body();

                //      int a = obtainedRecipes.size();

                //    for (int i = 0; i<= a; i++){
                //        Recipe recipe = obtainedRecipes.get(i);
                //       String recipeName = recipe.getName();
                //       recipesName.add(recipeName);
                //   }


                //       revRecyclerView.setAdapter(new ReviewAdapter(DetailActivity.this, obtainReviews.getReviews()));
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
    }

    public static <T> List<T> getRecipesFromJSON(String jsonString, Type type) {
        if (!isValidJson(jsonString)) {
            return null;
        }
        return new Gson().fromJson(jsonString, type);
    }

    public static boolean isValidJson(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    @Override
    public void onClick(int position) {

    }
}
