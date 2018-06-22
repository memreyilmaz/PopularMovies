package com.example.android.popularmoviesstagetwo.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmoviesstagetwo.model.Movie;

public class MovieViewModel extends ViewModel {

    private LiveData<Movie> favouritemovie;

    public MovieViewModel(FavouriteMoviesDatabase database, int movieId) {
        favouritemovie = database.movieDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie() {
        return favouritemovie;
    }
}
