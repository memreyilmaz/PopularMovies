package com.example.android.popularmoviesstagetwo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbTrailerInterface {

    @GET("movie/{id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("id") int id, @Query("api_key") String apiKey);

}
