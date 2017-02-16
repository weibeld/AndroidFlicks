package org.weibeld.flicks;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dw on 16/02/17.
 */

public class MovieList {

    List<Movie> results;

    public MovieList() {
        results = new ArrayList<>();
    }

    public String getTitles() {
        String s = "";
        for (Movie movie : results) {
            s += movie.getTitle() + "\n";
        }
        return s;
    }

    public static MovieList getMovieListFromJson(String jsonResponse) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.fromJson(jsonResponse, MovieList.class);
    }

}
