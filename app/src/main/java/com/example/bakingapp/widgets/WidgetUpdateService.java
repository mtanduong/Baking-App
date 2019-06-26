package com.example.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.example.bakingapp.activities.MainActivity;
import com.example.bakingapp.models.Ingredient;

import androidx.annotation.Nullable;

public class WidgetUpdateService extends IntentService {
    public static final String WIDGET_UPDATE_ACTION = "rodionkonioshko.com.bakingapp.update_widget";
    private Ingredient[]mIngredients;

    public WidgetUpdateService()
    {
        super("WidgetServiceUpdate");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        if (intent != null && intent.getAction().equals(WIDGET_UPDATE_ACTION))
        {
            Bundle bundle = intent.getBundleExtra(MainActivity.BUNDLE);
            Parcelable[] parcelables = bundle.getParcelableArray(MainActivity.INGREDIENTS);
            if (parcelables != null)
            {
                mIngredients = new Ingredient[parcelables.length];
                for (int i = 0; i < parcelables.length; i++)
                {
                    mIngredients[i] = (Ingredient) parcelables[i];
                }
            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppProvider.class));
            BakingAppProvider.updateAppWidget(this, appWidgetManager, appWidgetIds,mIngredients);
        }
    }
}
