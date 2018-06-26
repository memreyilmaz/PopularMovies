package com.example.android.popularmoviesstagetwo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.android.popularmoviesstagetwo.adapters.FavouritesAdapter;
import com.example.android.popularmoviesstagetwo.database.FavouriteMoviesDatabase;
import com.example.android.popularmoviesstagetwo.database.MainViewModel;
import com.example.android.popularmoviesstagetwo.model.Movie;
import com.example.android.popularmoviesstagetwo.model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity implements FavouritesAdapter.FavouritesAdapterOnClickHandler{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TMDB_REQUEST_URL = "https://api.themoviedb.org/3/movie";
    private static final String API_KEY = "fa0a36c54bae48da04a507ac7ce6126f";
    private static final int MOVIE_LOADER_ID = 1;
    List<Movie> movies= new ArrayList<>();
    private MovieResponse mMovieResponse;
    public FavouritesAdapter.FavouritesAdapterOnClickHandler clickHandler;
    private FavouritesAdapter mAdapter;
    private Movie mCurrentMovie;
    private TextView mEmptyStateTextView;
    RecyclerView favouriteMovieListView;
    private int recycler_position;
    Context context;
    private FavouriteMoviesDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        mDb = FavouriteMoviesDatabase.getsInstance(getApplicationContext());
        favouriteMovieListView = (RecyclerView) findViewById(R.id.favourite_movie_image);
        mAdapter = new FavouritesAdapter(this, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);

        favouriteMovieListView.setLayoutManager(layoutManager);
        favouriteMovieListView.setHasFixedSize(true);
        favouriteMovieListView.setAdapter(mAdapter);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

            MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    Log.d(TAG, "Updating list of movies from LiveData in ViewModel");
                    // mAdapter = new FavouritesAdapter(FavouriteActivity.this, clickHandler);
                    //favouriteMovieListView.setAdapter(mAdapter);
                    mAdapter.setFavouriteMovieData(movies);
                }
            });

        /*SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String sort_by = sharedPrefs.getString(
                getString(R.string.settings_sort_by_list_key),
                getString(R.string.settings_sort_by_list_default));*/
    }

    @Override
    public void onClick(Movie movie) {
        mCurrentMovie = movie;
        Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
        intent.putExtra(getResources().getString(R.string.parcel_movie), mCurrentMovie);

        startActivity(intent);
    }
}