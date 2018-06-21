package com.example.android.popularmoviesstagetwo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class MovieViewModel extends ViewModel {

    private LiveData<Movie> favouritemovie;

    public MovieViewModel(FavouriteMoviesDatabase database, int movieId) {
        favouritemovie = database.movieDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie() {
        return favouritemovie;
    }
}
