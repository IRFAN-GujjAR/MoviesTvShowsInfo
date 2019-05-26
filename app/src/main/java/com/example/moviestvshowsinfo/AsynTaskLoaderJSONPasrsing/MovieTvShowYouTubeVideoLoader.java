package com.example.moviestvshowsinfo.AsynTaskLoaderJSONPasrsing;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.moviestvshowsinfo.Utils.JSONUtils.JSONParsing;

/**
 * This loader loads the Youtube Video Key of specific Movie or Tv Show.
 */
public class MovieTvShowYouTubeVideoLoader extends AsyncTaskLoader<String> {

    private String url;

    public MovieTvShowYouTubeVideoLoader(Context context, String url){
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        String youTubeVideoKey= JSONParsing.fetchYoutubeVideoKey(url);
        return youTubeVideoKey;
    }
}
