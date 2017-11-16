package com.example.abhishek.catalogwithretro.activity.book;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.model.Book;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.BookInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewBookActivity extends AppCompatActivity {

    EditText etNewBookName, etNewBookLanguage, etNewBookPublishDate, etNewBookPages;
    Button btnSaveNewBook;
    final Calendar myCalendar = Calendar.getInstance();
    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);

        btnSaveNewBook = (Button) findViewById(R.id.btn_save_new_book);

        etNewBookName = (EditText) findViewById(R.id.et_new_book_name);
        etNewBookLanguage = (EditText) findViewById(R.id.et_new_book_language);
        etNewBookPages = (EditText) findViewById(R.id.et_new_book_pages);

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
                        etNewBookPublishDate.getText().toString(), Integer.parseInt(etNewBookPages.getText().toString())));
            }
        });
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
}
