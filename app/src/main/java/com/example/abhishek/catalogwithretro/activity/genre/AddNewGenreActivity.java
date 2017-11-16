package com.example.abhishek.catalogwithretro.activity.genre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.model.Genre;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.GenreInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewGenreActivity extends AppCompatActivity {

    Button btn_save_new_genre;
    EditText et_add_new_genre;
    private String TAG = "#";

    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_genre);

        btn_save_new_genre = (Button) findViewById(R.id.btn_add_new_genre);
        et_add_new_genre = (EditText) findViewById(R.id.et_add_new_genre);

        btn_save_new_genre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Genre> call = genreService.newGenreEntry(new Genre(et_add_new_genre.getText().toString()));
                call.enqueue(new Callback<Genre>() {
                    @Override
                    public void onResponse(Call<Genre> call, Response<Genre> response) {
                        Genre genre = response.body();
                        Log.d(TAG," id of new book received "+ genre.getId());
                        Toast.makeText(AddNewGenreActivity.this, "ID of new author is"+genre.getId(), Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(AddNewGenreActivity.this,GenreActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Genre> call, Throwable t) {
                        Log.e(TAG,t.toString());
                    }
                });

            }
        });
    }
}
