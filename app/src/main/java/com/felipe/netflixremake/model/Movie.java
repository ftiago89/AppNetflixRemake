package com.felipe.netflixremake.model;

import android.graphics.Bitmap;

public class Movie {

    private String coverUrl;
    private Bitmap movieImage;

    public Bitmap getMovieImage() {
        return movieImage;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setMovieImage(Bitmap movieImage) {
        this.movieImage = movieImage;
    }
}
