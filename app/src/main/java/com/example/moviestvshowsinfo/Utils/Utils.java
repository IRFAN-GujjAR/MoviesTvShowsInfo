package com.example.moviestvshowsinfo.Utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.moviestvshowsinfo.MovieTvShowsDetails;
import com.example.moviestvshowsinfo.Adapters.ScreenSlidePagerAdpater;

import java.util.ArrayList;
import java.util.List;


/**
 * This class is a storage class which will be used to store data in the fields of this class
 * and also we will get the data from this class.
 * In simple worlds, this is data sharing class which will be used to share data in other classes.
 */
public final class Utils {
    //TODO('Enter Your TMDB Api Key')
    private static String API_KEY="Enter Your Api key";

    public static boolean tvShow = false;

    public static boolean movie = false;

    public static boolean favouriteMovies = false;

    public static boolean favouriteTvShows = false;

    public static boolean isSearched = false;

    public static String searchedQuery = "";

    public static boolean emptyMovieTvShowList = false;

    public static ScreenSlidePagerAdpater screenSlidePagerAdpater;

    public static final String MOVIES_TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key="+API_KEY;

    public static final String MOVIES_NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key="+API_KEY;

    public static final String MOVIES_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;

    public static final String MOVIES_UPCOMING_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key="+API_KEY;

    public static final String TV_SHOWS_TOP_RATED_URL = "https://api.themoviedb.org/3/tv/top_rated?api_key="+API_KEY;

    public static final String TV_SHOWS_POPULAR_URL = "https://api.themoviedb.org/3/tv/popular?api_key="+API_KEY;

    public static final String TV_SHOWS_AIRING_TODAY_URL = "https://api.themoviedb.org/3/tv/airing_today?api_key="+API_KEY;

    public static final String TV_SHOWS_ON_THE_AIR_URL = "https://api.themoviedb.org/3/tv/on_the_air?api_key="+API_KEY;

    public static final String SEARCH_MOVIES_PRIMARY_URL = "https://api.themoviedb.org/3/search/movie?api_key="+API_KEY+"&query=";

    public static final String SEARCH_TV_SHOWS_PRIMARY_URL = "https://api.themoviedb.org/3/search/tv?api_key="+API_KEY+"&query=";

    public static int movieOrTvShowId;

    public static final String GET_DETAILS_MOVIES_STARTING_URL = "https://api.themoviedb.org/3/movie/";

    public static final String GET_DETAILS_MOVIES_ENDING_URL = "?api_key="+API_KEY;

    public static final String GET_DETAILS_TV_SHOWS_STARTING_URL = "https://api.themoviedb.org/3/tv/";

    public static final String GET_DETAILS_TV_SHOWS_ENDING_URL = "?api_key="+API_KEY;

    public static final String YOUTUBE_GET_VIDEOS_STARTING_URL = "https://api.themoviedb.org/3/movie/";

    public static final String YOUTUBE_GET_VIDEOS_ENDING_URL = "/videos?api_key="+API_KEY;

    public static String currentUrl;

    public static List<MovieTvShowsDetails> list = new ArrayList<>();

    public static Context mainContext;


    public static void setMovie(boolean movie) {
        Utils.movie = movie;
    }

    public static boolean isMovie() {
        return movie;
    }

    public static void setTvShow(boolean tvShow) {
        Utils.tvShow = tvShow;
    }

    public static boolean isTvShow() {
        return tvShow;
    }

    public static void setFavouriteMovies(boolean favouriteMovies) {
        Utils.favouriteMovies = favouriteMovies;
    }

    public static boolean isFavouriteMovies() {
        return favouriteMovies;
    }

    public static void setFavouriteTvShows(boolean favouriteTvShows) {
        Utils.favouriteTvShows = favouriteTvShows;
    }

    public static boolean isFavouriteTvShows() {
        return favouriteTvShows;
    }

    public static void setIsSearched(boolean isSearched) {
        Utils.isSearched = isSearched;
    }

    public static boolean isIsSearched() {
        return isSearched;
    }

    public static void setSearchedQuery(String searchedQuery) {
        Utils.searchedQuery = searchedQuery;
    }

    public static String getSearchedQuery() {
        return searchedQuery;
    }

    public static void setEmptyMovieTvShowList(boolean emptyMovieTvShowList) {
        Utils.emptyMovieTvShowList = emptyMovieTvShowList;
    }

    public static boolean isEmptyMovieTvShowList() {
        return emptyMovieTvShowList;
    }

    public static void setList(List<MovieTvShowsDetails> list) {
        Utils.list = list;
    }

    public static List<MovieTvShowsDetails> getList() {
        return list;
    }

    public static void setMainContext(Context mainContext) {
        Utils.mainContext = mainContext;
    }

    public static Context getMainContext() {
        return mainContext;
    }

    public static void setCurrentUrl(String currentUrl) {
        Utils.currentUrl = currentUrl;
    }

    public static String getCurrentUrl() {
        return currentUrl;
    }

    public static void setMovieOrTvShowId(int movieOrTvShowId) {
        Utils.movieOrTvShowId = movieOrTvShowId;
    }

    public static int getMovieOrTvShowId() {
        return movieOrTvShowId;
    }

    public static double getDisplayMetrics(Context context) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        double x = Math.pow(screenWidth / displayMetrics.xdpi, 2);
        double y = Math.pow(screenHeight / displayMetrics.ydpi, 2);

        double screenInches = Math.sqrt(x + y);
        screenInches = (double) Math.round(screenInches * 100) / 100;

        return screenInches;
    }


}
