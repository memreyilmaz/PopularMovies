package com.example.android.popularmoviesstagetwo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
    private static final String TMDB_REQUEST_URL = "https://api.themoviedb.org/3/movie";
    private static final String API_KEY = "fa0a36c54bae48da04a507ac7ce6126f";
    private static final int MOVIE_LOADER_ID = 1;
    List<Movie> movies= new ArrayList<>();
    private MovieResponse mMovieResponse;
    public MovieAdapter.MovieAdapterOnClickHandler clickHandler;
    private MovieAdapter mAdapter;
    private Movie mCurrentMovie;
    private TextView mEmptyStateTextView;
    RecyclerView movieListView;
    MainViewModel viewModel;
    private int recycler_position;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieListView = (RecyclerView) findViewById(R.id.movie_image);
        mAdapter = new MovieAdapter(movies, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        movieListView.setLayoutManager(layoutManager);
        movieListView.setHasFixedSize(true);
        movieListView.setAdapter(mAdapter);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);


        /*if (movies.isEmpty()) {
            movieListView .setVisibility(View.GONE);
            mEmptyStateTextView .setVisibility(View.VISIBLE);
        }
        else {
            movieListView .setVisibility(View.VISIBLE);
            mEmptyStateTextView .setVisibility(View.GONE);
        }*/
        // Create a new adapter that takes an empty list of movies as input
        //movieListView.setOnClickListener();

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
                //mEmptyStateTextView.setText(R.string.no_internet_connection);
                Log.e(TAG, t.toString());
            }
        });
    }

    public void loadFavorites() {
        /*final FavouriteMoviesDatabase database = FavouriteMoviesDatabase.getsInstance(context);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //if (!(movie.getId().equals(database.movieDao().loadMovieById(movie.getId()).getValue().getId()))) {
                database.movieDao().loadAllMovies();
                //}
            }
        });*/

        //if (mAdapter != null)
          //  mAdapter = null;
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies != null) {
                    mAdapter = new MovieAdapter(movies, MainActivity.this );
                    movieListView.setAdapter(mAdapter);

                    //movieListView.smoothScrollToPosition(recycler_position);
                    //mAdapter.setMovieData(mMovieResponse);
                    //mAdapter.notifyDataSetChanged();
                    //movies = mMovieResponse.getMovies();
                    //movieListView.setVisibility(View.VISIBLE);
                    //progressBar.setVisibility(View.INVISIBLE);
                } else {
                    //navigation.setSelectedItemId(R.id.navigation_popular);
                    //Toast.makeText(MainActivity.this, R.string.no_favorite_movies, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initViews1() {

        movieListView = findViewById(R.id.movie_image);
        //movieListView.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        movieListView.setLayoutManager(layoutManager);

        //movieListView.setItemAnimator(new DefaultItemAnimator());
        loadFavorites();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.most_popular) {
             Intent sortIntent = new Intent(this, SortActivity.class);
             startActivity(sortIntent);
             return true;
        }else if (id == R.id.most_rated) {
            Intent sortIntent = new Intent(this, SortActivity.class);
            startActivity(sortIntent);
            return true;
        }else if (id == R.id.favourite_movies) {
            //Intent sortIntent = new Intent(this, SortActivity.class);
            //startActivity(sortIntent);
            initViews1();
            return true;}
            return super.onOptionsItemSelected(item);
    }

  ///
    @Override
    public void onClick(Movie movie) {
        //Context context = this;
        mCurrentMovie = movie;

        //movie = movies.get(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(getResources().getString(R.string.parcel_movie), mCurrentMovie);

        startActivity(intent);
    }
}