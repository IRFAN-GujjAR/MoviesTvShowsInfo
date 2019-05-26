package com.example.moviestvshowsinfo.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * This is the Entity of database and this class contains all the fields of the table in
 * the database.
 */
@Entity(tableName = "MoviesTVShows")
public class MovieTvShowEntity {

    @PrimaryKey(autoGenerate = false)
    private int movieTvShowId;
    private String movieTitle;
    private String tvShowName;
    private String originalLanguage;
    private String movieReleaseDate;
    private String tvShowFirstAirDate;
    private boolean adult;
    private double voteAverage;
    private String posterImagePath;
    private String backgroundImagePath;
    private String genres;
    private String overview;
    private int budget;
    private int revenue;
    private String productionCompanies;
    private String youtubeKey;


    public MovieTvShowEntity(int movieTvShowId, String movieTitle, String tvShowName, String originalLanguage, String movieReleaseDate, String tvShowFirstAirDate,
                             boolean adult, double voteAverage, String posterImagePath, String backgroundImagePath, String genres, String overview,
                             int budget, int revenue, String productionCompanies, String youtubeKey) {
        this.movieTvShowId = movieTvShowId;
        this.movieTitle = movieTitle;
        this.tvShowName = tvShowName;
        this.originalLanguage = originalLanguage;
        this.movieReleaseDate = movieReleaseDate;
        this.tvShowFirstAirDate = tvShowFirstAirDate;
        this.adult = adult;
        this.voteAverage = voteAverage;
        this.posterImagePath = posterImagePath;
        this.backgroundImagePath = backgroundImagePath;
        this.genres = genres;
        this.overview = overview;
        this.budget = budget;
        this.revenue = revenue;
        this.productionCompanies = productionCompanies;
        this.youtubeKey=youtubeKey;
    }


    public void setMovieTvShowId(int movieTvShowId) {
        this.movieTvShowId = movieTvShowId;
    }

    public int getMovieTvShowId() {
        return movieTvShowId;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setTvShowName(String tvShowName) {
        this.tvShowName = tvShowName;
    }

    public String getTvShowName() {
        return tvShowName;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setTvShowFirstAirDate(String tvShowFirstAirDate) {
        this.tvShowFirstAirDate = tvShowFirstAirDate;
    }

    public String getTvShowFirstAirDate() {
        return tvShowFirstAirDate;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setPosterImagePath(String posterImagePath) {
        this.posterImagePath = posterImagePath;
    }

    public String getPosterImagePath() {
        return posterImagePath;
    }


    public void setBackgroundImagePath(String backgroundImagePath) {
        this.backgroundImagePath = backgroundImagePath;
    }

    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getGenres() {
        return genres;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getBudget() {
        return budget;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setProductionCompanies(String productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public String getProductionCompanies() {
        return productionCompanies;
    }

    public String getYoutubeKey() {
        return youtubeKey;
    }

    public void setYoutubeKey(String youtubeKey) {
        this.youtubeKey = youtubeKey;
    }
}

