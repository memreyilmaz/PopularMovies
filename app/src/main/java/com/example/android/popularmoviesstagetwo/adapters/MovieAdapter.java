package com.example.android.popularmoviesstagetwo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstagetwo.R;
import com.example.android.popularmoviesstagetwo.model.Movie;
import com.example.android.popularmoviesstagetwo.model.MovieResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185//";
    private List<Movie> mMovies;
    private MovieResponse mMovieResponse;
    private Context context;
    private MovieAdapterOnClickHandler movieAdapterOnClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MovieAdapter(List<Movie> movies, MovieAdapterOnClickHandler movieAdapterOnClickHandler) {
        this.movieAdapterOnClickHandler = movieAdapterOnClickHandler;
        this.mMovies = movies;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView posterImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            posterImageView = view.findViewById(R.id.movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies.get(adapterPosition);
            movieAdapterOnClickHandler.onClick(movie);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        Movie movie = mMovies.get(position);
        String poster = POSTER_PATH + movie.getPosterPath();
        Picasso.with(context)
                .load(poster)
                .resize(506, 759)
                .centerCrop()
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovies) return 0;
        return mMovies.size();
    }

    public List<Movie> getMovies () {
        return mMovies;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setMovieData(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }
}








