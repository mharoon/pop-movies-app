package com.example.mhyousuf.popmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.mhyousuf.popmovies.data.TMDBContract;
import com.example.mhyousuf.popmovies.data.TMDBHelper;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;

/**
 * Created by mhyousuf on 8/1/2015.
 */
public class TestProvider extends AndroidTestCase {
    private static String LOG_TAG = TestDB.class.getSimpleName();

    /*public void testDeleteDb() throws Throwable {
        mContext.deleteDatabase(TMDBHelper.DATABASE_NAME);
    }*/

    public void testDeleteAllRecords() {
        Cursor cursor;
        mContext.getContentResolver().delete(TMDBContract.MovieEntry.CONTENT_URI
                , null
                , null);

        cursor = mContext.getContentResolver().query(TMDBContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals(cursor.getCount(), 0);
        cursor.close();

        mContext.getContentResolver().delete(TMDBContract.ReviewEntry.CONTENT_URI
                , null
                , null);

        cursor = mContext.getContentResolver().query(TMDBContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals(cursor.getCount(), 0);
        cursor.close();
    }

    public void testInsertReadProvider() {


        ContentValues values = getMovieContentValues();


        Uri insertUri = mContext.getContentResolver().insert(TMDBContract.MovieEntry.CONTENT_URI, values);
        long movieRowId = ContentUris.parseId(insertUri);

        assertTrue(movieRowId != -1);
        Log.d(LOG_TAG, "New Row id: " + movieRowId);

        /*String[] columns = {
                MovieEntry._ID,
                MovieEntry.COLUMN_BACKDROP_PATH,
                MovieEntry.COLUMN_OVERVIEW,
                MovieEntry.COLUMN_RELEASE_DATE,
                MovieEntry.COLUMN_POSTER_PATH,
                MovieEntry.COLUMN_POPULARITY,
                MovieEntry.COLUMN_TITLE,
                MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieEntry.COLUMN_VOTE_COUNT
        };

        Cursor cursor = db.query(
                TMDBContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );*/

        Cursor cursor = mContext.getContentResolver().query(TMDBContract.MovieEntry.buildMovieUri(movieRowId),
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {

            validateCursor(values, cursor);


        } else {
            fail("No Values returned :(");
        }

        cursor.close();
    }

    public void testUpdateMovies() {
        testDeleteAllRecords();

        ContentValues values = getMovieContentValues();

        Uri movieUri = mContext.getContentResolver().insert(TMDBContract.MovieEntry.CONTENT_URI, values);

        long movieRowId = ContentUris.parseId(movieUri);

        assertTrue(movieRowId != -1);
        Log.d(LOG_TAG, "New Row id: " + movieRowId);

        ContentValues values2 = new ContentValues(values);
        values.put(TMDBContract.MovieEntry._ID, movieRowId);
        values.put(TMDBContract.MovieEntry.COLUMN_TITLE, "Movie The Movie!!");

        int count = mContext.getContentResolver().update(TMDBContract.MovieEntry.CONTENT_URI, values2, TMDBContract.MovieEntry._ID + " = ?", new String[]{Long.toString(movieRowId)});

        assertEquals(count, 1);

        Cursor cursor = mContext.getContentResolver().query(TMDBContract.MovieEntry.buildMovieUri(movieRowId), null, null, null, null);

        if (cursor.moveToFirst()) {
            validateCursor(values2, cursor);
        }
    }

    public void testReviewInsertReadProvider() {


        ContentValues values = getReviewContentValues();


        Uri insertUri = mContext.getContentResolver().insert(TMDBContract.ReviewEntry.CONTENT_URI, values);
        long reviewRowId = ContentUris.parseId(insertUri);

        assertTrue(reviewRowId != -1);
        Log.d(LOG_TAG, "New Review Row id: " + reviewRowId);

        Cursor cursor = mContext.getContentResolver().query(TMDBContract.ReviewEntry.buildReviewUri(reviewRowId),
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {

            validateCursor(values, cursor);


        } else {
            fail("No Values returned :(");
        }

        cursor.close();
    }


    public void testTrailerInsertReadProvider() {


        ContentValues values = getTrailerContentValues();


        Uri insertUri = mContext.getContentResolver().insert(TMDBContract.TrailerEntry.CONTENT_URI, values);
        long trailerRowId = ContentUris.parseId(insertUri);

        assertTrue(trailerRowId != -1);
        Log.d(LOG_TAG, "New Trailer Row id: " + trailerRowId);

        Cursor cursor = mContext.getContentResolver().query(TMDBContract.TrailerEntry.buildTrailerUri(trailerRowId),
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {

            validateCursor(values, cursor);


        } else {
            fail("No Values returned :(");
        }

        cursor.close();
    }


    private ContentValues getMovieContentValues() {
        DecimalFormat df = new DecimalFormat("####0.00");
        ContentValues values = new ContentValues();
        String testID = "135399";
        String testBackdropPath = "/dkMD5qlogeRMiEixC4YNPUvax2T.jpg";
        String testOverview = "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.";
        String testReleaseDate = "2015-06-12";
        String testPosterPath = "/uXZYawqUsChGSj54wcuBtEdUJbh.jpg";
        String testPopularity = "52.6809";
        String testTitle = "Jurassic World";
        double testVoteAverage = 7.12;
        int testVoteCount = 1538;

        values.put(TMDBContract.MovieEntry._ID, testID);
        values.put(TMDBContract.MovieEntry.COLUMN_BACKDROP_PATH, testBackdropPath);
        values.put(TMDBContract.MovieEntry.COLUMN_OVERVIEW, testOverview);
        values.put(TMDBContract.MovieEntry.COLUMN_RELEASE_DATE, testReleaseDate);
        values.put(TMDBContract.MovieEntry.COLUMN_POSTER_PATH, testPosterPath);
        values.put(TMDBContract.MovieEntry.COLUMN_POPULARITY, testPopularity);
        values.put(TMDBContract.MovieEntry.COLUMN_TITLE, testTitle);
        values.put(TMDBContract.MovieEntry.COLUMN_VOTE_AVERAGE, testVoteAverage);
        values.put(TMDBContract.MovieEntry.COLUMN_VOTE_COUNT, testVoteCount);

        return values;
    }

    private ContentValues getReviewContentValues() {

        ContentValues values = new ContentValues();
        long movieId = 1;
        String reviewId = "53f11b7c0e0a2675b8004053";
        String author = "Binawoo";
        String content = "This movie was so AWESOME! I loved it all and i had a bad day before watching it but it turned it around. I love action packed movies and this was great.";

        values.put(TMDBContract.ReviewEntry.COLUMN_MOVIE_KEY, movieId);
        values.put(TMDBContract.ReviewEntry.COLUMN_REVIEW_ID, reviewId);
        values.put(TMDBContract.ReviewEntry.COLUMN_AUTHOR, author);
        values.put(TMDBContract.ReviewEntry.COLUMN_CONTENT, content);

        return values;
    }

    private ContentValues getTrailerContentValues() {

        ContentValues values = new ContentValues();
        long movieId = 1;
        String trailerId = "53b47955c3a3682ee2009d93";
        String key = "XT4CLuTbI7A";
        String name = "International Trailer";

        values.put(TMDBContract.TrailerEntry.COLUMN_MOVIE_KEY, movieId);
        values.put(TMDBContract.TrailerEntry.COLUMN_TRAILER_ID, trailerId);
        values.put(TMDBContract.TrailerEntry.COLUMN_VIDEO_KEY, key);
        values.put(TMDBContract.TrailerEntry.COLUMN_NAME, name);

        return values;
    }


    static public void validateCursor(ContentValues expectedValues, Cursor valueCursor) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int indx = valueCursor.getColumnIndex(columnName);

            assertFalse(indx == -1);

            String expectedValue = entry.getValue().toString();

            assertEquals(expectedValue, valueCursor.getString(indx));
        }
    }

    public void testGetType() {

        // content://com.example.android.popmovie.app/movies/
        String type = mContext.getContentResolver().getType(TMDBContract.MovieEntry.CONTENT_URI);
        //vnd.cursor.android.dir/com.example.android.popmovies.app/movies
        assertEquals(TMDBContract.MovieEntry.CONTENT_TYPE, type);

        // content://com.example.android.popmovies.app/movies/135399
        type = mContext.getContentResolver().getType(TMDBContract.MovieEntry.buildMovieUri(135399L));
        //vnd.cursor.android.item/com.example.android.popmovies.app/movies
        assertEquals(TMDBContract.MovieEntry.CONTENT_ITEM_TYPE, type);

        //content://com.example.android.popmovies.app/reviews/
        type = mContext.getContentResolver().getType(TMDBContract.ReviewEntry.CONTENT_URI);
        //vnd.cursor.android.dir/com.example.android.popmovies.app/reviews
        assertEquals(TMDBContract.ReviewEntry.CONTENT_TYPE, type);

        //content://com.example.android.popmovies.app/reviews/
        type = mContext.getContentResolver().getType(TMDBContract.ReviewEntry.buildReviewUri(118340L));
        //vnd.cursor.android.dir/com.example.android.popmovies.app/reviews/118340
        assertEquals(TMDBContract.ReviewEntry.CONTENT_TYPE, type);


        //content://com.example.android.popmovies.app/trailers/
        type = mContext.getContentResolver().getType(TMDBContract.TrailerEntry.CONTENT_URI);
        //vnd.cursor.android.dir/com.example.android.popmovies.app/trailers
        assertEquals(TMDBContract.TrailerEntry.CONTENT_TYPE, type);

        //content://com.example.android.popmovies.app/trailers/
        type = mContext.getContentResolver().getType(TMDBContract.TrailerEntry.buildTrailerUri(118340L));
        //vnd.cursor.android.dir/com.example.android.popmovies.app/trailers/118340
        assertEquals(TMDBContract.TrailerEntry.CONTENT_TYPE, type);

    }
}
