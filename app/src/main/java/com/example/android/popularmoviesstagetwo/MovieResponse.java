package com.example.android.popularmoviesstagetwo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse implements Parcelable {

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("results")
    @Expose
    private List<Movie> results;
    public final static Creator<MovieResponse> CREATOR = new Creator<MovieResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MovieResponse createFromParcel(Parcel parcel) {
            return new MovieResponse(parcel);
        }

        public MovieResponse[] newArray(int i) {
            return (new MovieResponse[i]);
        }

    };

    protected MovieResponse(Parcel parcel) {

        page = parcel.readInt();
        totalResults = parcel.readInt();

        totalPages = parcel.readInt();
        //movies = parcel.readList(List<Movie>);
        //this.page = ((int) parcel.readValue((int.class.getClassLoader())));
        //this.totalResults = ((int) parcel.readValue((int.class.getClassLoader())));
        //this.totalPages = ((int) parcel.readValue((int.class.getClassLoader())));
        parcel.readList(this.results, (Movie.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     */
    public MovieResponse() {
    }

    /**
     * @param movies
     * @param totalResults
     * @param page
     * @param totalPages
     */
    public MovieResponse(int page, int totalResults, int totalPages, List<Movie> movies) {
        super();
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.results = movies;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public MovieResponse withPage(int page) {
        this.page = page;
        return this;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public MovieResponse withTotalResults(int totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public MovieResponse withTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public List<Movie> getMovies() {
        return results;
    }

    public void setMovies(List<Movie> movies) {
        this.results = movies;
    }

    public MovieResponse withMovies(List<Movie> movies) {
        this.results = movies;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(page);
        parcel.writeValue(totalResults);
        parcel.writeValue(totalPages);
        parcel.writeList(results);
    }

    public int describeContents() {
        return 0;
    }
}