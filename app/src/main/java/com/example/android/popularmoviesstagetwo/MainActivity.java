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
    /** URL for movie data from the TMDB dataset */
    private static final String TMDB_REQUEST_URL =
            "https://api.themoviedb.org/3/movie";

    private static final String API_KEY = "fa0a36c54bae48da04a507ac7ce6126f";
    /**
     * Constant value for the movie loader ID.
     */
    private static final int MOVIE_LOADER_ID = 1;
    List<Movie> movies= new ArrayList<>();
    private MovieResponse mMovieResponse;
    public MovieAdapter.MovieAdapterOnClickHandler clickHandler;
    /**
     * Adapter for the list of movies
     */
    private MovieAdapter mAdapter;
    private Movie mCurrentMovie;
    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    RecyclerView movieListView;
    MainViewModel viewModel;
    private int recycler_position;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link GridView} in the layout
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
                //movieListView.setAdapter(new MovieAdapter(movies, clickHandler));
                mAdapter.setMovieData(mMovieResponse);
                mAdapter.notifyDataSetChanged();
                movies = mMovieResponse.getMovies();
                //mAdapter.addAll(movies);
                //mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                //mEmptyStateTextView.setText(R.string.no_internet_connection);
                Log.e(TAG, t.toString());
            }
        });
        // Get a reference to the ConnectivityManager to check state of network connectivity
        /*ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }*/
    }

    // onCreateLoader instantiates and returns a new Loader for the given ID
    /*@Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String sort_by = sharedPrefs.getString(
                getString(R.string.settings_sort_by_list_key),
                getString(R.string.settings_sort_by_list_default)
        );
        Uri baseUri = Uri.parse(TMDB_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(sort_by);
        uriBuilder.appendQueryParameter("api_key", API_KEY)
                  .build();

        return new MovieLoader(this, uriBuilder.toString());
    }*/

    /*@Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        // Clear the adapter of previous movie data
        mAdapter.clear();

        // If there is a valid list of {@link Movie), then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (movies != null && !movies.isEmpty()) {
            mAdapter.addAll(movies);
            mAdapter.notifyDataSetChanged();
        }
    }*/

    /*@Override
    public void onLoaderReset (Loader < List < Movie >> loader) {
        mAdapter.clear();
    }*/

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
        //progressBar = findViewById(R.id.progress);
        //progressBar.setIndeterminate(true);
        //progressBar.setVisibility(View.VISIBLE);

        movieListView = findViewById(R.id.movie_image);
        //movieListView.setVisibility(View.VISIBLE);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        //final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        //final float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        //final int numColumns = (int) (dpWidth / WIDTH_OF_COLUMNS);
        //gridLayoutManager = new GridLayoutManager(MainActivity.this, numColumns);
        movieListView.setLayoutManager(layoutManager);

        //movieListView.setItemAnimator(new DefaultItemAnimator());


        loadFavorites();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
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