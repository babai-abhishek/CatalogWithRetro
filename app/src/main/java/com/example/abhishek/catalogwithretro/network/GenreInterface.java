package com.example.abhishek.catalogwithretro.network;


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

public interface GenreInterface {
    @GET("/genres")
    Call<List<Genre>> getAllGenres();

    @GET("/genres/{id}")
    Call<Genre> getGenre(@Path("id")String id);

    @POST("/genres")
    Call<Genre> newGenreEntry(@Body Genre genre);

    @DELETE("/genres/{id}")
    Call<ResponseBody> deleteGenreEntry(@Path("id")String id);

    @PUT("/genres/{id}")
    Call<Genre> updateGenreEntry(@Path("id")String id, @Body Genre genre);
}
