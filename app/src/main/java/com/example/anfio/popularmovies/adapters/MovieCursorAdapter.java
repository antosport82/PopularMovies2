package com.example.anfio.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.anfio.popularmovies.R;
import com.example.anfio.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

public class MovieCursorAdapter extends RecyclerView.Adapter<MovieCursorAdapter.MovieViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;
    public static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/w342";

    //on-click handler defined to make it easy for an Activity to interface with
    private final MovieCursorAdapterOnClickHandler mClickHandler;

    // The interface that receives onClick messages.
    public interface MovieCursorAdapterOnClickHandler {
        void onClick(int id, String title, String imageUrl, String synopsis, double rating, String releaseDate);
    }

    /**
     * Constructor for the MovieCursorAdapter that initializes the Context.
     *
     * @param context the current Context
     * @param cursor the  Cursor
     */
    public MovieCursorAdapter(Context context, Cursor cursor, MovieCursorAdapter.MovieCursorAdapterOnClickHandler clickHandler) {
        this.mContext = context;
        this.mCursor = cursor;
        this.mClickHandler = clickHandler;
    }

    // Inner class for creating ViewHolders
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        // Class variables
        public final ImageView imageView;

        /**
         * Constructor for the MovieViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public MovieViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_movie_picture);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            // get movie data at the current position
            mCursor.moveToPosition(adapterPosition);
            int id = mCursor.getInt(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
            String title = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
            String imageUrl = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER));
            String synopsis = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS));
            double rating = mCursor.getDouble(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
            String releaseDate = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
            // call onClick and pass the movie data
            mClickHandler.onClick(id, title, imageUrl, synopsis, rating, releaseDate);
        }
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_list_item, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCursorAdapter.MovieViewHolder holder, int position) {
        // Move the mCursor to the position of the movie to be displayed
        if (mCursor.moveToPosition(position)){
            // build image path
            String path = BASE_URL_IMAGE + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER));
            Picasso.with(holder.imageView.getContext())
                    .load(path)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .into(holder.imageView);
        }
    }

    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
}