package com.example.android.popularmovie;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    // Set up member variables for the movie
    private String mTitle;
    private String mPoster;
    private String mRating;
    private String mDescription;
    private String mReleaseDate;
    private static final String MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/";

    public Movie() { }
    /**
     * BASE CONSTRUCTOR
     * @param movieTitle string of the movie title
     * @param moviePoster string of the path of movie poster
     * @param movieRating string of the movie rating average
     * @param movieDescription string of the movie synopsis
     * @param movieReleaseDate string of the movie release date
     */
    public Movie(String movieTitle, String moviePoster, String movieRating,
                 String movieDescription, String movieReleaseDate) {
        mTitle = movieTitle;
        mPoster = moviePoster;
        mRating = movieRating;
        mDescription = movieDescription;
        mReleaseDate = movieReleaseDate;
    }

    /**
     * Constructor used by the Saved Instance
     * @param parcel the object that contains the movie data
     */
    private Movie(Parcel parcel) {
        mTitle = parcel.readString();
        mPoster = parcel.readString();
        mRating = parcel.readString();
        mDescription = parcel.readString();
        mReleaseDate = parcel.readString();
    }

    // Set up get/set methods for class properties
    public String getmTitle() { return mTitle; }
    public void setmTitle(String movieTitle) { mTitle = movieTitle; }

    public String getmPoster() { return mPoster; }
    public void setmPoster(String moviePoster) { mPoster = moviePoster; }

    public String getmRating() { return mRating; }
    public void setmRating(String movieRatings) { mRating = movieRatings;}

    public String getmDescription() { return mDescription; }
    public void setmDescription(String movieDescription) { mDescription = movieDescription; }

    public String getmReleaseDate() { return mReleaseDate; }
    public void setmReleaseDate(String movieReleaseDate) { mReleaseDate = movieReleaseDate; }

    /**
     * This method returns the complete poster path based on screen size
     *
     * @param context application context
     * @return the path used by the Picasso library to display an image
     */
    public String buildPosterPath(Context context) {
        String posterWidth = context.getResources().getString(R.string.poster_size);
        return MOVIE_POSTER_URL + posterWidth + getmPoster();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mPoster);
        parcel.writeString(mRating);
        parcel.writeString(mDescription);
        parcel.writeString(mReleaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) { return new Movie(parcel); }

        @Override
        public Movie[] newArray(int i) { return new Movie[i]; }
    };
}
