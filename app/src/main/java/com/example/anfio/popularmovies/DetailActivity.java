package com.example.anfio.popularmovies;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anfio.popularmovies.adapters.MovieApiAdapter;
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

public class DetailActivity extends AppCompatActivity implements VideoApiAdapter.VideoApiAdapterOnClickHandler,
        ReviewApiAdapter.ReviewApiAdapterOnClickHandler {

    private TextView titleTv;
    private ImageView imageUrlIv;
    private TextView synopsisTv;
    private TextView ratingTv;
    private TextView releaseDateTv;
    private ImageView favIconIv;
    private RecyclerView videoRecyclerView;
    private RecyclerView reviewRecyclerView;
    private VideoApiAdapter mVideoApiAdapter;
    private ReviewApiAdapter mReviewApiAdapter;
    //private MovieFavAdapter mMovieFavAdapter;
    public Context mContext;
    private boolean favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleTv = findViewById(R.id.tv_title);
        imageUrlIv = findViewById(R.id.iv_movie_image);
        synopsisTv = findViewById(R.id.tv_synopsis);
        ratingTv = findViewById(R.id.tv_rating);
        releaseDateTv = findViewById(R.id.tv_release_date);
        favIconIv = findViewById(R.id.iv_fav_icon);
        mContext = getApplicationContext();
        favorite = false;

        videoRecyclerView = findViewById(R.id.rv_videos);
        RecyclerView.LayoutManager videoLayoutManager
                = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        videoRecyclerView.setLayoutManager(videoLayoutManager);
        videoRecyclerView.setHasFixedSize(true);
        mVideoApiAdapter = new VideoApiAdapter(this);
        videoRecyclerView.setAdapter(mVideoApiAdapter);

        reviewRecyclerView = findViewById(R.id.rv_review);
        RecyclerView.LayoutManager reviewLayoutManager
                = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewRecyclerView.setHasFixedSize(true);
        mReviewApiAdapter = new ReviewApiAdapter(this);
        reviewRecyclerView.setAdapter(mReviewApiAdapter);

        // get Movie data from the intent (Parcelable is used)
        Movie myParcelable = getIntent().getParcelableExtra("myDataKey");
        int id = myParcelable.getId();
        String title = myParcelable.getTitle();
        String imageUrl = myParcelable.getImageUrl();
        String synopsis = myParcelable.getSynopsis();
        double rating = myParcelable.getRating();
        String releaseDate = myParcelable.getReleaseDate();
        boolean favorite = false;

        titleTv.setText(title);
        // build complete image path
        String path = MovieApiAdapter.BASE_URL_IMAGE + imageUrl;
        Picasso.with(this).load(path).into(imageUrlIv);
        synopsisTv.setText(synopsis);
        ratingTv.setText(String.valueOf(rating));
        releaseDateTv.setText(releaseDate);

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
        getSupportLoaderManager().initLoader(Constants.ID_CURSOR_LOADER_FAVORITE, queryBundleFavorite, favoriteLoader);
    }

    private LoaderManager.LoaderCallbacks<Video[]> videoLoader = new LoaderManager.LoaderCallbacks<Video[]>() {
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

    private LoaderManager.LoaderCallbacks<Review[]> reviewLoader = new LoaderManager.LoaderCallbacks<Review[]>() {
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

    private LoaderManager.LoaderCallbacks<Cursor> favoriteLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
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
            if ((!(data.moveToFirst()) || data.getCount() ==0)) {
                favIconIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_48dp));
            } else {
                favIconIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_48dp));
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    };

    @Override
    public void onClick(String key) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = Constants.BASE_URL_VIDEO + key;
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}