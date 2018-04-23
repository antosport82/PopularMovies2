package com.example.anfio.popularmovies.utilities;

public class Constants {

    private static final String API_KEY = "6fc62dd8c53da1d83a1b2eadac3054e9";

    // used in MainActivity
    public static final int ID_ASYNCTASK_LOADER = 44;
    public static final String STRING_URL_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
    public static final String STRING_URL_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
    public static final String API_MOVIES_LIST = "api_movies_list";
}
