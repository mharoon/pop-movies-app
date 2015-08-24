package com.example.mhyousuf.popmovies.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mhyousuf on 6/10/2015.
 */
public class TMDBMovie {
    private static final String CDN_URL =  "http://image.tmdb.org/t/p/w185/";
    private static final String CDN_URL_BACKDROP =  "http://image.tmdb.org/t/p/w342/";

    private boolean adult;
    private String backdrop_path;
    private List<Integer> genre_ids = new ArrayList<Integer>();
    private int id;
    private String original_language;
    private String original_title;
    private String overview;
    private String release_date;
    private String poster_path;
    private double popularity;
    private String title;
    private boolean video;
    private double vote_average;
    private int vote_count;
    private String movie_year;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The adult
     */
    public boolean isAdult() {
        return adult;
    }

    /**
     *
     * @param adult
     * The adult
     */
    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    /**
     *
     * @return
     * The backdrop_path
     */
    public String getBackdropPath() {
        return CDN_URL_BACKDROP + backdrop_path;
    }

    /**
     *
     * @param backdropPath
     * The backdrop_path
     */
    public void setBackdropPath(String backdropPath) {
        this.backdrop_path = CDN_URL_BACKDROP + backdropPath;
    }

    /**
     *
     * @return
     * The genre_ids
     */
    public List<Integer> getGenreIds() {
        return genre_ids;
    }

    /**
     *
     * @param genreIds
     * The genre_ids
     */
    public void setGenre_ids(List<Integer> genreIds) {
        this.genre_ids = genreIds;
    }

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The original_language
     */
    public String getOriginalLanguage() {
        return original_language;
    }

    /**
     *
     * @param originalLanguage
     * The original_language
     */
    public void setOriginalLanguage(String originalLanguage) {
        this.original_language = originalLanguage;
    }

    /**
     *
     * @return
     * The original_title
     */
    public String getOriginalTitle() {
        return original_title;
    }

    /**
     *
     * @param originalTitle
     * The original_title
     */
    public void setOriginalTitle(String originalTitle) {
        this.original_title = originalTitle;
    }

    /**
     *
     * @return
     * The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     *
     * @param overview
     * The overview
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     *
     * @return
     * The release_date
     */
    public String getReleaseDate() {
        return release_date;
    }

    /**
     *
     * @param releaseDate
     * The release_date
     */
    public void setReleaseDate(String releaseDate) {
        this.release_date = releaseDate;
    }

    /**
     *
     * @return
     * The poster_path
     */
    public String getPosterPath() {
        return CDN_URL + poster_path;
    }

    /**
     *
     * @param posterPath
     * The poster_path
     */
    public void setPosterPath(String posterPath) {
        this.poster_path = CDN_URL + posterPath;
    }

    /**
     *
     * @return
     * The popularity
     */
    public double getPopularity() {
        return popularity;
    }

    /**
     *
     * @param popularity
     * The popularity
     */
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The video
     */
    public boolean isVideo() {
        return video;
    }

    /**
     *
     * @param video
     * The video
     */
    public void setVideo(boolean video) {
        this.video = video;
    }

    /**
     *
     * @return
     * The vote_average
     */
    public double getVoteAverage() {
        return vote_average;
    }

    /**
     *
     * @param voteAverage
     * The vote_average
     */
    public void setVoteAverage(double voteAverage) {
        this.vote_average = voteAverage;
    }

    /**
     *
     * @return
     * The vote_count
     */
    public int getVoteCount() {
        return vote_count;
    }

    /**
     *
     * @param voteCount
     * The vote_count
     */
    public void setVoteCount(int voteCount) {
        this.vote_count = vote_count;
    }

    /**
     *
     * @return
     * The movie_year
     */
    public String getReleaseYear() {

        String movie_year = "0000";
        if(release_date != null) {
            String str[] = release_date.split("-");
            if (str.length > 0) {
                movie_year = str[0];
            }
        }
        return movie_year;
    }

    /**
     *
     * @param dateFormat
     * @return
     */
    public String getReleaseDate(int dateFormat) {

        try {
            if(release_date != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
                Date date = formatter.parse(release_date);

                return DateFormat.getDateInstance(dateFormat).format(date);
            }
            else
                return  "0000-00-00";
        }
        catch (ParseException pe){
            return "0000-00-00";
        }
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
