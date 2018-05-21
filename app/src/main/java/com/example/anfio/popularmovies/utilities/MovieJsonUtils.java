package com.example.anfio.popularmovies.utilities;

import com.example.anfio.popularmovies.models.Movie;
import com.example.anfio.popularmovies.models.Review;
import com.example.anfio.popularmovies.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anfio on 28/02/2018 - Popular Movies App - Udacity Project
 */

public class MovieJsonUtils {

    public static Movie[] getMovies(String movieJsonString) throws JSONException {
        JSONObject jsonMovieString = new JSONObject(movieJsonString);
        JSONArray jsonArrayResults = jsonMovieString.getJSONArray("results");
        Movie[] movies = new Movie[jsonArrayResults.length()];
        for (int i = 0; i < jsonArrayResults.length(); i++) {
            JSONObject jsonMovie = jsonArrayResults.getJSONObject(i);
            int id = jsonMovie.getInt("id");
            String title = jsonMovie.getString("title");
            double rating = jsonMovie.getDouble("vote_average");
            String imageUrl = jsonMovie.getString("poster_path");
            String synopsis = jsonMovie.getString("overview");
            String releaseDate = jsonMovie.getString("release_date");
            movies[i] = new Movie(id, title, imageUrl, synopsis, rating, releaseDate);
        }
        return movies;
    }

    public static Video[] getVideos(String videoJsonString) throws JSONException {
        JSONObject jsonMovieVideoString = new JSONObject(videoJsonString);
        JSONArray jsonArrayResults = jsonMovieVideoString.getJSONArray("results");
        Video[] videos = new Video[jsonArrayResults.length()];
        for (int i = 0; i < jsonArrayResults.length(); i++) {
            JSONObject jsonVideo = jsonArrayResults.getJSONObject(i);
            String id = jsonVideo.getString("id");
            String key = jsonVideo.getString("key");
            String name = jsonVideo.getString("name");
            String site = jsonVideo.getString("site");
            String type = jsonVideo.getString("type");
            videos[i] = new Video(id, key, name, site, type);
        }
        return videos;
    }

    public static Review[] getReviews(String reviewJsonString) throws JSONException {
        JSONObject jsonMovieReviewString = new JSONObject(reviewJsonString);
        JSONArray jsonArrayResults = jsonMovieReviewString.getJSONArray("results");
        Review[] reviews = new Review[jsonArrayResults.length()];
        for (int i = 0; i < jsonArrayResults.length(); i++) {
            JSONObject jsonReview = jsonArrayResults.getJSONObject(i);
            String id = jsonReview.getString("id");
            String author = jsonReview.getString("author");
            String content = jsonReview.getString("content");
            reviews[i] = new Review(id, author, content);
        }
        return reviews;
    }
}