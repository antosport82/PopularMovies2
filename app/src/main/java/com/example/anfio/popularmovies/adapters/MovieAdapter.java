package com.example.anfio.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.anfio.popularmovies.R;
import com.example.anfio.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by anfio on 28/02/2018 - Popular Movies App - Udacity Project
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context mContext;
    private Movie[] mMovieData;
    public static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/w342";

    //on-click handler defined to make it easy for an Activity to interface with
    private final MovieAdapterOnClickHandler mClickHandler;

    // The interface that receives onClick messages.
    public interface MovieAdapterOnClickHandler {
        void onClick(int id, String title, String imageUrl, String synopsis, double rating, String releaseDate);
    }

    /**
     * Creates a MovieAdapter.
     *
     * @param context      The context
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        super();
        mContext = context;
        mClickHandler = clickHandler;
    }

    // custom ViewHolder that implements OnClickListener
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView imageView;

        // setup the onClickListener on the imageView
        MovieViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_movie_picture);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            // get movie data at the current position
            int id = mMovieData[adapterPosition].getId();
            String title = mMovieData[adapterPosition].getTitle();
            String imageUrl = mMovieData[adapterPosition].getImageUrl();
            String synopsis = mMovieData[adapterPosition].getSynopsis();
            double rating = mMovieData[adapterPosition].getRating();
            String releaseDate = mMovieData[adapterPosition].getReleaseDate();
            // call onClick and pass the movie data
            mClickHandler.onClick(id, title, imageUrl, synopsis, rating, releaseDate);
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutForItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutForItem, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        // build image path
        String path = BASE_URL_IMAGE + mMovieData[position].getImageUrl();
        Picasso.with(holder.imageView.getContext()).load(path).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) {
            return 0;
        } else {
            return mMovieData.length;
        }
    }

    public void setMovieData(Movie[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}