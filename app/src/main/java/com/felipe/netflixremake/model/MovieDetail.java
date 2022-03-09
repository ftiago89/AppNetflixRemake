package com.felipe.netflixremake.model;

import java.util.List;

public class MovieDetail {

    private final Movie movie;
    private final List<Movie> similarMovies;

    public MovieDetail(Movie movie, List<Movie> similarMovies) {
        this.movie = movie;
        this.similarMovies = similarMovies;
    }

    public Movie getMovie() {
        return movie;
    }

    public List<Movie> getSimilarMovies() {
        return similarMovies;
    }
}
