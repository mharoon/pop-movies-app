package com.example.mhyousuf.popmovies.constants;

/**
 * Created by mhyousuf on 6/13/2015.
 */
public class Constants {
    public static final String SERVICE_END_POINT = "http://api.themoviedb.org/3";
    public static final String API_KEY = "a66282dfcb32e5db1422dd285f9d8e8f";

    public static final String PREF_SORT_LIST_KEY = "sort_list";

    //fetch data threshold on grid item scrolling
    public static final int ITEM_THRESHOLD = 10;

    public static  final String SORT_BY_POPULARITY = "popularity.desc";
    public static  final String SORT_BY_VOTE_AVG = "vote_average.desc";

    //Intent extra key for movie details object
    public static final String MOVIE_DETAIL_ID_EXTRA = "MovieDetail:Id";
    //Intent extra key for movie details object
    public static final String IS_TWO_PANE_LAYOUT = "TwoPane:Layout";

    //Intent extra key for trailer object
    public static final String TRAILER_OBJECT_EXTRA = "TMDB_Trailer:object";

    public static final String MOVIE_DETAIL_STATE_SAVED = "STATE_SAVED_DETAIL";

}
