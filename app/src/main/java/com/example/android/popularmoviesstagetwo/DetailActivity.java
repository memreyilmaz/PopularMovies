package com.example.android.popularmoviesstagetwo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
    public TrailerAdapter.TrailerAdapterOnClickHandler clickHandler;
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailAdapter;
    private Review mCurrentReview;
    private Trailer mCurrentTrailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
        //double movieVoteAverage = movie.getVoteAverage();

        // Find the TextView with view ID user rating
        //TextView voteAverageTextView = (TextView) findViewById(R.id.movie_vote_average);

        // Display the rating of the current movie in that TextView
        //voteAverageTextView.setText(getString(R.string.label_vote_display, movie.getVoteAverage()));

        // Get the overview string from the Movie object,
        String overview = mCurrentMovie.getOverview();

        // Find the TextView with view ID overview
        TextView overviewTextView = (TextView) findViewById(R.id.movie_overview);

        // Display the overview of the current movie in that TextView
        overviewTextView.setText(overview);


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
            }

    @Override
    public void onClick(Trailer trailer) {
        Uri url = Uri.parse("https://www.youtube.com/watch?v=" + trailer.getKey());

        Intent intent = new Intent(Intent.ACTION_VIEW, url);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(intent);
    }
}