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
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmoviesstagetwo.adapters.FavouritesAdapter;
import com.example.android.popularmoviesstagetwo.database.FavouriteMoviesDatabase;
import com.example.android.popularmoviesstagetwo.database.MainViewModel;
import com.example.android.popularmoviesstagetwo.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity implements FavouritesAdapter.FavouritesAdapterOnClickHandler{

    private static final String TAG = FavouriteActivity.class.getSimpleName();
    List<Movie> movies= new ArrayList<>();
    public FavouritesAdapter.FavouritesAdapterOnClickHandler clickHandler;
    private FavouritesAdapter mAdapter;
    private Movie mCurrentMovie;
    private TextView mEmptyStateTextView;
    RecyclerView favouriteMovieListView;
    Context context;
    private FavouriteMoviesDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        mDb = FavouriteMoviesDatabase.getsInstance(getApplicationContext());
        favouriteMovieListView = findViewById(R.id.favourite_movie_image);
        mEmptyStateTextView = findViewById(R.id.favourites_empty_view);

        mAdapter = new FavouritesAdapter(this, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);

        favouriteMovieListView.setLayoutManager(layoutManager);
        favouriteMovieListView.setHasFixedSize(true);
        favouriteMovieListView.setAdapter(mAdapter);
            MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    mAdapter.setFavouriteMovieData(movies);
                    if (movies.isEmpty()) {
                        favouriteMovieListView.setVisibility(View.GONE);
                        mEmptyStateTextView.setVisibility(View.VISIBLE);
                        mEmptyStateTextView.setText(R.string.no_favourite_movies);
                    }
                    else {
                        favouriteMovieListView.setVisibility(View.VISIBLE);
                        mEmptyStateTextView.setVisibility(View.GONE);
                    }
                }
            });
    }

    @Override
    public void onClick(Movie movie) {
        mCurrentMovie = movie;
        Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
        intent.putExtra(getResources().getString(R.string.parcel_movie), mCurrentMovie);

        startActivity(intent);
    }
}