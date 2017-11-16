package com.example.abhishek.catalogwithretro.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.activity.author.AuthorActivity;
import com.example.abhishek.catalogwithretro.activity.book.BookActivity;
import com.example.abhishek.catalogwithretro.activity.genre.GenreActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_book_catalog, btn_author_catalaog, btn_genre_catalog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btn_book_catalog = (Button) findViewById(R.id.btn_book_cat);
        btn_book_catalog.setOnClickListener(this);

        btn_author_catalaog = (Button) findViewById(R.id.btn_author_cat);
        btn_author_catalaog.setOnClickListener(this);

        btn_genre_catalog = (Button) findViewById(R.id.btn_genre_cat);
        btn_genre_catalog.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()){
            case R.id.btn_book_cat:
                intent = new Intent(MainActivity.this, BookActivity.class);
                break;
            case R.id.btn_author_cat:
                intent = new Intent(MainActivity.this, AuthorActivity.class);
                break;
            case R.id.btn_genre_cat:
                intent = new Intent(MainActivity.this, GenreActivity.class);
                break;
        }
        startActivity(intent);

    }
}
