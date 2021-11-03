package com.example.moviestvshowsinfo.AsynTaskLoaderJSONPasrsing;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.moviestvshowsinfo.Utils.JSONUtils.JSONParsing;
import com.example.moviestvshowsinfo.MovieTvShowsDetails;

/**
 * This loader loads all the details of the Movie or Tv Show.
 */
public class MovieTvShowDetailsLoader extends AsyncTaskLoader<MovieTvShowsDetails> {

    private String url;

    public MovieTvShowDetailsLoader(Context context, String url){
        super(context);
        this.url=url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public MovieTvShowsDetails loadInBackground() {
        MovieTvShowsDetails movieTvShowsDetails= JSONParsing.fetchDataForDetails(url);
        return movieTvShowsDetails;
    }
}
