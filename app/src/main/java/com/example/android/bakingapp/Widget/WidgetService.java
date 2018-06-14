package com.example.android.bakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Retrofit.Model.Ingredient;

import java.util.ArrayList;

public class WidgetService extends RemoteViewsService {

    private ArrayList<Ingredient> widgetIngredients;
    private String widgetIngredientsText;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        widgetIngredients = intent.getParcelableArrayListExtra(MainActivity.RECIPE_INGREDIENTS);
        return new ListViewsFactory(getApplicationContext(), widgetIngredients);
    }

    class ListViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        final Context mContext;
        final ArrayList<Ingredient> mIngredients;

        ListViewsFactory(Context context, ArrayList<Ingredient> ingredients) {
            mContext = context;
            mIngredients = ingredients;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {

            StringBuilder recipeIngredients = new StringBuilder();

            for (int i = 0; i < mIngredients.size(); i++) {

                double quantity = mIngredients.get(i).getQuantity();
                String stringQuantity;
                if (quantity - (int) quantity != 0) {
                    stringQuantity = String.valueOf(quantity);
                } else {
                    stringQuantity = String.valueOf((int) quantity);
                }

                recipeIngredients.append(stringQuantity);
                recipeIngredients.append(" ");
                recipeIngredients.append(mIngredients.get(i).getMeasure());
                recipeIngredients.append(" ");
                recipeIngredients.append(mIngredients.get(i).getIngredient());
                recipeIngredients.append(",\n");
            }
            widgetIngredientsText = recipeIngredients.toString();
        }


        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_app_widget);
            views.setTextViewText(R.id.recipe_ingredients_widget, widgetIngredientsText);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}