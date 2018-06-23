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

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "1";
    private static List<Ingredient> ingredientList;
    private Context mContext;
    private String recipeName;
    private static String widgetIngredientsText;

    public WidgetProvider() {
    }

    public WidgetProvider(Context context) {
        mContext = context;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int position, String recipeName, ArrayList<Ingredient> ingredients, ArrayList<Recipe> recipes) {

        StringBuilder recipeIngredients = new StringBuilder();

        for (int i = 0; i < ingredients.size(); i++) {

            recipeIngredients.append(ingredients.get(i).getIngredient());
            recipeIngredients.append("/");
        }
        widgetIngredientsText = recipeIngredients.toString();

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        // Set widget title and create an intent to launch RecipeDetailsActivity
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(MainActivity.RECIPE_POSITION, position);
        intent.putExtra(MainActivity.RECIPE_NAME, recipeName);
        //   intent.putParcelableArrayListExtra(MainActivity.ALL_RECIPES, recipes);
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
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}