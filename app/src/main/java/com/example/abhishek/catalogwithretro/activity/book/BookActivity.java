package com.example.abhishek.catalogwithretro.activity.book;

import android.app.ProgressDialog;
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

    private static final String TAG = "#";
    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);
    private boolean shouldReloadOnResume = false;
    private List<Book> bookList;
    //private BookDetailsAdapter adapter;
    private BookListAdapter bookListAdapter;
    private RecyclerView recyclerView;

    private static final String KEY_BOOKS = "books";
    private static final String KEY_SHOULD_RELOAD_ON_RESUME = "shouldLoadOnResume";
    public static final String SELECTED_BOOK = "selectedBook";

    /*ProgressDialog mProgressDialog;*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

       /* mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
*/
        recyclerView = (RecyclerView) findViewById(R.id.book_recycler_view);
        bookList = new ArrayList<>();
        bookListAdapter = new BookListAdapter(bookList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(bookListAdapter);

        if(savedInstanceState!=null){
            bookList = (List<Book>) Arrays.asList(new Gson().fromJson(savedInstanceState.getString(KEY_BOOKS), (new Book[0]).getClass()));
            shouldReloadOnResume=savedInstanceState.getBoolean(KEY_SHOULD_RELOAD_ON_RESUME);
            if(!shouldReloadOnResume){
                bookListAdapter.setAdapter(bookList);
            }

        } else {
            loadBooks();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(shouldReloadOnResume){
            loadBooks();
        }
        shouldReloadOnResume = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_BOOKS,new Gson().toJson(bookList));
        outState.putBoolean(KEY_SHOULD_RELOAD_ON_RESUME,shouldReloadOnResume);

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

       /* mProgressDialog.setMessage("Downloading all books");
        mProgressDialog.show();
*/

        Call<List<Book>> call = bookService.getAllBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                bookList = response.body();
                Log.d(TAG," no of books received "+ bookList.size());
                bookListAdapter.setAdapter(bookList);
                /*mProgressDialog.cancel();*/
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e(TAG,t.toString());
               /* mProgressDialog.cancel();*/
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
}

