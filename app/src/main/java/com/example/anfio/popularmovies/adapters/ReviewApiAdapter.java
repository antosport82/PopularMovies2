package com.example.anfio.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anfio.popularmovies.R;
import com.example.anfio.popularmovies.models.Review;

public class ReviewApiAdapter extends RecyclerView.Adapter<ReviewApiAdapter.ReviewViewHolder>  {


    private Review[] mReviewData;
    private final ReviewApiAdapter.ReviewApiAdapterOnClickHandler mClickHandler;

    // The interface that receives onClick messages.
    public interface ReviewApiAdapterOnClickHandler {
        void onClick(String id, String author, String content);
    }

    /**
     * Creates a ReviewApiAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public ReviewApiAdapter(ReviewApiAdapter.ReviewApiAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForItem, parent, false);
        return new ReviewApiAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        String author = mReviewData[position].getAuthor();
        String content = mReviewData[position].getContent();
        holder.textViewAuthor.setText(author);
        holder.textViewContent.setText(content);
    }

    @Override
    public int getItemCount() {
        if (null == mReviewData) {
            return 0;
        } else {
            return mReviewData.length;
        }
    }

    // custom ViewHolder that implements OnClickListener
    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView textViewAuthor;
        final TextView textViewContent;

        ReviewViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.tv_author);
            textViewContent = itemView.findViewById(R.id.tv_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            // get movie data at the current position
            String id = mReviewData[adapterPosition].getId();
            String author = mReviewData[adapterPosition].getAuthor();
            String content = mReviewData[adapterPosition].getContent();
            // call onClick and pass the review data
            mClickHandler.onClick(id, author, content);
        }
    }

    public void setReviewData(Review[] reviewData) {
        mReviewData = reviewData;
        notifyDataSetChanged();
    }
}
