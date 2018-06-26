package com.example.android.popularmoviesstagetwo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.popularmoviesstagetwo.adapters.ReviewAdapter;
import com.example.android.popularmoviesstagetwo.adapters.TrailerAdapter;
import com.example.android.popularmoviesstagetwo.database.AppExecutors;
import com.example.android.popularmoviesstagetwo.database.FavouriteMoviesDatabase;
import com.example.android.popularmoviesstagetwo.database.MovieViewModel;
import com.example.android.popularmoviesstagetwo.database.MovieViewModelFactory;
import com.example.android.popularmoviesstagetwo.model.Movie;
import com.example.android.popularmoviesstagetwo.model.Review;
import com.example.android.popularmoviesstagetwo.model.ReviewResponse;
import com.example.android.popularmoviesstagetwo.model.Trailer;
import com.example.android.popularmoviesstagetwo.model.TrailerResponse;
import com.example.android.popularmoviesstagetwo.rest.TmdbApiClient;
import com.example.android.popularmoviesstagetwo.rest.TmdbReviewInterface;
import com.example.android.popularmoviesstagetwo.rest.TmdbTrailerInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String API_KEY = "fa0a36c54bae48da04a507ac7ce6126f";
    private Parcelable mStateParcel;

    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185//";
    private Movie mCurrentMovie;
    List<Review> reviews= new ArrayList<>();
    List<Trailer> trailers= new ArrayList<>();
    private ReviewResponse mReviewResponse;
    private TrailerResponse mTrailerResponse;
    public TrailerAdapter.TrailerAdapterOnClickHandler clickHandler;
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailAdapter;
    private Review mCurrentReview;
    private Trailer mCurrentTrailer;
    private FavouriteMoviesDatabase mDb;
    private String SAVED_STATE_KEY;
    boolean togglebuttonstatus = false;
    Context context;
    private boolean mIsFavoriteMovie;
    private static final String STATE_TAG_FAVORITE = "tag_favorite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDb = FavouriteMoviesDatabase.getsInstance(getApplicationContext());
        Intent intent = getIntent();
        mCurrentMovie = intent.getParcelableExtra(getString(R.string.parcel_movie));

        // Get the title string from the Movie object,
        String movieTitle = mCurrentMovie.getTitle();

        // Find the TextView with view ID title
        TextView titleTextView = (TextView) findViewById(R.id.movie_title);

        // Display the title of the current movie in that TextView
        titleTextView.setText(movieTitle);

        // Get the release date string from the Movie object,
        String movieReleaseDate = mCurrentMovie.getReleaseDate();

        // Find the TextView with view ID release date
        TextView releaseDateTextView = (TextView) findViewById(R.id.movie_release_date);

        // Display the release date of the current movie in that TextView
        releaseDateTextView.setText(movieReleaseDate);

        // Find the ImageView with view ID poster
        ImageView posterImageView = (ImageView) findViewById(R.id.movie_poster);

        String poster = POSTER_PATH + mCurrentMovie.getPosterPath();

        Picasso.with(this)
                .load(poster)
                .into(posterImageView);

        // Get the user rating string from the Movie object,
        double movieVoteAverage = mCurrentMovie.getVoteAverage();

        // Find the TextView with view ID user rating
        TextView voteAverageTextView = (TextView) findViewById(R.id.movie_vote_average);

        // Display the rating of the current movie in that TextView
        voteAverageTextView.setText(getString(R.string.label_vote_display, mCurrentMovie.getVoteAverage()));

        // Get the overview string from the Movie object,
        String overview = mCurrentMovie.getOverview();

        // Find the TextView with view ID overview
        TextView overviewTextView = (TextView) findViewById(R.id.movie_overview);

        // Display the overview of the current movie in that TextView
        overviewTextView.setText(overview);

        final ToggleButton addfavouritesbutton = (ToggleButton) findViewById(R.id.add_favourite_button);
        addfavouritesbutton.setBackgroundResource(R.drawable.ic_star_border);
        //addfavouritesbutton.setChecked(false);
       /* addfavouritesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Movie movie = new Movie();
                //mDb.movieDao().insertMovie(movie);
            }
        });*/

       /*addfavouritesbutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               togglebuttonstatus = addfavouritesbutton.isChecked();
               if (!addfavouritesbutton.isChecked())  {

                   saveFavorite();
                   //addfavouritesbutton.setChecked(true);
                   addfavouritesbutton.setBackgroundResource(R.drawable.ic_star);
                   Log.d(TAG, "ADDED");
                   Toast.makeText(getApplicationContext(),"favourited",Toast.LENGTH_SHORT).show();
               }
               else if(addfavouritesbutton.isChecked()){

                   deleteFavorite();
                  // addfavouritesbutton.setChecked(false);
                   addfavouritesbutton.setBackgroundResource(R.drawable.ic_star_border);
                   Log.d(TAG, "DELETED");
                   Toast.makeText(getApplicationContext(),"unfavourited",Toast.LENGTH_SHORT).show();
               }
           }
       });*/


        addfavouritesbutton.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                Log.w("togglebuttonstatus",String.valueOf(togglebuttonstatus));

                togglebuttonstatus = addfavouritesbutton.isChecked();

                if (toggleButton.isChecked()) {
                    //addfavouritesbutton.setChecked(true);
                    saveFavorite();
                    addfavouritesbutton.setBackgroundResource(R.drawable.ic_star);
                    Log.d(TAG, "ADDED");
                    Toast.makeText(getApplicationContext(),"favourited",Toast.LENGTH_SHORT).show();
                }
                else if(!toggleButton.isChecked()){
                    //addfavouritesbutton.setChecked(false);
                    deleteFavorite();
                    addfavouritesbutton.setBackgroundResource(R.drawable.ic_star_border);
                    Log.d(TAG, "DELETED");
                    Toast.makeText(getApplicationContext(),"unfavourited",Toast.LENGTH_SHORT).show();
                }
            }
        });


        final RecyclerView reviewCardView = (RecyclerView) findViewById(R.id.movie_review_recycler_view);
        mReviewAdapter = new ReviewAdapter(reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        reviewCardView.setLayoutManager(layoutManager);
        reviewCardView.setHasFixedSize(true);
        reviewCardView.setAdapter(mReviewAdapter);
        reviewCardView.setItemAnimator(new DefaultItemAnimator());

        TmdbReviewInterface reviewApiService =
                TmdbApiClient.getClient().create(TmdbReviewInterface.class);

        Call<ReviewResponse> reviewCall = reviewApiService.getMovieReviews(mCurrentMovie.getId(), API_KEY);
        reviewCall.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> reviewCall, Response<ReviewResponse> response) {
                Log.d(TAG, reviewCall.request().url().toString());
                int reviewStatusCode = response.code();
                mReviewResponse = response.body();
                mReviewAdapter.setReviewData(mReviewResponse);
                mReviewAdapter.notifyDataSetChanged();
                reviews = mReviewResponse.getReviews();
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.e(TAG, t.toString());

            }
        });

        final RecyclerView trailerCardView = (RecyclerView) findViewById(R.id.movie_trailer_recycler_view);
        mTrailAdapter = new TrailerAdapter(trailers,  this);
        LinearLayoutManager trailerlayoutManager = new LinearLayoutManager(this);
        trailerlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        trailerCardView.setLayoutManager(trailerlayoutManager);
        trailerCardView.setHasFixedSize(true);
        trailerCardView.setAdapter(mTrailAdapter);
        trailerCardView.setItemAnimator(new DefaultItemAnimator());

        TmdbTrailerInterface trailerapiService =
                 TmdbApiClient.getClient().create(TmdbTrailerInterface.class);

        Call<TrailerResponse> trailerCall = trailerapiService.getMovieTrailers(mCurrentMovie.getId(), API_KEY);
        trailerCall.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> trailerCall, Response<TrailerResponse> response) {
                Log.d(TAG, trailerCall.request().url().toString());
                int trailerStatusCode = response.code();
                mTrailerResponse = response.body();
                mTrailAdapter.setTrailerData(mTrailerResponse);
                mTrailAdapter.notifyDataSetChanged();
                trailers = mTrailerResponse.getTrailers();

            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.e(TAG, t.toString());

            }
        });

        MovieViewModelFactory modelFactory = new MovieViewModelFactory(mDb, mCurrentMovie.getId());
        final MovieViewModel movieViewModel = ViewModelProviders.of(this, modelFactory).get(MovieViewModel.class);
        movieViewModel.getMovie().observe(DetailActivity.this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie == null) {
                    movieViewModel.getMovie().removeObserver(this);
                } else {
                    movieViewModel.getMovie().removeObserver(this);
                }
            }
        });

        /*addfavouritesbutton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {


                       MovieViewModelFactory modelFactory = new MovieViewModelFactory(mDb, mCurrentMovie.getId());
                        final MovieViewModel movieViewModel1 = ViewModelProviders.of(DetailActivity.this, modelFactory).get(MovieViewModel.class);
                        movieViewModel.getMovie().observe(DetailActivity.this, new Observer<Movie>() {
                            @Override
                            public void onChanged(@Nullable Movie movie) {

                                if ((favorite) && (movie == null)) {
                                    movieViewModel.getMovie().removeObserver(this);
                                    if (!justDelete) {
                                        saveFavorite();
                                        //TODO BURAYA SET TEXT ON FAVOURITE BUTTON
                                    }
                                    else {
                                        justDelete = false;
                                    }
                                } else if (!(favorite) && (movie != null)) {
                                    movieViewModel.getMovie().removeObserver(this);
                                    //TODO BURAYA SET TEXT ON FAVOURITE BUTTON ve toast ekle
                                    deleteFavorite();
                                    justDelete = true;
                                }
                            }
                        });
                    }

                });*/


    }

    @Override
    public void onClick(Trailer trailer) {
        Uri url = Uri.parse("https://www.youtube.com/watch?v=" + trailer.getKey());

        Intent intent = new Intent(Intent.ACTION_VIEW, url);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(intent);
    }

    public void saveFavorite() {
        final Movie favoriteMovie = new Movie(
                mCurrentMovie.getId(),
                mCurrentMovie.getTitle(),
                mCurrentMovie.getReleaseDate(),
                mCurrentMovie.getVoteAverage(),
                mCurrentMovie.getOverview(),
                mCurrentMovie.getPosterPath()
        );

        final FavouriteMoviesDatabase database = FavouriteMoviesDatabase.getsInstance(context);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //if (!(movie.getId().equals(database.movieDao().loadMovieById(movie.getId()).getValue().getId()))) {
                database.movieDao().insertMovie(favoriteMovie);
                //}
            }
        });
    }

    private void deleteFavorite() {
        final Movie favoriteMovie = new Movie(
                mCurrentMovie.getId(),
                mCurrentMovie.getTitle(),
                mCurrentMovie.getReleaseDate(),
                mCurrentMovie.getVoteAverage(),
                mCurrentMovie.getOverview(),
                mCurrentMovie.getPosterPath()
        );
        final FavouriteMoviesDatabase database = FavouriteMoviesDatabase.getsInstance(context);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //if (movie.getId().equals(database.movieDao().loadMovieById(movie.getId()).getValue().getId())) {
                database.movieDao().deleteMovie(favoriteMovie);
                //}
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Bundle bundle = new Bundle();

        outState.putParcelable(SAVED_STATE_KEY, mCurrentMovie);
        outState.putBoolean(STATE_TAG_FAVORITE, mIsFavoriteMovie);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mStateParcel = ((Bundle) savedInstanceState).getParcelable(SAVED_STATE_KEY);
            mIsFavoriteMovie = savedInstanceState.getBoolean(STATE_TAG_FAVORITE);
        }
    }
}