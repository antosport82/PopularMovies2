package com.example.anfio.popularmovies.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.anfio.popularmovies.data.MovieContract;
import com.example.anfio.popularmovies.models.Movie;
import com.example.anfio.popularmovies.utilities.Constants;
import com.example.anfio.popularmovies.utilities.MovieJsonUtils;
import com.example.anfio.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieApiLoader extends AsyncTaskLoader<Movie[]> {

    private final String mUrl;
    private Movie[] movieData;

    public MovieApiLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (movieData != null) {
            deliverResult(movieData);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public Movie[] loadInBackground() {
        // if no url is passed return null
        if (mUrl == null) {
            return null;
        }
        try {
            URL movieRequestUrl = new URL(mUrl);
            // get json response in a string
            String jsonMovieResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl);
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
    public void deliverResult(@Nullable Movie[] data) {
        super.deliverResult(data);
        movieData = data;
    }

    // created for debug purpose. Fast deletion of movies from DB
    private void deleteMoviesFromDb(String stringUrlFavorite) {
        Uri contentUri;
        switch (stringUrlFavorite) {
            case Constants.STRING_URL_FAVORITE:
                contentUri = MovieContract.MovieEntry.CONTENT_URI_FAVORITE;
                break;
            default:
                return;
        }
        getContext().getContentResolver().delete(contentUri, null, null);
    }

    // created for debug purpose. Fast insertion of movies into favorites table
    private void insertMoviesIntoDb(Movie[] moviesForDb, String urlString) {
        Uri contentUri;
        switch (urlString) {
            case Constants.STRING_URL_FAVORITE:
                contentUri = MovieContract.MovieEntry.CONTENT_URI_FAVORITE;
                break;
            default:
                return;
        }
        ContentValues[] movieValuesArr = new ContentValues[3];
        for (int i = 0; i < 3; i++) {
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
        getContext().getContentResolver().bulkInsert(contentUri, movieValuesArr);
    }
}