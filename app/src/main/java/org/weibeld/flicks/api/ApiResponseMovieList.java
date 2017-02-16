package org.weibeld.flicks.api;

import java.util.ArrayList;

/**
 * Created by dw on 16/02/17.
 */

public class ApiResponseMovieList {
    public ArrayList<Movie> results;
    public int page;
    public int totalPages;
    public int totalResults;
    public Dates dates;

    public static class Movie {
        public String posterPath;
        public boolean adult;
        public String overview;
        public String releaseDate;
        public int[] genreIds;
        public int id;
        public String originalTitle;
        public String originalLanguage;
        public String title;
        public String backdropPath;
        public double popularity;
        public int voteCount;
        public double voteAverage;
    }

    public static class Dates {
        public String minimum;
        public String maximum;
    }
}
