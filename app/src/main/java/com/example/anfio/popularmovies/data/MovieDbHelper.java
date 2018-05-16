package com.example.anfio.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;



    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*final String SQL_CREATE_POPULAR_MOVIES_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME_POPULAR + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
                        " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE" +
                        ");";

        db.execSQL(SQL_CREATE_POPULAR_MOVIES_TABLE);

        final String SQL_CREATE_TOP_RATED_MOVIES_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME_TOP_RATED + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
                        " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE" +
                        ");";

        db.execSQL(SQL_CREATE_TOP_RATED_MOVIES_TABLE);*/

        final String SQL_CREATE_FAVORITE_MOVIES_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME_FAVORITE + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
                        " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE" +
                        ");";

        db.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME_POPULAR);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME_TOP_RATED);*/
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME_FAVORITE);
        onCreate(db);
    }
}
