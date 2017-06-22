package com.vitech.bakersbook;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.vitech.bakersbook.adapters.IngredientListAdapter;
import com.vitech.bakersbook.adapters.InstructionListAdapter;
import com.vitech.bakersbook.adapters.NavRecipeAdapter;
import com.vitech.bakersbook.adapters.RecipeListAdapter;
import com.vitech.bakersbook.widget.IngredientsWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vitech.bakersbook.InstructionActivity.ARG_CURRENT_INSTRUCTION;
import static com.vitech.bakersbook.InstructionActivity.ARG_INSTRUCTION_SET;
import static com.vitech.bakersbook.InstructionFragment.ARG_INSTRUCTION;
import static com.vitech.bakersbook.widget.WidgetListAdapterService.ARG_INGREDIENTS;

public class RecipeActivity extends AppCompatActivity {
@BindView(R.id.ingredients_list)
 RecyclerView ingredientsList;
@BindView(R.id.instructions_list)
 RecyclerView instructionList;
@BindView(R.id.all_recipes)
 RecyclerView allRecipes;
@BindView(R.id.recipe_drawer)
 DrawerLayout recipeDrawer;
@Nullable
@BindView(R.id.instruction_container)
 FrameLayout instructionContainer;
 private ActionBarDrawerToggle toggle;
private static boolean TABLET_MODE;
public static final String EXTRA_RECIPE = "recipe";
public static final String EXTRA_RECIPES = "recipes";
private static final String ARG_APP_STATE = "app_state";

private Bundle appState;

private JSONArray ingredients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        TABLET_MODE = instructionContainer!=null;
        appState = getIntent().getExtras();
        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(ARG_APP_STATE)){
                appState = savedInstanceState.getBundle(ARG_APP_STATE);
            }
        }
        setContentView(appState);
    }

    @OnClick(R.id.add_tohomeScreen)
    void addToHomeScreenWidget(View v ){
        AppWidgetManager manager = AppWidgetManager.getInstance(this) ;
        int[] widgets = manager.getAppWidgetIds(new ComponentName(this,IngredientsWidget.class));

        for(int widget:widgets){
            Log.d("widget","clicked_recipe");
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(ARG_INGREDIENTS,ingredients.toString()).commit();
            Intent widgetUpdater = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            widgetUpdater.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widget);
            sendBroadcast(widgetUpdater);
        }

        Toast.makeText(this,R.string.message_widget_added,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        appState = intent.getExtras();
        setContentView(appState);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }


    private void setContentView(Bundle intent){
        recipeDrawer.closeDrawer(GravityCompat.START);
       toggle = new ActionBarDrawerToggle(this,recipeDrawer,R.string.open_drawer,R.string.close_drawer);
        recipeDrawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);




        try{
            final  JSONObject recipe = new JSONObject(intent.getString(EXTRA_RECIPE));
            final JSONArray recipes = new JSONArray(intent.getString(EXTRA_RECIPES));
            final JSONArray steps = recipe.getJSONArray("steps");
            ingredients = recipe.getJSONArray("ingredients");
            setTitle(recipe.getString("name"));
            NavRecipeAdapter adapter = new NavRecipeAdapter(recipes);
            allRecipes.setAdapter(adapter);
            allRecipes.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            adapter.setOnRecipeClickListener(new RecipeListAdapter.OnRecipeClickListener() {
                @Override
                public void onRecipeClicked(JSONObject object) {
                    startActivity(new Intent(RecipeActivity.this,RecipeActivity.class).putExtra(EXTRA_RECIPES,recipes.toString()).putExtra(EXTRA_RECIPE,object.toString()));

                }
            });
            ingredientsList.setAdapter(new IngredientListAdapter(this, ingredients));
            instructionList.setAdapter(new InstructionListAdapter(recipe.getJSONArray("steps")));
            ingredientsList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            instructionList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });

            ((InstructionListAdapter)instructionList.getAdapter()).setOnInstructionClickedListener(new InstructionListAdapter.OnInstructionClickedListener() {
                @Override
                public void OnInstructionClicked(int position) {
                    Bundle instruction = new Bundle();
                    try {
                        instruction.putString(ARG_INSTRUCTION, steps.getJSONObject(position).toString());
                        instruction.putString(ARG_INSTRUCTION_SET, steps.toString());
                        instruction.putInt(ARG_CURRENT_INSTRUCTION,position);
                        if (TABLET_MODE) {
                            InstructionFragment instructionFragment = new InstructionFragment();
                            instructionFragment.setArguments(instruction);
                            getSupportFragmentManager().beginTransaction().replace(R.id.instruction_container,instructionFragment).commit();
                        } else {
                            startActivity(new Intent(RecipeActivity.this, InstructionActivity.class).putExtras(instruction));
                        }
                    }catch (JSONException j){
                        j.printStackTrace();
                    }
                }
            });
        }catch (JSONException j){
            j.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(ARG_APP_STATE,appState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return true;
    }


}
