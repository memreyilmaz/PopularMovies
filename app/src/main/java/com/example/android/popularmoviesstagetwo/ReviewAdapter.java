package com.example.android.popularmoviesstagetwo;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    private List<Review> reviews;
    private ReviewResponse mReviewResponse;
    private Context context;


    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView reviewAuthorTextView;
        public TextView reviewTextView;
        public CardView reviewCardView;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            reviewAuthorTextView = view.findViewById(R.id.movie_review_author);
            reviewTextView = view.findViewById(R.id.movie_review);
            reviewCardView = view.findViewById(R.id.movie_review_card_view);
        }

    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);

        return new ReviewAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {

        Review review = mReviewResponse.getReviews().get(position);

        holder.reviewAuthorTextView.setText(review.getAuthor());
        holder.reviewTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (null == mReviewResponse) return 0;
        return mReviewResponse.getReviews().size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setReviewData(ReviewResponse reviewResponse) {
        mReviewResponse = reviewResponse;
        reviews = mReviewResponse.getReviews();
        notifyDataSetChanged();
    }

}