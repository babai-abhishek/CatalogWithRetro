package com.example.abhishek.catalogwithretro.activity.author;

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
import com.example.abhishek.catalogwithretro.adapters.AuthorAdapter;
import com.example.abhishek.catalogwithretro.adapters.RecyclerEditDeleteClickActionListener;
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

public class AuthorActivity extends AppCompatActivity implements RecyclerEditDeleteClickActionListener {

    private static final String KEY_AUTHORS = "authors";
    private static final String KEY_SHOULD_RELOAD_ON_RESUME = "shouldLoadOnResume";
    public static final String KEY_IS_AUTHOR_LOADED = "isAuthorLoaded";
    private boolean shouldReloadOnResume = false;

    private static final String ACTION_AUTHOR_LIST_API_SUCCESS = "com.example.abhishek.catalogwithretro.api.authors.all.result.success";
    private static final String ACTION_AUTHOR_LIST_API_FAILURE = "com.example.abhishek.catalogwithretro.api.authors.all.result.failure";

    private LocalBroadcastManager broadcastManager = null;
    ProgressDialog mProgressDialog;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case ACTION_AUTHOR_LIST_API_SUCCESS:
                    Toast.makeText(AuthorActivity.this, "Api Success", Toast.LENGTH_SHORT).show();
                    authorList = Arrays.asList((Author[]) intent.getParcelableArrayExtra(KEY_AUTHORS));
                    adapter.setAuthorList(authorList);
                    isAuthorLoaded = true;
                    postLoad();
                    break;


                case ACTION_AUTHOR_LIST_API_FAILURE:
                    Toast.makeText(AuthorActivity.this, "Api Failure", Toast.LENGTH_SHORT).show();
                    isAuthorLoaded = true;
                    postLoad();
                    break;
            }

        }
    };

    public String AUTHOR_NAME = "authName", AUTHOR_ID = "authId", AUTHOR_LANGUAGE = "authLang", AUTHOR_COUNTRY = "authCoun";

    private static final String TAG = "#";
    private RecyclerView recyclerView;
    List<Author> authorList;
    AuthorAdapter adapter;

    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);
    private boolean isAuthorLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);

        broadcastManager = LocalBroadcastManager.getInstance(AuthorActivity.this);

        //set-up dialog box
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        recyclerView = (RecyclerView) findViewById(R.id.author_recycler_view);
        authorList = new ArrayList<>();
        adapter = new AuthorAdapter(authorList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        if (savedInstanceState != null) {
            authorList = (List<Author>) Arrays.asList(new Gson().fromJson(savedInstanceState.getString(KEY_AUTHORS), (new Author[0]).getClass()));

            isAuthorLoaded = savedInstanceState.getBoolean(KEY_IS_AUTHOR_LOADED);
            if (!isAuthorLoaded) {
                showLoading();
            }


            shouldReloadOnResume = savedInstanceState.getBoolean(KEY_SHOULD_RELOAD_ON_RESUME);
            if (!shouldReloadOnResume) {
                adapter.setAuthorList(authorList);
            }
        } else {
            loadAuthors();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //register broadcastreceiver  with Actions
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_AUTHOR_LIST_API_SUCCESS);
        filter.addAction(ACTION_AUTHOR_LIST_API_FAILURE);
        broadcastManager.registerReceiver(broadcastReceiver, filter);

        if (shouldReloadOnResume) {
            loadAuthors();
        }
        shouldReloadOnResume = false;
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
        outState.putString(KEY_AUTHORS, new Gson().toJson(authorList));
        outState.putBoolean(KEY_SHOULD_RELOAD_ON_RESUME, shouldReloadOnResume);
        outState.putBoolean(KEY_IS_AUTHOR_LOADED, isAuthorLoaded);
    }

    private void loadAuthors() {

        isAuthorLoaded = false;
        showLoading();

        Call<List<Author>> call = authorService.getAllAuthors();
        call.enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                Intent intent = new Intent(ACTION_AUTHOR_LIST_API_SUCCESS);
                intent.putExtra(KEY_AUTHORS, response.body().toArray(new Author[0]));
                broadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {
                Log.e(TAG, t.toString());
                Intent intent = new Intent(ACTION_AUTHOR_LIST_API_FAILURE);
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
        switch (item.getItemId()) {
            case R.id.action_new_genre:
                shouldReloadOnResume = true;
                startActivity(new Intent(this, AddNewAuthorActivity.class));
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAction(int position, int action) {

        Author author = authorList.get(position);
        Log.d(TAG, "id " + authorList.get(position).getId());
        switch (action) {
            case AuthorAdapter.ACTION_EDIT:
                shouldReloadOnResume = true;
                Intent intent = new Intent(AuthorActivity.this, EditAuthorActivity.class);
                intent.putExtra(AUTHOR_NAME, author.getName());
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
                        Log.e(TAG, t.toString());
                    }
                });
                break;
        }

    }

    private void showLoading() {
        adapter.setLoading(true);
        if (mProgressDialog.isShowing())
            return;
        mProgressDialog.setMessage("Loading.......");
        mProgressDialog.show();
    }

    private void hideLoading() {
        adapter.setLoading(false);
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    private void postLoad() {
        if (isAuthorLoaded)
            hideLoading();
    }
}
