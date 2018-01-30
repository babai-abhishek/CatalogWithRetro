package com.example.abhishek.catalogwithretro.activity.book;

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
import com.example.abhishek.catalogwithretro.adapters.BookListAdapter;
import com.example.abhishek.catalogwithretro.model.Book;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.BookInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookActivity extends AppCompatActivity implements RecyclerClickListener {

    private static final String TAG = BookActivity.class.getSimpleName();

    private static final String ACTION_BOOK_LIST_API_SUCCESS="com.example.abhishek.catalogwithretro.api.books.all.result.success";
    private static final String ACTION_BOOK_LIST_API_FAILURE="com.example.abhishek.catalogwithretro.api.books.all.result.failure";


    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);
    private boolean shouldReloadOnResume = false;
    private List<Book> bookList;


    private BookListAdapter bookListAdapter;
    private RecyclerView recyclerView;

    private static final String KEY_SHOULD_RELOAD_ON_RESUME = "shouldLoadOnResume";
    private static final String KEY_BOOKS="books";
    public static final String KEY_IS_BOOK_LOADED = "isBookLoaded";

    public static final String SELECTED_BOOK = "selectedBook";

    private LocalBroadcastManager broadcastManager = null;

    ProgressDialog mProgressDialog;
    boolean isBookLoaded = false;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case ACTION_BOOK_LIST_API_SUCCESS:
                    Toast.makeText(BookActivity.this, "Api Success", Toast.LENGTH_SHORT).show();
                    bookList= Arrays.asList((Book[])intent.getParcelableArrayExtra(KEY_BOOKS));
                    bookListAdapter.setBookList(bookList);
                    isBookLoaded = true;
                    postLoad();
                    break;

                case ACTION_BOOK_LIST_API_FAILURE:
                    Toast.makeText(BookActivity.this, "Api Failure", Toast.LENGTH_SHORT).show();
                    isBookLoaded = true;
                    postLoad();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        broadcastManager = LocalBroadcastManager.getInstance(BookActivity.this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        recyclerView = (RecyclerView) findViewById(R.id.book_recycler_view);
        bookList = new ArrayList<>();
        bookListAdapter = new BookListAdapter(bookList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(bookListAdapter);

        if(savedInstanceState!=null){
            bookList = (List<Book>) Arrays.asList(new Gson().fromJson(savedInstanceState.getString(KEY_BOOKS), (new Book[0]).getClass()));
            isBookLoaded = savedInstanceState.getBoolean(KEY_IS_BOOK_LOADED);
            if(!isBookLoaded){
                showLoading();
            }
            shouldReloadOnResume=savedInstanceState.getBoolean(KEY_SHOULD_RELOAD_ON_RESUME);
            if(!shouldReloadOnResume){
                bookListAdapter.setBookList(bookList);
            }

        } else {
            loadBooks();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_BOOK_LIST_API_SUCCESS);
        filter.addAction(ACTION_BOOK_LIST_API_FAILURE);
        broadcastManager.registerReceiver(broadcastReceiver, filter);
        if(shouldReloadOnResume){
            loadBooks();
        }
        shouldReloadOnResume = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_BOOKS,new Gson().toJson(bookList));
        outState.putBoolean(KEY_SHOULD_RELOAD_ON_RESUME,shouldReloadOnResume);
        outState.putBoolean(KEY_IS_BOOK_LOADED, isBookLoaded);

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
                startActivity(new Intent(this, AddNewBookActivity.class));
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void loadBooks(){
        isBookLoaded = false;
        showLoading();

        Call<List<Book>> call = bookService.getAllBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                //bookList = response.body();
                Log.d(TAG," no of books received "+ bookList.size());
                //bookListAdapter.setAdapter(bookList);
                Intent intent = new Intent(ACTION_BOOK_LIST_API_SUCCESS);
                intent.putExtra(KEY_BOOKS,response.body().toArray(new Book[0]));
                broadcastManager.sendBroadcast(intent);

            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e(TAG,t.toString());
                Intent intent = new Intent(ACTION_BOOK_LIST_API_FAILURE);
                broadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        shouldReloadOnResume = true;
        Intent intent = new Intent(this, BookDetailsActivity.class);
        intent.putExtra(SELECTED_BOOK,bookList.get(position));
        startActivity(intent);
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

        if (isBookLoaded)
            hideLoading();
    }
}

