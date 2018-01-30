package com.example.abhishek.catalogwithretro.activity.genre;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.activity.book.BookActivity;
import com.example.abhishek.catalogwithretro.adapters.GenreAdapter;
import com.example.abhishek.catalogwithretro.adapters.RecyclerEditDeleteClickActionListener;
import com.example.abhishek.catalogwithretro.model.Book;
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
    public static final String KEY_IS_GENRE_LOADED = "isGenreLoaded";

    private static final String ACTION_GENRE_LIST_API_SUCCESS="com.example.abhishek.catalogwithretro.api.genres.all.result.success";
    private static final String ACTION_GENRE_LIST_API_FAILURE="com.example.abhishek.catalogwithretro.api.genres.all.result.failure";

    private LocalBroadcastManager broadcastManager = null;
    ProgressDialog mProgressDialog;


    private static final String TAG = "#";
    private RecyclerView recyclerView;
    List<Genre> genres = new ArrayList<>();
    GenreAdapter adapter ;
    private boolean shouldReloadOnResume = false;
    private boolean isGenreLoaded = false;

    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()){

                    //action taken on sucessful genre list loading
                case ACTION_GENRE_LIST_API_SUCCESS:
                    Toast.makeText(GenreActivity.this, "Api Success", Toast.LENGTH_SHORT).show();
                    genres= Arrays.asList((Genre[])intent.getParcelableArrayExtra(KEY_GENRES));
                    adapter.setGenreList(genres);
                    isGenreLoaded = true;
                    postLoad();
                    break;

                    //action taken on un-sucessful genre list loading
                case ACTION_GENRE_LIST_API_FAILURE:
                    Toast.makeText(GenreActivity.this, "Api Failure", Toast.LENGTH_SHORT).show();
                    isGenreLoaded = true;
                    postLoad();
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

        broadcastManager = LocalBroadcastManager.getInstance(GenreActivity.this);

        //set up the "loading...." dialog box
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        recyclerView = (RecyclerView) findViewById(R.id.genre_recycler_view);
        genres = new ArrayList<>();
        adapter=new GenreAdapter(genres,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        if(savedInstanceState!=null){
            genres = (List<Genre>) Arrays.asList(new Gson().fromJson(savedInstanceState.getString(KEY_GENRES), (new Genre[0]).getClass()));

            isGenreLoaded = savedInstanceState.getBoolean(KEY_IS_GENRE_LOADED);

            //if genre list loading is still not completed from server
            //then show the dialog box
            if(!isGenreLoaded){
                showLoading();
            }

            shouldReloadOnResume=savedInstanceState.getBoolean(KEY_SHOULD_RELOAD_ON_RESUME);
            if(!shouldReloadOnResume){
                adapter.setGenreList(genres);
            }

        } else {
            loadGenres();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //register broadcastreceiver  with Actions
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_GENRE_LIST_API_SUCCESS);
        filter.addAction(ACTION_GENRE_LIST_API_FAILURE);
        broadcastManager.registerReceiver(broadcastReceiver, filter);

        if(shouldReloadOnResume){
            loadGenres();
        }

        shouldReloadOnResume=false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        //un-register broadcastreceiver
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_GENRES, new Gson().toJson(genres));
        outState.putBoolean(KEY_SHOULD_RELOAD_ON_RESUME, shouldReloadOnResume);
        outState.putBoolean(KEY_IS_GENRE_LOADED, isGenreLoaded);
    }


    private void loadGenres(){

        isGenreLoaded = false;
        showLoading();

        Call<List<Genre>> call = genreService.getAllGenres();
        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                Intent intent = new Intent(ACTION_GENRE_LIST_API_SUCCESS);
                intent.putExtra(KEY_GENRES,response.body().toArray(new Genre[0]));
                broadcastManager.sendBroadcast(intent);

            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                Log.e(TAG, t.toString());
                Intent intent = new Intent(ACTION_GENRE_LIST_API_FAILURE);
                broadcastManager.sendBroadcast(intent);
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
                shouldReloadOnResume = true;
                //Toast.makeText(GenreActivity.this, g.getName() + " Edit", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GenreActivity.this, EditGenreActivity.class);
                intent.putExtra("genre_name", g.getName());
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
                        Log.e(TAG, t.toString());
                    }
                });
                break;
        }
    }


        private void showLoading() {
            if (mProgressDialog.isShowing())
                return;
            mProgressDialog.setMessage("Loading.......");
            mProgressDialog.show();
        }

        private void hideLoading() {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }

        private void postLoad() {
            if (isGenreLoaded)
                hideLoading();
        }


    }

