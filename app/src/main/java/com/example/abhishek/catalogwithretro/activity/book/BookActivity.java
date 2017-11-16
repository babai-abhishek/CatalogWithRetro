package com.example.abhishek.catalogwithretro.activity.book;

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
import com.example.abhishek.catalogwithretro.adapters.BookAdapter;
import com.example.abhishek.catalogwithretro.adapters.RecyclerClickListener;
import com.example.abhishek.catalogwithretro.model.Book;
import com.example.abhishek.catalogwithretro.model.Genre;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.BookInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookActivity extends AppCompatActivity implements RecyclerClickListener{

    private static final String TAG = "BookActivity";
    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);
    private boolean shouldReloadOnResume = false;
    private List<Book> bookList;
    private BookAdapter adapter;
    private RecyclerView recyclerView;

    private static final String KEY_BOOKS = "books";
    private static final String KEY_SHOULD_RELOAD_ON_RESUME = "shouldLoadOnResume";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        recyclerView = (RecyclerView) findViewById(R.id.book_recycler_view);
        bookList = new ArrayList<>();
        adapter = new BookAdapter(bookList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        if(savedInstanceState!=null){
            bookList = Arrays.asList(new Gson().fromJson(savedInstanceState.getString(KEY_BOOKS), (new Book[0]).getClass()));
            shouldReloadOnResume=savedInstanceState.getBoolean(KEY_SHOULD_RELOAD_ON_RESUME);
            if(!shouldReloadOnResume){
                adapter.setAdapter(bookList);
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

    @Override
    public void onAction(int position, int action) {

        Book book = bookList.get(position);
        switch (action){
            case BookAdapter.ACTION_EDIT:
                shouldReloadOnResume=true;
                Intent intent = new Intent(BookActivity.this, EditBookActivity.class);
                intent.putExtra("bookName",book.getName());
                intent.putExtra("bookId",book.getId());
                intent.putExtra("bookLang",book.getLanguage());
                intent.putExtra("bookPublishDate",book.getPublished());
                intent.putExtra("bookPages",book.getPages());
                startActivity(intent);
                break;
            case BookAdapter.ACTION_DELETE:
                Call<ResponseBody> call = bookService.deleteBookEntry(book.getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                       // Toast.makeText(BookActivity.this, "Sucessfully deleted entry",Toast.LENGTH_SHORT).show();
                        loadBooks();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG,t.toString());
                    }
                });

                break;
        }

    }

    private void loadBooks(){

        Call<List<Book>> call = bookService.getAllBooks();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                bookList = response.body();
                Log.d(TAG," no of books received "+ bookList.size());
                adapter.setAdapter(bookList);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e(TAG,t.toString());
            }
        });


    }
}

