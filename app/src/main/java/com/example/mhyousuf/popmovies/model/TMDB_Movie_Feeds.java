package com.example.mhyousuf.popmovies.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mhyousuf on 6/9/2015.
 */
public class TMDB_Movie_Feeds {
    private int page;
    private List<TMDBMovie> results = new ArrayList<TMDBMovie>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The page
     */
    public int getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * @return The results
     */
    public List<TMDBMovie> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<TMDBMovie> results) {
        this.results = results;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
