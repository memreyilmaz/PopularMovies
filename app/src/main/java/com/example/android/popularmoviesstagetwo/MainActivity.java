package com.example.android.popularmoviesstagetwo;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,LoaderManager.LoaderCallbacks<List<Movie>>{

    /** URL for movie data from the TMDB dataset */
    private static final String TMDB_REQUEST_URL =
            "https://api.themoviedb.org/3/movie";

    private static final String API_KEY = "fa0a36c54bae48da04a507ac7ce6126f";
    /**
     * Constant value for the movie loader ID.
     */
    private static final int MOVIE_LOADER_ID = 1;
    List<Movie> movies= new ArrayList<>();

    public MovieAdapter.MovieAdapterOnClickHandler clickHandler;
    /**
     * Adapter for the list of movies
     */
    private MovieAdapter mAdapter;
    private Movie movie;
    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link GridView} in the layout
        RecyclerView movieListView = (RecyclerView) findViewById(R.id.movie_image);
        mAdapter = new MovieAdapter(movies, clickHandler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        movieListView.setLayoutManager(layoutManager);
        movieListView.setHasFixedSize(true);
        movieListView.setAdapter(mAdapter);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        //movieListView.setEmptyView(mEmptyStateTextView);
        // Create a new adapter that takes an empty list of movies as input
        //movieListView.setOnClickListener();

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
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
        }
    }

    // onCreateLoader instantiates and returns a new Loader for the given ID
    @Override
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
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        // Clear the adapter of previous movie data
        mAdapter.clear();

        // If there is a valid list of {@link Movie), then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (movies != null && !movies.isEmpty()) {
            mAdapter.addAll(movies);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset (Loader < List < Movie >> loader) {
        mAdapter.clear();
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
        if (id == R.id.action_settings) {
             Intent sortIntent = new Intent(this, SortActivity.class);
             startActivity(sortIntent);
             return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view, int position) {
        Context context = this;
        //mCurrentMovie = movie;

        movie = movies.get(position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(getResources().getString(R.string.parcel_movie), movie);

        startActivity(intent);
    }
}