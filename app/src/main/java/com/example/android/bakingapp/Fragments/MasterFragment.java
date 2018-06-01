package com.example.android.bakingapp.Fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Retrofit.Model.Recipe;

public class MasterFragment extends Fragment {

    private Recipe mRecipe;
    private RecyclerView mRecyclerView;
    private Context mContext;

    public MasterFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.recipe_master_fragment, container, false);
        mRecyclerView = rootView.findViewById(R.id.recipe_master_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }
}
