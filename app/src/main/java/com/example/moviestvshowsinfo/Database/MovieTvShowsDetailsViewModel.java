package com.example.moviestvshowsinfo.Database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MovieTvShowsDetailsViewModel extends ViewModel {

    private LiveData<MovieTvShowEntity> movieTvShowEntityLiveData;

    public MovieTvShowsDetailsViewModel(AppDataBase appDataBase, int movieOrTvShowId){
       movieTvShowEntityLiveData=appDataBase.movieTvShowDao().loadSignleMovieOrTvShow(movieOrTvShowId);
    }

    public LiveData<MovieTvShowEntity> getMovieTvShowEntityLiveData() {
        return movieTvShowEntityLiveData;
    }
}
