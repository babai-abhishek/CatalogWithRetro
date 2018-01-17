package com.example.abhishek.catalogwithretro.activity.book;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.activity.book.AuthorNameSelection.Select_author_name_activity;
import com.example.abhishek.catalogwithretro.activity.book.GenreTypeSelection.Select_genre_type_activity;
import com.example.abhishek.catalogwithretro.model.Author;
import com.example.abhishek.catalogwithretro.model.Book;
import com.example.abhishek.catalogwithretro.model.Genre;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.AuthorInterface;
import com.example.abhishek.catalogwithretro.network.BookInterface;
import com.example.abhishek.catalogwithretro.network.GenreInterface;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewBookActivity extends AppCompatActivity{

    EditText etNewBookName, etNewBookLanguage, etNewBookPublishDate, etNewBookPages;
    Button btnSaveNewBook;
    final Calendar myCalendar = Calendar.getInstance();
    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);
    Genre selectedGenre =null;
    Author selectedAuthor = null;
    TextView tvGenreType, tvAuthorName;

    public static final int REQUEST_CODE_GENRETYPE = 1 , REQUEST_CODE_AUTHORNAME = 2 ;
    private final String SELECTED_GENRE_KEY = "genres";
    private final String SELECTED_AUTHOR_KEY = "authors";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);

        btnSaveNewBook = (Button) findViewById(R.id.btn_save_new_book);

        tvGenreType = (TextView) findViewById(R.id.tv_select_genre_type);
        tvAuthorName = (TextView) findViewById(R.id.tv_select_author_name);

        etNewBookName = (EditText) findViewById(R.id.et_new_book_name);
        etNewBookLanguage = (EditText) findViewById(R.id.et_new_book_language);
        etNewBookPages = (EditText) findViewById(R.id.et_new_book_pages);

        if(savedInstanceState!=null){
            selectedAuthor = (Author) savedInstanceState.getSerializable(SELECTED_AUTHOR_KEY);
            selectedGenre = (Genre) savedInstanceState.getSerializable(SELECTED_GENRE_KEY);
            setGenre(selectedGenre);
            setAuthor(selectedAuthor);
        }
        else {
            setGenre(null);
            setAuthor(null);
        }
        tvGenreType.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddNewBookActivity.this, Select_genre_type_activity.class),
                        REQUEST_CODE_GENRETYPE);
            }
        });

        tvAuthorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddNewBookActivity.this,
                        Select_author_name_activity.class),
                        REQUEST_CODE_AUTHORNAME);
            }
        });

        etNewBookPublishDate = (EditText) findViewById(R.id.et_new_book_publishDate);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        etNewBookPublishDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddNewBookActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSaveNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewBookEntry(new Book(etNewBookName.getText().toString(), etNewBookLanguage.getText().toString(),
                        etNewBookPublishDate.getText().toString(), Integer.parseInt(etNewBookPages.getText().toString()),
                        selectedAuthor.getId(),selectedGenre.getId()));
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
                setGenre(selectedGenre);
                Log.d("#","selected genre "+selectedGenre.getName()+" with id "+selectedGenre.getId());

            case REQUEST_CODE_AUTHORNAME:
                Bundle bun = data.getExtras();

                selectedAuthor = (Author) bun.getSerializable("selectedAuthorName");
                setAuthor(selectedAuthor);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SELECTED_GENRE_KEY, selectedGenre);
        outState.putSerializable(SELECTED_AUTHOR_KEY, selectedAuthor);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etNewBookPublishDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void createNewBookEntry(Book book) {

        Call<Book> call = bookService.newBookEntry(book);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Book book = response.body();
                Log.d("#", " id of new book received " + book.getId());
                Toast.makeText(AddNewBookActivity.this, "ID of new books is" + book.getId(), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("#", t.toString());
            }
        });


    }

    private void setGenre(Genre genre){
        selectedGenre =genre;
        if(genre==null){
            tvGenreType.setText("Click to select genre");
        }
        else{
            tvGenreType.setText("Selected Genre : "+genre.getName());
        }
    }

    private void setAuthor(Author author) {
        selectedAuthor = author;
        if(author==null){
            tvAuthorName.setText("Click to select author");
        }
        else{
            tvAuthorName.setText("Selected Author : "+author.getName());
        }
    }
}
