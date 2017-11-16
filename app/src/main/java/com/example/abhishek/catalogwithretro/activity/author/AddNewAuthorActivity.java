
package com.example.abhishek.catalogwithretro.activity.author;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishek.catalogwithretro.R;
import com.example.abhishek.catalogwithretro.model.Author;
import com.example.abhishek.catalogwithretro.network.ApiClient;
import com.example.abhishek.catalogwithretro.network.AuthorInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewAuthorActivity extends AppCompatActivity {

    EditText et_new_auth_name, et_new_auth_language, et_new_auth_country;
    Button btn_add_new_author;

    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_author);

        et_new_auth_name = (EditText) findViewById(R.id.et_add_new_author_name);
        et_new_auth_language = (EditText) findViewById(R.id.et_add_new_author_language);
        et_new_auth_country = (EditText) findViewById(R.id.et_add_new_author_country);

        btn_add_new_author = (Button) findViewById(R.id.btn_add_new_author);

        btn_add_new_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Author> call = authorService.newAuthorEntry(new Author(et_new_auth_name.getText().toString(),
                                                                            et_new_auth_language.getText().toString(),
                                                                            et_new_auth_country.getText().toString()));
                call.enqueue(new Callback<Author>() {
                    @Override
                    public void onResponse(Call<Author> call, Response<Author> response) {
                        Author author = response.body();
                        //Log.d("#"," id of new book received "+ author.getId());
                        Toast.makeText(AddNewAuthorActivity.this, "ID of new author is"+author.getId(), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Author> call, Throwable t) {
                        Log.e("#",t.toString());
                    }
                });
            }
        });
    }
}
