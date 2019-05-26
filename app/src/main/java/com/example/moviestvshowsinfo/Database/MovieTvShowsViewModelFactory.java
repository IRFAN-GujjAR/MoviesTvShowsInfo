package com.example.moviestvshowsinfo.Database;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MovieTvShowsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDataBase appDataBase;

    public MovieTvShowsViewModelFactory(AppDataBase appDataBase){
        this.appDataBase=appDataBase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieTvShowsViewModel(appDataBase);
    }
}
