package com.example.android.bakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Retrofit.Model.Ingredient;

import java.util.ArrayList;

public class WidgetService extends RemoteViewsService {

    private ArrayList<Ingredient> widgetIngredients;
    private String widgetIngredientsText;
    SharedPreferences preferences = this.getSharedPreferences("ingredients_widget", Context.MODE_PRIVATE);
    private String ingredientsString;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewsFactory(this.getApplicationContext());
    }

    class ListViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;

        public ListViewsFactory(Context context) {
            mContext = context;
        }


        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

            ingredientsString = preferences.getString("ingredients_string", widgetIngredientsText);

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
            views.setTextViewText(R.id.recipe_ingredients_widget, ingredientsString);

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