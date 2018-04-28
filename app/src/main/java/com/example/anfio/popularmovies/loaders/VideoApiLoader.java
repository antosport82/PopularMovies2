package com.example.anfio.popularmovies.loaders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.anfio.popularmovies.models.Video;
import com.example.anfio.popularmovies.utilities.MovieJsonUtils;
import com.example.anfio.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class VideoApiLoader extends AsyncTaskLoader<Video[]> {

    private String mUrl;

    public VideoApiLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public Video[] loadInBackground() {
        // if no url is passed return null
        if (mUrl == null) {
            return null;
        }
        try {
            URL movieRequestUrl = new URL(mUrl);
            // get json response in a string
            String jsonMovieResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl);
            // return an array of video objects
            return MovieJsonUtils.getVideos(jsonMovieResponse);
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
}
