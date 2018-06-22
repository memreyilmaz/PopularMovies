package com.example.android.popularmoviesstagetwo.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;


import com.example.android.popularmoviesstagetwo.model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> favouritemovies;

    public MainViewModel(Application application) {
        super(application);
        FavouriteMoviesDatabase database = FavouriteMoviesDatabase.getsInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the movies from the DataBase");
        favouritemovies = database.movieDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return favouritemovies;
    }
}
