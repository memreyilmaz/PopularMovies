package com.example.android.popularmoviesstagetwo;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final FavouriteMoviesDatabase mDb;
    private final int mMovieId;

    public MovieViewModelFactory(FavouriteMoviesDatabase database, int movieId) {
        mDb = database;
        mMovieId = movieId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MovieViewModel(mDb, mMovieId);
    }
}