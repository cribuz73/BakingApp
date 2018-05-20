package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.bakingapp.Retrofit.Model.API_Trailer;
import com.example.android.bakingapp.Retrofit.Model.Recipe;
import com.example.android.bakingapp.Retrofit.Model.Recipe_Interface;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {


    private Recipe_Interface recipe_interface;
    private static final String TAG = "Main Activity";
    public ArrayList<Recipe> recipes = new ArrayList<>();
    public Recipe recipe;

    private RecyclerView recipesNameRV;
    public RecipeAdapter adapter;
    public ArrayList<String> recipeNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipesNameRV = findViewById(R.id.recipe_name_RV);
        recipesNameRV.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecipeAdapter(this);
        recipesNameRV.setAdapter(adapter);

        getRecipesData();

    }


    public void getRecipesData() {

        recipe_interface = API_Trailer.getClient().create(Recipe_Interface.class);
        String a = recipe_interface.getAnswer().request().url().toString();
        recipe_interface.getAnswer().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                final String recipesString = response.body().toString();

                retriveRecipesName(recipesString);

                adapter.setRecipesNames(recipeNames);


            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
    }


    @Override
    public void onClick(int position) {
        String a = recipeNames.get(position);
    }

    private ArrayList<String> retriveRecipesName(String responseJson) {
        try {
            JSONArray jsonArray = new JSONArray(responseJson);
            for (int i = 0; i <= jsonArray.length(); i++) {
                JSONObject recipe = jsonArray.getJSONObject(i);
                String name = recipe.opt("name").toString();
                recipeNames.add(name);
            }

        } catch (org.json.JSONException e) {
            Log.e(TAG, "eroare");
        }

        return recipeNames;
    }
}
