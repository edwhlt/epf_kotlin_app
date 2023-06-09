/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: TMDB
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.tmdb.retrofit;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.hedwin.tmdb.object.*;
import fr.hedwin.tmdb.model.MovieSortBy;
import fr.hedwin.tmdb.model.SerieSortBy;
import fr.hedwin.tmdb.utils.CompletableFuture;
import fr.hedwin.tmdb.utils.Future;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TMDB {

    //private static final Logger logger = LoggerFactory.getLogger(fr.hedwin.tmdblib.TMDB.class);

    public static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlOTQ2OGYxNDQyMDU4OWIxYTNhZTdmZTkyN2M0MmUwMyIsInN1YiI6IjYwNzViYzUxMTg4NjRiMDAyY2VhYzExMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.LbGvmAuZ9mL4qTiBY4hUmcU1gcDbUfoY-j2-kALi3Dw";
    private static Retrofit retrofit;
    public static TmdbService SERVICE = api().create(TmdbService.class);
    public static Retrofit api() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/") // Remplacez par votre URL de base
                    .addConverterFactory(JacksonConverterFactory.create()) // Remplacez par le convertisseur approprié (par exemple, MoshiConverterFactory.create() pour Moshi)
                    .build();
        }
        return retrofit;
    }


    /**
     * <pre>Français</pre>
     */
    public static final String FR = "fr-FR";

    /**
     * <pre>Anglais</pre>
     */
    private static final String US = "en-US";

    public static Future<Videos> getMovieVideos(int id){
        return CompletableFuture.async(() -> {
            return SERVICE.movieVideos(id, FR).execute().body();
        });
    }

    public static Future<Credits> getMovieCredits(int id){
        //TmdbURL tmdbURL = new TmdbURL(TmdbURL.MOVIE+id+"/credits").addLanguage(FR);
        //return ClientHttp.executeRequest(tmdbURL, Credits.class);
        return null;
    }
    public static Future<DbMovie.ResultsMovie> searchMovie(String request) {
        return CompletableFuture.async(() -> new DbMovie.ResultsMovie((page) -> new CompletableFuture<ResultsPage<DbMovie>>(() -> {
            Response<ResultsPage<DbMovie>> movies = SERVICE.searchMovies(request.replace(" ", "+"), page, FR).execute();
            return movies.body();
        })));
    }

    public static Future<ResultsPage<DbMovie>> searchMovie(String request, int page){
        return CompletableFuture.async(() -> {
           return SERVICE.searchMovies(request.replace(" ", "+"), page, FR).execute().body();
        });
    }


    public static Future<ResultsPage<DbMovie>> trendingMovies(){
        return CompletableFuture.async(() -> {
            return SERVICE.trendingMovie("day", FR).execute().body();
        });
    }


    public static Future<Genre.GenreList> getGenresMovie(){
        //TmdbURL tmdbURL = new TmdbURL(TmdbURL.GENRES_MOVIE).addLanguage(FR);
        //return ClientHttp.executeRequest(tmdbURL, Genre.GenreList.class);
        return null;
    }

    public static Future<Genre.GenreList> getGenresSerie(){
        //TmdbURL tmdbURL = new TmdbURL(TmdbURL.GENRES_SERIES).addLanguage(FR);
        //return ClientHttp.executeRequest(tmdbURL, Genre.GenreList.class);
        return null;
    }


}
