package com.vitech.bakersbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitech.bakersbook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientHolder> {
private Context context;
private JSONArray ingredients;

    public IngredientListAdapter(Context context, JSONArray ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IngredientHolder(LayoutInflater.from(context).inflate(R.layout.ingredient_item,parent,false));
    }

    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {
              try{
                  holder.bindIngredient(ingredients.getJSONObject(position));
              }catch (JSONException j){
                  j.printStackTrace();
              }

    }

    @Override
    public int getItemCount() {
        return ingredients.length();
    }

 class IngredientHolder extends RecyclerView.ViewHolder{
         @BindView(R.id.ingredient_name)TextView ingredientName;
         @BindView(R.id.ingredient_quantity)TextView ingredientQuantity;
         @BindView(R.id.ingredient_measure)TextView ingredientMeasure;
         IngredientHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        void bindIngredient(JSONObject ingredient)throws JSONException{
            ingredientName.setText(ingredient.getString("ingredient"));
            ingredientQuantity.setText(String.format(Locale.getDefault(),"%.1f",ingredient.getDouble("quantity")));
            ingredientMeasure.setText(ingredient.getString("measure"));
        }
    }
}
