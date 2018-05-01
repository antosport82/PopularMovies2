package com.example.anfio.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieProvider extends ContentProvider {

    private static final int CODE_MOVIES_POPULAR = 100;
    private static final int CODE_MOVIES_TOP_RATED = 200;
    private static final int CODE_MOVIES_FAVORITE = 300;
    private static final int CODE_MOVIE_POPULAR_WITH_ID = 101;
    private static final int CODE_MOVIE_TOP_RATED_WITH_ID = 201;
    private static final int CODE_MOVIE_FAVORITE_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        // This URI is content://com.example.anfio.popularmovies/movies/ */
        matcher.addURI(authority, MovieContract.PATH_POPULAR_MOVIES, CODE_MOVIES_POPULAR);
        matcher.addURI(authority, MovieContract.PATH_TOP_RATED_MOVIES, CODE_MOVIES_TOP_RATED);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIES, CODE_MOVIES_FAVORITE);

        /*
         * This URI would look something like content://com.example.anfio.popularmovies/movies/7
         * The "/#" signifies to the UriMatcher that if PATH_MOVIES is followed by ANY number,
         * that it should return the CODE_MOVIE_WITH_ID code
         */
        matcher.addURI(authority, MovieContract.PATH_POPULAR_MOVIES + "/#", CODE_MOVIE_POPULAR_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_TOP_RATED_MOVIES + "/#", CODE_MOVIE_TOP_RATED_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIES + "/#", CODE_MOVIE_FAVORITE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mOpenHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        /*
         * Here's the switch statement that, given a URI, will determine what kind of request is
         * being made and query the database accordingly.
         */
        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIES_POPULAR:
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME_POPULAR,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;

            case CODE_MOVIES_TOP_RATED:
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME_TOP_RATED,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;

            case CODE_MOVIES_FAVORITE:
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME_FAVORITE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;

            case CODE_MOVIE_POPULAR_WITH_ID:

                String id = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{id};

                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME_POPULAR,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );

                break;

            case CODE_MOVIE_TOP_RATED_WITH_ID:

                String idTopRated = uri.getLastPathSegment();
                String[] selectionArgumentsTopRated = new String[]{idTopRated};

                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME_TOP_RATED,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArgumentsTopRated,
                        null,
                        null,
                        sortOrder
                );

                break;

            case CODE_MOVIE_FAVORITE_WITH_ID:

                String idFavorite = uri.getLastPathSegment();
                String[] selectionArgumentsFavorite = new String[]{idFavorite};

                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME_TOP_RATED,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArgumentsFavorite,
                        null,
                        null,
                        sortOrder
                );

                break;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the movies directory
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {

            case CODE_MOVIES_POPULAR:
                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME_POPULAR, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI_POPULAR, id);
                } else {
                    throw new SQLException("Error in inserting data for uri: " + uri);
                }
                break;

            case CODE_MOVIES_TOP_RATED:
                long idTopRated = db.insert(MovieContract.MovieEntry.TABLE_NAME_TOP_RATED, null, values);
                if (idTopRated > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI_TOP_RATED, idTopRated);
                } else {
                    throw new SQLException("Error in inserting data for uri: " + uri);
                }
                break;

            case CODE_MOVIES_FAVORITE:
                long idFavorite = db.insert(MovieContract.MovieEntry.TABLE_NAME_FAVORITE, null, values);
                if (idFavorite > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI_FAVORITE, idFavorite);
                } else {
                    throw new SQLException("Error in inserting data for uri: " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the movies directory
        int match = sUriMatcher.match(uri);
        String tableName;
        switch (match) {
            case CODE_MOVIES_POPULAR:
                tableName = MovieContract.MovieEntry.TABLE_NAME_POPULAR;
                break;
            case CODE_MOVIES_TOP_RATED:
                tableName = MovieContract.MovieEntry.TABLE_NAME_TOP_RATED;
                break;
            case CODE_MOVIES_FAVORITE:
                tableName = MovieContract.MovieEntry.TABLE_NAME_FAVORITE;
                break;
            default:
                return super.bulkInsert(uri, values);
        }

        // allows for multiple transactions
        db.beginTransaction();

        // keep track of successful inserts
        int numInserted = 0;

        try {
            for (ContentValues value : values) {
                if (value == null) {
                    throw new IllegalArgumentException("Cannot have null content values");
                }
                long _id = -1;

                try {
                    _id = db.insert(tableName,
                            null, value);
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }
                if (_id != -1) {
                    numInserted++;
                }
            }
            if (numInserted > 0) {
                // If no errors, declare a successful transaction.
                // database will not populate if this is not called
                db.setTransactionSuccessful();
            }
        } finally {
            // all transactions occur at once
            db.endTransaction();
        }
        if (numInserted > 0) {
            // if there was successful insertion, notify the content resolver that there
            // was a change
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numInserted;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[]
            selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the movies directory
        int match = sUriMatcher.match(uri);
        int deletedRows;

        switch (match) {
            case CODE_MOVIES_TOP_RATED:
                deletedRows = db.delete(MovieContract.MovieEntry.TABLE_NAME_TOP_RATED, selection, selectionArgs);
                break;
            case CODE_MOVIES_POPULAR:
                deletedRows = db.delete(MovieContract.MovieEntry.TABLE_NAME_POPULAR, selection, selectionArgs);
                break;
            case CODE_MOVIE_FAVORITE_WITH_ID:
                String idFavorite = uri.getLastPathSegment();
                String[] selectionArgumentsFavorite = new String[]{idFavorite};

                deletedRows = db.delete(MovieContract.MovieEntry.TABLE_NAME_FAVORITE,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArgumentsFavorite);
                break;
            default:
                return 0;
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String
            selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}