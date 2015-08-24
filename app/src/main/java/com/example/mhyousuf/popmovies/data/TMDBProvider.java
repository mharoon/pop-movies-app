package com.example.mhyousuf.popmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by mhyousuf on 8/1/2015.
 */
public class TMDBProvider extends ContentProvider {

    private static String LOG_TAG = TMDBProvider.class.getSimpleName();

    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 101;

    private static final int REVIEWS = 200;
    private static final int MOVIE_REVIEWS = 201;

    private static final int TRAILERS = 300;
    private static final int MOVIE_TRAILERS = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private TMDBHelper mTMDBHelper;


    @Override
    public boolean onCreate() {
        mTMDBHelper = new TMDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES: {
                retCursor = mTMDBHelper.getReadableDatabase().query(
                        TMDBContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_ID: {
                retCursor = mTMDBHelper.getReadableDatabase().query(
                        TMDBContract.MovieEntry.TABLE_NAME,
                        projection,
                        TMDBContract.MovieEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REVIEWS: {
                retCursor = mTMDBHelper.getReadableDatabase().query(
                        TMDBContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_REVIEWS: {
                retCursor = mTMDBHelper.getReadableDatabase().query(
                        TMDBContract.ReviewEntry.TABLE_NAME,
                        projection,
                        TMDBContract.ReviewEntry.COLUMN_MOVIE_KEY + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TRAILERS: {
                retCursor = mTMDBHelper.getReadableDatabase().query(
                        TMDBContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_TRAILERS: {
                retCursor = mTMDBHelper.getReadableDatabase().query(
                        TMDBContract.TrailerEntry.TABLE_NAME,
                        projection,
                        TMDBContract.TrailerEntry.COLUMN_MOVIE_KEY + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri" + uri);
            }
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "Get Type URI:" + uri);

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return TMDBContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_ID:
                return TMDBContract.MovieEntry.CONTENT_ITEM_TYPE;
            case REVIEWS:
                return TMDBContract.ReviewEntry.CONTENT_TYPE;
            case MOVIE_REVIEWS:
                return TMDBContract.ReviewEntry.CONTENT_TYPE;
            case TRAILERS:
                return TMDBContract.TrailerEntry.CONTENT_TYPE;
            case MOVIE_TRAILERS:
                return TMDBContract.TrailerEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mTMDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                long _id = db.insert(TMDBContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = TMDBContract.MovieEntry.buildMovieUri(_id);
                } else
                    throw new android.database.sqlite.SQLiteException("Faild to insert row into " + uri);
                break;
            }
            case REVIEWS:{
                long _id = db.insert(TMDBContract.ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = TMDBContract.ReviewEntry.buildReviewUri(_id);
                } else
                    throw new android.database.sqlite.SQLiteException("Faild to insert row into " + uri);
                break;
            }
            case TRAILERS:{
                long _id = db.insert(TMDBContract.TrailerEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = TMDBContract.TrailerEntry.buildTrailerUri(_id);
                } else
                    throw new android.database.sqlite.SQLiteException("Faild to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mTMDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case MOVIES:
                rowsDeleted = db.delete(TMDBContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEWS:
                rowsDeleted = db.delete(TMDBContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAILERS:
                rowsDeleted = db.delete(TMDBContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }

        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mTMDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(TMDBContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REVIEWS:
                rowsUpdated = db.update(TMDBContract.ReviewEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TRAILERS:
                rowsUpdated = db.update(TMDBContract.TrailerEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }

        if (selection == null || rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mTMDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String selection;
        String[] selectionArgs = null;
        int returnCount = 0;
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                selection = TMDBContract.MovieEntry._ID + " = ?";
                selectionArgs = null;
                returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        selectionArgs = new String[] {value.getAsString(TMDBContract.MovieEntry._ID)};

                        int affected = db.update(TMDBContract.MovieEntry.TABLE_NAME, value, selection, selectionArgs);
                        if(affected == 0) {
                            long _id = db.insert(TMDBContract.MovieEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case REVIEWS:
                db.beginTransaction();
                selection = TMDBContract.ReviewEntry.COLUMN_REVIEW_ID + " = ?";
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        selectionArgs = new String[] {value.getAsString(TMDBContract.ReviewEntry.COLUMN_REVIEW_ID)};

                        int affected = db.update(TMDBContract.ReviewEntry.TABLE_NAME, value, selection, selectionArgs);
                        if(affected == 0) {
                            long _id = db.insert(TMDBContract.ReviewEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case TRAILERS:
                db.beginTransaction();
                selection = TMDBContract.TrailerEntry.COLUMN_TRAILER_ID + " = ?";
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        selectionArgs = new String[] {value.getAsString(TMDBContract.TrailerEntry.COLUMN_TRAILER_ID)};

                        int affected = db.update(TMDBContract.TrailerEntry.TABLE_NAME, value, selection, selectionArgs);
                        if(affected == 0) {
                            long _id = db.insert(TMDBContract.TrailerEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TMDBContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, TMDBContract.PATH_MOVIES, MOVIES);
        matcher.addURI(authority, TMDBContract.PATH_MOVIES + "/#", MOVIE_ID);

        matcher.addURI(authority, TMDBContract.PATH_REVIEWS, REVIEWS);
        matcher.addURI(authority, TMDBContract.PATH_REVIEWS + "/#", MOVIE_REVIEWS);

        matcher.addURI(authority, TMDBContract.PATH_TRAILERS, TRAILERS);
        matcher.addURI(authority, TMDBContract.PATH_TRAILERS + "/#", MOVIE_TRAILERS);

        return matcher;
    }
}
