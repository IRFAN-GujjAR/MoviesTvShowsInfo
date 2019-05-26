package com.example.moviestvshowsinfo.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class MovieTvShowsDetailsViewModel extends ViewModel {

    private LiveData<MovieTvShowEntity> movieTvShowEntityLiveData;

    public MovieTvShowsDetailsViewModel(AppDataBase appDataBase, int movieOrTvShowId){
       movieTvShowEntityLiveData=appDataBase.movieTvShowDao().loadSignleMovieOrTvShow(movieOrTvShowId);
    }

    public LiveData<MovieTvShowEntity> getMovieTvShowEntityLiveData() {
        return movieTvShowEntityLiveData;
    }
}
