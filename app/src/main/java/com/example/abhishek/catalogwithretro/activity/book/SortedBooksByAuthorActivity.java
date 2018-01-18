package com.example.abhishek.catalogwithretro.activity.book;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.adapters.SortedBooksByAuthorAdapter;
import com.example.abhishek.catalogwithretro.model.Book;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.BookInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SortedBooksByAuthorActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tvAuthorName;
    List<Book> bookList;
    SortedBooksByAuthorAdapter adapter;
    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorted_books_by_author);

        String authorId = getIntent().getStringExtra("authorId");

        tvAuthorName = (TextView) findViewById(R.id.author_name);
        tvAuthorName.setText("List of all books by author : "+getIntent().getStringExtra("authorName"));

        recyclerView = (RecyclerView) findViewById(R.id.recylerView_books_by_author);
        bookList = new ArrayList<>();
        adapter = new SortedBooksByAuthorAdapter(bookList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        loadAllBooksByAuthor(authorId);

    }

    private void loadAllBooksByAuthor(String authorId) {
        Call<List<Book>> call = bookService.getBooksByAuthorId(authorId);
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
