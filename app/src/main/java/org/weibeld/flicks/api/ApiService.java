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
    // Image base URL and image sizes can be obtained through the /configuration  API call
    String BASE_URL_IMG = "https://image.tmdb.org/t/p/";
    // Poster size defines width of the poster in px, the W:H ratio is in many cases 2:3
    String POSTER_SIZE_W92 = "w92";    // 92x139 (WxH) 2:3
    String POSTER_SIZE_W154 = "w154";  // 154x231 (WxH) 2:3
    String POSTER_SIZE_W185 = "w185";  // 185x278 (WxH) 2:3
    String POSTER_SIZE_W342 = "w342";  // 342x513 (WxH) 2:3
    String POSTER_SIZE_W500 = "w500";  // 500x750 (WxH) 2:3
    String POSTER_SIZE_W780 = "w780";  // 780x1170 (WxH) 2:3
    // Backdrop size defines width of the backdrop image in px, the W:H ratio is in many cases 16:9
    String BACKDROP_SIZE_W300 = "w300";    // 300x169 (WxH) 16:9
    String BACKDROP_SIZE_W780 = "w780";    // 780x439 (WxH) 16:9
    String BACKDROP_SIZE_W1280 = "w1280";  // 1280x720 (WxH) 16:9

    @GET(NOW_PLAYING)
    Call<ApiResponseMovieList> apiGetNowPlaying();

}
