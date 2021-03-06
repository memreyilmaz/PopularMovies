package com.example.android.popularmoviesstagetwo.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstagetwo.R;
import com.example.android.popularmoviesstagetwo.model.Review;
import com.example.android.popularmoviesstagetwo.model.ReviewResponse;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private List<Review> reviews;
    private ReviewResponse mReviewResponse;

    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView reviewAuthorTextView;
        private TextView reviewTextView;
        private CardView reviewCardView;

        private ReviewAdapterViewHolder(View view) {
            super(view);
            reviewAuthorTextView = view.findViewById(R.id.movie_review_author);
            reviewTextView = view.findViewById(R.id.movie_review);
            reviewCardView = view.findViewById(R.id.movie_review_card_view);
        }
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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