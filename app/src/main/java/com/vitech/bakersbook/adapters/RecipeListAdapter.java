package com.vitech.bakersbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vitech.bakersbook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;



public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeHolder> {
    private Context context;
    private JSONArray recipes;
    private OnRecipeClickListener onRecipeClickListener;

    public RecipeListAdapter(Context context, JSONArray recipes,OnRecipeClickListener listener) {
        this.context = context;
        this.recipes = recipes;
        this.onRecipeClickListener  = listener;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeHolder(LayoutInflater.from(context).inflate(R.layout.recipe_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {

        try {
            holder.setRecipe(recipes.getJSONObject(position));
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return recipes.length();
    }

    class RecipeHolder extends RecyclerView.ViewHolder{
        JSONObject recipe;
        @BindView(R.id.recipe_image)ImageView recipeImage;
        @BindView(R.id.recipe_title)TextView recipeTitle;
        @BindView(R.id.recipe_servings)TextView recipeServings;
        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecipeClickListener.onRecipeClicked(recipe);

                }
            });
        }

         void setRecipe(JSONObject recipe) throws JSONException {
            this.recipe = recipe;
            recipeTitle.setText(recipe.getString("name"));
            recipeServings.setText(context.getResources().getString(R.string.text_servings, recipe.getInt("servings")));
            try {
                Picasso.with(context).load(recipe.getString("image")).placeholder(R.drawable.ic_cookies).error(R.drawable.ic_cookies).into(recipeImage);
            }catch (IllegalArgumentException e){
                recipeImage.setImageResource(R.drawable.ic_cookies);
            }
        }
    }
  public   interface OnRecipeClickListener{
        void onRecipeClicked(JSONObject object);
    }
}
