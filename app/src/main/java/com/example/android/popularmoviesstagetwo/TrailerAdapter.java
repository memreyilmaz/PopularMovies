package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();

    private List<Trailer> trailers;
    private TrailerResponse mTrailerResponse;
    private Context context;
    private TrailerAdapter.TrailerAdapterOnClickHandler trailerAdapterOnClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }


    public TrailerAdapter(List<Trailer> trailers, TrailerAdapterOnClickHandler trailerAdapterOnClickHandler) {
        this.trailerAdapterOnClickHandler = trailerAdapterOnClickHandler;
        this.trailers = trailers;
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView trailerImageView;
        public TextView trailerTextView;
        public CardView trailerCardView;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            trailerImageView = view.findViewById(R.id.movie_trailer_play);
            trailerTextView = view.findViewById(R.id.movie_trailer);
            trailerCardView = view.findViewById(R.id.movie_trailer_card_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Trailer trailer = mTrailerResponse.getTrailers().get(adapterPosition);
            trailerAdapterOnClickHandler.onClick(trailer);
        }
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);

        return new TrailerAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {

        Trailer trailer = mTrailerResponse.getTrailers().get(position);

        holder.trailerImageView.setImageResource(R.drawable.ic_play_circle_filled);
        holder.trailerTextView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerResponse) return 0;
        return mTrailerResponse.getTrailers().size();
    }

    /*public void addAll(List<Movie> movie) {
        if (movies != null)
            movies.clear();
        movies.addAll(movie);
        notifyDataSetChanged();
    }*/

    /*public void clear() {
        if (movies != null)
            movies.clear();
        notifyDataSetChanged();
    }*/

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setTrailerData(TrailerResponse trailerResponse) {
        mTrailerResponse = trailerResponse;
        trailers = mTrailerResponse.getTrailers();
        notifyDataSetChanged();
    }

}