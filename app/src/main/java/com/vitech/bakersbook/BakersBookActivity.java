package com.vitech.bakersbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.vitech.bakersbook.adapters.RecipeListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.vitech.bakersbook.RecipeActivity.EXTRA_RECIPE;
import static com.vitech.bakersbook.RecipeActivity.EXTRA_RECIPES;

public class BakersBookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    ProgressDialog dialog;
    @BindView(R.id.recipe_list)
    RecyclerView recipeList;
    private static final int LOADER_ID=1258;
    private  static  final String KEY_RESPONSE = "response_data";
    String responseData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakers_book);
        ButterKnife.bind(this);

        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(KEY_RESPONSE)){
                responseData = savedInstanceState.getString(KEY_RESPONSE);
            }
        }
        dialog = new ProgressDialog(BakersBookActivity.this);
        dialog.setMessage("Loading...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        LoaderManager manager  = getSupportLoaderManager();
        Bundle args  = new Bundle();
        manager.restartLoader(LOADER_ID,args,this);



    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(responseData==null) {

                    dialog.show();
                    forceLoad();
                }else {
                    deliverResult(responseData);
                }

            }

            @Override
            public String loadInBackground() {
                String response;
                try {
                   OkHttpClient client  = new OkHttpClient.Builder().followRedirects(true).followSslRedirects(true).build();

                    Request request = new Request.Builder().url(getResources().getString(R.string.resource_url)).get().build();
                    response = client.newCall(request).execute().body().string();
                }catch (Exception e) {
                    e.printStackTrace();
                    if (e instanceof UnknownHostException) {
                        response = getResources().getString(R.string.no_internet);
                    } else if (e instanceof SocketException) {
                        response = getResources().getString(R.string.connection_error);
                    } else if (e instanceof SocketTimeoutException) {
                        response = getResources().getString(R.string.connection_timed_out);
                    } else {
                        response = getResources().getString(R.string.unknown_error);
                    }
                }
                return response;

            }

            @Override
            public void deliverResult(String data) {
                responseData = data;
                super.deliverResult(data);
            }
        };


    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
dialog.cancel();
        try{
            final JSONArray recipes  = new JSONArray(data);
            recipeList.setAdapter(new RecipeListAdapter(this, recipes, new RecipeListAdapter.OnRecipeClickListener() {
                @Override
                public void onRecipeClicked(JSONObject object) {
                    startActivity(new Intent(BakersBookActivity.this,RecipeActivity.class).putExtra(EXTRA_RECIPES,recipes.toString()).putExtra(EXTRA_RECIPE,object.toString()));
                }
            }));
            recipeList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        }catch (JSONException e){
            responseData = null;
            Toast.makeText(this,data,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_RESPONSE,responseData);
        super.onSaveInstanceState(outState);
    }
}
