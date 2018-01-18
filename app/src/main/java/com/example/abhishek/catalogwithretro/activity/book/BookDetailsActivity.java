package com.example.abhishek.catalogwithretro.activity.book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.adapters.RecyclerEditDeleteClickActionListener;
import com.example.abhishek.catalogwithretro.model.Author;
import com.example.abhishek.catalogwithretro.model.Book;
import com.example.abhishek.catalogwithretro.model.Genre;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.AuthorInterface;
import com.example.abhishek.catalogwithretro.network.BookInterface;
import com.example.abhishek.catalogwithretro.network.GenreInterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailsActivity extends AppCompatActivity implements RecyclerEditDeleteClickActionListener {
    Book book;
    TextView tvBookName, tvBookLanguage, tvBookPublishDate, tvBookNoOfPages, tvBookId,
            tvGenreType, tvAuthoName, tvFindBookByGenre, tvFindBookByAuthor;
    Button btnEditBook, btnDeleteBook;
    private static final String GENRE_KEY = "genre";
    private static final String AUTHOR_KEY = "author";
    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);


  /*  ProgressDialog mProgressDialog;
*/
    Author author;
    Genre genre;
    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);
    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Bundle bookBundle = getIntent().getExtras();
        book = (Book) bookBundle.getSerializable(BookActivity.SELECTED_BOOK);

        tvBookName = (TextView) findViewById(R.id.tv_book_name);
        tvBookId = (TextView) findViewById(R.id.tv_book_id);
        tvBookLanguage = (TextView) findViewById(R.id.tv_book_language);
        tvBookPublishDate = (TextView) findViewById(R.id.tv_book_dateOfPublish);
        tvBookNoOfPages = (TextView) findViewById(R.id.tv_book_pages);
        tvGenreType = (TextView) findViewById(R.id.tv_book_genreType);
        tvAuthoName = (TextView) findViewById(R.id.tv_book_authorName);

        tvFindBookByAuthor = (TextView) findViewById(R.id.tv_find_all_books_by_author);
        tvFindBookByGenre = (TextView) findViewById(R.id.tv_find_all_by_genre_type);


        btnEditBook = (Button) findViewById(R.id.btn_book_edit);
        btnDeleteBook = (Button) findViewById(R.id.btn_book_delete);

      /*  mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);*/

        Log.d("#","selected bookName "+book.getName());

        tvBookName.setText(book.getName());
        tvBookId.setText(book.getId());
        tvBookLanguage.setText(book.getLanguage());
        tvBookPublishDate.setText(book.getPublished());
        tvBookNoOfPages.setText(String.valueOf(book.getPages()));


        if(savedInstanceState==null){
            loadGenreType(book.getGenreId());
            loadAuthorName(book.getAuthorId());
        }
        else {
            tvAuthoName.setText(savedInstanceState.getString(AUTHOR_KEY));
            tvGenreType.setText(savedInstanceState.getString(GENRE_KEY));
        }

        tvFindBookByAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailsActivity.this, SortedBooksByAuthorActivity.class);
                intent.putExtra("authorId",book.getAuthorId());
                intent.putExtra("authorName",tvAuthoName.getText().toString());
                startActivity(intent);
            }
        });

        tvFindBookByGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailsActivity.this, SortedBooksByGenreActivity.class);
                intent.putExtra("genreId",genre.getId());
                intent.putExtra("genreName",tvGenreType.getText().toString());
                startActivity(intent);
            }
        });

        btnEditBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailsActivity.this, EditBookActivity.class);
                intent.putExtra("bookName",book.getName());
                intent.putExtra("bookId",book.getId());
                intent.putExtra("bookLang",book.getLanguage());
                intent.putExtra("bookPublishDate",book.getPublished());
                intent.putExtra("bookPages",String.valueOf(book.getPages()));
                intent.putExtra("authName",tvAuthoName.getText());
                intent.putExtra("genreType",tvGenreType.getText());
                intent.putExtra("authId",book.getAuthorId());
                intent.putExtra("genreId",book.getGenreId());
                startActivity(intent);
            }
        });
       btnDeleteBook.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Call<ResponseBody> call = bookService.deleteBookEntry(book.getId());
               call.enqueue(new Callback<ResponseBody>() {
                   @Override
                   public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                       // Toast.makeText(BookActivity.this, "Sucessfully deleted entry",Toast.LENGTH_SHORT).show();
                       finish();
                   }

                   @Override
                   public void onFailure(Call<ResponseBody> call, Throwable t) {
                       Log.e("#",t.toString());
                   }
               });

           }
       });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AUTHOR_KEY,tvAuthoName.getText().toString());
        outState.putString(GENRE_KEY, tvGenreType.getText().toString());

    }

    private void loadAuthorName(String authorId) {

       /* mProgressDialog.setMessage("Downloading author name");
        mProgressDialog.show();
*/
        Call<Author> call = authorService.getAuthor(authorId);
        call.enqueue(new Callback<Author>() {
            @Override
            public void onResponse(Call<Author> call, Response<Author> response) {
                author = response.body();
                tvAuthoName.setText(author.getName());
              /*  mProgressDialog.cancel();*/
            }

            @Override
            public void onFailure(Call<Author> call, Throwable t) {
                Log.e("#",t.toString());
            }
        });

    }

    private void loadGenreType(String genreId) {

      /*  mProgressDialog.setMessage("Downloading genre type");
        mProgressDialog.show();
*/
        Call<Genre> call = genreService.getGenre(genreId);
        call.enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(Call<Genre> call, Response<Genre> response) {
                genre = response.body();
                tvGenreType.setText(genre.getName());
               /* mProgressDialog.cancel();*/
            }

            @Override
            public void onFailure(Call<Genre> call, Throwable t) {
                Log.e("#", t.toString());
            }
        });

    }

    @Override
    public void onAction(int position, int action) {

    }
}
