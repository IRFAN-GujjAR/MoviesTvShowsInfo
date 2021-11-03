package com.example.moviestvshowsinfo.AsynTaskLoaderJSONPasrsing;


import android.content.Context;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.moviestvshowsinfo.Utils.JSONUtils.JSONParsing;
import com.example.moviestvshowsinfo.MovieTvShowsDetails;
import com.example.moviestvshowsinfo.Utils.Utils;

import java.util.List;

/**
 * This loader loads the list of data about Movies or Tv shows.
 */
public class MovieTvShowListLoader extends AsyncTaskLoader<List<MovieTvShowsDetails>> {

    public MovieTvShowListLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<MovieTvShowsDetails> loadInBackground() {
        List<MovieTvShowsDetails> list= JSONParsing.fetchDataForList(Utils.currentUrl);
        return list;
    }
}
