package org.weibeld.flicks.api;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by dw on 16/02/17.
 */

public interface ApiService {

    // Static themoviedb.org API information
    String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    // Note: the base URL MUST end with a slash and the API calls MUST NOT start with a slash
    String BASE_URL = "https://api.themoviedb.org/3/";
    String NOW_PLAYING = "movie/now_playing";

    @GET(NOW_PLAYING)
    Call<ApiResponseMovieList> apiGetNowPlaying();

}
