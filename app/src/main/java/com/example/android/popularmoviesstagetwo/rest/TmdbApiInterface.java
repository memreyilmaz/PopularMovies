package com.example.android.popularmoviesstagetwo.rest;

import com.example.android.popularmoviesstagetwo.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbApiInterface {

        @GET("movie/top_rated")
        Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

        @GET("movie/popular")
        Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

        @GET("movie/{preference}")
        Call<MovieResponse>  getMovieList(@Path("preference") String preference, @Query("api_key") String apiKey);

        @GET("movie/{id}")
        Call<MovieResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}

