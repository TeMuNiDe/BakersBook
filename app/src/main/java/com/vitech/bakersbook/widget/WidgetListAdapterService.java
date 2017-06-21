package com.vitech.bakersbook.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.vitech.bakersbook.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;


public class WidgetListAdapterService extends RemoteViewsService {
    public static final String ARG_INGREDIENTS = "ingredients";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("wdiget","error");
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        try{
            Log.d("widget","onGetViewFactory");
        return new IngredientsListAdapter(new JSONArray(intent.getStringExtra(ARG_INGREDIENTS)),this.getApplicationContext());
        }catch (JSONException j){
            j.printStackTrace();
            Log.d("wdiget","error");
            return null;
        }
    }


}
class IngredientsListAdapter implements RemoteViewsService.RemoteViewsFactory{
private JSONArray ingredients;
private Context context;
     IngredientsListAdapter(JSONArray ingredients,Context context) {
        this.ingredients = ingredients;
         this.context = context;
         Log.d("widget","adapter_instance");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.length();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_item);
        try{
            Log.d("widget","getView");
            views.setTextViewText(R.id.ingredient_name,ingredients.getJSONObject(position).getString("ingredient"));
            views.setTextViewText(R.id.ingredient_quantity,String.format(Locale.getDefault(),"%.1f",ingredients.getJSONObject(position).getDouble("quantity")));
            views.setTextViewText(R.id.ingredient_measure,ingredients.getJSONObject(position).getString("measure"));
            Intent app = new Intent();
            views.setOnClickFillInIntent(R.id.ingredient_item,app);
        }catch (JSONException j){
            j.printStackTrace();
        }
        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

