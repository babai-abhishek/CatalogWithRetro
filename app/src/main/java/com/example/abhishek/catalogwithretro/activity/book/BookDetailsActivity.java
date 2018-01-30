package com.example.abhishek.catalogwithretro.activity.book;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishek.catalogwithretro.R;
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

public class BookDetailsActivity extends AppCompatActivity {

    TextView tvBookName, tvBookLanguage, tvBookPublishDate, tvBookNoOfPages, tvBookId,
            tvGenreType, tvAuthoName, tvFindBookByGenre, tvFindBookByAuthor;
    Button btnEditBook, btnDeleteBook;

    private static final String KEY_GENRE = "genre";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_SHOULD_RELOAD = "shouldReload";
    public static final String KEY_IS_AUTHOR_LOADED = "isAuthorLoaded";
    public static final String KEY_IS_GENRE_LOADED = "isGenreLoaded";

    private boolean shouldReload = false;
    private boolean isGenreLoaded = false, isAuthorLoaded = false, isBookLoaded = false;

    ProgressDialog mProgressDialog;

    Book book;
    Author author;
    Genre genre;

    BookInterface bookService = ApiClient.getClient().create(BookInterface.class);
    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);
    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);

    private static final String ACTION_AUTHOR_NAME_API_SUCCESS="com.example.abhishek.catalogwithretro.api.author.name.result.success";
    private static final String ACTION_AUTHOR_NAME_API_FAILURE="com.example.abhishek.catalogwithretro.api.author.name.result.failure";

    private static final String ACTION_GENRE_TYPE_API_SUCCESS="com.example.abhishek.catalogwithretro.api.genre.type.result.success";
    private static final String ACTION_GENRE_TYPE_API_FAILURE="com.example.abhishek.catalogwithretro.api.genre.type.result.failure";

    private static final String ACTION_RELOAD_BOOK_LIST_API_SUCCESS="com.example.abhishek.catalogwithretro.api.book.list.result.success";
    private static final String ACTION_RELOAD_BOOK_LIST_API_FAILURE="com.example.abhishek.catalogwithretro.api.book.list.result.failure";

    private LocalBroadcastManager broadcastManager = null;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case ACTION_AUTHOR_NAME_API_SUCCESS:
                    author = intent.getParcelableExtra(AUTHOR_KEY_FOR_BROADCASTRECEIVER);
                    tvAuthoName.setText(author.getName());
                    isAuthorLoaded = true;
                    postLoad();
                    break;

                case ACTION_AUTHOR_NAME_API_FAILURE:
                    Toast.makeText(BookDetailsActivity.this, "Api Failure", Toast.LENGTH_SHORT).show();
                    isAuthorLoaded = true;
                    postLoad();
                    break;

                case ACTION_GENRE_TYPE_API_SUCCESS:
                    genre = intent.getParcelableExtra(GENRE_KEY_FOR_BROADCASTRECEIVER);
                    tvGenreType.setText(genre.getName());
                    isGenreLoaded = true;
                    postLoad();
                    break;

                case ACTION_GENRE_TYPE_API_FAILURE:
                    Toast.makeText(BookDetailsActivity.this, "Api Failure", Toast.LENGTH_SHORT).show();
                    isGenreLoaded = true;
                    postLoad();
                    break;
                case ACTION_RELOAD_BOOK_LIST_API_SUCCESS:
                    book = intent.getParcelableExtra(BOOK_KEY_FOR_BROADCASTRECEIVER);
                    tvBookName.setText(book.getName());
                    tvBookId.setText(book.getId());
                    tvBookLanguage.setText(book.getLanguage());
                    tvBookPublishDate.setText(book.getPublished());
                    tvBookNoOfPages.setText(String.valueOf(book.getPages()));
                    isBookLoaded = true;
                    loadAuthorName(book.getAuthorId());
                    loadGenreType(book.getGenreId());
                    break;
                case ACTION_RELOAD_BOOK_LIST_API_FAILURE:
                    Toast.makeText(BookDetailsActivity.this, "Api Failure", Toast.LENGTH_SHORT).show();
                    isBookLoaded = true;
                    postLoad();
                    break;

            }
        }
    };
    private static final String AUTHOR_KEY_FOR_BROADCASTRECEIVER = "author";
    private static final String GENRE_KEY_FOR_BROADCASTRECEIVER = "genre";
    private static final String BOOK_KEY_FOR_BROADCASTRECEIVER = "book";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        broadcastManager = LocalBroadcastManager.getInstance(this);

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

            isAuthorLoaded = savedInstanceState.getBoolean(KEY_IS_AUTHOR_LOADED);
            if(!isAuthorLoaded){
                showLoading();
            }

            isGenreLoaded = savedInstanceState.getBoolean(KEY_IS_GENRE_LOADED);
            if(!isGenreLoaded){
                showLoading();
            }

            shouldReload = savedInstanceState.getBoolean(KEY_SHOULD_RELOAD);
            tvAuthoName.setText("XXXXXXXXXXXX");
            tvGenreType.setText("XXXXXXXXXXXX");
            if (!shouldReload) {
                tvAuthoName.setText(savedInstanceState.getString(KEY_AUTHOR));
                tvGenreType.setText(savedInstanceState.getString(KEY_GENRE));
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
        outState.putString(KEY_AUTHOR, tvAuthoName.getText().toString());
        outState.putString(KEY_GENRE, tvGenreType.getText().toString());
        outState.putBoolean(KEY_SHOULD_RELOAD, shouldReload);
        outState.putBoolean(KEY_IS_AUTHOR_LOADED, isAuthorLoaded);
        outState.putBoolean(KEY_IS_GENRE_LOADED, isGenreLoaded);

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_AUTHOR_NAME_API_SUCCESS);
        filter.addAction(ACTION_AUTHOR_NAME_API_FAILURE);
        filter.addAction(ACTION_GENRE_TYPE_API_SUCCESS);
        filter.addAction(ACTION_GENRE_TYPE_API_FAILURE);
        filter.addAction(ACTION_RELOAD_BOOK_LIST_API_SUCCESS);
        filter.addAction(ACTION_RELOAD_BOOK_LIST_API_FAILURE);
        broadcastManager.registerReceiver(broadcastReceiver, filter);

        if(shouldReload){
            reloadUpdatedBook(book.getId());
        }
     //   shouldReload = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void reloadUpdatedBook(String id) {
        isBookLoaded = false;
        showLoading();

        Call<Book> call = bookService.getBook(id);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Book b = response.body();
                Intent intent = new Intent(ACTION_RELOAD_BOOK_LIST_API_SUCCESS);
                intent.putExtra(BOOK_KEY_FOR_BROADCASTRECEIVER,b);
                broadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Intent intent = new Intent(ACTION_RELOAD_BOOK_LIST_API_FAILURE);
                broadcastManager.sendBroadcast(intent);
            }
        });
        /*
        shouldReload = false;*/
    }

    private void loadAuthorName(String authorId) {

        isAuthorLoaded=false;
        showLoading();

        Call<Author> call = authorService.getAuthor(authorId);
        call.enqueue(new Callback<Author>() {
            @Override
            public void onResponse(Call<Author> call, Response<Author> response) {
                Author auth = response.body();
                Intent intent = new Intent(ACTION_AUTHOR_NAME_API_SUCCESS);
                intent.putExtra(AUTHOR_KEY_FOR_BROADCASTRECEIVER,auth);
                broadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<Author> call, Throwable t) {
                Log.e("#", t.toString());
                Intent intent = new Intent(ACTION_AUTHOR_NAME_API_FAILURE);
                broadcastManager.sendBroadcast(intent);
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
                Genre gen = response.body();
                Intent intent = new Intent(ACTION_GENRE_TYPE_API_SUCCESS);
                intent.putExtra(GENRE_KEY_FOR_BROADCASTRECEIVER,gen);
                broadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<Genre> call, Throwable t) {
                Log.e("#", t.toString());
                Intent intent = new Intent(ACTION_GENRE_TYPE_API_FAILURE);
                broadcastManager.sendBroadcast(intent);
            }
        });

    }

    private void postLoad() {
        if (isAuthorLoaded && isGenreLoaded)
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
