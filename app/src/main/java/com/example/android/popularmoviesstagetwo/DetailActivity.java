package com.example.android.popularmoviesstagetwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185//";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.parcel_movie));

        // Get the title string from the Movie object,
        String movieTitle = movie.getTitle();

        // Find the TextView with view ID title
        TextView titleTextView = (TextView) findViewById(R.id.movie_title);

        // Display the title of the current movie in that TextView
        titleTextView.setText(movieTitle);

        // Get the release date string from the Movie object,
        String movieReleaseDate = movie.getReleaseDate();

        // Find the TextView with view ID release date
        TextView releaseDateTextView = (TextView) findViewById(R.id.movie_release_date);

        // Display the release date of the current movie in that TextView
        releaseDateTextView.setText(movieReleaseDate);

        // Find the ImageView with view ID poster
        ImageView posterImageView = (ImageView) findViewById(R.id.movie_poster);

        String poster = POSTER_PATH + movie.getMoviePoster();

        Picasso.with(this)
                .load(poster)
                .into(posterImageView);

        // Get the user rating string from the Movie object,
        String movieUserRating = movie.getUserRating();

        // Find the TextView with view ID user rating
        TextView ratingTextView = (TextView) findViewById(R.id.movie_user_rating);

        // Display the rating of the current movie in that TextView
        ratingTextView.setText(movieUserRating);

        // Get the plot synopsis string from the Movie object,
        String moviePlotSynopsis = movie.getPlotSynopsis();

        // Find the TextView with view ID plotsynopsis
        TextView plotsynopsisTextView = (TextView) findViewById(R.id.movie_plot_synopsis);

        // Display the plot synopsis of the current movie in that TextView
        plotsynopsisTextView.setText(moviePlotSynopsis);
    }
}