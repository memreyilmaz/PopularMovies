package com.example.android.popularmoviesstagetwo;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{

    private String title;
    private String releaseDate;
    private String moviePoster;
    private String userRating;
    private String plotSynopsis;

    public Movie(String title, String releaseDate, String moviePoster, String userRating, String plotSynopsis) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.userRating = userRating;
        this.plotSynopsis = plotSynopsis;
    }

    private Movie (Parcel in){
        title = in.readString();
        releaseDate = in.readString();
        moviePoster = in.readString();
        userRating = in.readString();
        plotSynopsis = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(moviePoster);
        parcel.writeString(userRating);
        parcel.writeString(plotSynopsis);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}