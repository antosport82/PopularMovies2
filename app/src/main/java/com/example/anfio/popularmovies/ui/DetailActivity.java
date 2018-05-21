package com.example.anfio.popularmovies.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anfio.popularmovies.R;
import com.example.anfio.popularmovies.adapters.MovieAdapter;
import com.example.anfio.popularmovies.adapters.ReviewApiAdapter;
import com.example.anfio.popularmovies.adapters.VideoApiAdapter;
import com.example.anfio.popularmovies.data.MovieContract;
import com.example.anfio.popularmovies.loaders.ReviewApiLoader;
import com.example.anfio.popularmovies.loaders.VideoApiLoader;
import com.example.anfio.popularmovies.models.Movie;
import com.example.anfio.popularmovies.models.Review;
import com.example.anfio.popularmovies.models.Video;
import com.example.anfio.popularmovies.utilities.Constants;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageView favIconIv;
    private VideoApiAdapter mVideoApiAdapter;
    private ReviewApiAdapter mReviewApiAdapter;
    private Context mContext;
    private boolean mFavorite;

    private final LoaderManager.LoaderCallbacks<Video[]> videoLoader = new LoaderManager.LoaderCallbacks<Video[]>() {
        @NonNull
        @Override
        public Loader<Video[]> onCreateLoader(int id, @Nullable Bundle args) {
            String url = "";
            if (args != null) {
                url = args.getString(Constants.API_MOVIES_VIDEOS);
            }
            return new VideoApiLoader(mContext, url);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Video[]> loader, Video[] data) {
            if (data != null) {
                mVideoApiAdapter.setVideoData(data);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Video[]> loader) {
        }
    };

    private final LoaderManager.LoaderCallbacks<Review[]> reviewLoader = new LoaderManager.LoaderCallbacks<Review[]>() {
        @NonNull
        @Override
        public Loader<Review[]> onCreateLoader(int id, @Nullable Bundle args) {
            String url = "";
            if (args != null) {
                url = args.getString(Constants.API_MOVIES_REVIEWS);
            }
            return new ReviewApiLoader(mContext, url);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Review[]> loader, Review[] data) {
            if (data != null) {
                mReviewApiAdapter.setReviewData(data);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Review[]> loader) {
        }
    };

    private final LoaderManager.LoaderCallbacks<Cursor> favoriteLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            String movie_id = null;
            if (args != null) {
                movie_id = args.getString(Constants.ID_MOVIE_FAVORITE);
            }
            String selection = "movie_id = ?";
            String[] selectionArgs = new String[]{String.valueOf(movie_id)};
            return new CursorLoader(mContext, MovieContract.MovieEntry.CONTENT_URI_FAVORITE, null, selection, selectionArgs, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if ((!(data.moveToFirst()) || data.getCount() == 0)) {
                favIconIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_48dp));
                mFavorite = false;
            } else {
                favIconIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_48dp));
                mFavorite = true;
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // get Movie data from the intent (Parcelable is used)
        final Movie myParcelable = getIntent().getParcelableExtra("myDataKey");
        final int id = myParcelable.getId();
        String title = myParcelable.getTitle();
        String imageUrl = myParcelable.getImageUrl();
        String synopsis = myParcelable.getSynopsis();
        double rating = myParcelable.getRating();
        String releaseDate = myParcelable.getReleaseDate();

        //ImageView topImageIv = findViewById(R.id.iv_top_movie_image);
        TextView titleTv = findViewById(R.id.tv_title);
        ImageView imageUrlIv = findViewById(R.id.iv_movie_image);
        TextView synopsisTv = findViewById(R.id.tv_synopsis);
        TextView ratingTv = findViewById(R.id.tv_rating);
        TextView releaseDateTv = findViewById(R.id.tv_release_date);
        favIconIv = findViewById(R.id.iv_fav_icon);
        mContext = getApplicationContext();
        mFavorite = false;

        RecyclerView videoRecyclerView = findViewById(R.id.rv_videos);
        RecyclerView.LayoutManager videoLayoutManager
                = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        videoRecyclerView.setLayoutManager(videoLayoutManager);
        videoRecyclerView.setHasFixedSize(true);
        mVideoApiAdapter = new VideoApiAdapter(new VideoApiAdapter.VideoApiAdapterOnClickHandler() {
            @Override
            public void onClick(String key) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String url = Constants.BASE_URL_VIDEO + key;
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        videoRecyclerView.setAdapter(mVideoApiAdapter);
        RecyclerView reviewRecyclerView = findViewById(R.id.rv_review);
        RecyclerView.LayoutManager reviewLayoutManager
                = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewRecyclerView.setHasFixedSize(true);
        mReviewApiAdapter = new ReviewApiAdapter(new ReviewApiAdapter.ReviewApiAdapterOnClickHandler() {
            @Override
            public void onClick(String id, String author, String content) {
                Review myParcelable = new Review(id, author, content);
                Intent intent = new Intent(mContext, DetailActivityExtra.class);
                intent.putExtra("myReviewKey", myParcelable);
                startActivity(intent);
            }
        });
        reviewRecyclerView.setAdapter(mReviewApiAdapter);
        titleTv.setText(title);
        // build complete image path
        String path = MovieAdapter.BASE_URL_IMAGE + imageUrl;
        Picasso.with(this).load(path).into(imageUrlIv);
        synopsisTv.setText(synopsis);
        ratingTv.setText(String.valueOf(rating));
        releaseDateTv.setText(releaseDate);

        favIconIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFavorite) {
                    mFavorite = true;
                    favIconIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_48dp));
                    insertMovieIntoDb(myParcelable, Constants.STRING_URL_FAVORITE);
                } else {
                    mFavorite = false;
                    favIconIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_48dp));
                    deleteMovieFromDb(id, Constants.STRING_URL_FAVORITE);
                }
            }
        });

        loadData(id);
    }

    private void loadData(int id) {
        String stringVideos = Constants.STRING_URL_VIDEOS_I + id + Constants.STRING_URL_VIDEOS_II;
        String stringReviews = Constants.STRING_URL_REVIEWS_I + id + Constants.STRING_URL_REVIEWS_II;
        Bundle queryBundleVideos = new Bundle();
        queryBundleVideos.putString(Constants.API_MOVIES_VIDEOS, stringVideos);
        Bundle queryBundleReviews = new Bundle();
        queryBundleReviews.putString(Constants.API_MOVIES_REVIEWS, stringReviews);
        Bundle queryBundleFavorite = new Bundle();
        queryBundleFavorite.putString(Constants.ID_MOVIE_FAVORITE, String.valueOf(id));
        getSupportLoaderManager().initLoader(Constants.ID_ASYNCTASK_LOADER_DETAIL_VIDEOS, queryBundleVideos, videoLoader);
        getSupportLoaderManager().initLoader(Constants.ID_ASYNCTASK_LOADER_DETAIL_REVIEWS, queryBundleReviews, reviewLoader);
        getSupportLoaderManager().initLoader(Constants.ID_CURSOR_LOADER_FAVORITE_CHECK, queryBundleFavorite, favoriteLoader);
    }

    private void insertMovieIntoDb(Movie movie, String urlString) {
        Uri contentUri;
        switch (urlString) {
            case Constants.STRING_URL_FAVORITE:
                contentUri = MovieContract.MovieEntry.CONTENT_URI_FAVORITE;
                break;
            default:
                return;
        }
        ContentValues contentValue = new ContentValues();

        int id = movie.getId();
        String title = movie.getTitle();
        String poster = movie.getImageUrl();
        String synopsis = movie.getSynopsis();
        double rating = movie.getRating();
        String releaseDate = movie.getReleaseDate();

        contentValue.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
        contentValue.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        contentValue.put(MovieContract.MovieEntry.COLUMN_POSTER, poster);
        contentValue.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, synopsis);
        contentValue.put(MovieContract.MovieEntry.COLUMN_RATING, rating);
        contentValue.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);

        // Insert a new movie via a ContentResolver
        Uri insertedMovie = getContentResolver().insert(contentUri, contentValue);
        if (insertedMovie != null) {
            Toast.makeText(DetailActivity.this, "The movie has been added to your favorites.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteMovieFromDb(int id, String urlString) {
        Uri contentUri;
        switch (urlString) {
            case Constants.STRING_URL_FAVORITE:
                contentUri = MovieContract.MovieEntry.buildMovieUriWithId(id);
                break;
            default:
                return;
        }

        // Delete a movie via a ContentResolver
        int deletedRows = getContentResolver().delete(contentUri, null, null);
        if (deletedRows > 0) {
            Toast.makeText(DetailActivity.this, "The movie has been deleted from your favorites.", Toast.LENGTH_SHORT).show();
        }
    }
}