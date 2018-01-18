package com.example.abhishek.catalogwithretro.activity.book;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.adapters.SortedBooksByGenreAdapter;
import com.example.abhishek.catalogwithretro.model.Book;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.BookInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SortedBooksByGenreActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tvGenreType;
    List<Book> bookList;
    SortedBooksByGenreAdapter adapter;
    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorted_books_by_genre);
        String genreId = getIntent().getStringExtra("genreId");

        tvGenreType = (TextView) findViewById(R.id.genre_type);
        tvGenreType.setText("List of all books by genre : "+getIntent().getStringExtra("genreName"));

        recyclerView = (RecyclerView) findViewById(R.id.recylerView_books_by_genre);
        bookList = new ArrayList<>();
        adapter = new SortedBooksByGenreAdapter(bookList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        loadAllBooksByGenre(genreId);

    }

    private void loadAllBooksByGenre(String genreId) {
        Call<List<Book>> call = bookService.getBooksByGenreId(genreId);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                bookList = response.body();
                Log.d("#"," no of selected author's books received "+ bookList.size());
                adapter.setBooks(bookList);
                /*mProgressDialog.cancel();*/
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("#",t.toString());
               /* mProgressDialog.cancel();*/
            }
        });

    }
}
