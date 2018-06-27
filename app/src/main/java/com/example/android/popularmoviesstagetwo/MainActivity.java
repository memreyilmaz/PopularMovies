package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.popularmoviesstagetwo.adapters.MovieAdapter;
import com.example.android.popularmoviesstagetwo.database.MainViewModel;
import com.example.android.popularmoviesstagetwo.model.Movie;
import com.example.android.popularmoviesstagetwo.model.MovieResponse;
import com.example.android.popularmoviesstagetwo.rest.TmdbApiClient;
import com.example.android.popularmoviesstagetwo.rest.TmdbApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String API_KEY = "fa0a36c54bae48da04a507ac7ce6126f";
    List<Movie> movies= new ArrayList<>();
    private MovieResponse mMovieResponse;
    public MovieAdapter.MovieAdapterOnClickHandler clickHandler;
    private MovieAdapter mAdapter;
    private Movie mCurrentMovie;
    private TextView mEmptyStateTextView;
    RecyclerView movieListView;
    MainViewModel viewModel;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieListView = findViewById(R.id.movie_image);
        mAdapter = new MovieAdapter(movies, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        movieListView.setLayoutManager(layoutManager);
        movieListView.setHasFixedSize(true);
        movieListView.setAdapter(mAdapter);
        mEmptyStateTextView = findViewById(R.id.empty_view);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String sort_by = sharedPrefs.getString(
                getString(R.string.settings_sort_by_list_key),
                getString(R.string.settings_sort_by_list_default));


        TmdbApiInterface apiService =
                TmdbApiClient.getClient().create(TmdbApiInterface.class);

        Call<MovieResponse> call = apiService.getMovieList(sort_by, API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, call.request().url().toString());
                int statusCode = response.code();
                mMovieResponse = response.body();
                mAdapter.setMovieData(mMovieResponse);
                mAdapter.notifyDataSetChanged();
                movies = mMovieResponse.getMovies();

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
              //  mEmptyStateTextView.setText(R.string.no_internet_connection);
                Log.e(TAG, t.toString());
            }
        });
       /* if (movies.isEmpty()) {
            movieListView.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
        else {
            movieListView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setVisibility(View.GONE);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.most_popular:
                startActivity (new Intent(this, SortActivity.class));
                return true;
            case R.id.most_rated:
                startActivity (new Intent(this, SortActivity.class));
                return true;
            case R.id.favourite_movies:
                startActivity (new Intent(this, FavouriteActivity.class));
                 return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movie) {
        mCurrentMovie = movie;

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(getResources().getString(R.string.parcel_movie), mCurrentMovie);

        startActivity(intent);
    }
}