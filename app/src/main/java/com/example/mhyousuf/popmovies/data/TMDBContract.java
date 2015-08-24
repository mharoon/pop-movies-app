package com.example.mhyousuf.popmovies.data;


import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.lang.ref.SoftReference;
import java.net.URI;

/**
 * Created by mhyousuf on 7/27/2015.
 */
public class TMDBContract {

    public static final String CONTENT_AUTHORITY = "com.example.mhyousuf.popmovies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_TRAILERS = "trailers";


    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;


        // Table name
        public static final String TABLE_NAME = "movies";

        public static String _ID = "id";
        public static String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static String COLUMN_OVERVIEW = "overview";
        public static String COLUMN_RELEASE_DATE = "release_date";
        public static String COLUMN_POSTER_PATH = "poster_path";
        public static String COLUMN_POPULARITY = "popularity";
        public static String COLUMN_TITLE = "title";
        public static String COLUMN_VOTE_AVERAGE = "vote_average";
        public static String COLUMN_VOTE_COUNT = "vote_count";
        public static String COLUMN_IS_FAVORITE = "is_favorite";


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public  static final String CONTENT_TYPE =
        "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public  static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        // Table name
        public static final String TABLE_NAME = "reviews";

        public static String _ID = "id";
        public static String COLUMN_REVIEW_ID = "review_id";
        public static String COLUMN_MOVIE_KEY = "movie_id";
        public static String COLUMN_AUTHOR = "author";
        public static String COLUMN_CONTENT = "content";

        public static Uri buildReviewUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

    public static final class TrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public  static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public  static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        // Table name
        public static final String TABLE_NAME = "trailers";

        public static String _ID = "id";
        public static String COLUMN_TRAILER_ID = "trailer_id";
        public static String COLUMN_MOVIE_KEY = "movie_id";
        public static String COLUMN_VIDEO_KEY = "key";
        public static String COLUMN_NAME = "name";

        public static Uri buildTrailerUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
