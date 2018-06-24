package com.example.android.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeActivity;
import com.example.android.bakingapp.Retrofit.Model.Ingredient;
import com.example.android.bakingapp.Retrofit.Model.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;

public class WidgetProvider extends AppWidgetProvider {


    private static String widgetIngredientsText;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int position, String recipeName, ArrayList<Ingredient> ingredients, ArrayList<Recipe> recipes) {

        StringBuilder recipeIngredients = new StringBuilder();

        for (int i = 0; i < ingredients.size(); i++) {

            recipeIngredients.append(ingredients.get(i).getIngredient());
            recipeIngredients.append("/");
        }
        widgetIngredientsText = recipeIngredients.toString();

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        Gson gson = new Gson();

        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(MainActivity.RECIPE_POSITION, position);
        intent.putExtra(MainActivity.RECIPE_NAME, recipeName);
        intent.putExtra("RECIPES_WIDGET_GSON", gson.toJson(recipes));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
        views.setTextViewText(R.id.recipe_name_widget, recipeName.concat(" ").concat(context.getString(R.string.ingredients)));
        views.setTextViewText(R.id.recipe_ingredients_widget, widgetIngredientsText);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}