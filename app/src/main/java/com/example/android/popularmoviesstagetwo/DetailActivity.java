package com.example.android.popularmoviesstagetwo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185//";
    private Movie mCurrentMovie;
    List<Review> reviews= new ArrayList<>();
    List<Trailer> trailers= new ArrayList<>();
    private ReviewResponse mReviewResponse;
    private TrailerResponse mTrailerResponse;
    //public TrailerAdapter.TrailerAdapterOnClickHandler clickHandler;
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailAdapter;
    private FavouriteMoviesDatabase mDb;
    boolean togglebuttonstatus = false;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDb = FavouriteMoviesDatabase.getsInstance(getApplicationContext());
        Intent intent = getIntent();
        mCurrentMovie = intent.getParcelableExtra(getString(R.string.parcel_movie));

        String movieTitle = mCurrentMovie.getTitle();
        TextView titleTextView = (TextView) findViewById(R.id.movie_title);
        titleTextView.setText(movieTitle);

        String movieReleaseDate = mCurrentMovie.getReleaseDate();
        TextView releaseDateTextView = (TextView) findViewById(R.id.movie_release_date);
        releaseDateTextView.setText(movieReleaseDate);

        ImageView posterImageView = (ImageView) findViewById(R.id.movie_poster);
        String poster = POSTER_PATH + mCurrentMovie.getPosterPath();
        Picasso.with(this)
                .load(poster)
                .into(posterImageView);

        TextView voteAverageTextView = (TextView) findViewById(R.id.movie_vote_average);
        voteAverageTextView.setText(getString(R.string.label_vote_display, mCurrentMovie.getVoteAverage()));

        String overview = mCurrentMovie.getOverview();
        TextView overviewTextView = (TextView) findViewById(R.id.movie_overview);
        overviewTextView.setText(overview);

        final ToggleButton addfavouritesbutton = (ToggleButton) findViewById(R.id.add_favourite_button);
        if (addfavouritesbutton.isChecked()) {
            addfavouritesbutton.setBackgroundResource(R.drawable.ic_star);
        }
        else if(!addfavouritesbutton.isChecked()){
            addfavouritesbutton.setBackgroundResource(R.drawable.ic_star_border);
        }

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

        outState.putParcelable("movieDetail", mCurrentMovie);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);

        if (outState != null) {
            mCurrentMovie = ((Bundle) outState).getParcelable("movieDetail");
        }
    }
}