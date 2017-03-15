package com.projects.alexanderauer.popularmovies.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alex on 09.03.2017.
 *
 * Entity class for a movie object
 *
 */

public class Movie implements Parcelable{

    @SerializedName("original_title")
    String title;
    @SerializedName("poster_path")
    String posterPath;
    @SerializedName("overview")
    String overview;
    @SerializedName("release_date")
    String releaseDate;

    @SerializedName("vote_average")
    double userRating;

    public Movie(String title, String posterPath, String overview, String releaseDate, double userRating) {
        setTitle(title);
        setPosterPath(posterPath);
        setOverview(overview);
        setReleaseDate(releaseDate);
        setUserRating(userRating);
    }

    protected Movie(Parcel in) {
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        userRating = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeDouble(userRating);
    }
}
