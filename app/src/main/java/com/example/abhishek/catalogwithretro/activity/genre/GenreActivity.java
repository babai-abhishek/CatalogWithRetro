package com.example.abhishek.catalogwithretro.activity.genre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.adapters.GenreAdapter;
import com.example.abhishek.catalogwithretro.adapters.RecyclerEditDeleteClickActionListener;
import com.example.abhishek.catalogwithretro.model.Genre;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.GenreInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreActivity extends AppCompatActivity implements RecyclerEditDeleteClickActionListener {
    private static final String KEY_GENRES = "genres";
    private static final String KEY_SHOULD_RELOAD_ON_RESUME = "shouldLoadOnResume";


   // Button btn_genre_create, btn_genre_retrive, btn_genre_update, btn_genre_delete;

    private static final String TAG = "#";
   // private GenreAdapter genreAdapter;
    private RecyclerView recyclerView;
    List<Genre> genres = new ArrayList<>();
    GenreAdapter adapter ;
    private boolean shouldReloadOnResume = false;
    // ArrayList<String> list ;

    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);


        recyclerView = (RecyclerView) findViewById(R.id.genre_recycler_view);
        genres = new ArrayList<>();
        adapter=new GenreAdapter(genres,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

       // shouldReloadOnResume=false;
        if(savedInstanceState!=null){
            genres = (List<Genre>) Arrays.asList(new Gson().fromJson(savedInstanceState.getString(KEY_GENRES), (new Genre[0]).getClass()));
            shouldReloadOnResume=savedInstanceState.getBoolean(KEY_SHOULD_RELOAD_ON_RESUME);
            if(!shouldReloadOnResume){
                adapter.setGenres(genres);
            }

        } else {
            loadGenres();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(shouldReloadOnResume){
            loadGenres();
        }

        shouldReloadOnResume=false;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_GENRES, new Gson().toJson(genres));
        outState.putBoolean(KEY_SHOULD_RELOAD_ON_RESUME, shouldReloadOnResume);
    }


    private void loadGenres(){

        Call<List<Genre>> call = genreService.getAllGenres();
        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                genres = response.body();
                adapter.setGenres(genres);
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new_genre:
                shouldReloadOnResume=true;
                startActivity(new Intent(this, AddNewGenreActivity.class));
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAction(int position, int action) {
        Genre g = genres.get(position);
        switch (action) {
            case GenreAdapter.ACTION_EDIT:
                shouldReloadOnResume=true;
                //Toast.makeText(GenreActivity.this, g.getName() + " Edit", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GenreActivity.this, EditGenreActivity.class);
                intent.putExtra("genre_name",g.getName());
                intent.putExtra("genre_id", g.getId());
                startActivity(intent);
                break;

            case GenreAdapter.ACTION_DELETE:
                //  Toast.makeText(GenreActivity.this, g.getName() + " Delete", Toast.LENGTH_SHORT).show();
                Call<ResponseBody> call = genreService.deleteGenreEntry(g.getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // Toast.makeText(GenreActivity.this, "Sucessfully deleted entry",Toast.LENGTH_SHORT).show();
                        loadGenres();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG,t.toString());
                    }
                });
                break;
        }

    }

}
