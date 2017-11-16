package com.example.abhishek.catalogwithretro.activity.author;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.activity.genre.AddNewGenreActivity;
import com.example.abhishek.catalogwithretro.adapters.AuthorAdapter;
import com.example.abhishek.catalogwithretro.adapters.GenreAdapter;
import com.example.abhishek.catalogwithretro.adapters.RecyclerClickListener;
import com.example.abhishek.catalogwithretro.model.Author;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.AuthorInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorActivity extends AppCompatActivity implements RecyclerClickListener{

    private static final String KEY_AUTHORS = "authors";
    private static final String KEY_SHOULD_RELOAD_ON_RESUME = "shouldLoadOnResume";
    private boolean shouldReloadOnResume = false;

    public String AUTHOR_NAME = "authName", AUTHOR_ID = "authId", AUTHOR_LANGUAGE = "authLang", AUTHOR_COUNTRY = "authCoun";

    private static final String TAG = "#";
    private RecyclerView recyclerView;
    List<Author> authorList;
    AuthorAdapter adapter;

    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);

        recyclerView = (RecyclerView) findViewById(R.id.author_recycler_view);
        authorList = new ArrayList<>();
        adapter=new AuthorAdapter(authorList,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        if(savedInstanceState!=null){
            authorList = Arrays.asList(new Gson().fromJson(savedInstanceState.getString(KEY_AUTHORS),(new Author[0]).getClass()));
            shouldReloadOnResume = savedInstanceState.getBoolean(KEY_SHOULD_RELOAD_ON_RESUME);
            if(!shouldReloadOnResume){
                adapter.setAuthors(authorList);
            }
        }
        else {
            loadAuthors();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(shouldReloadOnResume){
            loadAuthors();
        }
        shouldReloadOnResume = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_AUTHORS, new Gson().toJson(authorList));
        outState.putBoolean(KEY_SHOULD_RELOAD_ON_RESUME, shouldReloadOnResume);
    }

    private void loadAuthors() {
        Call<List<Author>> call = authorService.getAllAuthors();
        call.enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                authorList = response.body();
                adapter.setAuthors(authorList);
            }

            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {
                Log.e(TAG,t.toString());
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
                startActivity(new Intent(this, AddNewAuthorActivity.class));
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAction(int position, int action) {

        Author author = authorList.get(position);
        Log.d(TAG,"id "+authorList.get(position).getId());
        switch (action){
            case AuthorAdapter.ACTION_EDIT:
                shouldReloadOnResume = true;
                Intent intent = new Intent(AuthorActivity.this,EditAuthorActivity.class);
                intent.putExtra(AUTHOR_NAME,author.getName());
                intent.putExtra(AUTHOR_ID, author.getId());
                intent.putExtra(AUTHOR_LANGUAGE, author.getLanguage());
                intent.putExtra(AUTHOR_COUNTRY, author.getCountry());
                startActivity(intent);
                break;
            case AuthorAdapter.ACTION_DELETE:
                Call<ResponseBody> call = authorService.deleteAuthorEntry(author.getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                      //  Toast.makeText(AuthorActivity.this, "Sucessfully deleted entry",Toast.LENGTH_SHORT).show();
                        loadAuthors();
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
