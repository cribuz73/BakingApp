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

    public WidgetProvider() {
    }

    public WidgetProvider(Context context) {
        mContext = context;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int recipeId, String recipeName, ArrayList<Ingredient> ingredients) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        // Set widget title and create an intent to launch RecipeDetailsActivity
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(MainActivity.RECIPE_ID, recipeId);
        intent.putExtra(MainActivity.RECIPE_NAME, recipeName);
        intent.putParcelableArrayListExtra(MainActivity.RECIPE_INGREDIENTS, ingredients);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.recipe_name_widget, pendingIntent);
        views.setTextViewText(R.id.recipe_name_widget, recipeName.concat(" ").concat(context.getString(R.string.ingredients)));

        // Set the list of ingredients for the selected recipe
        Intent adapterIntent = new Intent(context, WidgetService.class);
        //    adapterIntent.putParcelableArrayListExtra(MainActivity.RECIPE_INGREDIENTS, ingredients);
        views.setRemoteAdapter(R.id.recipe_ingredients_widget, adapterIntent);

        // Instruct the widget manager to update the widget
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

