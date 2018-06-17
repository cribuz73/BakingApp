package com.example.android.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recipeAppearance() {

        Espresso.onView(withId(R.id.recipe_name_RV)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }

    @Test
    public void stepAppearance() {

        Espresso.onView(withId(R.id.recipe_name_RV)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Espresso.onView(withId(R.id.recipe_steps_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }

}
