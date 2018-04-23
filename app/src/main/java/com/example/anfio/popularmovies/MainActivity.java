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
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anfio.popularmovies.adapters.MovieAdapter;
import com.example.anfio.popularmovies.models.Movie;
import com.example.anfio.popularmovies.settings.SettingsActivity;
import com.example.anfio.popularmovies.utilities.Constants;
import com.example.anfio.popularmovies.utilities.MovieJsonUtils;
import com.example.anfio.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie[]>,
        MovieAdapter.MovieAdapterOnClickHandler {

    // declare API_KEY
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
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
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
        //new FetchMovieTask().execute(urlToExecute);
        Bundle queryBundle = new Bundle();
        queryBundle.putString(Constants.API_MOVIES_LIST, urlToExecute);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(Constants.ID_ASYNCTASK_LOADER, queryBundle, this);
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

    @Override
    public void onClick(int id, String title, String imageUrl, String synopsis, double rating, String releaseDate) {
        Context context = this;
        // Parcelable used to pass the object to the intent
        Movie myParcelable = new Movie(id, title, imageUrl, synopsis, rating, releaseDate);
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("myDataKey", myParcelable);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Movie[]> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<Movie[]>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null){
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Nullable
            @Override
            public Movie[] loadInBackground() {
                String url = args.getString(Constants.API_MOVIES_LIST);
                if (url == null || TextUtils.isEmpty(url)){
                    return null;
                }
                try {
                    URL movieRequestUrl = new URL(url);
                    // get json response in a string
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);
                    // return an array of movie objects
                    return MovieJsonUtils.getMovies(jsonMovieResponse);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
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

    /*public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            // if no url is passed return null
            if (params.length == 0) {
                return null;
            }
            String urlString = params[0];
            try {
                URL movieRequestUrl = new URL(urlString);
                // get json response in a string
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);
                // return an array of movie objects
                return MovieJsonUtils.getMovies(jsonMovieResponse);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showMoviesDataView();
                mMovieAdapter.setMovieData(movies);
            } else {
                showErrorMessage(getString(R.string.error_message));
            }
        }
    }*/

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