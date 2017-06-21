package com.vitech.bakersbook.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NavRecipeAdapter extends RecyclerView.Adapter<NavRecipeAdapter.RecipeHolder> {
    private JSONArray recipes;
    private RecipeListAdapter.OnRecipeClickListener onRecipeClickListener;

    public void setOnRecipeClickListener(RecipeListAdapter.OnRecipeClickListener onRecipeClickListener) {
        this.onRecipeClickListener = onRecipeClickListener;
    }

    public NavRecipeAdapter(JSONArray recipes) {
        this.recipes = recipes;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView recipeView = new TextView(parent.getContext());
        recipeView.setGravity(Gravity.CENTER);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
        recipeView.setLayoutParams(layoutParams);
        recipeView.setTextColor(Color.BLACK);
        return new RecipeHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        try {
            holder.recipeView.setText(recipes.getJSONObject(position).getString("name"));
            holder.object = recipes.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return recipes.length();
    }

    class RecipeHolder extends RecyclerView.ViewHolder {
        TextView recipeView;
        JSONObject object;

        public RecipeHolder(View itemView) {
            super(itemView);
            recipeView = (TextView) itemView;
            recipeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onRecipeClickListener!=null){
                        onRecipeClickListener.onRecipeClicked(object);
                    }
                }
            });


        }
    }
}