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

    public BakingAppProvider()
    {

    }

    /**
     * method will update the ListView each time the user opens the IngredientsActivity,
     * meaning that the widget will always show the last IngredientsActivity Ingredients[] that the user seen.
     * @param context app context
     * @param appWidgetManager  app WidgetManger
     * @param appWidgetIds ids which will be updated
     * @param ingredients the ingredients that will fill the ListView
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetIds[], Ingredient[] ingredients)
    {
        mIngredients = ingredients;
        for (int appWidgetId : appWidgetIds)
        {
            Intent intent = new Intent(context, listViewsService.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
            views.setRemoteAdapter(R.id.list_view_widget, intent);
            ComponentName component = new ComponentName(context, BakingAppProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view_widget);
            appWidgetManager.updateAppWidget(component, views);
        }

    }

    /**
     * the widget will update itself each time the IngredientsActivity will open,meaning that this method
     * is unnecessary in our implementation.
     * @param context app context
     * @param appWidgetManager the application WidgetManager
     * @param appWidgetIds ids which will be updated
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onEnabled(Context context)
    {

    }

    @Override
    public void onDisabled(Context context)
    {

    }
}
