package com.vitech.bakersbook.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.vitech.bakersbook.BakersBookActivity;
import com.vitech.bakersbook.R;

import static com.vitech.bakersbook.widget.WidgetListAdapterService.ARG_INGREDIENTS;

public class IngredientsWidget extends AppWidgetProvider {

   public void updateAppWidget(Context context,AppWidgetManager appWidgetManager,int appWidgetId){
        Log.d("widget","update called");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
       if(preferences.contains(ARG_INGREDIENTS)) {
           Intent adapterIntent = new Intent(context, com.vitech.bakersbook.widget.WidgetListAdapterService.class);
           remoteViews.setRemoteAdapter(R.id.ingredients_widget_list, adapterIntent);
           remoteViews.setViewVisibility(R.id.ingredients_widget_list, View.VISIBLE);
           remoteViews.setViewVisibility(R.id.empty_view, View.GONE);
       }else {
           remoteViews.setViewVisibility(R.id.ingredients_widget_list, View.GONE);
           remoteViews.setViewVisibility(R.id.empty_view, View.VISIBLE);
       }
        remoteViews.setViewVisibility(R.id.ingredients_widget_list, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.empty_view, View.GONE);
        Intent app = new Intent(context,BakersBookActivity.class);
        PendingIntent appLauncher = PendingIntent.getActivity(context,0,app,0);
        remoteViews.setPendingIntentTemplate(R.id.ingredients_widget_list,appLauncher);
        Log.d("widget","updated AppWidget");
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.ingredients_widget_list);
        appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("widget","ON UPDATE");
        for(int widget:appWidgetIds){
            updateAppWidget(context,appWidgetManager,widget);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            updateAppWidget(context,AppWidgetManager.getInstance(context),intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,0));
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

