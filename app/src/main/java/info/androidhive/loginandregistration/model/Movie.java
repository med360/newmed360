package info.androidhive.loginandregistration.model;

/**
 * Created by Siya on 3/30/2017.
 */

import java.util.ArrayList;

public class Movie {
    private String title, thumbnailUrl;
    private String year;
    private double rating;
    private String genre;
    private String did;
    public Movie() {
    }

    public Movie(String did,String name, String thumbnailUrl, String year, double rating,
                 String genre) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.rating = rating;
        this.genre = genre;
        this.did=did;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

}
