package com.example.abhishek.catalogwithretro.activity.book.GenreTypeSelection;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.activity.book.AddNewBookActivity;
import com.example.abhishek.catalogwithretro.activity.book.RecyclerClickListener;
import com.example.abhishek.catalogwithretro.activity.genre.AddNewGenreActivity;
import com.example.abhishek.catalogwithretro.model.Genre;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.GenreInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Select_genre_type_activity extends AppCompatActivity implements
        RecyclerClickListener {

    private RecyclerView recyclerView;
    List<Genre> genres = new ArrayList<>();
    Select_genre_type_adapter adapter ;
    FloatingActionButton fabNewGenreType;

    private boolean shouldReloadOnResume = false;
    private static final String KEY_GENRES = "genres";
    private static final String KEY_SHOULD_RELOAD_ON_RESUME = "shouldLoadOnResume";

    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_genre_type_activity);

        fabNewGenreType = (FloatingActionButton) findViewById(R.id.fab_add_new_genre);
        recyclerView = (RecyclerView) findViewById(R.id.select_genre_type_recycler_view);
        genres = new ArrayList<>();
        adapter=new Select_genre_type_adapter(genres, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        if(savedInstanceState!=null){
            genres = (List<Genre>) Arrays.asList(new Gson().fromJson(savedInstanceState.getString(KEY_GENRES), (new Genre[0]).getClass()));
            shouldReloadOnResume=savedInstanceState.getBoolean(KEY_SHOULD_RELOAD_ON_RESUME);
            if(!shouldReloadOnResume){
                adapter.setGenreTypes(genres);
            }

        } else {
            loadGenres();
        }

        fabNewGenreType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldReloadOnResume=true;
                startActivity(new Intent(Select_genre_type_activity.this, AddNewGenreActivity.class));

            }
        });
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
                adapter.setGenreTypes(genres);
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                Log.e("#", t.toString());
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
       // bundle.putSerializable("selectedGenreType", genres.get(position));
        intent.putExtras(bundle);
        setResult(AddNewBookActivity.REQUEST_CODE_GENRETYPE,intent);
        finish();
    }
}