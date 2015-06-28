package com.example.mhyousuf.popmovies.api;

import com.example.mhyousuf.popmovies.constants.Constants;
import com.example.mhyousuf.popmovies.model.TMDBFeedsData;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.http.GET;
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
        @GET("/movie")
        void getFeeds(@Query("sort_by") String sort_by, @Query("page") int page, @Query("api_key") String api_key, retrofit.Callback<TMDBFeedsData> callback);
    }
}
