package com.example.anfio.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anfio.popularmovies.R;
import com.example.anfio.popularmovies.adapters.MovieAdapter;
import com.example.anfio.popularmovies.adapters.MovieFavAdapter;
import com.example.anfio.popularmovies.data.MovieContract;
import com.example.anfio.popularmovies.loaders.MovieApiLoader;
import com.example.anfio.popularmovies.models.Movie;
import com.example.anfio.popularmovies.settings.SettingsActivity;
import com.example.anfio.popularmovies.utilities.Constants;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private MovieFavAdapter mMovieFavAdapter;
    private ProgressBar mProgressBar;
    private TextView mErrorMessage;
    // The two urls to use at this stage
    private final LoaderManager.LoaderCallbacks<Movie[]> movieLoader = new LoaderManager.LoaderCallbacks<Movie[]>() {
        @NonNull
        @Override
        public Loader<Movie[]> onCreateLoader(int id, @Nullable final Bundle args) {
            mProgressBar.setVisibility(View.VISIBLE);
            String url = "";
            if (args != null) {
                url = args.getString(Constants.API_MOVIES_LIST);
            }
            return new MovieApiLoader(mContext, url);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Movie[]> loader, Movie[] data) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (data != null) {
                showMoviesDataView();
                mMovieAdapter.setMovieData(data);
            } else {
                showErrorMessage(getString(R.string.error_message));
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Movie[]> loader) {
            mMovieAdapter.setMovieData(null);
        }
    };
    private final LoaderManager.LoaderCallbacks<Cursor> cursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            mProgressBar.setVisibility(View.VISIBLE);
            return new CursorLoader(mContext, MovieContract.MovieEntry.CONTENT_URI_FAVORITE, null, null, null, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            mProgressBar.setVisibility(View.INVISIBLE);
            boolean hasData = data.getCount() != 0;
            if (hasData) {
                showMoviesDataView();
                mMovieFavAdapter.swapCursor(data);
            } else if (!hasData) {
                showErrorMessage(getString((R.string.error_no_favorites)));
            } else {
                showErrorMessage(getString(R.string.error_message));
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            mMovieFavAdapter.swapCursor(null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rv_movies);
        mErrorMessage = findViewById(R.id.tv_error_message);
        mContext = getApplicationContext();
        // create the grid based on orientation
        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        }
        mRecyclerView.setHasFixedSize(true);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        // start of the process of loading the movies on the app
        loadMovies();
    }

    private void loadMovies() {
        String orderBy = getUrlByPref();
        if (!isOnline() && !orderBy.equals(Constants.STRING_URL_FAVORITE)) {
            showErrorMessage(getString(R.string.error_message_no_connection));
        } else if (orderBy.equals(Constants.STRING_URL_FAVORITE)) {
            mMovieFavAdapter = new MovieFavAdapter(mContext, new MovieFavAdapter.MovieFavAdapterOnClickHandler() {
                @Override
                public void onClick(int id, String title, String imageUrl, String synopsis, double rating, String releaseDate) {
                    // Parcelable used to pass the object to the intent
                    Movie myParcelable = new Movie(id, title, imageUrl, synopsis, rating, releaseDate);
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("myDataKey", myParcelable);
                    startActivity(intent);
                }
            });
            mRecyclerView.setAdapter(mMovieFavAdapter);
            showMoviesDataView();
            getSupportLoaderManager().initLoader(Constants.ID_CURSOR_LOADER, null, cursorLoader);
        } else {
            mMovieAdapter = new MovieAdapter(mContext, new MovieAdapter.MovieAdapterOnClickHandler() {
                @Override
                public void onClick(int id, String title, String imageUrl, String synopsis, double rating, String releaseDate) {
                    // Parcelable used to pass the object to the intent
                    Movie myParcelable = new Movie(id, title, imageUrl, synopsis, rating, releaseDate);
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("myDataKey", myParcelable);
                    startActivity(intent);
                }
            });
            mRecyclerView.setAdapter(mMovieAdapter);
            showMoviesDataView();
            Bundle queryBundle = new Bundle();
            queryBundle.putString(Constants.API_MOVIES_LIST, orderBy);
            getSupportLoaderManager().initLoader(Constants.ID_ASYNCTASK_LOADER, queryBundle, movieLoader);
        }
    }

    private String getUrlByPref() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        // assign the value to a string variable
        String urlToExecute;
        if (orderBy.equals(getString(R.string.settings_order_by_most_popular_value))) {
            urlToExecute = Constants.STRING_URL_POPULAR;
        } else if (orderBy.equals(getString(R.string.settings_order_by_top_rated_value))) {
            urlToExecute = Constants.STRING_URL_TOP_RATED;
        } else {
            urlToExecute = Constants.STRING_URL_FAVORITE;
        }
        return urlToExecute;
    }

    private void showMoviesDataView() {
        // movies are visible, error message is hidden
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String error) {
        // error message is visible, movies are hidden
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setText(error);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            // go to settings
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.refresh) {
            if (!isOnline()) {
                showErrorMessage(getString(R.string.error_message_no_connection));
            } else {
                loadMovies();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}