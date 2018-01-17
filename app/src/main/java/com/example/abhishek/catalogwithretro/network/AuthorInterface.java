package com.example.abhishek.catalogwithretro.network;

import com.example.abhishek.catalogwithretro.model.Author;
import com.example.abhishek.catalogwithretro.model.Book;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by abhishek on 7/11/17.
 */

public interface AuthorInterface {

    @GET("/authors")
    Call<List<Author>> getAllAuthors();

    @GET("/authors/{id}")
    Call<Author> getAuthor(@Path("id")String id);

    @POST("/authors")
    Call<Author> newAuthorEntry(@Body Author author);

    @DELETE("/authors/{id}")
    Call<ResponseBody> deleteAuthorEntry(@Path("id")String id);

    @PUT("/authors/{id}")
    Call<Author> updateAuthorEntry(@Path("id")String id, @Body Author author);
}
