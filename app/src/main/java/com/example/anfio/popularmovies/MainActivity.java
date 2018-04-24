package com.example.anfio.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anfio.popularmovies.adapters.MovieAdapter;
import com.example.anfio.popularmovies.loaders.MovieApiLoader;
import com.example.anfio.popularmovies.models.Movie;
import com.example.anfio.popularmovies.settings.SettingsActivity;
import com.example.anfio.popularmovies.utilities.Constants;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler {

    public Context mContext;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mProgressBar;
    private TextView mErrorMessage;
    // The two urls to use at this stage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);
        mErrorMessage = findViewById(R.id.tv_error_message);

        // GridLayout will be used
        RecyclerView.LayoutManager layoutManager
                = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mContext = getApplicationContext();

        // check if connection is available
        if (!isOnline()) {
            showErrorMessage(getString(R.string.error_message_no_connection));
        } else {
            // start of the process of loading the movies on the app
            loadMovies();
        }
    }

    private void loadMovies() {
        showMoviesDataView();
        // get shared preference value
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        // assign the value to a string variable
        String urlToExecute = "";
        if (orderBy.equals(getString(R.string.settings_order_by_most_popular_value))) {
            urlToExecute = Constants.STRING_URL_POPULAR;
        } else if (orderBy.equals(getString(R.string.settings_order_by_top_rated_value))) {
            urlToExecute = Constants.STRING_URL_TOP_RATED;
        }
        Bundle queryBundle = new Bundle();
        queryBundle.putString(Constants.API_MOVIES_LIST, urlToExecute);
        getSupportLoaderManager().initLoader(Constants.ID_ASYNCTASK_LOADER, queryBundle, movieLoader);
    }

    private LoaderManager.LoaderCallbacks<Movie[]> movieLoader = new LoaderManager.LoaderCallbacks<Movie[]>() {
        @NonNull
        @Override
        public Loader<Movie[]> onCreateLoader(int id, @Nullable final Bundle args) {
            mProgressBar.setVisibility(View.VISIBLE);
            String url = "";
            if (args != null){
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

        }
    };

    /*private LoaderManager.LoaderCallbacks<Cursor> cursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            mProgressBar.setVisibility(View.VISIBLE);
            return new CursorLoader(mContext, MovieContract.MovieEntry.CONTENT_URI_FAVORITE, null, null, null, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                showMoviesDataView();
                mMovieCursorAdapter = new MovieCursorAdapter(mContext, data, movieCursorAdapterOnClickHandler);
                mRecyclerView.setAdapter(mMovieCursorAdapter);
                mMovieCursorAdapter.swapCursor(data);
            } else {
                showErrorMessage(getString(R.string.error_message));
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    };*/

    /*private void insertMoviesIntoDb(Movie[] moviesForDb, String urlString) {
        Uri contentUri;
        switch (urlString) {
            case Constants.STRING_URL_POPULAR:
                contentUri = MovieContract.MovieEntry.CONTENT_URI_POPULAR;
                break;
            case Constants.STRING_URL_TOP_RATED:
                contentUri = MovieContract.MovieEntry.CONTENT_URI_TOP_RATED;
                break;
            default:
                return;
        }
        ContentValues[] movieValuesArr = new ContentValues[moviesForDb.length];
        for (int i = 0; i < moviesForDb.length; i++) {
            int id = moviesForDb[i].getId();
            String title = moviesForDb[i].getTitle();
            String poster = moviesForDb[i].getImageUrl();
            String synopsis = moviesForDb[i].getSynopsis();
            double rating = moviesForDb[i].getRating();
            String releaseDate = moviesForDb[i].getReleaseDate();

            movieValuesArr[i] = new ContentValues();
            movieValuesArr[i].put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
            movieValuesArr[i].put(MovieContract.MovieEntry.COLUMN_TITLE, title);
            movieValuesArr[i].put(MovieContract.MovieEntry.COLUMN_POSTER, poster);
            movieValuesArr[i].put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, synopsis);
            movieValuesArr[i].put(MovieContract.MovieEntry.COLUMN_RATING, rating);
            movieValuesArr[i].put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        }

        // Insert new movies data via a ContentResolver

        int deletedRows = getContentResolver().delete(contentUri, null, null);
        int insertedRows = getContentResolver().bulkInsert(contentUri, movieValuesArr);

    }*/

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

    @Override
    public void onClick(int id, String title, String imageUrl, String synopsis, double rating, String releaseDate) {
        Context context = this;
        // Parcelable used to pass the object to the intent
        Movie myParcelable = new Movie(id, title, imageUrl, synopsis, rating, releaseDate);
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("myDataKey", myParcelable);
        startActivity(intent);
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
        } else if (item.getItemId() == R.id.refresh){
            if (!isOnline()){
                showErrorMessage(getString(R.string.error_message_no_connection));
            } else {
                loadMovies();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}