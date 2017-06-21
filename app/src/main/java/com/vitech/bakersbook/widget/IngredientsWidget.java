package com.vitech.bakersbook.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.vitech.bakersbook.BakersBookActivity;
import com.vitech.bakersbook.R;
import com.vitech.bakersbook.widget.WidgetListAdapterService;

import org.json.JSONArray;
import org.json.JSONException;

import static com.vitech.bakersbook.widget.WidgetListAdapterService.ARG_INGREDIENTS;

public class IngredientsWidget extends AppWidgetProvider {

   public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                JSONArray ingredients, int appWidgetId)throws JSONException {
        Log.d("widget","update called");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
       //remoteViews.removeAllViews(R.id.widget);
        Intent adapterIntent  = new Intent(context, WidgetListAdapterService.class);
        adapterIntent.putExtra(ARG_INGREDIENTS,ingredients.toString());
        remoteViews.setRemoteAdapter(R.id.ingredients_widget_list,adapterIntent);
        remoteViews.setTextViewText(R.id.empty_view,"Empty Clicked");
        remoteViews.setViewVisibility(R.id.ingredients_widget_list, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.empty_view, View.GONE);
        Intent app = new Intent(context,BakersBookActivity.class);
        PendingIntent appLauncher = PendingIntent.getActivity(context,0,app,0);
        remoteViews.setPendingIntentTemplate(R.id.ingredients_widget_list,appLauncher);

        Log.d("widget","updated AppWidget");
        appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
    }

    void initiateWidget(AppWidgetManager appWidgetManager,Context context,int appWidgetId){
        Log.d("widget","update intiated");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.ingredients_widget);
        Intent app = new Intent(context,BakersBookActivity.class);
        PendingIntent appLauncher = PendingIntent.getActivity(context,0,app,0);
        remoteViews.setOnClickPendingIntent(R.id.widget,appLauncher);
        Log.d("widget","intiated AppWidget");
        remoteViews.setViewVisibility(R.id.ingredients_widget_list, View.GONE);

        remoteViews.setEmptyView(R.id.ingredients_list,R.id.empty_view);
        appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
Log.d("widget","ON UPDATE");
        for(int widget:appWidgetIds){
            initiateWidget(appWidgetManager,context,widget);
        }

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

