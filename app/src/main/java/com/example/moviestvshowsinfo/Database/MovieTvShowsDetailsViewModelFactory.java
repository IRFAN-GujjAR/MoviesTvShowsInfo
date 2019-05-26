package com.example.moviestvshowsinfo.Database;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MovieTvShowsDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDataBase appDataBase;
    private final int movieOrTvShowId;

    public MovieTvShowsDetailsViewModelFactory(AppDataBase appDataBase,int movieOrTvShowId){
        this.appDataBase=appDataBase;
        this.movieOrTvShowId=movieOrTvShowId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieTvShowsDetailsViewModel(appDataBase,movieOrTvShowId);
    }
}
