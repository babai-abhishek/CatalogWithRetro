package com.example.abhishek.catalogwithretro.activity.genre;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.activity.author.AuthorActivity;
import com.example.abhishek.catalogwithretro.model.Author;
import com.example.abhishek.catalogwithretro.model.Genre;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.GenreInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditGenreActivity extends AppCompatActivity {

    EditText etGenreName;
    Button btnGenreSave;
    String genreName, genreId;

    GenreInterface genreService = ApiClient.getClient().create(GenreInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_genre);

        genreName = getIntent().getStringExtra("genre_name");
        genreId = getIntent().getStringExtra("genre_id");

        etGenreName = (EditText) findViewById(R.id.et_genre_edit);
        btnGenreSave = (Button) findViewById(R.id.btn_save_genre);

        etGenreName.setText(genreName);
        btnGenreSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Genre> call = genreService.updateGenreEntry(genreId,new Genre(etGenreName.getText().toString()));
                call.enqueue(new Callback<Genre>() {
                    @Override
                    public void onResponse(Call<Genre> call, Response<Genre> response) {
                        Toast.makeText(EditGenreActivity.this, "Sucessfully updated with new name "+response.body().getName(),Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Genre> call, Throwable t) {
                       // Log.e(TAG,t.toString());
                    }
                });
            }
        });


    }
}
