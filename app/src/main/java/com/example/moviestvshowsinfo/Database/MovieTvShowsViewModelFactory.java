package com.example.moviestvshowsinfo.Database;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

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
