package com.example.android.popularmoviesstagetwo.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstagetwo.R;
import com.example.android.popularmoviesstagetwo.model.Trailer;
import com.example.android.popularmoviesstagetwo.model.TrailerResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    public static final String TRAILER_PATH_PREFIX = "http://img.youtube.com/vi/";
    public static final String TRAILER_PATH_SUFFIX = "/0.jpg";
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
        private ImageView trailerImageView;
        private TextView trailerTextView;
        private CardView trailerCardView;

        private TrailerAdapterViewHolder(View view) {
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);

        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {

        Trailer trailer = mTrailerResponse.getTrailers().get(position);
        String youtubetrailerthumbnail = TRAILER_PATH_PREFIX + trailer.getKey() + TRAILER_PATH_SUFFIX;
        Picasso.with(context)
                .load(youtubetrailerthumbnail)
                //.resize(506, 759)
                //.centerCrop()
                .into(holder.trailerImageView);
        holder.trailerTextView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerResponse) return 0;
        return mTrailerResponse.getTrailers().size();
    }

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