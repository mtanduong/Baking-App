package com.example.bakingapp.widgets;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Ingredient;

/**
 * service class that will update our ListView Widget
 */
public class listViewsService extends RemoteViewsService
{

    /**
     * @param intent intent that triggered this service
     * @return new ListViewsFactory Object with the appropriate implementation
     */
    public ListViewsFactory onGetViewFactory(Intent intent)
    {
        return new ListViewsFactory(this.getApplicationContext());
    }
}

class ListViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    private Context mContext;
    private Ingredient[] mIngredients;

    public ListViewsFactory(Context mContext)
    {
        this.mContext = mContext;
    }

    @Override
    public void onCreate()
    {

    }

    //Very Important,this is the place where the data is being changed each time by the adapter.
    @Override
    public void onDataSetChanged()
    {
        mIngredients = BakingAppProvider.mIngredients;
    }

    @Override
    public void onDestroy()
    {

    }

    @Override
    public int getCount()
    {
        if (mIngredients == null)
            return 0;
        return mIngredients.length;
    }

    /**
     * @param position position of current view in the ListView
     * @return a new RemoteViews object that will be one of many in the ListView
     */
    @Override
    public RemoteViews getViewAt(int position)
    {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_text_view_widget_layout);
        views.setTextViewText(R.id.text_view_recipe_widget, mIngredients[position].getmIngredient());
        return views;
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
