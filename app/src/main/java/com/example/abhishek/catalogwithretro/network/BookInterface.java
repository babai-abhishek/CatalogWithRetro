package com.example.abhishek.catalogwithretro.network;

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
import retrofit2.http.Query;

/**
 * Created by abhishek on 7/11/17.
 */

public interface BookInterface {

    @GET("/books")
    Call<List<Book>> getAllBooks();

    @GET("/books/{id}")
    Call<Book> getBook(@Path("id")String id);

    @GET("/books/genre/{genreId}")
    Call<List<Book>> getBooksByGenreId(@Path("genreId")String genreId);

    @GET("/books/author/{authorId}")
    Call<List<Book>> getBooksByAuthorId(@Path("authorId")String authorId);

    @POST("/books")
    Call<Book> newBookEntry(@Body Book book);

    @DELETE("/books/{id}")
    Call<ResponseBody> deleteBookEntry(@Path("id")String id);

    @PUT("/books/{id}")
    Call<Book> updateBookEntry(@Path("id")String id, @Body Book book);

}
