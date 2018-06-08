package com.example.android.popularmoviesstagetwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185//";
    private Movie mCurrentMovie;

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
    }
}