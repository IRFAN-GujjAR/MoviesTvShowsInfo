package com.example.moviestvshowsinfo.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.moviestvshowsinfo.Utils.Utils;

import java.util.List;

public class MovieTvShowsViewModel extends ViewModel {

    private LiveData<List<MovieTvShowEntity>> movieTvShowsEntities;

    public MovieTvShowsViewModel(AppDataBase appDataBase){
        if(Utils.isFavouriteMovies()) {
            movieTvShowsEntities = appDataBase.movieTvShowDao().loadAllMovies();
        }else if(Utils.isFavouriteTvShows()){
            movieTvShowsEntities=appDataBase.movieTvShowDao().loadAllTvShows();
        }
    }

    public LiveData<List<MovieTvShowEntity>> getMovieTvShowsEntities() {
        return movieTvShowsEntities;
    }
}
