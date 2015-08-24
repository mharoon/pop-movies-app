package com.example.mhyousuf.popmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.mhyousuf.popmovies.data.TMDBHelper;

import com.example.mhyousuf.popmovies.data.TMDBContract.MovieEntry;
import com.example.mhyousuf.popmovies.data.TMDBContract.ReviewEntry;

import java.util.Map;
import java.util.Set;

/**
 * Created by mhyousuf on 7/27/2015.
 */
public class TestDB extends AndroidTestCase {

    private static String LOG_TAG = TestDB.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(TMDBHelper.DATABASE_NAME);

        SQLiteDatabase db = new TMDBHelper(this.mContext)
                .getWritableDatabase();

        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDB() {



        SQLiteDatabase db = new TMDBHelper(mContext)
                .getWritableDatabase();

        ContentValues values = getMovieContentValues();

        long movieRowId;
        movieRowId = db.insert(MovieEntry.TABLE_NAME, null, values);

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
        };*/

        Cursor cursor = db.query(
                MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor.moveToFirst()){

            validateCursor(values,cursor);


        }else{
            fail("No Values returned :(");
        }

    }

    private ContentValues getMovieContentValues(){
        ContentValues values = new ContentValues();
        String testID = "135399";
        String testBackdropPath = "/dkMD5qlogeRMiEixC4YNPUvax2T.jpg";
        String testOverview = "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.";
        String testReleaseDate = "2015-06-12";
        String testPosterPath = "/uXZYawqUsChGSj54wcuBtEdUJbh.jpg";
        double testPopularity =  Double.parseDouble("85.5959");

        String testTitle = "Jurassic World";
        double testVoteAverage = 7.1;
        int testVoteCount = 1538;

        values.put(MovieEntry._ID, testID);
        values.put(MovieEntry.COLUMN_BACKDROP_PATH, testBackdropPath);
        values.put(MovieEntry.COLUMN_OVERVIEW, testOverview);
        values.put(MovieEntry.COLUMN_RELEASE_DATE, testReleaseDate);
        values.put(MovieEntry.COLUMN_POSTER_PATH, testPosterPath);
        values.put(MovieEntry.COLUMN_POPULARITY, testPopularity);
        values.put(MovieEntry.COLUMN_TITLE, testTitle);
        values.put(MovieEntry.COLUMN_VOTE_AVERAGE, testVoteAverage);
        values.put(MovieEntry.COLUMN_VOTE_COUNT, testVoteCount);

        return values;
    }

    static public void validateCursor(ContentValues expectedValues, Cursor valueCursor){
        Set<Map.Entry<String,Object>> valueSet = expectedValues.valueSet();

        for(Map.Entry<String,Object> entry : valueSet){
            String columnName = entry.getKey();
            int indx = valueCursor.getColumnIndex(columnName);

            assertFalse(indx == -1);

            String expectedValue = entry.getValue().toString();

            assertEquals(expectedValue,valueCursor.getString(indx));
        }
    }
}
