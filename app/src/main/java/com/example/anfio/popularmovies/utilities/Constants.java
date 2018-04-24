package com.example.anfio.popularmovies.utilities;

public class Constants {

    private static final String API_KEY = "6fc62dd8c53da1d83a1b2eadac3054e9";

    // used in MainActivity
    public static final int ID_ASYNCTASK_LOADER = 21;
    public static final int ID_CURSOR_LOADER = 45;
    public static final String STRING_URL_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
    public static final String STRING_URL_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
    public static final String API_MOVIES_LIST = "api_movies_list";

    // used in DetailActivity
    private static final String API_MOVIES_REVIEWS = "api_movies_reviews";
    private static final String API_MOVIES_VIDEOS = "api_movies_videos";
    private static final int ID_ASYNCTASK_LOADER_DETAIL = 54;
    private static final int ID_CURSOR_LOADER_DETAIL = 55;
    private static final String STRING_URL_REVIEWS = "http://api.themoviedb.org/3/movie/{movie_id}/reviews?api_key=" + Constants.API_KEY;
    private static final String STRING_URL_VIDEOS = "http://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=" + Constants.API_KEY;
    public static final String BASE_URL_VIDEO = "https://www.youtube.com/watch?v=";
}
