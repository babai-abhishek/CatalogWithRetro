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

public class EditAuthorActivity extends AppCompatActivity {

    EditText etEditAuthName, etEditAuthLang, etEditAuthCountry;
    Button btnEditAuthSave;
    AuthorInterface authorService = ApiClient.getClient().create(AuthorInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_author);

        etEditAuthName = (EditText) findViewById(R.id.et_edit_author_name);
        etEditAuthLang = (EditText) findViewById(R.id.et_edit_author_language);
        etEditAuthCountry = (EditText) findViewById(R.id.et_edit_author_country);

        btnEditAuthSave = (Button) findViewById(R.id.btn_edit_author);

        etEditAuthName.setText(getIntent().getStringExtra("authName"));
        etEditAuthLang.setText(getIntent().getStringExtra("authLang"));
        etEditAuthCountry.setText(getIntent().getStringExtra("authCoun"));

        btnEditAuthSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Author> call = authorService.updateAuthorEntry(getIntent().getStringExtra("authId").toString(),
                                                                    new Author(etEditAuthName.getText().toString(),
                                                                            etEditAuthLang.getText().toString(),
                                                                            etEditAuthCountry.getText().toString()));

                call.enqueue(new Callback<Author>() {
                    @Override
                    public void onResponse(Call<Author> call, Response<Author> response) {
                        //Log.e(TAG,response.body().getName());
                        Toast.makeText(EditAuthorActivity.this, "Sucessfully updated with new name "+response.body().getName(),Toast.LENGTH_SHORT).show();
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
