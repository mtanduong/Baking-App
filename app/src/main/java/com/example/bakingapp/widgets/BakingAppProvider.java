package com.example.bakingapp.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Ingredient;

public class BakingAppProvider extends AppWidgetProvider {
    public static Ingredient[] mIngredients;

    public BakingAppProvider() {

    }

    //Updates the list view each time a user opens the IngredientsActivity, which updates the widget with latest ingredient list
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetIds[], Ingredient[] ingredientList) {

        mIngredients = ingredientList;
        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, listViewsService.class);
            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
            view.setRemoteAdapter(R.id.list_view_widget, intent);
            ComponentName component = new ComponentName(context, BakingAppProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view_widget);
            appWidgetManager.updateAppWidget(component, view);
        }
    }

    //Updates the widget when IngredientsActivity is created
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
