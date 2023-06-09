/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: TmdbURL
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.tmdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TmdbURL {

    public static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlOTQ2OGYxNDQyMDU4OWIxYTNhZTdmZTkyN2M0MmUwMyIsInN1YiI6IjYwNzViYzUxMTg4NjRiMDAyY2VhYzExMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.LbGvmAuZ9mL4qTiBY4hUmcU1gcDbUfoY-j2-kALi3Dw";
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String MOVIE = "movie/";
    public static final String PERSON = "person/";
    public static final String TVSERIE = "tv/";
    public static final String SEARCH_MOVIE = "search/movie";
    public static final String SEARCH_PERSON = "search/person";
    public static final String SEARCH_TVSERIE = "search/tv";
    public static final String DISCORDER_MOVIE = "discover/movie";
    public static final String DISCORDER_SERIES = "discover/tv";
    public static final String GENRES_MOVIE = "genre/movie/list";
    public static final String GENRES_SERIES = "genre/tv/list";
    public static final String FIND = "find/";


    private final String baseUrl;
    private final Map<String, String> params = new HashMap<>();

    public TmdbURL(String link){
        this.baseUrl = BASE_URL+link;
        //addParams("api_key", API_KEY);
    }

    public TmdbURL addParams(String key, Object value){
        if(!params.containsKey(key)) params.put(key, value.toString());
        return this;
    }

    public TmdbURL editPage(Integer page){
        params.put("page", page.toString());
        return this;
    }

    public TmdbURL addLanguage(String lang){
        return addParams("language", lang);
    }

    public String getLink(){
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        if (params.size() > 0) {
            List<String> keys = new ArrayList<>(params.keySet());
            for (int i = 0; i < keys.size(); i++) {
                urlBuilder.append(i == 0 ? "?" : "&");
                String paramName = keys.get(i);
                urlBuilder.append(paramName).append("=").append(params.get(paramName));
            }
        }
        return urlBuilder.toString();
    }

}
