package fr.hedwin.tmdb.retrofit;

import fr.hedwin.tmdb.object.Credits;
import fr.hedwin.tmdb.object.DbMovie;
import fr.hedwin.tmdb.object.Genre;
import fr.hedwin.tmdb.object.ResultsPage;
import fr.hedwin.tmdb.object.Videos;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbService {
    @Headers("Authorization: Bearer "+ TMDB.API_KEY)
    @GET("search/movie")
    Call<ResultsPage<DbMovie>> searchMovies(@Query("query") String query, @Query("page") int page, @Query("language") String language);

    @Headers("Authorization: Bearer "+ TMDB.API_KEY)
    @GET("movie/{movie_id}")
    Call<DbMovie> searchMovie(@Path("movie_id") Integer movie_id, @Query("language") String language);

    @Headers("Authorization: Bearer "+ TMDB.API_KEY)
    @GET("movie/{movie_id}/credits")
    Call<Credits> searchMovieCredits(@Path("movie_id") Integer movie_id, @Query("language") String language);

    @Headers("Authorization: Bearer "+ TMDB.API_KEY)
    @GET("genre/movie/list")
    Call<Genre.GenreList> movieGenre(@Query("language") String language);

    @Headers("Authorization: Bearer "+ TMDB.API_KEY)
    @GET("movie/{movie_id}/videos")
    Call<Videos> movieVideos(@Path("movie_id") Integer movie_id, @Query("language") String language);

    @Headers("Authorization: Bearer "+ TMDB.API_KEY)
    @GET("trending/movie/{time_window}")
    Call<ResultsPage<DbMovie>> trendingMovie(@Path("time_window") String time_window, @Query("language") String language);

    @Headers("Authorization: Bearer "+ TMDB.API_KEY)
    @GET("movie/{movie_id}/recommendations")
    Call<ResultsPage<DbMovie>> recommandedMovieFrom(@Path("movie_id") Integer movie_id, @Query("language") String language);

}
