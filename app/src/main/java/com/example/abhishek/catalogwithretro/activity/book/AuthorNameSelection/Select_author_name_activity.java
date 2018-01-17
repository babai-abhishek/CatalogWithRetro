package com.example.abhishek.catalogwithretro.activity.book.AuthorNameSelection;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.activity.author.AddNewAuthorActivity;
import com.example.abhishek.catalogwithretro.activity.book.AddNewBookActivity;
import com.example.abhishek.catalogwithretro.activity.book.GenreTypeSelection.Select_genre_type_activity;
import com.example.abhishek.catalogwithretro.activity.book.GenreTypeSelection.Select_genre_type_adapter;
import com.example.abhishek.catalogwithretro.activity.book.SelectionListener;
import com.example.abhishek.catalogwithretro.activity.genre.AddNewGenreActivity;
import com.example.abhishek.catalogwithretro.model.Author;
import com.example.abhishek.catalogwithretro.model.Genre;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.AuthorInterface;
import com.example.abhishek.catalogwithretro.network.GenreInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Select_author_name_activity extends AppCompatActivity
                implements SelectionListener{

    private RecyclerView recyclerView;
    List<Author> authors;
    Select_author_name_adapter adapter ;
    FloatingActionButton fabNewAuthorName;

    private boolean shouldReloadOnResume = false;
    private static final String KEY_AUTHORS = "authors";
    private static final String KEY_SHOULD_RELOAD_ON_RESUME = "shouldLoadOnResume";

    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_author_name_activity);

        recyclerView = (RecyclerView) findViewById(R.id.select_author_name_recycler_view);
        authors = new ArrayList<>();
        fabNewAuthorName = (FloatingActionButton) findViewById(R.id.fab_add_new_author);

        adapter = new Select_author_name_adapter(authors, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        if(savedInstanceState!=null){
            authors = (List<Author>) Arrays.asList(new Gson().
                    fromJson(savedInstanceState.getString(KEY_AUTHORS),
                            (new Author[0]).getClass()));
            shouldReloadOnResume=savedInstanceState.getBoolean(KEY_SHOULD_RELOAD_ON_RESUME);
            if(!shouldReloadOnResume){
                adapter.setAuthorNames(authors);
            }

        } else {
            loadAuthors();
        }

        fabNewAuthorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldReloadOnResume=true;
                startActivity(new Intent(Select_author_name_activity.this, AddNewAuthorActivity.class));

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_AUTHORS, new Gson().toJson(authors));
        outState.putBoolean(KEY_SHOULD_RELOAD_ON_RESUME, shouldReloadOnResume);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(shouldReloadOnResume){
            loadAuthors();
        }

        shouldReloadOnResume=false;
    }

    private void loadAuthors() {
        Call<List<Author>> call = authorService.getAllAuthors();
        call.enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                authors = response.body();
                adapter.setAuthorNames(authors);
            }

            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {
                Log.e("#",t.toString());
            }
        });
    }

    @Override
    public void onSelect(int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedAuthorName", authors.get(position));
        intent.putExtras(bundle);
        setResult(AddNewBookActivity.REQUEST_CODE_AUTHORNAME,intent);
        finish();
    }
}
