package com.example.mhyousuf.popmovies.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mhyousuf on 7/4/2015.
 */
public class TMDB_Movie_Reviews {

    private int id;
    private int page;
    private List<TMDB_Review> results = new ArrayList<TMDB_Review>();
    private int total_pages;
    private int total_results;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * The page
     */
    public int getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The results
     */
    public List<TMDB_Review> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<TMDB_Review> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The total_pages
     */
    public int getTotal_pages() {
        return total_pages;
    }

    /**
     *
     * @param total_pages
     * The total_pages
     */
    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    /**
     *
     * @return
     * The total_results
     */
    public int getTotal_results() {
        return total_results;
    }

    /**
     *
     * @param total_results
     * The total_results
     */
    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
