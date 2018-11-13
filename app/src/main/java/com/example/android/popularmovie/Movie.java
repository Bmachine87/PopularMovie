package com.example.android.popularmovie;

public class Movie {
    // Set up member variables for the movie
    String mTitle;
    String mPopularity;
    String mRating;
    String mDescription;
    String mReleaseDate;

    public Movie(String movieTitle, String moviePopularity, String movieRating,
                 String movieDescription, String movieReleaseDate) {
        mTitle = movieTitle;
        mPopularity = moviePopularity;
        mRating = movieRating;
        mDescription = movieDescription;
        mReleaseDate = movieReleaseDate;
    }

    // Set up get methods

    public String getmTitle() {
        return mTitle;
    }

    public String getmPopularity() {
        return mPopularity;
    }

    public String getmRating() {
        return mRating;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

}
