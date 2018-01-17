package com.example.abhishek.catalogwithretro.activity.book;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.model.Book;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.BookInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBookActivity extends AppCompatActivity {

    EditText etBookName, etBookLang, etBookPublishDate, etBookPages;
    Button btnSaveBook;
    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        etBookName = (EditText) findViewById(R.id.et_edit_book_name);
        etBookLang = (EditText) findViewById(R.id.et_edit_book_language);
        etBookPublishDate = (EditText) findViewById(R.id.et_edit_book_publishdate);
        etBookPages = (EditText) findViewById(R.id.et_edit_book_pages);

        btnSaveBook = (Button) findViewById(R.id.btn_edit_book);

        etBookName.setText(getIntent().getStringExtra("bookName"));
        etBookLang.setText(getIntent().getStringExtra("bookLang"));
        etBookPublishDate.setText(getIntent().getStringExtra("bookPublishDate"));
        etBookPages.setText(getIntent().getStringExtra("bookPages"));

        btnSaveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<Book> call = bookService.updateBookEntry(getIntent().getStringExtra("bookId"),
                        new Book(etBookName.getText().toString(),
                                etBookLang.getText().toString(),
                                etBookPublishDate.getText().toString(),
                                Integer.parseInt(etBookPages.getText().toString()),
                                getIntent().getStringExtra("authId"),
                                getIntent().getStringExtra("genreId")));
                call.enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(Call<Book> call, Response<Book> response) {
                      //  Log.e(TAG,response.body().getName());
                        Toast.makeText(EditBookActivity.this, "Sucessfully updated with new name "+response.body().getName(),Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Book> call, Throwable t) {
                        Log.e("#",t.toString());
                    }
                });

            }
        });
    }
}
