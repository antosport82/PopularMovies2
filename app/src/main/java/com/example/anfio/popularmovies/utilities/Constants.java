package com.example.anfio.popularmovies.utilities;

public class Constants {

    // define API_KEY
    private static final String API_KEY = "insert-api-key-here";

    // used in MainActivity
    public static final int ID_ASYNCTASK_LOADER = 21;
    public static final int ID_CURSOR_LOADER = 45;
    public static final String STRING_URL_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
    public static final String STRING_URL_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
    public static final String STRING_URL_FAVORITE = "content://com.example.anfio.popularmovies/favorite_movies";
    public static final String API_MOVIES_LIST = "api_movies_list";

    // used in DetailActivity
    public static final String API_MOVIES_REVIEWS = "api_movies_reviews";
    public static final String API_MOVIES_VIDEOS = "api_movies_videos";
    public static final String ID_MOVIE_FAVORITE = "id_movie_favorite";
    public static final int ID_ASYNCTASK_LOADER_DETAIL_VIDEOS = 54;
    public static final int ID_ASYNCTASK_LOADER_DETAIL_REVIEWS = 55;
    public static final int ID_CURSOR_LOADER_FAVORITE_CHECK = 56;
    public static final String STRING_URL_REVIEWS_I = "http://api.themoviedb.org/3/movie/";
    public static final String STRING_URL_REVIEWS_II = "/reviews?api_key=" + Constants.API_KEY;
    public static final String STRING_URL_VIDEOS_I = "http://api.themoviedb.org/3/movie/";
    public static final String STRING_URL_VIDEOS_II = "/videos?api_key=" + Constants.API_KEY;
    public static final String BASE_URL_VIDEO = "https://www.youtube.com/watch?v=";
    public static final String BASE_URL_IMAGE_VIDEO = "https://img.youtube.com/vi/";
    public static final String BASE_URL_SUFFIX_VIDEO = "/0.jpg";
}