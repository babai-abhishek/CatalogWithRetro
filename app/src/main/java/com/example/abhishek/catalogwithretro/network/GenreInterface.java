package com.example.abhishek.catalogwithretro.network;

import com.example.abhishek.catalogwithretro.model.Author;
import com.example.abhishek.catalogwithretro.model.Genre;

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

public interface GenreInterface {
    @GET("/genres")
    Call<List<Genre>> getAllGenres();

    @POST("/genres")
    Call<Genre> newGenreEntry(@Body Genre genre);

    @DELETE("/genres/{id}")
    Call<ResponseBody> deleteGenreEntry(@Path("id")String id);

    @PUT("/genres/{id}")
    Call<Genre> updateGenreEntry(@Path("id")String id, @Body Genre genre);
}
