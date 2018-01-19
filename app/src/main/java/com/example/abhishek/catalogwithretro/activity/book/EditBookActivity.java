package com.example.abhishek.catalogwithretro.activity.book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.activity.book.AuthorNameSelection.Select_author_name_activity;
import com.example.abhishek.catalogwithretro.activity.book.GenreTypeSelection.Select_genre_type_activity;
import com.example.abhishek.catalogwithretro.model.Author;
import com.example.abhishek.catalogwithretro.model.Book;
import com.example.abhishek.catalogwithretro.model.Genre;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.BookInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBookActivity extends AppCompatActivity {

    EditText etBookName, etBookLang, etBookPublishDate, etBookPages, etBookGenre, etBookAuthor;
    Button btnSaveBook, btnChangeAuthor, btnChangeGenre;
    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);
    Author selectedAuthor = new Author();
    Genre selectedGenre = new Genre();

    public static final int REQUEST_CODE_GENRETYPE = 1 , REQUEST_CODE_AUTHORNAME = 2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        etBookName = (EditText) findViewById(R.id.et_edit_book_name);
        etBookLang = (EditText) findViewById(R.id.et_edit_book_language);
        etBookPublishDate = (EditText) findViewById(R.id.et_edit_book_publishdate);
        etBookPages = (EditText) findViewById(R.id.et_edit_book_pages);
        etBookAuthor = (EditText) findViewById(R.id.et_edit_book_author);
        etBookGenre = (EditText) findViewById(R.id.et_edit_book_genre);

        btnSaveBook = (Button) findViewById(R.id.btn_edit_book);
        btnChangeAuthor = (Button) findViewById(R.id.btn_change_author);
        btnChangeGenre = (Button) findViewById(R.id.btn_change_genre);

        etBookName.setText(getIntent().getStringExtra("bookName"));
        etBookLang.setText(getIntent().getStringExtra("bookLang"));
        etBookPublishDate.setText(getIntent().getStringExtra("bookPublishDate"));
        etBookPages.setText(getIntent().getStringExtra("bookPages"));
        selectedAuthor.setId(getIntent().getStringExtra("authId"));
        selectedAuthor.setName(getIntent().getStringExtra("authName"));
        setAuthor(selectedAuthor.getName());

        selectedGenre.setId(getIntent().getStringExtra("genreId"));
        selectedGenre.setName(getIntent().getStringExtra("genreType"));
        setGenre(selectedGenre.getName());


        btnChangeAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(EditBookActivity.this,
                                Select_author_name_activity.class),
                        REQUEST_CODE_AUTHORNAME);
            }
        });

        btnChangeGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(EditBookActivity.this,
                                Select_genre_type_activity.class),
                        REQUEST_CODE_GENRETYPE);

            }
        });
        btnSaveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<Book> call = bookService.updateBookEntry(getIntent().getStringExtra("bookId"),
                        new Book(etBookName.getText().toString(),
                                etBookLang.getText().toString(),
                                etBookPublishDate.getText().toString(),
                                Integer.parseInt(etBookPages.getText().toString()),
                                selectedAuthor.getId(),
                                selectedGenre.getId()));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_GENRETYPE:
                // Intent intent = this.getIntent();
                Bundle bundle = data.getExtras();
                selectedGenre = (Genre) bundle.getSerializable("selectedGenreType");
                setGenre(selectedGenre.getName());
                break;

            case REQUEST_CODE_AUTHORNAME:
                Bundle bun = data.getExtras();
                selectedAuthor = (Author) bun.getSerializable("selectedAuthorName");
                setAuthor(selectedAuthor.getName());
                break;
        }

    }

    private void setAuthor(String authorName) {
        etBookAuthor.setText(authorName);
    }


    private void setGenre(String name) {
        etBookGenre.setText(name);
    }


}
