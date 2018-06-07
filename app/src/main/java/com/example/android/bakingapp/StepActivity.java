package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.Fragments.Video_step;
import com.example.android.bakingapp.Retrofit.Model.Step;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {

    private static final String TAG = "verificare video";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_detail_activity);

        if (savedInstanceState == null) {

            int stepNumber = getIntent().getIntExtra(RecipeActivity.STEP_POSITION, 0);

            FragmentManager fragmentManager = getSupportFragmentManager();

            Video_step stepFragment = new Video_step();
            ArrayList<Step> mSteps = getIntent().getParcelableArrayListExtra(RecipeActivity.STEPS_ARRAY_LIST);
            stepFragment.setSteps(mSteps);
            stepFragment.setPosition(stepNumber);
            stepFragment.hideNavigation(false);
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, stepFragment)
                    .commit();
        }


    }
}

