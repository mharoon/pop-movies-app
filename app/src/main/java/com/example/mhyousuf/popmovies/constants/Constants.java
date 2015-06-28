package com.example.mhyousuf.popmovies.constants;

/**
 * Created by mhyousuf on 6/13/2015.
 */
public class Constants {
    public static final String SERVICE_END_POINT = "http://api.themoviedb.org/3/discover";
    public static final String API_KEY = "a66282dfcb32e5db1422dd285f9d8e8f";

    //fetch data threshold on grid item scrolling
    public static final int ITEM_THRESHOLD = 10;

    public static  final String SortByPopularity = "popularity.desc";
    public static  final String SortByHighestRated = "vote_average.desc";

    //Intent extra key for movie details object
    public static final String MOVIE_OBJECT_EXTRA = "MovieDetail:object";

}
