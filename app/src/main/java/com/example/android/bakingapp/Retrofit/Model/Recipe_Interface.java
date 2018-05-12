package com.example.android.bakingapp.Retrofit.Model;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Recipe_Interface {
    @GET("baking.json")
    Call<JsonArray> getAnswer();
}
