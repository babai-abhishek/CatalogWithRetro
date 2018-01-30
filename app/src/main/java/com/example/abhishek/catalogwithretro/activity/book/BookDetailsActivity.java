package com.example.abhishek.catalogwithretro.activity.book;

import android.app.ProgressDialog;
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

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailsActivity extends AppCompatActivity {

    Book book;
    TextView tvBookName, tvBookLanguage, tvBookPublishDate, tvBookNoOfPages, tvBookId,
            tvGenreType, tvAuthoName, tvFindBookByGenre, tvFindBookByAuthor;
    Button btnEditBook, btnDeleteBook;
    private static final String GENRE_KEY = "genre";
    private static final String AUTHOR_KEY = "author";
    private static final String KEY_SHOULD_RELOAD = "shouldReload";
    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);
    private boolean shouldReload = false;

    ProgressDialog mProgressDialog;

    Author author;
    Genre genre;
    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);
    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);

    private boolean isGenreLoaded = false, isAuthorLoaded = false, isBookLoaded = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Bundle bookBundle = getIntent().getExtras();
        book = (Book) bookBundle.getParcelable(BookActivity.SELECTED_BOOK);

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

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        Log.d("#", "selected bookName " + book.getName());

        tvBookName.setText(book.getName());
        tvBookId.setText(book.getId());
        tvBookLanguage.setText(book.getLanguage());
        tvBookPublishDate.setText(book.getPublished());
        tvBookNoOfPages.setText(String.valueOf(book.getPages()));


        if (savedInstanceState == null) {

            loadGenreType(book.getGenreId());
            loadAuthorName(book.getAuthorId());
        } else {
            shouldReload = savedInstanceState.getBoolean(KEY_SHOULD_RELOAD);
            tvAuthoName.setText("XXXXXXXXXXXX");
            tvGenreType.setText("XXXXXXXXXXXX");
            if (shouldReload) {
                reloadUpdatedBook(book.getId());
            } else {
                tvAuthoName.setText(savedInstanceState.getString(AUTHOR_KEY));
                tvGenreType.setText(savedInstanceState.getString(GENRE_KEY));
            }

        }

        tvFindBookByAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldReload = false;
                Intent intent = new Intent(BookDetailsActivity.this, SortedBooksByAuthorActivity.class);
                intent.putExtra("authorId", book.getAuthorId());
                intent.putExtra("authorName", tvAuthoName.getText().toString());
                startActivity(intent);
            }
        });

        tvFindBookByGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldReload = false;
                Intent intent = new Intent(BookDetailsActivity.this, SortedBooksByGenreActivity.class);
                intent.putExtra("genreId", genre.getId());
                intent.putExtra("genreName", tvGenreType.getText().toString());
                startActivity(intent);
            }
        });

        btnEditBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldReload = true;
                Intent intent = new Intent(BookDetailsActivity.this, EditBookActivity.class);
                intent.putExtra("bookName", book.getName());
                intent.putExtra("bookId", book.getId());
                intent.putExtra("bookLang", book.getLanguage());
                intent.putExtra("bookPublishDate", book.getPublished());
                intent.putExtra("bookPages", String.valueOf(book.getPages()));
                intent.putExtra("authName", tvAuthoName.getText());
                intent.putExtra("genreType", tvGenreType.getText());
                intent.putExtra("authId", book.getAuthorId());
                intent.putExtra("genreId", book.getGenreId());
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
                        Log.e("#", t.toString());
                    }
                });

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AUTHOR_KEY, tvAuthoName.getText().toString());
        outState.putString(GENRE_KEY, tvGenreType.getText().toString());
        outState.putBoolean(KEY_SHOULD_RELOAD, shouldReload);

    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        if(shouldReload){
          tvAuthoName.setText("XXXXXXXXXXXX");
          tvGenreType.setText("XXXXXXXXXXXX");
          reloadUpdatedBook(book.getId());
          shouldReload=false;
        }
    }*/

    private void reloadUpdatedBook(String id) {

        Call<Book> call = bookService.getBook(id);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                book = response.body();
                tvBookName.setText(book.getName());
                tvBookId.setText(book.getId());
                tvBookLanguage.setText(book.getLanguage());
                tvBookPublishDate.setText(book.getPublished());
                tvBookNoOfPages.setText(String.valueOf(book.getPages()));
                loadAuthorName(book.getAuthorId());
                loadGenreType(book.getGenreId());

                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("#", t.toString());
                mProgressDialog.dismiss();
            }
        });
        shouldReload = false;
    }

    private void loadAuthorName(String authorId) {

        isAuthorLoaded=false;
        showLoading();
        Call<Author> call = authorService.getAuthor(authorId);
        call.enqueue(new Callback<Author>() {
            @Override
            public void onResponse(Call<Author> call, Response<Author> response) {
                author = response.body();
                tvAuthoName.setText(author.getName());
                isAuthorLoaded=true;
                postLoad();
            }

            @Override
            public void onFailure(Call<Author> call, Throwable t) {
                Log.e("#", t.toString());
                isAuthorLoaded=true;
                postLoad();
            }
        });

    }

    private void loadGenreType(String genreId) {
        isGenreLoaded=false;
        showLoading();
        Call<Genre> call = genreService.getGenre(genreId);
        call.enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(Call<Genre> call, Response<Genre> response) {
                genre = response.body();
                tvGenreType.setText(genre.getName());
               /* mProgressDialog.cancel();*/
               isGenreLoaded=true;
               postLoad();
            }

            @Override
            public void onFailure(Call<Genre> call, Throwable t) {
                Log.e("#", t.toString());
                isGenreLoaded=true;
                postLoad();
            }
        });

    }

    private void postLoad() {
        if (isAuthorLoaded && isGenreLoaded && isBookLoaded)
            hideLoading();
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

}
