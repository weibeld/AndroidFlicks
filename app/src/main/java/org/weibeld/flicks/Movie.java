package org.weibeld.flicks;

/**
 * Created by dw on 16/02/17.
 */

public class Movie {

    String posterPath;
    boolean adult;
    String overview;
    String releaseDate;
    int[] genreIds;
    int id;
    String originalTitle;
    String originalLanguage;
    String title;
    String backdropPath;
    double popularity;
    int voteCount;
    double voteAverage;

    public String getPosterPath() {
        return posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }
}
