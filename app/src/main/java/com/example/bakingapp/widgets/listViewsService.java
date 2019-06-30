package com.example.bakingapp.widgets;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Ingredient;

public class listViewsService extends RemoteViewsService {

    public ListViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewsFactory(this.getApplicationContext());
    }
}

class ListViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Ingredient[] ingredientList;
    private Context context;

    public ListViewsFactory(Context context)
    {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged()
    {
        ingredientList = BakingAppProvider.mIngredients;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {

        if (ingredientList == null)
            return 0;
        return ingredientList.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.recipe_text_view_widget_layout);
        view.setTextViewText(R.id.text_view_recipe_widget, ingredientList[position].getmIngredient());
        return view;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        return null;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }
}
