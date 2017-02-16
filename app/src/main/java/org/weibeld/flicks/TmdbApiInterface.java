package org.weibeld.flicks;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by dw on 16/02/17.
 */

public interface TmdbApiInterface {

    // Interface of a method (callback)
    @GET("movie/now_playing")
    public Call<MovieList> getCurrentMovies();

}
