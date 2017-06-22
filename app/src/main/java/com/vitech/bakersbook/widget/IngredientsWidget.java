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

public class IngredientsWidget extends AppWidgetProvider {

   public static void updateAppWidget(Context context,AppWidgetManager appWidgetManager,int appWidgetId){
        Log.d("widget","update called");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
       Intent adapterIntent  = new Intent(context, com.vitech.bakersbook.widget.WidgetListAdapterService.class);
       appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.ingredients_widget_list);
        remoteViews.setRemoteAdapter(R.id.ingredients_widget_list,adapterIntent);
        remoteViews.setViewVisibility(R.id.ingredients_widget_list, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.empty_view, View.GONE);
        Intent app = new Intent(context,BakersBookActivity.class);
        PendingIntent appLauncher = PendingIntent.getActivity(context,0,app,0);
        remoteViews.setPendingIntentTemplate(R.id.ingredients_widget_list,appLauncher);
        Log.d("widget","updated AppWidget");
        appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
    }

    private void initiateWidget(AppWidgetManager appWidgetManager, Context context, int appWidgetId){
        Log.d("widget","update initiated");
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

