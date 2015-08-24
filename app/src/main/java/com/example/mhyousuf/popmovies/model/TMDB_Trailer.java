package com.example.mhyousuf.popmovies.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mhyousuf on 7/4/2015.
 */
public class TMDB_Trailer {
    private final String YOUTUBE_VIDEO_THUMBNAIL_MQ_URL = "http://img.youtube.com/vi/%s/mqdefault.jpg";
    private String id;
    private String iso_639_1;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The iso_639_1
     */
    public String getIso_639_1() {
        return iso_639_1;
    }

    /**
     *
     * @param iso_639_1
     * The iso_639_1
     */
    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    /**
     *
     * @return
     * The key
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @param key
     * The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The site
     */
    public String getSite() {
        return site;
    }

    /**
     *
     * @param site
     * The site
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     *
     * @return
     * The size
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @param size
     * The size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @return
     * The thumbnail
     */
    public String getTrailerThumbnail() {
        return String.format(YOUTUBE_VIDEO_THUMBNAIL_MQ_URL, key);
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
