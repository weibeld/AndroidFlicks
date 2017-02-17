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
    // The following information can be obrtained through the /configuration  API call
    String BASE_URL_IMG = "https://image.tmdb.org/t/p/";
    String POSTER_SIZE_W92 = "w92";    // 92x139 (WxH) 2:3
    String POSTER_SIZE_W154 = "w154";  // 154x231 (WxH) 2:3
    String POSTER_SIZE_W185 = "w185";  // 185x278 (WxH) 2:3
    String POSTER_SIZE_W342 = "w342";  // 342x513 (WxH) 2:3
    String POSTER_SIZE_W500 = "W500";  // 500X750 (WxH) 2:3
    String POSTER_SIZE_W780 = "w780";  // 780x1170 (WxH) 2:3

    @GET(NOW_PLAYING)
    Call<ApiResponseMovieList> apiGetNowPlaying();

}
