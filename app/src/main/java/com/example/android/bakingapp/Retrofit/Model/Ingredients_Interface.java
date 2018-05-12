package com.example.android.bakingapp.Retrofit.Model;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Ingredients_Interface {
    @GET()
    Call<Ingredients> getAnswer();
}
