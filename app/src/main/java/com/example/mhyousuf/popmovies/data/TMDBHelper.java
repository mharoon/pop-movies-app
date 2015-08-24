package com.example.mhyousuf.popmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.mhyousuf.popmovies.data.TMDBContract.MovieEntry;
import com.example.mhyousuf.popmovies.data.TMDBContract.ReviewEntry;
import com.example.mhyousuf.popmovies.data.TMDBContract.TrailerEntry;

/**
 * Created by mhyousuf on 7/27/2015.
 */
public class TMDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movies.db";

    public TMDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE," + //AUTOINCREMENT
                        MovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                        MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                        MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                        MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                        MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                        MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                        MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0 " + ");";


        final String SQL_CREATE_REVIEWS_TABLE =
                "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                        ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + //AUTOINCREMENT
                        ReviewEntry.COLUMN_REVIEW_ID + " TEXT NOT NULL, " +
                        ReviewEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +
                        ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                        ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +

                        " FOREIGN KEY (" + ReviewEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                        MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + "), " +

                        " UNIQUE (" + ReviewEntry.COLUMN_REVIEW_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_TRAILERS_TABLE =
                "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
                        TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + //AUTOINCREMENT

                        TrailerEntry.COLUMN_TRAILER_ID + " TEXT NOT NULL, " +
                        TrailerEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +
                        TrailerEntry.COLUMN_VIDEO_KEY + " INTEGER NOT NULL, " +
                        TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +

                        " FOREIGN KEY (" + TrailerEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                        MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + "), " +

                        " UNIQUE (" + TrailerEntry.COLUMN_TRAILER_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_REVIEWS_TABLE);
        db.execSQL(SQL_CREATE_TRAILERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE  IF EXISTS " + MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE  IF EXISTS " + ReviewEntry.TABLE_NAME);
        onCreate(db);
    }
}
