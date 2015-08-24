package com.example.mhyousuf.popmovies.api;

import com.example.mhyousuf.popmovies.constants.Constants;
import com.example.mhyousuf.popmovies.model.TMDB_Movie_Feeds;
import com.example.mhyousuf.popmovies.model.TMDB_Movie_Reviews;
import com.example.mhyousuf.popmovies.model.TMDB_Movie_Trailers;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by mhyousuf on 6/8/2015.
 */
public class TMDBService {
    private static final String LOG_TAG = TMDBService.class.getSimpleName();
    private static TMDBApiInterface iTMDBService;

    /**
     * Get the client of TheMovieDatabase
     * @return api defined by TheMovieDatabase Interface
     */
    public static TMDBApiInterface getTMDBApiClient() {
        if (iTMDBService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.SERVICE_END_POINT)
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .setLog(new AndroidLog(LOG_TAG))
                    .build();

            iTMDBService = restAdapter.create(TMDBApiInterface.class);
        }

        return iTMDBService;
    }

    /**
     * TheMovieDatabase Interface
     */
    public interface TMDBApiInterface {
        /**
         * Get the top movie feeds by sort criteria
         * @param sort_by sort criteria
         * @param page page index
         * @param api_key auth token
         * @param callback
         */
        @GET("/discover/movie")
        void getMovieFeeds(@Query("sort_by") String sort_by, @Query("page") int page, @Query("api_key") String api_key, retrofit.Callback<TMDB_Movie_Feeds> callback);

        /**
         * Get the trailers for a particular movie
         * @param api_key auth token
         * @param callback
         */
        @GET("/movie/{id}/videos")
        void getMovieTrailers(@Path("id") long id, @Query("api_key") String api_key, retrofit.Callback<TMDB_Movie_Trailers> callback);

        /**
         * get the reviews for a particular movie
         * @param id moive id
         * @param api_key auth token
         * @param callback
         */
        @GET("/movie/{id}/reviews")
        void getMovieReviews(@Path("id") long id, @Query("api_key") String api_key, retrofit.Callback<TMDB_Movie_Reviews> callback);
    }
}
