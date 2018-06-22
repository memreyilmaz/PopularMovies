package com.example.android.popularmoviesstagetwo.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "favourite_movies")
public class Movie implements Parcelable{
    @ColumnInfo(name = "id")
    @PrimaryKey
    @SerializedName("id")
    @Expose
    public int id;
    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    @Expose
    private double voteAverage;
    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    @Expose
    private String overview;

    public Movie(int id,String title, String releaseDate, double voteAverage, String overview, String posterPath) {
        super();
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.posterPath = posterPath;
    }
    @Ignore
    public Movie() {
    }

    private Movie (Parcel in){

        //moviePoster = in.readString();
        id = in.readInt();
        title = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
     //   this.voteAverage = ((double) in.readValue((double.class.getClassLoader())));
    //    this.title = ((String) in.readValue((String.class.getClassLoader())));
     //   this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
    //    this.overview = ((String) in.readValue((String.class.getClassLoader())));
     //   this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeDouble(voteAverage);
        parcel.writeString(overview);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Movie withVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Movie withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Movie withPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }



    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Movie withOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Movie withReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        /*@SuppressWarnings({
                "unchecked"
        })*/
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        //@Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}

