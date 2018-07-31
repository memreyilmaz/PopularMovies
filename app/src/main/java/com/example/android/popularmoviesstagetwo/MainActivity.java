package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmoviesstagetwo.adapters.MovieAdapter;
import com.example.android.popularmoviesstagetwo.model.Movie;
import com.example.android.popularmoviesstagetwo.model.MovieResponse;
import com.example.android.popularmoviesstagetwo.rest.TmdbApiClient;
import com.example.android.popularmoviesstagetwo.rest.TmdbApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{
    public static final String POPULAR_MOVIES = "popular";
    public static final String TOP_RATED_MOVIES = "top_rated";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String API_KEY = "";

    private ArrayList<Movie> movies = new ArrayList<>();
    private MovieResponse mMovieResponse;
    public MovieAdapter.MovieAdapterOnClickHandler clickHandler;
    private MovieAdapter mAdapter;
    private Movie mCurrentMovie;
    private TextView mEmptyStateTextView;
    RecyclerView movieListView;
    TmdbApiInterface apiService;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        movieListView = findViewById(R.id.movie_image);

        mEmptyStateTextView = findViewById(R.id.empty_view);

        if (savedInstanceState == null || !savedInstanceState.containsKey("key")) {
            initializeUi();
            getMovies();
        } else {
            movies = savedInstanceState.getParcelableArrayList("key");

            initializeUi();
            mAdapter = new MovieAdapter(movies, this);
            movieListView.setAdapter(mAdapter);
        }

        if (isInternetAvailable()) {
           movieListView.setVisibility(View.VISIBLE);
           mEmptyStateTextView.setVisibility(View.GONE);
        } else {
           movieListView.setVisibility(View.GONE);
           mEmptyStateTextView.setVisibility(View.VISIBLE);
           mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    private void initializeUi(){

        layoutManager = new GridLayoutManager(this, 2);
        movieListView.setLayoutManager(layoutManager);
        movieListView.setHasFixedSize(true);
    }

    private void getMovies(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
           String sort_by = sharedPrefs.getString(
                getString(R.string.settings_sort_by_list_key),
                getString(R.string.settings_sort_by_list_default));

        apiService = TmdbApiClient.getClient().create(TmdbApiInterface.class);

        Call<MovieResponse> call = apiService.getMovieList(sort_by, API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                Log.d(TAG, call.request().url().toString());
                int statusCode = response.code();
                movies = response.body().getMovies();
                mAdapter = new MovieAdapter(movies, MainActivity.this);
                movieListView.setAdapter(mAdapter);
                mAdapter.setMovieData(movies);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void getTopRatedMovies() {

        Call<MovieResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    Log.d(TAG, call.request().url().toString());
                    int statusCode = response.code();
                    movies = response.body().getMovies();
                    mAdapter.setMovieData(movies);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
    }

    private void getMostPopularMovies() {

        Call<MovieResponse> call = apiService.getPopularMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, call.request().url().toString());
                int statusCode = response.code();
                movies = response.body().getMovies();
                mAdapter.setMovieData(movies);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
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

                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.settings_sort_by_list_key), MODE_PRIVATE).edit();
                editor.putString(getString(R.string.settings_sort_by_list_key), POPULAR_MOVIES);
                editor.apply();
                getMostPopularMovies();
                mAdapter.notifyDataSetChanged();

                return true;

            case R.id.most_rated:
                editor = getSharedPreferences(getString(R.string.settings_sort_by_list_key), MODE_PRIVATE).edit();
                editor.putString(getString(R.string.settings_sort_by_list_key), TOP_RATED_MOVIES);
                editor.apply();
                getTopRatedMovies();
                mAdapter.notifyDataSetChanged();

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

  private boolean isInternetAvailable() {
      ConnectivityManager connectivityManager
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
      return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key", movies);
    }
}
